package ru.phoenix.game.logic.element;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.GROUP_A;
import static ru.phoenix.core.config.Constants.GROUP_G;

public class GridElement {
    // came from
    private GridElement element;
    private int step;
    private int cost;
    // vbo
    private boolean visible;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;
    // textures
    private Texture cursorOnGraund;
    private Texture grayZona;
    private Texture redZona;
    private Texture greenZona;
    private Texture goldZona;
    private Texture blueZona;
    private Texture tempTexture;
    private Texture pointTexture;
    private Texture texture;
    private boolean isBlueZona;
    private boolean isGoldZona;
    // recognize info
    private float id;
    private boolean target;
    // block info
    private float distance;
    private Block block;
    private float currentHeight;
    // block control
    private boolean bevel;
    private float bevelDirection;
    private boolean blocked;
    private boolean occupied;
    private boolean water;
    private boolean wayPoint;

    private boolean skip;
    private boolean isCursor;

    public GridElement(float id, Vector3f position, Block block, boolean bevel, float bevelDirection, boolean blocked, Texture gray, Texture red, Texture green, Texture gold, Texture blue, Texture cursor) {
        cursorOnGraund = cursor;
        grayZona = gray;
        redZona = red;
        greenZona = green;
        goldZona = gold;
        blueZona = blue;

        setSkip(false);

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
        setOccupied(false);
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

    public void update(Vector3f pixel){
        if(pixel.getY() == id){
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

    public boolean isSkip() {
        return !skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
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
        tempTexture = grayZona;
        isBlueZona = false;
        isGoldZona = false;
    }

    public void setRedZona(){
        texture = redZona;
        pointTexture = redZona;
    }

    public void setGreenZona(){
        texture = greenZona;
        pointTexture = greenZona;
    }

    public void setGoldZona() {
        texture = goldZona;
        tempTexture = goldZona;
        isGoldZona = true;
    }

    public  void setBlueZona(){
        texture = blueZona;
        tempTexture = blueZona;
        isBlueZona = true;
    }

    public boolean isBlueZona() {
        return isBlueZona;
    }

    public boolean isGoldZona() {
        return isGoldZona;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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

    public int getEstimateFullPathLength(){
        return getStep() + getCost();
    }

    public boolean isCursor() {
        return isCursor;
    }

    public void setCursor(boolean cursor) {
        isCursor = cursor;
    }

    public int getTravelCost(){
        int cost = block.getCost();

        if(isWater()){
            cost *= 3;
        }

        return cost;
    }

    public void draw(Shader shader){
        if(isCursor){
            drawCursor(shader);
        }else {
            if (isVisible()) {
                if (isWayPoint() || isTarget()) {
                    this.texture = this.pointTexture;
                } else {
                    this.texture = this.tempTexture;
                }
                // контролеры
                shader.setUniform("instance", 0);
                shader.setUniform("board", 0);
                shader.setUniform("grid", 1);
                shader.setUniform("isActive", 0);
                shader.setUniform("noPaint", 0);
                // доп данные
                shader.setUniform("model_m", projection.getModelMatrix());
                shader.setUniform("xOffset", 0.0f);
                shader.setUniform("yOffset", 0.0f);
                shader.setUniform("zOffset", 0.0f);
                shader.setUniform("group", GROUP_G);
                shader.setUniform("id", getId());
                shader.setUniform("onTarget", isTarget() ? 1 : 0);
                shader.setUniform("water", 0);
                // end

                if (texture == null) {
                    texture = grayZona;
                }

                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
                shader.setUniform("image", 0);

                vbo.draw();
            }
        }
    }

    private void drawCursor(Shader shader){
        Default.setCursorAngle(Default.getCursorAngle() + 1.0f);
        if(Default.getCursorAngle() > 359.0f){
            Default.setCursorAngle(0.0f);
        }
        Projection tempProjection = new Projection();
        Matrix4f matrix = new Matrix4f(projection.getModelMatrix());
        tempProjection.setModelMatrix(matrix);
        tempProjection.setTranslation(new Vector3f(0.0f,0.1f,0.0f));
        tempProjection.setRotation(Default.getCursorAngle(),new Vector3f(0.0f,1.0f,0.0f));
        tempProjection.setScaling(0.7f);
        // контролеры
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 0);
        shader.setUniform("grid",1);
        shader.setUniform("isActive", 0);
        shader.setUniform("bigTree",0);
        shader.setUniform("noPaint",0);
        // доп данные
        shader.setUniform("model_m", tempProjection.getModelMatrix());
        shader.setUniform("xOffset", 0.0f);
        shader.setUniform("yOffset", 0.0f);
        shader.setUniform("zOffset", 0.0f);
        shader.setUniform("group", GROUP_A);
        shader.setUniform("id", 0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("water",0);
        // end

        texture = cursorOnGraund;

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);

        vbo.draw();
    }

    public void invisibleDraw(Shader shader){
        if(isWayPoint() || isTarget()){
            this.texture = this.pointTexture;
        }else{
            this.texture = this.tempTexture;
        }
        // контролеры
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 0);
        shader.setUniform("grid",1);
        shader.setUniform("isActive", 0);
        shader.setUniform("noPaint",1);
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

        if(texture == null){
            texture = grayZona;
        }

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);

        vbo.draw();
    }
}
