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
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.movement.MoveAnimation;
import ru.phoenix.game.logic.movement.PathfindingAlgorithm;

import java.util.*;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_R;

public class Person extends ObjectControl implements Object {
    private int event;
    private final int PREPARED_ACTION       = 0x90001;
    private final int CRATE_AREA            = 0x90002;
    private final int CREATE_PATH           = 0x90003;

    /*
    IDLE TEXTURES ARRAYS NUMBER
    0 - Stand
    1 - Walk
    ...
    */
    private List<Texture> textures;
    // parametri
    private Characteristic characteristic;
    // control
    private int sampleData;
    private boolean action;

    private PathfindingAlgorithm pathfindingAlgorithm;
    private MoveAnimation moveAnimation;

    // конструкторы
    public Person(float id){
        super();

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
        moveAnimation = new MoveAnimation();
    }

    public Person(Person object){
        super();

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
        moveAnimation = new MoveAnimation();
    }

    // методы
    public void init(Matrix4f[] matrix){
        event = 0;
        int currentTexture = 0;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = 3;
        int column = 1;
        float objectWidth = 2.0f;
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        setup(textures,row,column,objectWidth,objectHeight,currentTexture,new Vector3f(),null);
    }

    public void update(List<GridElement> gridElements){
        if(Default.isWait()){
            if(action) {
                switch (event){
                    case PREPARED_ACTION:
                        pathfindingAlgorithm = new PathfindingAlgorithm();
                        pathfindingAlgorithm.setup(gridElements,characteristic);
                        pathfindingAlgorithm.start();
                        event = CRATE_AREA;
                        break;
                    case CRATE_AREA:
                        if(!pathfindingAlgorithm.isAlive()) {
                            pathfindingAlgorithm = new PathfindingAlgorithm();
                            pathfindingAlgorithm.setup(gridElements,characteristic,getPosition());
                            pathfindingAlgorithm.start();
                            event = 0;
                        }
                        break;
                }
            }
        }else{
            if(sampleData != Time.getSecond()){
                characteristic.setInitiative(characteristic.getInitiative() + characteristic.getInitiativeCharge());
                if(characteristic.getInitiative() >= 100){
                    characteristic.setInitiative(0);
                    this.action = true;
                    event = PREPARED_ACTION;
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