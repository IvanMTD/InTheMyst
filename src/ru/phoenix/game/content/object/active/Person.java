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
import ru.phoenix.game.logic.movement.MoveAnimation;
import ru.phoenix.game.logic.movement.PathfindingAlgorithm;

import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_R;

public class Person extends ObjectControl implements Object {
    private int event;
    private final int PREPARED_ACTION       = 0x90001;
    private final int CRATE_AREA            = 0x90002;
    private final int CREATE_PATH           = 0x90003;
    private final int MOVEMENT_ANIMATION    = 0x90004;

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
    private List<Vector3f> wayPoints;
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
        wayPoints = new ArrayList<>();
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
        wayPoints = new ArrayList<>();
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
                            event = CREATE_PATH;
                        }
                        break;
                    case CREATE_PATH:
                        if(!pathfindingAlgorithm.isAlive()) {
                            pathfindingAlgorithm = new PathfindingAlgorithm();
                            pathfindingAlgorithm.setup(gridElements,characteristic,getPosition(),getFinishPosition(gridElements));
                            pathfindingAlgorithm.start();
                            if(Input.getInstance().isMousePressed(GLFW_MOUSE_BUTTON_LEFT)){
                                wayPoints.clear();
                                GridElement finishElement = getFinishElement(gridElements);
                                if(finishElement != null) {
                                    List<Vector3f> reverse = new ArrayList<>();
                                    while (finishElement.cameFrom() != null) {
                                        reverse.add(finishElement.getPosition());
                                        finishElement = finishElement.cameFrom();
                                    }
                                    for (int i = reverse.size() - 1; i >= 0; i--) {
                                        wayPoints.add(reverse.get(i));
                                    }
                                    if(!wayPoints.isEmpty()) {
                                        moveAnimation.setup(getPosition(), wayPoints);
                                        for (GridElement element : gridElements) {
                                            if (element.getPosition().equals(getPosition())) {
                                                element.setBlocked(false);
                                            }
                                        }
                                        event = MOVEMENT_ANIMATION;
                                    }
                                }
                            }
                        }
                        break;
                    case MOVEMENT_ANIMATION:
                        Vector3f position = new Vector3f();
                        boolean end = moveAnimation.move(position,characteristic);
                        setPosition(position);
                        if(end){
                            for(GridElement element : gridElements){
                                element.setVisible(false);
                                element.setWayPoint(false);
                                if(element.getPosition().equals(getPosition())){
                                    element.setBlocked(true);
                                }
                            }
                            if(characteristic.getCurentActionPoint() > 0){
                                event = PREPARED_ACTION;
                            }else {
                                characteristic.setCurentActionPoint(characteristic.getTotalActionPoint());
                                Default.setWait(false);
                                this.action = false;
                            }
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

    private Vector3f getFinishPosition(List<GridElement> elements){
        Vector3f finishPos = null;
        for(GridElement element : elements){
            if(element.isTarget()){
                finishPos = element.getPosition();
            }
        }
        return finishPos;
    }

    private GridElement getFinishElement(List<GridElement> gridElements){
        GridElement finishElement = null;

        for(GridElement element : gridElements){
            if(element.isTarget()){
                finishElement = element;
            }
        }

        return finishElement;
    }
}