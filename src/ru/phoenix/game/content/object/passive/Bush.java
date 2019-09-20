package ru.phoenix.game.content.object.passive;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.logic.element.GridElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.MOUNTAIN_MAP;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;

public class Bush extends ObjectControl implements Object {
    private List<Texture> textures;

    public Bush(){
        super();
        Texture bush_1 = new Texture2D();
        Texture bush_2 = new Texture2D();
        Texture bush_3 = new Texture2D();
        Texture bush_4 = new Texture2D();
        Texture bush_5 = new Texture2D();

        bush_1.setup(null,"./data/content/texture/bush/tree05.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        bush_2.setup(null,"./data/content/texture/bush/tree07.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        bush_3.setup(null,"./data/content/texture/bush/tree02.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        bush_4.setup(null,"./data/content/texture/bush/tree03.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        bush_5.setup(null,"./data/content/texture/bush/tree04.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        textures = new ArrayList<>(Arrays.asList(bush_1,bush_2,bush_3,bush_4,bush_5));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
    }

    public Bush(Bush object){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
    }

    public Bush(Bush object, int seed){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        if(seed == PLAIN_MAP){
            if(Math.random() * 100.0f <= 99.0f){
                textures.remove(4);
                textures.remove(3);
            }
        }else if(seed == MOUNTAIN_MAP){
            Texture temp = textures.get(3);
            Texture temp1 = textures.get(4);
            textures.clear();
            textures = new ArrayList<>(Arrays.asList(temp,temp1));
        }
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
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
    public void update(List<GridElement> gridElements){

    }

    @Override
    public List<Texture> getTextures(){
        return textures;
    }
}
