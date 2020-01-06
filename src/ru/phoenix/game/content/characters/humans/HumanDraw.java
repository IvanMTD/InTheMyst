package ru.phoenix.game.content.characters.humans;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public abstract class HumanDraw extends HumanControl {
    private ImageAnimation animation;
    private Texture texture;
    private Projection projection;

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
    }

    protected HumanDraw(Character character){
        super(character);
        animation = new ImageAnimation();
        texture = new Texture2D();
        projection = new Projection();
        useAdditionalAnimation = false;
        additionalProjection = new Projection();
        stop = false;
    }
    // конструкторы - конец

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
