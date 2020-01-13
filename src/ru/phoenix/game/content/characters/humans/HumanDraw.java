package ru.phoenix.game.content.characters.humans;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public abstract class HumanDraw extends HumanControl {
    private ImageAnimation animation;
    private Texture texture;
    private Projection projection;

    // работа с маской
    private Texture maskTexture;
    private VertexBufferObject maskVbo;
    private Projection maskProjection;

    // дополнительные анимации и текстуры
    private boolean useAdditionalAnimation;
    private ImageAnimation additionalAnimation;
    private Texture additionalTexture;
    private Projection additionalProjection;
    // контроль анимации
    private boolean stop;

    // конструкторы - начало
    protected HumanDraw(){
        super();
        animation = new ImageAnimation();
        texture = new Texture2D();
        projection = new Projection();
        useAdditionalAnimation = false;
        additionalProjection = new Projection();
        stop = false;
        createMask();
    }

    protected HumanDraw(Character character){
        super(character);
        animation = new ImageAnimation();
        texture = new Texture2D();
        projection = new Projection();
        useAdditionalAnimation = false;
        additionalProjection = new Projection();
        stop = false;
        createMask();
    }
    // конструкторы - конец

    private void createMask(){
        maskTexture = new Texture2D();
        maskTexture.setup(null,"./data/content/texture/hud/mask/mask.png",GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
        maskVbo = new MeshConfig(GL_DYNAMIC_DRAW, GL_TRIANGLES);
        maskProjection = new Projection();
        maskProjection.getModelMatrix().identity();
        Vector3f screenPos = getScreenPos(new Vector3f(getPosition()).add(new Vector3f(0.0f,1.0f,0.0f)));
        screenPos.setZ(-0.01f);
        maskProjection.setTranslation(screenPos);
        Vector3f low = getScreenPos(getPosition());
        Vector3f hei = getScreenPos(new Vector3f(getPosition()).add(new Vector3f(0.0f,2.0f,0.0f)));
        float r = Math.abs(low.sub(hei).length()) * 2.0f;

        float[] pos = new float[]{
                -r,  r, 0.0f,
                -r, -r, 0.0f,
                 r, -r, 0.0f,
                 r,  r, 0.0f
        };

        float[] tex = new float[]{
                0.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
                1.0f,1.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };
        maskVbo.allocate(pos,null,tex,null,null,null,null,null,indices);
    }

    private Vector3f getScreenPos(Vector3f studyPos){
        Vector3f position = new Vector3f(studyPos);
        // извлекаем матрицы перспективы и вида
        Matrix4f perspective = new Matrix4f(Camera.getInstance().getPerspective().getProjection());
        Matrix4f view = new Matrix4f(Camera.getInstance().getPerspective().getViewMatrix());
        // получение мировой матрицы путем перемножения матриц перспективы и вида
        Matrix4f world = new Matrix4f(perspective.mul(view));
        // вычисление ндсПозиции обьекта
        Vector3f ndcPosition = new Vector3f(world.mulOnVector(position));
        ndcPosition = new Vector3f(ndcPosition.getX() / ndcPosition.getZ(), ndcPosition.getY() / ndcPosition.getZ(), 0.01f);
        // Вычисление позиции объекта в экранных координатах
        float X = (ndcPosition.getX() + 1.0f) * Window.getInstance().getWidth() / 2.0f;
        float Y = (Window.getInstance().getHeight() - ((ndcPosition.getY() + 1.0f) * Window.getInstance().getHeight() / 2.0f));
        return new Vector3f(X,Y,0.0f);
    }

    public void updateAnimation(Texture texture, ImageAnimation animation){
        setTexture(texture);
        setAnimation(animation);
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    protected void draw(Shader shader, boolean shadow){
        if(!isStop()) {
            setTurn();
        }
        setUniforms(shader);
        animation.draw();
        if(isAdditionalAnimation()){
            setUniforms(shader);
            shader.setUniform("model_m",additionalProjection.getModelMatrix());
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, additionalTexture.getTextureID());
            shader.setUniform("image", 1);
            additionalAnimation.draw();
        }
        if(!shadow && (isTarget() || isShowIndicators())) {
            getSelfIndicators().draw(shader);
        }
    }

    public void drawMask(Shader shader){
        maskUpdate();
        shader.setUniform("model_m",maskProjection.getModelMatrix());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, maskTexture.getTextureID());
        shader.setUniform("image", 2);
        maskVbo.draw();
    }

    private void maskUpdate(){
        maskProjection.getModelMatrix().identity();
        Vector3f screenPos = getScreenPos(new Vector3f(getPosition()).add(new Vector3f(0.0f,1.0f,0.0f)));
        screenPos.setZ(-0.01f);
        maskProjection.setTranslation(screenPos);
        Vector3f low = getScreenPos(getPosition());
        Vector3f hei = getScreenPos(new Vector3f(getPosition()).add(new Vector3f(0.0f,2.0f,0.0f)));
        float r = Math.abs(low.sub(hei).length()) * 1.5f;

        float[] pos = new float[]{
                -r,  r, 0.0f,
                -r, -r, 0.0f,
                 r, -r, 0.0f,
                 r,  r, 0.0f
        };
        maskVbo.setNewPos(pos);
    }

    private void setUniforms(Shader shader){
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 1);
        shader.setUniform("grid", 0);
        shader.setUniform("isActive", 1);
        shader.setUniform("bigTree", 0);
        shader.setUniform("viewDirect", new Vector3f(Camera.getInstance().getFront().normalize()));
        shader.setUniform("turn", isTurn() ? 1 : 0);
        shader.setUniform("discardReverse", 0);
        shader.setUniform("discardControl", -1.0f);
        shader.setUniform("noPaint", 0);

        shader.setUniform("model_m", projection.getModelMatrix());
        shader.setUniform("xOffset", 0.0f);
        shader.setUniform("yOffset", 0.0f);
        shader.setUniform("zOffset", 0.0f);
        shader.setUniform("group", getGroup());
        shader.setUniform("id", getId());
        shader.setUniform("onTarget", isTarget() ? 1 : 0);
        shader.setUniform("water", 0);
        // end
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
    }

    // методы сетеры и гетеры - начало
    public ImageAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(ImageAnimation animation){
        this.animation = animation;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }

    // дополнительные анимации и текстуры
    protected void updateAdditionalAnimation(Texture texture, ImageAnimation animation){
        setAdditionalTexture(texture);
        setAdditionalAnimation(animation);
    }

    private void setAdditionalAnimation(ImageAnimation additionalAnimation) {
        this.additionalAnimation = additionalAnimation;
    }

    private void setAdditionalTexture(Texture additionalTexture) {
        this.additionalTexture = additionalTexture;
    }

    private boolean isAdditionalAnimation() {
        return useAdditionalAnimation;
    }

    protected void useAdditionalAnimation(boolean use) {
        this.useAdditionalAnimation = use;
    }

    protected void setAdditionalProjection(Vector3f ammoPosition){
        additionalProjection.getModelMatrix().identity();
        additionalProjection.setTranslation(ammoPosition);
    }

    // методы сетеры и гетеры - конец
}
