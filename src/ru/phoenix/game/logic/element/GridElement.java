package ru.phoenix.game.logic.element;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.GROUP_G;

public class GridElement {
    // came from
    private GridElement element;
    private int step;
    // vbo
    private boolean visible;
    private VertexBufferObject vbo;
    private Projection projection;
    // textures
    private Texture grayZona;
    private Texture redZona;
    private Texture greenZona;
    private Texture texture;
    // recognize info
    private float id;
    private boolean target;
    // block info
    private float distance;
    private Vector3f position;
    private Block block;
    private float currentHeight;
    // block control
    private boolean bevel;
    private float bevelDirection;
    private boolean blocked;
    private boolean water;
    private boolean wayPoint;
    // can go
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    public GridElement(float id, Vector3f position, Block block, boolean bevel, float bevelDirection, boolean blocked, Texture gray, Texture red, Texture green) {
        grayZona = gray;
        redZona = red;
        greenZona = green;

        setLeft(false);
        setUp(false);
        setRight(false);
        setDown(false);

        vbo = new MeshConfig();
        projection = new Projection();
        texture = new Texture2D();
        setVisible(false);
        setBlock(block);
        setPosition(position);
        setCurrentHeight(position.getY());
        setBevel(bevel,bevelDirection);
        setBlocked(blocked);
        setWayPoint(false);
        setId(id);
        target = false;

        float[] pos = new float[]{
                -0.5f,  0.01f,  0.5f,
                -0.5f,  0.01f, -0.5f,
                 0.5f,  0.01f, -0.5f,
                 0.5f,  0.01f,  0.5f
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

        vbo.allocate(pos,null,tex,null,null,null,null,null,indices);

        projection.getModelMatrix().identity();
        projection.setTranslation(getPosition());
        if(bevel){
            projection.setRotation(this.bevelDirection, new Vector3f(0.0f, 1.0f, 0.0f));
            projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
        }
        setGrayZona();
    }

    public void update(){
        if(Pixel.getPixel().getY() == id){
            setTarget(true);
        }else{
            setTarget(false);
        }
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public Vector3f getPosition() {
        return position;
    }

    private void setPosition(Vector3f position) {
        this.position = position;
    }

    public Block getBlock() {
        return block;
    }

    private void setBlock(Block block) {
        this.block = block;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
        projection.getModelMatrix().identity();
        projection.setTranslation(new Vector3f(position.getX(),getCurrentHeight(),position.getZ()));
        if(bevel){
            projection.setRotation(bevelDirection, new Vector3f(0.0f, 1.0f, 0.0f));
            projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
        }
    }

    public boolean isBevel() {
        return bevel;
    }

    private void setBevel(boolean bevel, float bevelDirection) {
        this.bevel = bevel;
        this.bevelDirection = bevelDirection;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isWayPoint() {
        return wayPoint;
    }

    public void setWayPoint(boolean wayPoint) {
        this.wayPoint = wayPoint;
    }

    public void setGrayZona(){
        texture = grayZona;
    }

    public void setRedZona(){
        texture = redZona;
    }

    public void setGreenZona(){
        texture = greenZona;
    }

    public GridElement cameFrom() {
        return element;
    }

    public void setCameFromElement(GridElement element){
        this.element = element;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getTravelCost(){
        int cost = block.getCost();

        if(isWater()){
            cost *=3;
        }

        return cost;
    }

    public void clearDirection(){
        setCameFromElement(null);
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void draw(Shader shader){
        if(isVisible()) {
            if(isWayPoint() || isTarget()){
                setGreenZona();
            }else{
                setGrayZona();
            }
            // контролеры
            shader.setUniform("instance", 0);
            shader.setUniform("animated", 0);
            shader.setUniform("board", 0);
            shader.setUniform("isActive", 0);
            // доп данные
            shader.setUniform("model_m", projection.getModelMatrix());
            shader.setUniform("xOffset", 0.0f);
            shader.setUniform("yOffset", 0.0f);
            shader.setUniform("zOffset", 0.0f);
            shader.setUniform("group", GROUP_G);
            shader.setUniform("id", getId());
            shader.setUniform("onTarget", isTarget() ? 1 : 0);
            shader.setUniform("water",0);
            // end
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
            shader.setUniform("image", 0);

            glEnable(GL_CULL_FACE);
            glFrontFace(GL_CW);
            glCullFace(GL_BACK);
            vbo.draw();
            glDisable(GL_CULL_FACE);
        }
    }
}
