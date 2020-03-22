package ru.phoenix.game.content.object.passive;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class LittleThing extends ObjectControl implements Object {
    private List<Texture> textures;

    private boolean apply;

    public LittleThing(){
        super();
        apply = true;
        Texture littleThing = new Texture2D();
        littleThing.setup(null,"./data/content/texture/items/little_things.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>();
        textures.add(littleThing);
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(false);
    }

    public LittleThing(LittleThing object){
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
        int currentTexture = 0;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 5;
        int column = 5;
        float objectWidth = (float)(0.5f + Math.random() * 0.3f);
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        if(matrix != null){
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),matrix);
        }else{
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
        }
    }

    @Override
    public void update(Cell[][] grid, Vector3f pixel, Cell finishCell){

    }

    @Override
    public boolean isApplying() {
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
}
