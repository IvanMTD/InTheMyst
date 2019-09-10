package ru.phoenix.game.content.object.passive;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.MOUNTAIN_MAP;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;

public class Tree extends ObjectControl implements Object {
    private List<Texture> textures;

    public Tree(){
        super();
        Texture tree_1 = new Texture2D();
        Texture tree_2 = new Texture2D();
        Texture tree_3 = new Texture2D();
        Texture tree_4 = new Texture2D();
        Texture tree_5 = new Texture2D();

        tree_1.setup(null,"./data/content/texture/tree/tree01.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        tree_2.setup(null,"./data/content/texture/tree/tree12.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        tree_3.setup(null,"./data/content/texture/tree/tree13.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        tree_4.setup(null,"./data/content/texture/tree/tree11.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        // мертовые деревья!!!
        tree_5.setup(null,"./data/content/texture/tree/tree08.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>(Arrays.asList(tree_1,tree_2,tree_3,tree_4, tree_5));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
    }

    public Tree(Tree object){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
    }

    public Tree(Tree object, int seed){
        super();
        this.textures = new ArrayList<>(object.getTextures());
        if(seed == PLAIN_MAP) {
            if (Math.random() * 100.0f <= 99.0f) {
                this.textures.remove(4);
            }
        }else if(seed == MOUNTAIN_MAP){
            textures.remove(3);
            textures.remove(0);
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
        float objectWidth = (float)(3.5f + Math.random() * 1.5f);
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        if(matrix != null){
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),matrix);
        }else{
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
        }
    }

    @Override
    public void update(){

    }

    @Override
    public List<Texture> getTextures(){
        return textures;
    }
}
