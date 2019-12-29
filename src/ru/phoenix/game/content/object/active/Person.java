package ru.phoenix.game.content.object.active;

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
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.property.Characteristic;
import ru.phoenix.game.hud.assembled.SelfIndicators;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.logic.movement.MotionAnimation;
import ru.phoenix.game.logic.movement.PathfindingAlgorithm;
import ru.phoenix.game.property.GameController;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class Person extends ObjectControl implements Object {
    private int event;
    private int studyEvent;
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
    private ImageAnimation battleStand;
    private ImageAnimation attack;
    private ImageAnimation walk;
    private ImageAnimation jump;
    private ImageAnimation climbing;
    // textures
    private List<Texture> textures;
    private TextureConfig standConfig;
    private TextureConfig battleStandConfig;
    private TextureConfig attackConfig;
    private TextureConfig walkConfig;
    private TextureConfig jumpConfig;
    private TextureConfig climbingConfig;
    // parametri
    private SelfIndicators selfIndicators;
    private Characteristic characteristic;
    // control
    private int recognition;
    private List<Cell> wayPoints;
    private int sampleData;
    private boolean action;
    private boolean battle;
    private boolean firstStart;
    private boolean studyModeActive;
    private boolean remap;
    private boolean moveClick;
    private boolean pathCheck;

    // NPC
    private boolean waiting;
    private int timer;
    private int waitTime;

    private PathfindingAlgorithm pathfindingAlgorithm;
    private MotionAnimation motionAnimation;

    private boolean standControl;
    private int count;

    private Cell finishElement;
    private Vector3f tempFinish;
    private Vector3f lagerPoint;
    private float tempX;
    private float tempZ;

    private int idleID;
    private int counter;
    private int counterMax;

    // конструкторы
    public Person(int idleID, float id, Vector3f position, Vector3f lagerPoint, int recognition){
        super();

        setIdleID(idleID);
        setPosition(position);
        setLagerPoint(lagerPoint);
        setRecognition(recognition);

        standConfig = Default.getStandIdle(getIdleID());
        battleStandConfig = Default.getBattleStandIdle(getIdleID());
        attackConfig = Default.getBattleStandIdle(getIdleID());
        walkConfig = Default.getWalkIdle(getIdleID());
        jumpConfig = Default.getJumpIdle(getIdleID());
        climbingConfig = Default.getClimbingIdle(getIdleID());

        Texture person_stand = new Texture2D();
        assert standConfig != null;
        person_stand.setup(null, standConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_battle_stand = new Texture2D();
        assert battleStandConfig != null;
        person_battle_stand.setup(null,battleStandConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_attack = new Texture2D();
        assert attackConfig != null;
        person_attack.setup(null,attackConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_walk = new Texture2D();
        assert walkConfig != null;
        person_walk.setup(null, walkConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_jump = new Texture2D();
        assert jumpConfig != null;
        person_jump.setup(null, jumpConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        Texture person_climbing = new Texture2D();
        assert climbingConfig != null;
        person_climbing.setup(null,climbingConfig.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);

        textures = new ArrayList<>(Arrays.asList(person_stand,person_battle_stand,person_attack,person_walk,person_jump,person_climbing));

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

    public Person(Person object, float id, Vector3f position, Vector3f lagerPoint){
        super();

        setIdleID(object.getIdleID());
        setPosition(position);
        setLagerPoint(lagerPoint);
        setRecognition(object.getRecognition());

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

        standConfig = Default.getStandIdle(object.getIdleID());
        battleStandConfig = Default.getBattleStandIdle(object.getIdleID());
        attackConfig = Default.getAttackIdle(object.getIdleID());
        walkConfig = Default.getWalkIdle(object.getIdleID());
        jumpConfig = Default.getJumpIdle(object.getIdleID());
        climbingConfig = Default.getClimbingIdle(object.getIdleID());
    }

    // методы
    public void init(Matrix4f[] matrix){
        firstStart = true;
        event = PREPARED_AREA;
        studyEvent = CREATE_PATH;

        int currentTexture = 0;
        int texWid = textures.get(currentTexture).getWidth();
        int texHei = textures.get(currentTexture).getHeight();
        int row = standConfig.getRow();
        int column = standConfig.getColumn();
        float objectWidth = 3.5f;
        float objectHeight = (texHei / column) * objectWidth / (texWid / row);
        stand = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        stand.setBlock(true);

        currentTexture = 1;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = battleStandConfig.getRow();
        column = battleStandConfig.getColumn();
        battleStand = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        battleStand.setBlock(true);

        currentTexture = 2;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = attackConfig.getRow();
        column = attackConfig.getColumn();
        attack = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        attack.setBlock(true);

        currentTexture = 3;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = walkConfig.getRow();
        column = walkConfig.getColumn();
        walk = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        walk.setBlock(true);

        currentTexture = 4;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = jumpConfig.getRow();
        column = jumpConfig.getColumn();
        jump = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        jump.setBlock(true);

        currentTexture = 5;
        texWid = textures.get(currentTexture).getWidth();
        texHei = textures.get(currentTexture).getHeight();
        row = climbingConfig.getRow();
        column = climbingConfig.getColumn();
        climbing = ImageAnimLoader.load(textures.get(currentTexture), row, column, objectWidth, objectHeight,null,0);
        climbing.setBlock(true);

        selfIndicators = new SelfIndicators(1.2f,new Vector3f(0.0f,1.7f,0.0f));

        setup(textures,stand,0);
        standControl = false;
        count = 0;
        tempFinish = new Vector3f();
        tempX = getPosition().getX();
        tempZ = getPosition().getZ();

        setBattle(false);
        setSelected(false);
        studyModeActive = false;
        waiting = true;
        remap = false;
        moveClick = false;
        pathCheck = false;
        counter = 0;
        timer = 0;
        waitTime = (int)(250.0f + Math.random() * 250.0f);
        counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
    }

    @Override
    public void update(Cell[][] grid, Vector3f pixel, Cell finishElement){
        // проверка режима для выбранного персонажа
        if(isSelected() && getRecognition() == ALLY){
            // только для выбранного персонажа!
            if(finishElement != null){
                this.finishElement = finishElement;
            }

            if(isBattle()){
                Default.setBattleMode(true);
            }else{
                Default.setBattleMode(false);
            }
        }

        if (pixel.getX() == getId()) {
            setOnTarget(true);
        } else {
            setOnTarget(false);
        }

        switch (recognition) {
            case ALLY:
                if (isBattle()) {
                    if(Default.isBattleMode()) {
                        battleMode(grid);
                    }
                }else {
                    researchMode(grid);
                }
                break;
            case ENEMY:
                if(isBattle()){
                    System.out.println("not work yeat!");
                }else{
                    characteristic.setStamina(characteristic.getTotalStamina());
                    blink();
                    if(waiting){
                        timer++;
                        if(timer > waitTime){
                            this.finishElement = getElement(grid, Generator.getRandomPos(grid,lagerPoint,5.0f,false));
                            waiting = false;
                            timer = 0;
                            waitTime = (int)(250.0f + Math.random() * 250.0f);
                        }
                    }else{
                        switch (studyEvent){
                            case CREATE_PATH:
                                createPath(grid);
                                break;
                            case MOVEMENT_ANIMATION:
                                movementAnimation();
                                break;
                        }
                    }
                }
                break;
        }

        if(tempX != getPosition().getX() || tempZ != getPosition().getZ() || isJump()) {
            checkTurn();
        }

        selfIndicators.update(getPosition(),characteristic);
        setSelfIndicators(selfIndicators);

        tempX = getPosition().getX();
        tempZ = getPosition().getZ();
    }

    private void researchMode(Cell[][] grid){
        // Заплатка! Стамина расходуется в классе MotionAnimation
        characteristic.setStamina(characteristic.getTotalStamina());
        blink();

        if(remap){
            studyEventMode(grid);
        }else {
            if (isSelected()) {
                studyEventMode(grid);
            } else {
                if (studyEvent == MOVEMENT_ANIMATION) {
                    movementAnimation();
                }
            }
        }
    }

    private void studyEventMode(Cell[][] grid){
        switch (studyEvent){
            case CREATE_PATH:
                createPath(grid);
                break;
            case MOVEMENT_ANIMATION:
                if(GameController.getInstance().isLeftClick()){
                    moveClick = true;
                    Vector3f currentPos = new Vector3f(getPosition());
                    currentPos.setY(0.0f);
                    int lastIndex = -1;
                    for(int i=0; i<wayPoints.size(); i++){
                        Vector3f somePos = new Vector3f(wayPoints.get(i).getPosition());
                        somePos.setY(0.0f);
                        if(Math.abs(currentPos.sub(somePos).length()) <= 1.0f){
                            lastIndex = i;
                        }
                    }

                    if(lastIndex != -1){
                        List<Cell> newWayPoints = new ArrayList<>();
                        for(int i=0; i<=lastIndex; i++){
                            newWayPoints.add(wayPoints.get(i));
                        }
                        motionAnimation.setWayPoints(newWayPoints);
                    }
                }
                movementAnimation();
                break;
        }
    }

    private void createPath(Cell[][] grid){

        /*if(remap){
            GridElement startPosition = null;
            for (GridElement element : gridElements) {
                if (element.getPosition().equals(getPosition())) {
                    startPosition = element;
                    break;
                }
            }
            finishElement.setOccupied(false);
            finishElement.setCameFromElement(null);
            pathfindingAlgorithm = new PathfindingAlgorithm();
            pathfindingAlgorithm.setup(gridElements, characteristic, startPosition, this.finishElement);
            pathfindingAlgorithm.start();
            finishElement.setOccupied(true);
            remap = false;

            studyModeActive = true;

            // Очищаем вей поинты
            wayPoints.clear();
            // Создаем реверсивный список пути заполненный в алгоритме поиска пути
            List<GridElement> reverse = new ArrayList<>();
            while (finishElement.cameFrom() != null) {
                reverse.add(finishElement);
                finishElement = finishElement.cameFrom();
            }
            // Заполняем вей поинты в обратном напровлении (прямой путь)
            for (int i = reverse.size() - 1; i >= 0; i--) {
                wayPoints.add(reverse.get(i));
            }

            GridElement point = null;
            for(GridElement element : gridElements){
                if(element.getPosition().equals(getPosition())){
                    point = element;
                    break;
                }
            }
            wayPoints.add(0,point);
        }else {
            if (leftClick && finishPosition != null) {
                this.finishElement = finishPosition;
                GridElement startPosition = null;
                for (GridElement element : gridElements) {
                    if (element.getPosition().equals(getPosition())) {
                        startPosition = element;
                        break;
                    }
                }
                finishPosition.setCameFromElement(null);
                pathfindingAlgorithm = new PathfindingAlgorithm();
                pathfindingAlgorithm.setup(gridElements, characteristic, startPosition, finishPosition);
                pathfindingAlgorithm.start();
                pathCheck = true;
            }else if(repath){
                repath = false;
                GridElement startPosition = null;
                for (GridElement element : gridElements) {
                    if (element.getPosition().equals(getPosition())) {
                        startPosition = element;
                        break;
                    }
                }
                this.finishElement.setCameFromElement(null);
                finishElement.setOccupied(false);
                pathfindingAlgorithm = new PathfindingAlgorithm();
                pathfindingAlgorithm.setup(gridElements, characteristic, startPosition, this.finishElement);
                pathfindingAlgorithm.start();
                finishElement.setOccupied(true);
                pathCheck = true;
            }
        }

        if(pathCheck && !pathfindingAlgorithm.isAlive()){
            pathCheck = false;
            studyModeActive = true;

            Vector3f temp = this.finishElement.getPosition();
            // Очищаем вей поинты
            wayPoints.clear();
            // Создаем реверсивный список пути заполненный в алгоритме поиска пути
            List<GridElement> reverse = new ArrayList<>();
            while (finishElement.cameFrom() != null) {
                reverse.add(finishElement);
                finishElement = finishElement.cameFrom();
            }
            // Заполняем вей поинты в обратном напровлении (прямой путь)
            for (int i = reverse.size() - 1; i >= 0; i--) {
                wayPoints.add(reverse.get(i));
            }

            if(wayPoints.size() == 0){
                for(GridElement element : gridElements){
                    if(element.getPosition().equals(temp)){
                        this.finishElement = element;
                        break;
                    }
                }
                studyModeActive = false;
                repath = true;
            }else{
                GridElement point = null;
                for(GridElement element : gridElements){
                    if(element.getPosition().equals(getPosition())){
                        point = element;
                        break;
                    }
                }
                wayPoints.add(0,point);
            }
        }

        if (studyModeActive) {
            // Инициируем стартовые данные для анимации движенния и механики передвижения
            if (!wayPoints.isEmpty()) {
                motionAnimation.setup(getPosition(), wayPoints);
                walk.setFrames(1);
                stand.setFrames(1);
                jump.setFrames(1);
                climbing.setFrames(1);
                studyEvent = MOVEMENT_ANIMATION;
                setJump(false);
                studyModeActive = false;
                firstStart = true;
                counter = 0;
            }
        }*/

        if(Default.isCreatePath()) {
            if (remap) {
                if (finishElement != null && !pathCheck) {
                    Default.setCreatePath(false);
                    bypass(grid);
                }
            } else {
                if (finishElement != null && !pathCheck) {
                    Default.setCreatePath(false);
                    path(grid);
                }
            }
        }

        if(pathCheck){
            if(!pathfindingAlgorithm.isAlive()){
                pathCheck = false;
                remap = false;
                studyModeActive = true;

                Vector3f temp = this.finishElement.getModifiedPosition();

                /*wayPoints.clear();
                List<Cell> reverse = new ArrayList<>();
                while (finishElement.getParent() != null) {
                    reverse.add(finishElement);
                    finishElement = finishElement.getParent();
                }
                for (int i = reverse.size() - 1; i >= 0; i--) {
                    wayPoints.add(reverse.get(i));
                }*/

                if(wayPoints.size() == 0){
                    Default.setCreatePath(true);
                    studyModeActive = false;
                    remap = true;
                }else{
                    Cell point = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                    wayPoints.add(0,point);
                }
                this.finishElement = grid[(int)temp.getX()][(int)temp.getZ()];
            }
        }

        if (studyModeActive) {
            Default.setCreatePath(true);
            // Инициируем стартовые данные для анимации движенния и механики передвижения
            motionAnimation.setup(getPosition(), wayPoints);
            walk.setFrames(1);
            stand.setFrames(1);
            jump.setFrames(1);
            climbing.setFrames(1);
            studyEvent = MOVEMENT_ANIMATION;
            setJump(false);
            studyModeActive = false;
            firstStart = true;
            counter = 0;
        }
    }

    private void path(Cell[][] grid){
        Cell startPosition = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
        this.finishElement.setParent(null);
        pathfindingAlgorithm = new PathfindingAlgorithm();
        pathfindingAlgorithm.setup(grid, characteristic, startPosition, this.finishElement, wayPoints,true);
        pathfindingAlgorithm.start();
        pathCheck = true;
    }

    private void bypass(Cell[][] grid){
        Cell startPosition = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
        this.finishElement.setParent(null);
        this.finishElement.setOccupied(false);
        pathfindingAlgorithm = new PathfindingAlgorithm();
        pathfindingAlgorithm.setup(grid, characteristic, startPosition, this.finishElement, wayPoints,false);
        pathfindingAlgorithm.start();
        pathCheck = true;
    }



    private void movementAnimation(){
        if (firstStart) {
            setTurn();
            firstStart = false;
        } else {
            Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
            int motion = motionAnimation.motion(new BattleGround(), position, getPosition(), characteristic, jump, climbing, walk);

            if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
                setPosition(position);
            }

            if (motion == 0) {
                if(moveClick){
                    remap = true;
                    moveClick = false;
                }else {
                    this.finishElement = null;
                }
                counter = 0;
                updateAnimation(stand, 0);
                setJump(false);
                studyEvent = CREATE_PATH;
                waiting = true;
            }else if (motion == 1) {
                counter = 0;
                setJump(false);
                updateAnimation(walk, 3);
            }else if (motion == 2) {
                counter = 0;
                setJump(true);
                updateAnimation(jump, 4);
            }else if (motion == 3) {
                counter = 0;
                setJump(false);
                updateAnimation(climbing, 5);
            }else if(motion == 4){
                counter++;
                if(counter > counterMax) {
                    if(getPosition().sub(wayPoints.get(wayPoints.size() - 1).getModifiedPosition()).length() > 2.5f) {
                        studyEvent = CREATE_PATH;
                        this.finishElement = wayPoints.get(wayPoints.size() - 1);
                        remap = true;
                        counter = 0;
                        counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
                    }else{
                        this.finishElement = null;
                        updateAnimation(stand, 0);
                        setJump(false);
                        studyEvent = CREATE_PATH;
                        waiting = true;
                        counter = 0;
                        counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
                    }
                }else {
                    updateAnimation(stand, 0);
                    setJump(false);
                }
            }
        }
    }

    private void battleMode(Cell[][] grid){
        blink();
        if(Default.isWait()){
            if(action) {
                switch (event) {
                    case PREPARED_AREA:
                        pathfindingAlgorithm = new PathfindingAlgorithm();
                        pathfindingAlgorithm.setup(grid, characteristic, getPosition());
                        pathfindingAlgorithm.start();
                        event = CREATE_PATH;
                        break;
                    case CREATE_PATH:
                        Vector3f finish = getFinishPosition(grid);
                        if (finish == null) {
                            for(int x=0; x<grid.length;x++){
                                for(int z=0; z<grid[0].length;z++){
                                    if(grid[x][z].isVisible()){
                                        grid[x][z].setWayPoint(false);
                                    }
                                }
                            }
                        } else {
                            if (!finish.equals(tempFinish)) {
                                if (!pathfindingAlgorithm.isAlive()) {
                                    pathfindingAlgorithm = new PathfindingAlgorithm();
                                    pathfindingAlgorithm.setup(grid, characteristic, getPosition(), finish);
                                    pathfindingAlgorithm.start();
                                }
                            }
                            if (GameController.getInstance().isLeftClick()) {
                                wayPoints.clear();
                                Cell finishElement = getFinishElement(grid);
                                if (finishElement != null) {
                                    List<Cell> reverse = new ArrayList<>();
                                    while (finishElement.getParent() != null) {
                                        reverse.add(finishElement);
                                        finishElement = finishElement.getParent();
                                    }
                                    for (int i = reverse.size() - 1; i >= 0; i--) {
                                        wayPoints.add(reverse.get(i));
                                    }

                                    Cell point = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                                    wayPoints.add(0,point);

                                    if (!wayPoints.isEmpty()) {
                                        motionAnimation.setup(getPosition(), wayPoints);
                                        walk.setFrames(1);
                                        stand.setFrames(1);
                                        jump.setFrames(1);
                                        climbing.setFrames(1);
                                        event = MOVEMENT_ANIMATION;
                                        setJump(false);
                                        for(int x=0; x<grid.length; x++){
                                            for(int z=0; z<grid[0].length; z++){
                                                if(!grid[x][z].isWayPoint()){
                                                    grid[x][z].setVisible(false);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (finish == null) {
                            tempFinish = new Vector3f(-1.0f, -1.0f, -1.0f);
                        } else {
                            tempFinish = finish;
                        }
                        break;
                    case MOVEMENT_ANIMATION:
                        if (firstStart) {
                            setTurn();
                            firstStart = false;
                        } else {
                            Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
                            int motion = motionAnimation.motion(new BattleGround(), position, getPosition(), characteristic, jump, climbing, walk);
                            if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
                                setPosition(position);
                            }

                            if (motion == 0) {
                                Cell currentElement = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                                if (currentElement.isBlueZona()) {
                                    characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 1);
                                } else if (currentElement.isGoldZona()) {
                                    characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 2);
                                }
                                updateAnimation(stand, 0);
                                setJump(false);
                                for(int x=0; x<grid.length; x++){
                                    for(int z=0; z<grid[0].length; z++){
                                        grid[x][z].setGrayZona();
                                        grid[x][z].setVisible(false);
                                        grid[x][z].setWayPoint(false);
                                    }
                                }
                                if (characteristic.getCurentActionPoint() > 0) {
                                    if (characteristic.getStamina() > 0) {
                                        event = PREPARED_AREA;
                                        firstStart = true;
                                    } else {
                                        Default.setWait(false);
                                        this.action = false;
                                    }
                                } else {
                                    Default.setWait(false);
                                    this.action = false;
                                }
                            }else if (motion == 1) {
                                setJump(false);
                                updateAnimation(walk, 3);
                            }else if (motion == 2) {
                                setJump(true);
                                updateAnimation(jump, 4);
                            }else if (motion == 3) {
                                setJump(false);
                                updateAnimation(climbing, 5);
                            }else if(motion == 4){
                                setJump(false);
                                blink();
                                updateAnimation(stand,0);
                            }
                        }
                        break;
                }

                // ТЕСТОВЫЙ ТРИГЕР!!!!! --- НАЧАЛО
                if(Input.getInstance().isPressed(GLFW_KEY_X)){
                    updateAnimation(stand,0);
                    setJump(false);
                    for(int x=0; x<grid.length; x++){
                        for(int z=0; z<grid[0].length; z++){
                            grid[x][z].setGrayZona();
                            grid[x][z].setVisible(false);
                            grid[x][z].setWayPoint(false);
                            if(grid[x][z].getModifiedPosition().equals(getPosition())){
                                grid[x][z].setOccupied(true);
                            }
                        }
                    }
                    Default.setWait(false);
                    this.action = false;
                    event = PREPARED_AREA;
                }
                // ТЕСТОВЫЙ!!!!! --- КОНЕЦ
            }
        }else{
            if(sampleData != Time.getSecond()){
                characteristic.setInitiative(characteristic.getInitiative() + characteristic.getInitiativeCharge());
                if(characteristic.getInitiative() >= 100){
                    characteristic.setCurentActionPoint(characteristic.getTotalActionPoint());
                    characteristic.setInitiative(0);
                    characteristic.updateIndicators();
                    this.action = true;
                    this.firstStart = true;
                    event = PREPARED_AREA;
                    Default.setWait(true);
                }
            }
            sampleData = Time.getSecond();
        }
    }

    public List<Texture> getTextures(){
        return textures;
    }

    private Cell getFinishElement(Cell[][] grid){
        Cell finishElement = null;

        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                if(grid[x][z].isTarget() && !grid[x][z].isBlocked()){
                    finishElement = grid[x][z];
                    break;
                }
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
                    if (wayPoints.get(i).getModifiedPosition().equals(getPosition())) {
                        Vector3f nextPoint;
                        if (i + 1 < wayPoints.size()) {
                            nextPoint = wayPoints.get(i + 1).getModifiedPosition();
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
                    if(wayPoints.get(i).getModifiedPosition().equals(getPosition())){
                        Vector3f nextPoint;
                        if(i + 1 < wayPoints.size()) {
                            nextPoint = wayPoints.get(i + 1).getModifiedPosition();
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
            if (wayPoints.get(0).getModifiedPosition().getX() > getPosition().getX()) {
                setTurn(true);
            } else if (wayPoints.get(0).getModifiedPosition().getZ() < getPosition().getZ()) {
                setTurn(true);
            } else if (wayPoints.get(0).getModifiedPosition().getX() < getPosition().getX()) {
                setTurn(false);
            } else if (wayPoints.get(0).getModifiedPosition().getZ() > getPosition().getZ()) {
                setTurn(false);
            }
        }else{
            if (wayPoints.get(0).getModifiedPosition().getX() < getPosition().getX()){
                setTurn(true);
            }else if(wayPoints.get(0).getModifiedPosition().getZ() < getPosition().getZ()){
                setTurn(true);
            }else if(wayPoints.get(0).getModifiedPosition().getX() > getPosition().getX()){
                setTurn(false);
            }else if (wayPoints.get(0).getModifiedPosition().getZ() > getPosition().getZ()){
                setTurn(false);
            }
        }
    }

    @Override
    public boolean isBattle() {
        return battle;
    }

    @Override
    public void setBattle(boolean battle) {
        this.battle = battle;
    }

    @Override
    public int getRecognition() {
        return recognition;
    }

    @Override
    public void setRecognition(int recognition) {
        this.recognition = recognition;
    }

    private int getIdleID() {
        return idleID;
    }

    private void setIdleID(int idleID) {
        this.idleID = idleID;
    }

    private Vector3f getLagerPoint(){
        return lagerPoint;
    }

    private void setLagerPoint(Vector3f lagerPoint){
        this.lagerPoint = lagerPoint;
    }

    private void blink(){
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
        updateAnimation(stand,0);
    }

    private Vector3f getFinishPosition(Cell[][]grid){
        Vector3f finishPos = null;
        // ВРЕМЕННО! НАДО ПОДУМАТЬ!!!
        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                if(grid[x][z].isTarget() && !grid[x][z].isBlocked()){
                    finishPos = grid[x][z].getModifiedPosition();
                    break;
                }
            }
        }

        return finishPos;
    }

    private Cell getElement(Cell[][] grid, Vector3f soughtPosition){
        Cell element = null;

        if(0 <= soughtPosition.getX() && soughtPosition.getX() < grid.length){
            if(0 <= soughtPosition.getZ() && soughtPosition.getZ() < grid[0].length){
                element = grid[(int)soughtPosition.getX()][(int)soughtPosition.getZ()];
            }
        }

        return element;
    }
}
