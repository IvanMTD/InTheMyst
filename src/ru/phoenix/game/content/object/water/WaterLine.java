package ru.phoenix.game.content.object.water;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class WaterLine extends ObjectControl implements Object {

    private List<Texture> textures;

    private int offsetInfo;

    public WaterLine(){
        super();
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
    public void update(){
        if(offsetInfo == CENTER_BOARD) {
            setyOffset(Default.getOffset());
        }else if(offsetInfo == LEFT_BOARD){
            setxOffset(-Default.getOffset());
        }else if(offsetInfo == RIGHT_BOARD){
            setxOffset(Default.getOffset());
        }else if(offsetInfo == UP_BOARD){
            setzOffset(-Default.getOffset());
        }else if(offsetInfo == DOWN_BOARD){
            setzOffset(Default.getOffset());
        }
    }

    @Override
    public List<Texture> getTextures(){
        return textures;
    }
}
