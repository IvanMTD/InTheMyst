package ru.phoenix.game.content.object.passive;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class Tree extends ObjectControl implements Object {
    private List<Texture> textures;

    private boolean apply;
    private int textureNum;

    private float objectWidth;
    private float objectHeight;

    public Tree(){
        super();
        apply = true;

        Texture tree_1 = new Texture2D();
        Texture tree_2 = new Texture2D();
        Texture tree_3 = new Texture2D();
        Texture tree_4 = new Texture2D();
        Texture tree_5 = new Texture2D();
        Texture tree_6 = new Texture2D();

        tree_1.setup(null,"./data/content/texture/tree/tree11.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // липа? 0
        tree_2.setup(null,"./data/content/texture/tree/tree12.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // сосна 1
        tree_3.setup(null,"./data/content/texture/tree/tree13.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // ель   2
        tree_4.setup(null,"./data/content/texture/tree/tree14.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // мертвая сосна 3
        tree_5.setup(null,"./data/content/texture/tree/tree15.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // мертвая ель 4
        tree_6.setup(null,"./data/content/texture/tree/tree16.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // кактус 5

        textures = new ArrayList<>(Arrays.asList(tree_1,tree_2,tree_3, tree_4,tree_5,tree_6));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setTree(true);
    }

    public Tree(int type){
        super();
        apply = true;

        Texture tree = new Texture2D();

        if(type == 0) {
            tree.setup(null, "./data/content/texture/tree/tree11.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // липа? 0
        }else if(type == 1) {
            tree.setup(null, "./data/content/texture/tree/tree12.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // сосна 1
        }else if(type == 2) {
            tree.setup(null, "./data/content/texture/tree/tree13.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // ель   2
        }else if(type == 3) {
            tree.setup(null, "./data/content/texture/tree/tree14.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // мертвая сосна 3
        }else if(type == 4) {
            tree.setup(null, "./data/content/texture/tree/tree15.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // мертвая ель 4
        }else if(type == 5) {
            tree.setup(null, "./data/content/texture/tree/tree16.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // кактус 5
        }else{
            apply = false;
        }

        textures = new ArrayList<>();
        textures.add(tree);
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setTree(true);
        setSimple(true);
    }

    public Tree(Tree object){
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

    public Tree(Tree object, float currentHeight){
        super();
        apply = true;
        textureNum = 0;
        this.textures = new ArrayList<>(object.getTextures());
        if(23.0f < currentHeight && currentHeight < 27.0f){
            Texture temp = textures.get(0);
            textures.clear();
            textures.add(temp);
        }else if(27.0f < currentHeight && currentHeight < 42.0f){
            textures.remove(5);
            textures.remove(0);
        }else if(3.0f < currentHeight && currentHeight < 10.0f){
            Texture temp = textures.get(5);
            textures.clear();
            textures.add(temp);
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
        textureNum = currentTexture;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 4;
        int column = 1;
        objectWidth = (float)(3.5f + Math.random() * 1.5f);
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
        int row = 4;
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
