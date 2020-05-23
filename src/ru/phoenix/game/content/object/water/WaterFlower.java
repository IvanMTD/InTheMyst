package ru.phoenix.game.content.object.water;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class WaterFlower extends ObjectControl implements Object {
    private List<Texture> textures;

    private boolean apply;
    private int textureNum;

    private float objectWidth;
    private float objectHeight;

    public WaterFlower(){
        super();
        apply = true;
        Texture water_flower_1 = new Texture2D();
        water_flower_1.setup(null,"./data/content/texture/water/waterflower.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>(Collections.singletonList(water_flower_1));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(false);
    }

    public WaterFlower(WaterFlower object){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(false);
    }

    @Override
    public void init(Matrix4f[] matrix){
        int currentTexture = (int)Math.floor(Math.random() * (textures.size() - 0.1f));
        textureNum = currentTexture;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 2;
        int column = 1;
        objectWidth = 0.8f;
        objectHeight = (texHei / column) * objectWidth / (texWid / row);
        if(matrix != null){
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),matrix);
        }else{
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
        }
    }

    @Override
    public void init(Matrix4f[] matrix, SaveElement saveElement){
        int currentTexture = saveElement.getTextureNum();
        textureNum = currentTexture;
        int row = 2;
        int column = 1;
        objectWidth = saveElement.getObjectWidth();
        objectHeight = saveElement.getObjectHeight();
        if(matrix != null){
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),matrix);
        }else{
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
        }
    }

    @Override
    public void update(Cell[][] grid, Vector3f pixel, Cell finishCell){
        setyOffset(Default.getOffset());
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
        return textureNum;
    }

    @Override
    public float getObjectWidth() {
        return objectWidth;
    }

    @Override
    public void setObjectWidth(float objectWidth) {
        this.objectWidth = objectWidth;
    }

    @Override
    public float getObjectHeight() {
        return objectHeight;
    }

    @Override
    public void setObjectHeight(float objectHeight) {
        this.objectHeight = objectHeight;
    }
}
