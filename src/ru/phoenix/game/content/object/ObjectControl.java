package ru.phoenix.game.content.object;

import ru.phoenix.core.loader.ImageAnimLoader;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class ObjectControl {
    // vbo
    private List<Texture> textures;
    private ImageAnimation sprite;
    private Projection projection;
    private Vector3f position;
    // animated control
    private int currentTexture;
    private float distance;
    private float id;
    private int count;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    // shader control
    private boolean instance;
    private boolean animated;
    private boolean onTarget;
    private boolean board;
    private boolean shadow;
    private boolean active;

    public ObjectControl(){
        textures = new ArrayList<>();
        projection = new Projection();
        position = new Vector3f();
        currentTexture = 1;
        distance = 0;
        count = 0;
        xOffset = 0.0f;
        yOffset = 0.0f;
        zOffset = 0.0f;
        instance = false;
        animated = false;
        shadow = false;
        active = false;
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

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public boolean isOnTarget() {
        return onTarget;
    }

    public void setOnTarget(boolean onTarget) {
        this.onTarget = onTarget;
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

    public void draw(Shader shader){
        if(instance) {
            sprite.updateInstanceMatrix();
        }
        // setUniforms
        shader.useProgram();
        // глобальный юниформ
        shader.setUniformBlock("matrices",0);
        // контролеры
        shader.setUniform("instance",sprite.getVbo().isInstances() ? 1 : 0);
        shader.setUniform("animated",0);
        shader.setUniform("board",isBoard() ? 1 : 0);
        shader.setUniform("isActive",isActive() ? 1 : 0);
        // доп данные
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("xOffset",xOffset);
        shader.setUniform("yOffset",yOffset);
        shader.setUniform("zOffset",zOffset);
        shader.setUniform("group",GROUP_A);
        shader.setUniform("id",id);
        shader.setUniform("onTarget", isOnTarget() ? 1 : 0);
        // end
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textures.get(currentTexture).getTextureID());
        shader.setUniform("image",0);

        sprite.draw();

        if(animated) {
            if(isBoard()) {
                if (count > 10.0f + Math.random() * 10.0f) {
                    sprite.nextFrame();
                    count = 0;
                }
                count++;
            }else{
                if (count > 20.0f) {
                    sprite.nextFrame();
                    count = 0;
                }
                count++;
            }
        }
    }
}
