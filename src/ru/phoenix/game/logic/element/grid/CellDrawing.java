package ru.phoenix.game.logic.element.grid;

import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.GROUP_A;
import static ru.phoenix.core.config.Constants.GROUP_G;

abstract class CellDrawing extends CellManagement {
    // буфер вершин и матрицы проекции
    private Mesh mesh;
    private Projection projection;
    // текстуры
    // основные текстуры хранилище
    private Texture cursorOnGraund;
    private Texture grayZona;
    private Texture redZona;
    private Texture greenZona;
    private Texture goldZona;
    private Texture blueZona;
    // вспомогательные текстуры контроля
    private Texture tempTexture;
    private Texture pointTexture;
    private Texture texture;

    // конструктор класса
    // начало
    CellDrawing(){
        super();
        // матрицы проекции
        projection = new Projection();
        // текстуры
        // основные текстуры хранилище
        cursorOnGraund = new Texture2D();
        grayZona = new Texture2D();
        redZona = new Texture2D();
        greenZona = new Texture2D();
        goldZona = new Texture2D();
        blueZona = new Texture2D();
        // вспомогательные текстуры контроля
        tempTexture = new Texture2D();
        pointTexture = new Texture2D();
        texture = new Texture2D();
    }

    CellDrawing(Cell cell){
        super(cell);
        // матрицы проекции
        setupMesh(cell.getMesh());
        projection = cell.getProjection();
        // текстуры
        // основные текстуры хранилище
        cursorOnGraund = cell.getCursorOnGraund();
        grayZona = cell.getGrayZona();
        redZona = cell.getRedZona();
        greenZona = cell.getGreenZona();
        goldZona = cell.getGoldZona();
        blueZona = cell.getBlueZona();
        // вспомогательные текстуры контроля
        tempTexture = cell.getTempTexture();
        pointTexture = cell.getPointTexture();
        texture = cell.getTexture();
    }
    // конец

    // методы первичной установки
    // начало
    public void setupMesh(Mesh mesh){
        this.mesh = mesh;
    }

    public void setupTexture(Texture cursor, Texture gray, Texture red, Texture green, Texture gold, Texture blue){
        // текстуры
        // основные текстуры хранилище
        cursorOnGraund = cursor;
        grayZona = gray;
        redZona = red;
        greenZona = green;
        goldZona = gold;
        blueZona = blue;
        setGrayZona();
    }
    // конец

    // методы контроля отрисовки
    // начало
    public void setGrayZona(){
        texture = grayZona;
        tempTexture = grayZona;
        pointTexture = grayZona;
        setBlueZona(false);
        setGoldZona(false);
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
        setGoldZona(true);
        setBlueZona(false);
    }

    public  void setBlueZona(){
        texture = blueZona;
        tempTexture = blueZona;
        setBlueZona(true);
        setGoldZona(false);
    }
    // конец

    // методы получения данных
    // начало

    public Mesh getMesh() {
        return mesh;
    }

    public Projection getProjection() {
        return projection;
    }

    public Texture getCursorOnGraund() {
        return cursorOnGraund;
    }

    public Texture getGrayZona() {
        return grayZona;
    }

    public Texture getRedZona() {
        return redZona;
    }

    public Texture getGreenZona() {
        return greenZona;
    }

    public Texture getGoldZona() {
        return goldZona;
    }

    public Texture getBlueZona() {
        return blueZona;
    }

    public Texture getTempTexture() {
        return tempTexture;
    }

    public Texture getPointTexture() {
        return pointTexture;
    }

    public Texture getTexture() {
        return texture;
    }

    // конец

    // методы отрисовки
    // начало
    public void draw(Shader shader){
        if(isCursor()){
            drawCursor(shader);
        }else {
            if (isVisible()){
                if(isWayPoint() || isTarget()){
                    this.texture = this.pointTexture;
                }else{
                    this.texture = this.tempTexture;
                }
                if(texture == null){
                    texture = grayZona;
                }
                setUniforms(shader);
                mesh.draw();
            }
        }
    }

    private void drawCursor(Shader shader){
        texture = cursorOnGraund;
        // контролеры
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 0);
        shader.setUniform("grid", 1);
        shader.setUniform("isActive", 0);
        shader.setUniform("bigTree",0);
        shader.setUniform("noPaint", 0);
        shader.setUniform("discardReverse",0);
        shader.setUniform("discardControl",-1.0f);
        // доп данные
        projection.getModelMatrix().identity();
        if(getPosition().getY() != getCurrentHeight()){
            projection.setTranslation(new Vector3f(0.0f,(getCurrentHeight() - getPosition().getY()) + 0.02f,0.0f));
        }else{
            projection.setTranslation(new Vector3f(0.0f,0.02f,0.0f));
        }
        shader.setUniform("model_m", projection.getModelMatrix());
        shader.setUniform("xOffset", 0.0f);
        shader.setUniform("yOffset", 0.0f);
        shader.setUniform("zOffset", 0.0f);
        shader.setUniform("group", GROUP_A);
        shader.setUniform("id", 0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("water", 0);
        // текстуры
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        // end
        mesh.draw();
    }

    private void setUniforms(Shader shader){
        // контролеры
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 0);
        shader.setUniform("grid", 1);
        shader.setUniform("isActive", 0);
        shader.setUniform("bigTree",0);
        shader.setUniform("noPaint", 0);
        shader.setUniform("discardReverse",0);
        shader.setUniform("discardControl",-1.0f);
        shader.setUniform("battlefield",0);
        // доп данные
        projection.getModelMatrix().identity();
        if(getPosition().getY() != getCurrentHeight()){
            projection.setTranslation(new Vector3f(0.0f,(getCurrentHeight() - getPosition().getY()) + 0.01f,0.0f));
        }else{
            projection.setTranslation(new Vector3f(0.0f,0.01f,0.0f));
        }
        shader.setUniform("model_m", projection.getModelMatrix());
        shader.setUniform("xOffset", 0.0f);
        shader.setUniform("yOffset", 0.0f);
        shader.setUniform("zOffset", 0.0f);
        shader.setUniform("group", GROUP_G);
        shader.setUniform("id", getId());
        shader.setUniform("onTarget", isTarget() ? 1 : 0);
        shader.setUniform("water", 0);
        // текстуры
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        // end
    }
    // конец
}
