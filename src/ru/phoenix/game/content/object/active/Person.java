package ru.phoenix.game.content.object.active;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Camera;
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

import static org.lwjgl.glfw.GLFW.*;
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
    private ImageAnimation climbing;
    // textures
    private List<Texture> textures;
    private TextureConfig standConfig;
    private TextureConfig walkConfig;
    private TextureConfig jumpConfig;
    private TextureConfig climbingConfig;
    // parametri
    private Characteristic characteristic;
    // control
    private List<GridElement> wayPoints;
    private int sampleData;
    private boolean action;

    private PathfindingAlgorithm pathfindingAlgorithm;
    private MotionAnimation motionAnimation;

    private boolean standControl;
    private int count;

    private Vector3f tempFinish;
    private boolean firstStart;
    private float tempX;
    private float tempZ;

    // конструкторы
    public Person(float id, Vector3f position){
        super();

        setPosition(position);

        standConfig = Default.getStandIdle(id);
        walkConfig = Default.getWalkIdle(id);
        jumpConfig = Default.getJumpIdle(id);
        climbingConfig = Default.getClimbingIdle(id);

        Texture person_stand = new Texture2D();
        assert standConfig != null;
        person_stand.setup(null, standConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_walk = new Texture2D();
        assert walkConfig != null;
        person_walk.setup(null, walkConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_jump = new Texture2D();
        assert jumpConfig != null;
        person_jump.setup(null, jumpConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_climbing = new Texture2D();
        assert climbingConfig != null;
        person_climbing.setup(null,climbingConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        textures = new ArrayList<>(Arrays.asList(person_stand,person_walk,person_jump,person_climbing));

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

    public Person(Person object, float id, Vector3f position){
        super();

        setPosition(position);

        textures = new ArrayList<>(object.getTextures());

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

        standConfig = Default.getStandIdle(object.getId());
        walkConfig = Default.getWalkIdle(object.getId());
        jumpConfig = Default.getJumpIdle(object.getId());
        climbingConfig = Default.getClimbingIdle(object.getId());
    }

    // методы
    public void init(Matrix4f[] matrix){
        firstStart = true;
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
        walk.setBlock(true);
        currentTexture = 2;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = jumpConfig.getRow();
        column = jumpConfig.getColumn();
        objectWidth = (texWid / row) * objectHeight / (texHei / column);
        objectHeight = (texHei / column) * objectWidth / (texWid / row);
        jump = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        jump.setBlock(true);
        currentTexture = 3;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = climbingConfig.getRow();
        column = climbingConfig.getColumn();
        objectWidth = (texWid / row) * objectHeight / (texHei / column);
        objectHeight = (texHei / column) * objectWidth / (texWid / row);
        climbing = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        climbing.setBlock(true);

        setup(textures,stand,0);
        standControl = false;
        count = 0;
        tempFinish = new Vector3f();
        tempX = getPosition().getX();
        tempZ = getPosition().getZ();
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

            // ТЕСТОВЫЙ ТРИГЕР!!!!! --- НАЧАЛО
            if(Input.getInstance().isPressed(GLFW_KEY_X)){
                updateAnimation(stand,0);
                setJump(false);
                for(GridElement element : gridElements){
                    element.setVisible(false);
                    element.setWayPoint(false);
                    if(element.getPosition().equals(getPosition())){
                        element.setBlocked(true);
                    }
                }
                characteristic.setCurentActionPoint(characteristic.getTotalActionPoint());
                Default.setWait(false);
                this.action = false;
            }
            // ТЕСТОВЫЙ!!!!! --- КОНЕЦ

            if(action) {
                switch (event){
                    case PREPARED_AREA:
                        pathfindingAlgorithm = new PathfindingAlgorithm();
                        pathfindingAlgorithm.setup(gridElements,characteristic,getPosition());
                        pathfindingAlgorithm.start();
                        event = CREATE_PATH;
                        break;
                    case CREATE_PATH:
                        Vector3f finish = getFinishPosition(gridElements);
                        if(finish == null){
                            for(GridElement element : gridElements){
                                if(element.isVisible()){
                                    element.setWayPoint(false);
                                }
                            }
                        }else{
                            if(!finish.equals(tempFinish)) {
                                if (!pathfindingAlgorithm.isAlive()) {
                                    pathfindingAlgorithm = new PathfindingAlgorithm();
                                    pathfindingAlgorithm.setup(gridElements, characteristic, getPosition(), finish);
                                    pathfindingAlgorithm.start();
                                }
                            }
                            if (Input.getInstance().buttonActionVerification(true,GLFW_MOUSE_BUTTON_LEFT) == Constants.CLICK) {
                                wayPoints.clear();
                                GridElement finishElement = getFinishElement(gridElements);
                                if (finishElement != null) {
                                    List<GridElement> reverse = new ArrayList<>();
                                    while (finishElement.cameFrom() != null) {
                                        reverse.add(finishElement);
                                        finishElement = finishElement.cameFrom();
                                    }
                                    for (int i = reverse.size() - 1; i >= 0; i--) {
                                        wayPoints.add(reverse.get(i));
                                    }

                                    if (!wayPoints.isEmpty()) {
                                        motionAnimation.setup(getPosition(), wayPoints);
                                        for (GridElement element : gridElements) {
                                            if (element.getPosition().equals(getPosition())) {
                                                element.setBlocked(false);
                                            }
                                        }
                                        int coin = Math.round((float)Math.random() * 2.0f);
                                        if(coin == 1){
                                            walk.setFrames(1);
                                        }else{
                                            walk.setFrames(5);
                                        }
                                        stand.setFrames(1);
                                        jump.setFrames(1);
                                        climbing.setFrames(1);
                                        event = MOVEMENT_ANIMATION;
                                        setJump(false);
                                        for (GridElement element : gridElements) {
                                            if (!element.isWayPoint()) {
                                                element.setVisible(false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if(finish == null){
                            tempFinish = new Vector3f();
                        }else {
                            tempFinish = finish;
                        }
                        break;
                    case MOVEMENT_ANIMATION:
                        if(firstStart){
                            setTurn();
                            firstStart = false;
                        }else {
                            Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
                            int motion = motionAnimation.motion(gridElements, position, characteristic, jump, climbing, walk);
                            if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
                                setPosition(position);
                            }
                            if (motion == 1) {
                                setJump(false);
                                updateAnimation(walk, 1);
                            } else if (motion == 2) {
                                setJump(true);
                                updateAnimation(jump, 2);
                            } else if(motion == 3){
                                setJump(false);
                                updateAnimation(climbing,3);
                            }

                            if (motion == 0) {
                                GridElement currentElement = null;
                                for(GridElement point : wayPoints){
                                    if(point.getPosition().equals(getPosition())){
                                        currentElement = point;
                                        break;
                                    }
                                }

                                assert currentElement != null;
                                if(currentElement.isBlueZona()){
                                    characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 1);
                                }else if(currentElement.isGoldZona()){
                                    characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 2);
                                }

                                updateAnimation(stand, 0);
                                setJump(false);
                                for (GridElement element : gridElements) {
                                    element.setVisible(false);
                                    element.setWayPoint(false);
                                    if (element.getPosition().equals(getPosition())) {
                                        element.setBlocked(true);
                                    }
                                }
                                if (characteristic.getCurentActionPoint() > 0) {
                                    if(characteristic.getStamina() > 0) {
                                        event = PREPARED_AREA;
                                        firstStart = true;
                                    }else{
                                        characteristic.setCurentActionPoint(characteristic.getTotalActionPoint());
                                        Default.setWait(false);
                                        this.action = false;
                                    }
                                } else {
                                    characteristic.setCurentActionPoint(characteristic.getTotalActionPoint());
                                    Default.setWait(false);
                                    this.action = false;
                                }
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
                    characteristic.setStamina(characteristic.getStamina() + characteristic.getStaminaCharge());
                    if(characteristic.getStamina() >= characteristic.getStaminaTotal()){
                        characteristic.setStamina(characteristic.getStaminaTotal());
                    }
                    this.action = true;
                    this.firstStart = true;
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

        if(tempX != getPosition().getX() || tempZ != getPosition().getZ() || isJump()) {
            checkTurn();
        }

        tempX = getPosition().getX();
        tempZ = getPosition().getZ();
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

    private void checkTurn(){
        float yaw = Camera.getInstance().getYaw();
        if(yaw == 46.0f || yaw == 226.0f) {
            if (getPosition().getX() > tempX){
                setTurn(true);
            }else if (getPosition().getZ() < tempZ){
                setTurn(true);
            }else if(getPosition().getX() < tempX){
                setTurn(false);
            }else if (getPosition().getZ() > tempZ){
                setTurn(false);
            }
            if(isJump()){
                for (int i = 0; i < wayPoints.size(); i++) {
                    if (wayPoints.get(i).getPosition().equals(getPosition())) {
                        Vector3f nextPoint;
                        if (i + 1 < wayPoints.size()) {
                            nextPoint = wayPoints.get(i + 1).getPosition();
                            if (nextPoint.getX() > getPosition().getX()) {
                                setTurn(true);
                            } else if (nextPoint.getZ() < getPosition().getZ()) {
                                setTurn(true);
                            } else if (nextPoint.getX() < getPosition().getX()) {
                                setTurn(false);
                            } else if (nextPoint.getZ() > getPosition().getZ()) {
                                setTurn(false);
                            }
                        }
                    }
                }
            }
        }else{
            if (getPosition().getX() < tempX){
                setTurn(true);
            }else if(getPosition().getZ() < tempZ){
                setTurn(true);
            }else if(getPosition().getX() > tempX){
                setTurn(false);
            }else if (getPosition().getZ() > tempZ){
                setTurn(false);
            }
            if(isJump()){
                for(int i=0; i<wayPoints.size(); i++){
                    if(wayPoints.get(i).getPosition().equals(getPosition())){
                        Vector3f nextPoint;
                        if(i + 1 < wayPoints.size()) {
                            nextPoint = wayPoints.get(i + 1).getPosition();
                            if (nextPoint.getX() < getPosition().getX()){
                                setTurn(true);
                            }else if(nextPoint.getZ() < getPosition().getZ()){
                                setTurn(true);
                            }else if(nextPoint.getX() > getPosition().getX()){
                                setTurn(false);
                            }else if (nextPoint.getZ() > getPosition().getZ()){
                                setTurn(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setTurn(){
        float yaw = Camera.getInstance().getYaw();
        if(yaw == 46.0f || yaw == 226.0f) {
            if (wayPoints.get(0).getPosition().getX() > getPosition().getX()) {
                setTurn(true);
            } else if (wayPoints.get(0).getPosition().getZ() < getPosition().getZ()) {
                setTurn(true);
            } else if (wayPoints.get(0).getPosition().getX() < getPosition().getX()) {
                setTurn(false);
            } else if (wayPoints.get(0).getPosition().getZ() > getPosition().getZ()) {
                setTurn(false);
            }
        }else{
            if (wayPoints.get(0).getPosition().getX() < getPosition().getX()){
                setTurn(true);
            }else if(wayPoints.get(0).getPosition().getZ() < getPosition().getZ()){
                setTurn(true);
            }else if(wayPoints.get(0).getPosition().getX() > getPosition().getX()){
                setTurn(false);
            }else if (wayPoints.get(0).getPosition().getZ() > getPosition().getZ()){
                setTurn(false);
            }
        }
    }
}