package ru.phoenix.game.content.object;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.CoreEngine;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.ImageAnimLoader;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.assembled.SelfIndicators;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.*;

public abstract class ObjectControl {
    // vbo
    private SelfIndicators selfIndicators;
    private List<Texture> textures;
    private ImageAnimation sprite;
    private Projection projection;
    private Vector3f position;
    // animated control
    private int currentTexture;
    private float distance;
    private float id;
    private int group;
    private float counter;
    private float lastCount;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    // shader control
    private boolean instance;
    private boolean animated;
    private boolean onTarget;
    private boolean selected;
    private boolean board;
    private boolean shadow;
    private boolean active;
    private boolean water;
    private boolean jump;
    private boolean turn;
    private boolean bigTree;
    private boolean tree;

    private boolean isIndicatorOn;
    private boolean tapStop;

    public ObjectControl(){
        isIndicatorOn = false;
        tapStop = false;
        selfIndicators = null;
        textures = new ArrayList<>();
        projection = new Projection();
        position = new Vector3f();
        currentTexture = 1;
        distance = 0;
        counter = 0.0f;
        lastCount = (float)glfwGetTime();
        xOffset = 0.0f;
        yOffset = 0.0f;
        zOffset = 0.0f;
        instance = false;
        animated = false;
        shadow = false;
        active = false;
        water = false;
        bigTree = false;
        tree = false;
        group = GROUP_A;
    }

    protected void setup(List<Texture> textures, int textureIndex, Vector3f position){
        this.textures = new ArrayList<>(textures);
        currentTexture = textureIndex;
    }

    protected void setup(List<Texture> textures, int row, int column, float width, float height, int textureIndex, Vector3f position, Matrix4f[] matrix){
        this.textures = new ArrayList<>(textures);

        currentTexture = textureIndex;

        if(matrix != null){
            sprite = ImageAnimLoader.load(textures.get(currentTexture), row, column, width, height,matrix,0);
            instance = true;
        }else {
            sprite = ImageAnimLoader.load(textures.get(currentTexture), row, column, width, height,null,0);
            instance = false;
        }

        if(position != null) {
            this.position = position;
            projection.getModelMatrix().identity();
            projection.setTranslation(position);
        }

        int num = (int)Math.round(Math.random() * row);
        for(int i=0; i<num; i++){
            sprite.nextFrame();
        }
        if(!animated){
            int num2 = (int)Math.round(Math.random() * column);
            sprite.setCurrentAnimation(num2);
        }
    }

    protected void setup(List<Texture> textures, int row, int column, float width, float height, int textureIndex, Vector3f position, Matrix4f[] matrix, int mode){
        this.textures = new ArrayList<>(textures);

        currentTexture = textureIndex;

        if(matrix != null){
            sprite = ImageAnimLoader.load(textures.get(currentTexture), row, column, width, height,matrix,mode);
            instance = true;
        }else {
            sprite = ImageAnimLoader.load(textures.get(currentTexture), row, column, width, height,null,mode);
            instance = false;
        }

        if(position != null) {
            this.position = position;
            projection.getModelMatrix().identity();
            projection.setTranslation(position);
        }

        int num = (int)Math.round(Math.random() * row);
        for(int i=0; i<num; i++){
            sprite.nextFrame();
        }
        if(!animated){
            int num2 = (int)Math.round(Math.random() * column);
            sprite.setCurrentAnimation(num2);
        }
    }

    protected void setup(List<Texture> textures, ImageAnimation sprite, int currentTexture){
        this.textures = new ArrayList<>(textures);
        this.currentTexture = currentTexture;
        this.sprite = sprite;
    }

    protected void updateAnimation(ImageAnimation sprite, int currentTexture){
        this.currentTexture = currentTexture;
        this.sprite = sprite;
    }

    protected void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Vector3f getPosition(){
        return position;
    }

    public void setPosition(Vector3f position){
        this.position = position;
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
    }

