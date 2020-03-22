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
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class Bush extends ObjectControl implements Object {
    private List<Texture> textures;

    private boolean apply;

    public Bush(){
        super();
        apply = true;
        Texture bush_1 = new Texture2D();
        Texture bush_2 = new Texture2D();
        Texture bush_3 = new Texture2D();
        Texture bush_4 = new Texture2D();
        Texture bush_5 = new Texture2D();

        bush_1.setup(null,"./data/content/texture/bush/tree05.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // равнинное
        bush_2.setup(null,"./data/content/texture/bush/tree07.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // елочка
        bush_3.setup(null,"./data/content/texture/bush/tree02.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // равнинное
        bush_4.setup(null,"./data/content/texture/bush/tree03.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // мертвое
        bush_5.setup(null,"./data/content/texture/bush/tree04.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // мертвое

        textures = new ArrayList<>(Arrays.asList(bush_1,bush_2,bush_3,bush_4,bush_5));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setTree(true);
    }

    public Bush(Bush object){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setTree(true);
    }

    public Bush(Bush object, float currentHeight){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());

        if(23.0f < currentHeight && currentHeight < 27.0f){
            Texture temp1 = textures.get(0);
            Texture temp2 = textures.get(2);
            textures.clear();
            textures.add(temp1);
            textures.add(temp2);
        }else if(27.0f < currentHeight && currentHeight < 40.0f){
            Texture temp1 = textures.get(1);
            textures.clear();
            textures.add(temp1);
        }else if(40.0f < currentHeight && currentHeight < 43.0f){
            textures.remove(2);
            textures.remove(0);
        }else{
            apply = false;
        }

        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setTree(true);
    }

    @Override
    public void init(Matrix4f[] matrix){
        int currentTexture = (int)Math.floor(Math.random() * (textures.size() - 0.1f));
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 4;
        int column = 1;
        float objectWidth = (float)(1.2f + Math.random() * 0.3f);
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
