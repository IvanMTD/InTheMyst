package ru.phoenix.game.content.object.active;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.loader.ImageAnimLoader;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.sprite.TextureConfig;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.ObjectControl;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.movement.MotionAnimation;
import ru.phoenix.game.logic.movement.PathfindingAlgorithm;

import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_R;

public class Person extends ObjectControl implements Object {
    private int event;
    private final int PREPARED_AREA         = 0x90001;
    private final int CREATE_PATH           = 0x90002;
    private final int MOVEMENT_ANIMATION    = 0x90003;

    /*
    IDLE TEXTURES ARRAYS NUMBER
    0 - Stand
    1 - Walk
    ...
    */
    // animation
    private ImageAnimation stand;
    private ImageAnimation walk;
    private ImageAnimation jump;
    // textures
    private List<Texture> textures;
    private TextureConfig standConfig;
    private TextureConfig walkConfig;
    private TextureConfig jumpConfig;
    // parametri
    private Characteristic characteristic;
    // control
    private List<Vector3f> wayPoints;
    private int sampleData;
    private boolean action;

    private PathfindingAlgorithm pathfindingAlgorithm;
    private MotionAnimation motionAnimation;

    private float dirX;
    private boolean standControl;
    private int count;

    // конструкторы
    public Person(float id){
        super();

        standConfig = Default.getStandIdle(id);
        walkConfig = Default.getWalkIdle(id);
        jumpConfig = Default.getJumpIdle(id);

        Texture person_stand = new Texture2D();
        assert standConfig != null;
        person_stand.setup(null, standConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_walk = new Texture2D();
        assert walkConfig != null;
        person_walk.setup(null, walkConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_jump = new Texture2D();
        assert jumpConfig != null;
        person_jump.setup(null, jumpConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        textures = new ArrayList<>(Arrays.asList(person_stand,person_walk,person_jump));

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
        motionAnimation = new MotionAnimation();
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
        motionAnimation = new MotionAnimation();
        wayPoints = new ArrayList<>();

        standConfig = Default.getStandIdle(getId());
        walkConfig = Default.getWalkIdle(getId());
        jumpConfig = Default.getJumpIdle(getId());
    }

    // методы
    public void init(Matrix4f[] matrix){
        event = 0;
        int currentTexture = 0;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = standConfig.getRow();
        int column = standConfig.getColumn();
        float objectWidth = 2.0f;
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        stand = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        stand.setBlock(true);
        currentTexture = 1;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = walkConfig.getRow();
        column = walkConfig.getColumn();
        objectWidth = (texWid / row) * objectHeight / (texHei / column);
        objectHeight = (texHei / column) * objectWidth / (texWid / row);
        walk = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        currentTexture = 2;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = jumpConfig.getRow();
        column = jumpConfig.getColumn();
        objectWidth = (texWid / row) * objectHeight / (texHei / column);
        objectHeight = (texHei / column) * objectWidth / (texWid / row);
        jump = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        setup(textures,stand,0);
        standControl = false;
        count = 0;
    }

    public void update(List<GridElement> gridElements){

        if(Math.random() * 100.0f <= 0.4f && !standControl){
            stand.setFrames(2);
            standControl = true;
        }else{
            if(!standControl) {
                stand.setFrames(1);
            }else{
                count++;
                if(count > 20){
                    standControl =false;
                    count = 0;
                }
            }
        }

        if(Default.isWait()){
            if(action) {
                switch (event){
                    case PREPARED_AREA:
                        pathfindingAlgorithm = new PathfindingAlgorithm();
                        pathfindingAlgorithm.setup(gridElements,characteristic,getPosition());
                        pathfindingAlgorithm.start();
                        event = CREATE_PATH;
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
                                        motionAnimation.setup(getPosition(), wayPoints);
                                        for (GridElement element : gridElements) {
                                            if (element.getPosition().equals(getPosition())) {
                                                element.setBlocked(false);
                                            }
                                        }
                                        walk.setCurrentAnimation(1);
                                        jump.setCurrentAnimation(1);
                                        event = MOVEMENT_ANIMATION;
                                        setJump(false);
                                    }
                                }
                            }
                        }
                        break;
                    case MOVEMENT_ANIMATION:
                        Vector3f position = new Vector3f(-1.0f,-1.0f,-1.0f);
                        int motion = motionAnimation.motion(gridElements,position,characteristic,jump);
                        if(!position.equals(new Vector3f(-1.0f,-1.0f,-1.0f))) {
                            setPosition(position);
                        }
                        /*if(getPosition().getX() < dirX){

                        }*/
                        if(motion == 1){
                            setJump(false);
                            updateAnimation(walk,1);
                        }else if(motion == 2){
                            setJump(true);
                            updateAnimation(jump,2);
                        }
                        if(motion == 0){
                            updateAnimation(stand,0);
                            setJump(false);
                            for(GridElement element : gridElements){
                                element.setVisible(false);
                                element.setWayPoint(false);
                                if(element.getPosition().equals(getPosition())){
                                    element.setBlocked(true);
                                }
                            }
                            if(characteristic.getCurentActionPoint() > 0){
                                event = PREPARED_AREA;
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
                    event = PREPARED_AREA;
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

        dirX = getPosition().getX();
    }

    public List<Texture> getTextures(){
        return textures;
    }

    private Vector3f getFinishPosition(List<GridElement> elements){
        Vector3f finishPos = null;
        for(GridElement element : elements){
            if(element.isTarget() && !element.isBlocked()){
                finishPos = element.getPosition();
                break;
            }
        }
        return finishPos;
    }

    private GridElement getFinishElement(List<GridElement> gridElements){
        GridElement finishElement = null;

        for(GridElement element : gridElements){
            if(element.isTarget()){
                finishElement = element;
                break;
            }
        }

        return finishElement;
    }
}