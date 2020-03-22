package ru.phoenix.game.content.object.passive;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class BigTree extends ObjectControl implements Object {
    private List<Texture> textures;

    private boolean apply;

    public BigTree(){
        super();
        apply = true;
        Texture tree_1 = new Texture2D();
        Texture tree_2 = new Texture2D();

        tree_1.setup(null,"./data/content/texture/bigtree/tree01.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // Дуб
        tree_2.setup(null,"./data/content/texture/bigtree/tree08.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // Мертвый дуб

        textures = new ArrayList<>(Arrays.asList(tree_1,tree_2));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setBigTree(true);
        setTree(true);
    }

    public BigTree(BigTree object){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setBigTree(true);
        setTree(true);
    }

    public BigTree(BigTree object, float currentHeight){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());

        if(23.0f < currentHeight && currentHeight < 33.0f) {
            textures.remove(1);
        }else if(39.0f < currentHeight && currentHeight < 42.0f){
            textures.remove(0);
        }else{
            apply = false;
        }

        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setBigTree(true);
        setTree(true);
    }

    @Override
    public void init(Matrix4f[] matrix){
        int currentTexture = (int)Math.floor(Math.random() * (textures.size() - 0.1f));
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 4;
        int column = 1;
        float objectWidth = (float)(4.0f + Math.random() * 1.5f);
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        if(matrix != null){
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),matrix);
        }else{
            setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
        }
    }

    @Override
    public void update(Cell[][] grid, Vector3f pixel, Cell finishCell) {

    }

    @Override
    public boolean isApplying() {
        return apply;
    }

    @Override
    public List<Texture> getTextures() {
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