    protected void setSelfIndicators(SelfIndicators selfIndicators) {
        this.selfIndicators = selfIndicators;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isOnTarget() {
        return onTarget;
    }

    public void setOnTarget(boolean onTarget) {
        this.onTarget = onTarget;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isBoard() {
        return board;
    }

    protected void setBoard(boolean board) {
        this.board = board;
    }

    public boolean isInstance() {
        return instance;
    }

    public boolean isShadow() {
        return shadow;
    }

    protected void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    protected void setWater(boolean water){
        this.water = water;
    }

    private boolean isWater(){
        return water;
    }

    protected float getxOffset() {
        return xOffset;
    }

    protected void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    protected float getyOffset() {
        return yOffset;
    }

    protected void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    protected float getzOffset() {
        return zOffset;
    }

    protected void setzOffset(float zOffset) {
        this.zOffset = zOffset;
    }

    public void setProjection(Projection projection){
        this.projection = projection;
    }

    private int getGroup() {
        return group;
    }

    protected void setGroup(int group) {
        this.group = group;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    protected boolean isBigTree() {
        return bigTree;
    }

    protected void setBigTree(boolean bigTree) {
        this.bigTree = bigTree;
    }

    public boolean isTree() {
        return tree;
    }

    protected void setTree(boolean tree) {
        this.tree = tree;
    }

    public void draw(Shader shader, boolean shadow){

        if(checkObjectOnScreen() || sprite.getVbo().isInstances()) {

            boolean currentTurn;
            float yaw = Camera.getInstance().getYaw();
            if (90.0f < yaw && yaw < 270.0f) {
                currentTurn = !isTurn();
            } else {
                currentTurn = isTurn();
            }
            /*if(instance) {
                sprite.updateInstanceMatrix();
            }*/
                // setUniforms
            /*shader.useProgram();
            // глобальный юниформ
            shader.setUniformBlock("matrices",0);*/
            // контролеры
            shader.setUniform("instance", sprite.getVbo().isInstances() ? 1 : 0);
            shader.setUniform("animated", 0);
            shader.setUniform("board", isBoard() ? 1 : 0);
            shader.setUniform("grid", 0);
            shader.setUniform("isActive", isActive() ? 1 : 0);
            shader.setUniform("bigTree", isBigTree() ? 1 : 0);
            shader.setUniform("tree", isTree() ? 1 : 0);
            shader.setUniform("showAlpha", Default.isShowAlpha() ? 1 : 0);
            shader.setUniform("viewDirect", new Vector3f(Camera.getInstance().getFront().normalize()));
            shader.setUniform("turn", currentTurn ? 1 : 0);
            shader.setUniform("discardReverse", 0);
            shader.setUniform("discardControl", -1.0f);
            shader.setUniform("noPaint", 0);
            // доп данные
            shader.setUniform("model_m", projection.getModelMatrix());
            shader.setUniform("xOffset", xOffset);
            shader.setUniform("yOffset", yOffset);
            shader.setUniform("zOffset", zOffset);
            shader.setUniform("group", group);
            shader.setUniform("id", id);
            shader.setUniform("onTarget", isOnTarget() ? 1 : 0);
            shader.setUniform("water", isWater() ? 1 : 0);
            // end
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textures.get(currentTexture).getTextureID());
            shader.setUniform("image", 0);

            sprite.draw();

            if (animated) {
                if (isBoard()) {
                    if (isActive()) {
                        if (!isJump() && !sprite.isBlock()) {
                            if (counter > sprite.getCondition()) {
                                sprite.nextFrame();
                                counter = 0.0f;
                            }
                        }
                    } else {
                        if (counter > 10.0f + Math.random() * 10.0f) {
                            sprite.nextFrame();
                            counter = 0.0f;
                        }
                    }
                } else {
                    if (counter > 20.0f) {
                        sprite.nextFrame();
                        counter = 0.0f;
                    }
                }
                float tik = (float) glfwGetTime() - lastCount;
                counter += (tik * CoreEngine.getFps());
                lastCount = (float) glfwGetTime();
            }

            boolean tap = false;

            if (!tapStop) {
                tap = Input.getInstance().isPressed(GLFW_KEY_F);
                tapStop = true;
            }

            if (!Input.getInstance().isPressed(GLFW_KEY_F)) {
                tapStop = false;
            }

            if (tap) {
                isIndicatorOn = !isIndicatorOn;
            }

            if (selfIndicators != null && isIndicatorOn && !shadow) {
                selfIndicators.draw(shader);
            } else {
                if (selfIndicators != null && isOnTarget() && !shadow) {
                    selfIndicators.draw(shader);
                }
            }
        }
    }

    private boolean checkObjectOnScreen(){
        boolean check = false;
        Vector3f position = new Vector3f(getPosition());
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
        Vector2f objectPos = new Vector2f(X,Y);

        int offset = 1000;

        if(-offset <= objectPos.getX() && objectPos.getX() <= Window.getInstance().getWidth() + offset){
            if(-offset <= objectPos.getY() && objectPos.getY() <= Window.getInstance().getHeight() + offset){
                check = true;
            }
        }

        return check;
    }
}
