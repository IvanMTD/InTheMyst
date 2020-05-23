package ru.phoenix.game.content.object.passive;

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
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.MOUNTAIN_AREA;

public class Weed extends ObjectControl implements Object {

    private List<Texture> textures;

    private boolean apply;
    private int textureNum;

    private float objectWidth;
    private float objectHeight;

    public Weed(){
        super();
        apply = true;
        Texture weed_grass_1 = new Texture2D();
        Texture weed_grass_2 = new Texture2D();
        Texture weed_grass_3 = new Texture2D();
        Texture weed_grass_4 = new Texture2D();
        Texture weed_grass_5 = new Texture2D();
        weed_grass_1.setup(null,"./data/content/texture/grass/grass01.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // луг
        weed_grass_2.setup(null,"./data/content/texture/grass/grass02.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // луг
        weed_grass_3.setup(null,"./data/content/texture/grass/grass03.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // степной
        weed_grass_4.setup(null,"./data/content/texture/grass/grass04.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // лес
        weed_grass_5.setup(null,"./data/content/texture/grass/grass05.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER); // лес
        textures = new ArrayList<>(Arrays.asList(weed_grass_1, weed_grass_2, weed_grass_3,weed_grass_4,weed_grass_5));
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    public Weed(int type){
        super();
        apply = true;
        Texture weed = new Texture2D();

        if(type == 0) {
            weed.setup(null, "./data/content/texture/grass/grass01.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // луг
        }else if(type == 1) {
            weed.setup(null, "./data/content/texture/grass/grass02.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // луг
        }else if(type == 2) {
            weed.setup(null, "./data/content/texture/grass/grass03.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // степной
        }else if(type == 3) {
            weed.setup(null, "./data/content/texture/grass/grass04.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // лес
        }else if(type == 4) {
            weed.setup(null, "./data/content/texture/grass/grass05.png", GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER); // лес
        }else{
            apply = false;
        }

        textures = new ArrayList<>();
        textures.add(weed);
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
        setSimple(true);
    }

    public Weed(Weed object){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());
        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    public Weed(Weed object, float height){
        super();
        apply = true;
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

    public Weed(Weed object, int type){
        super();
        apply = true;
        this.textures = new ArrayList<>(object.getTextures());

        if(type == 1){
            Texture temp = textures.get(2);
            textures.clear();
            textures.add(temp);
        }else if(type == 2){
            Texture temp1 = textures.get(2);
            Texture temp2 = textures.get(3);
            textures.clear();
            textures.add(temp1);
            textures.add(temp2);
        }else if(type == 3){
            Texture temp1 = textures.get(0);
            Texture temp2 = textures.get(1);
            textures.clear();
            textures.add(temp1);
            textures.add(temp2);
        }else if(type == 4){
            Texture temp1 = textures.get(0);
            Texture temp2 = textures.get(1);
            Texture temp3 = textures.get(4);
            textures.clear();
            textures.add(temp1);
            textures.add(temp2);
            textures.add(temp3);
        }else if(type == 5){
            Texture temp1 = textures.get(3);
            Texture temp2 = textures.get(4);
            textures.clear();
            textures.add(temp1);
            textures.add(temp2);
        }else{
            apply = false;
        }

        setId(0.0f);
        setOnTarget(false);
        setBoard(true);
        setAnimated(true);
    }

    @Override
    public void init(Matrix4f[] matrix){
        int currentTexture = (int)Math.floor(Math.random() * (textures.size() - 0.1f));
        textureNum = currentTexture;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 4;
        int column = 1;
        objectWidth = 1.0f;
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
