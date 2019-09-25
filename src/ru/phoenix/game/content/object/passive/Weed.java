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

public class Weed extends ObjectControl implements Object {

    private List<Texture> textures;

    public Weed(){
        super();
        Texture weed_grass_1 = new Texture2D();
        Texture weed_grass_2 = new Texture2D();
        Texture weed_grass_3 = new Texture2D();
        Texture weed_grass_4 = new Texture2D();
        Texture weed_grass_5 = new Texture2D();
        weed_grass_1.setup(null,"./data/content/texture/grass/grass01.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        weed_grass_2.setup(null,"./data/content/texture/grass/grass02.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        weed_grass_3.setup(null,"./data/content/texture/grass/grass03.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        weed_grass_4.setup(null,"./data/content/texture/grass/grass04.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        weed_grass_5.setup(null,"./data/content/texture/grass/grass05.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>(Arrays.asList(weed_grass_1, weed_grass_2, weed_grass_3,weed_grass_4,weed_grass_5));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    public Weed(Weed object){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    public Weed(Weed object, float height){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        if(height <= -0.5f){
            Texture texture = textures.get(2);
            textures.clear();
            textures.add(texture);
        }
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    public Weed(Weed object, int seed){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        if(seed == MOUNTAIN_MAP){
            Texture texture = textures.get(2);
            textures.clear();
            textures.add(texture);
        }else if(seed == PLAIN_MAP){
            textures.remove(2);
        }
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    @Override
    public void init(Matrix4f[] matrix){
        int currentTexture = (int)Math.floor(Math.random() * (textures.size() - 0.1f));
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 4;
        int column = 1;
        float objectWidth = 1.0f;
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
