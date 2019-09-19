package ru.phoenix.game.content.object.active;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.generator.GraundGenerator;
import ru.phoenix.game.logic.movement.GridMaster;
import ru.phoenix.game.logic.movement.MoveAnimation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
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
    // parametri
    private Characteristic characteristic;
    // control
    private int sampleData;
    private boolean action;
    private int step;

    private GridMaster gridMaster;
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
        gridMaster = new GridMaster();
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
        gridMaster = new GridMaster();
        moveAnimation = new MoveAnimation();
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
                    gridMaster.createMoveGrid(getPosition(),characteristic);
                    gridMaster.run();
                    step++;
                }else if(step == 1){
                    Vector3f finishPos = null;
                    for(GridElement element : GraundGenerator.getGridElements()){
                        if(element.isTarget()){
                            finishPos = new Vector3f(element.getPosition());
                        }
                    }

                    if(finishPos != null) {
                        if(!gridMaster.isAlive()) {
                            gridMaster.createPath(getPosition(), finishPos, characteristic);
                            gridMaster.run();
                        }
                        if(Input.getInstance().isMousePressed(GLFW_MOUSE_BUTTON_LEFT)){
                            for(GridElement element : GraundGenerator.getGridElements()){
                                if(element.getPosition().equals(getPosition())){
                                    element.setBlocked(false);
                                }
                            }
                            step++;
                            moveAnimation.setup(getPosition(),GraundGenerator.getWayPoints());
                        }
                    }else{
                        for(GridElement element : GraundGenerator.getGridElements()){
                            element.setWayPoint(false);
                        }
                        GraundGenerator.getWayPoints().clear();
                    }
                }else if(step == 2){
                    Vector3f pos = new Vector3f();
                    boolean end = moveAnimation.move(pos,characteristic);
                    setPosition(pos);
                    if(end){
                        for(GridElement element : GraundGenerator.getGridElements()){
                            if(element.getPosition().equals(getPosition())){
                                element.setBlocked(true);
                            }
                        }
                        if(characteristic.getCurentActionPoint() != 0) {
                            step = 0;
                        }else {
                            step++;
                        }
                    }
                }else if(step == 3){
                    Default.setWait(false);
                    this.action = false;
                    step = 0;
                    characteristic.setCurentActionPoint(characteristic.getTotalActionPoint());
                    for(GridElement element : GraundGenerator.getGridElements()){
                        element.setVisible(false);
                        element.setWayPoint(false);
                    }
                }
            }
        }else{
            if(sampleData != Time.getSecond()){
                characteristic.setInitiative(characteristic.getInitiative() + characteristic.getInitiativeCharge());
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