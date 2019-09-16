package ru.phoenix.game.content.object.active;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.movement.GridMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_R;

public class Person extends ObjectControl implements Object {

    /*
    IDLE TEXTURES ARRAYS NUMBER
    0 - Stand
    1 - Walk
    ...
    */

    private List<Texture> textures;

    private Characteristic characteristic;
    private GridMaster gridMaster;

    private int sampleData;
    private boolean action;
    private int step;

    // конструкторы
    public Person(float id){
        super();
        gridMaster = new GridMaster();

        Texture person_stand = new Texture2D();
        person_stand.setup(null, Default.getStandIdle(id),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        textures = new ArrayList<>(Arrays.asList(person_stand));

        characteristic = new Characteristic();

        setGroup(GROUP_R);
        setId(id);
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setActive(true);
        action = false;
        sampleData = Time.getSecond();
    }

    public Person(Person object){
        super();
        gridMaster = new GridMaster();

        textures = new ArrayList<>(object.getTextures());

        characteristic = new Characteristic();

        setGroup(GROUP_R);
        setId(object.getId());
        setOnTarget(false);
        setBoard(true);
        setShadow(true);
        setAnimated(true);
        setActive(true);
        action = false;
        sampleData = Time.getSecond();
    }

    // методы
    public void init(Matrix4f[] matrix){
        step = 0;
        int currentTexture = 0;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 3;
        int column = 1;
        float objectWidth = 2.0f;
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
    }

    public void update(){

        if(Default.isWait()){
            if(action) {
                if(step == 0){
                    gridMaster.preperMove(getPosition(),characteristic);
                    gridMaster.start();
                    step++;
                }
            }
        }else{
            if(sampleData != Time.getSecond()){
                characteristic.setInitiative(characteristic.getInitiative() + characteristic.getSpeed());
                if(characteristic.getInitiative() >= 100){
                    characteristic.setInitiative(0);
                    this.action = true;
                    Default.setWait(true);
                }
            }
            sampleData = Time.getSecond();
        }

        float id = Pixel.getPixel().getX();
        if(id == getId()){
            setOnTarget(true);
        }else{
            setOnTarget(false);
        }
    }

    public List<Texture> getTextures(){
        return textures;
    }
}