package ru.phoenix.game.content.object.water;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class WaterLine extends ObjectControl implements Object {

    private List<Texture> textures;

    private int offsetInfo;

    private boolean apply;

    public WaterLine(){
        super();
        apply = true;
        Texture waterLine = new Texture2D();
        waterLine.setup(null,"./data/content/texture/water/waterSet.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>(Collections.singletonList(waterLine));
        setId(0.0f);
        setOnTarget(false);
        setBoard(false);
        setAnimated(true);
        setWater(true);
        offsetInfo = CENTER_BOARD;
    }

    public WaterLine(WaterLine object, int offsetInfo){
        super();
        apply = true;
        textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(false);
        setAnimated(true);
        setWater(true);
        this.offsetInfo = offsetInfo;
    }

    public WaterLine(int offsetInfo){
        super();
        apply = true;
        Texture waterLine = new Texture2D();
        waterLine.setup(null,"./data/content/texture/water/waterSet.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>(Collections.singletonList(waterLine));
        setId(0.0f);
        setOnTarget(false);
        setBoard(false);
        setAnimated(true);
        setWater(true);
        this.offsetInfo = offsetInfo;
    }

    @Override
    public void init(Matrix4f[] matrix){
        int currentTexture = 0;
        int row = 10;
        int column = 1;
        float objectWidth = 1.0f;
        float objectHeight = 1.0f;
        if(matrix != null){
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),matrix, 1);
        }else{
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null, 1);
        }
    }

    @Override
    public void init(Matrix4f[] matrix, SaveElement saveElement) {

    }

    @Override
    public void update(Cell[][] grid, Vector3f pixel, Cell finishCell){
        switch (offsetInfo) {
            case CENTER_BOARD:
                setyOffset(Default.getOffset());
                break;
            case LEFT_BOARD:
                setxOffset(-Default.getOffset());
                break;
            case RIGHT_BOARD:
                setxOffset(Default.getOffset());
                break;
            case UP_BOARD:
                setzOffset(-Default.getOffset());
                break;
            case DOWN_BOARD:
                setzOffset(Default.getOffset());
                break;
        }
    }

    @Override
    public boolean isApplying(){
        return apply;
    }

    @Override
    public List<Texture> getTextures(){
        return textures;
    }

    @Override
    public int getRecognition() {
        return 0;
    }

    @Override
    public void setRecognition(int recognition) {

    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }

    @Override
    public boolean isBattle() {
        return false;
    }

    @Override
    public void setBattle(boolean battle) {

    }

    @Override
    public int getTextureNum() {
        return 0;
    }

    @Override
    public float getObjectWidth() {
        return 0;
    }

    @Override
    public void setObjectWidth(float objectWidth) {

    }

    @Override
    public float getObjectHeight() {
        return 0;
    }

    @Override
    public void setObjectHeight(float objectHeight) {

    }
}
