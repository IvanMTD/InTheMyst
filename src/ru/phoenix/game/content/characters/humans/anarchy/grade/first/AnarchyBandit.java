package ru.phoenix.game.content.characters.humans.anarchy.grade.first;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.loader.ImageAnimLoader;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.sprite.TextureConfig;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.HumanDraw;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.logic.battle.SimpleAI;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.logic.generator.RandomPosition;
import ru.phoenix.game.property.GameController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class AnarchyBandit extends HumanDraw implements Character {
    // константы
    private final int PREPARED_AREA         = 0x90001;
    private final int CREATE_PATH           = 0x90002;
    private final int MOVEMENT_ANIMATION    = 0x90003;
    private final int ATTACK_ANIMATION      = 0x90004;
    private final int CHOICE_DIRECTION      = 0x90005;

    // константы класса
    private final TextureConfig baseStance = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_stand.png",3,1);
    private final TextureConfig walk = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_walk.png",12,1);
    private final TextureConfig jump = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_jump.png",7,1);
    private final TextureConfig goUpDown = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_climbing.png",4,1);
    private final TextureConfig battleStancePrepare = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_bs_open.png",8,1);
    private final TextureConfig battleStance = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_bs.png",6,1);
    private final TextureConfig baseAttack = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_base_attack.png",10,1);
    private final TextureConfig damage = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_damage.png",6,1);
    private final TextureConfig dead = new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_dead.png",6,1);

    // анимации
    private ImageAnimation baseStanceAnimation;
    private ImageAnimation walkAnimation;
    private ImageAnimation jumpAnimation;
    private ImageAnimation goUpDownAnimation; // может быть стоит разделить?
    private ImageAnimation battleStancePrepareAnimation;
    private ImageAnimation battleStanceAnimation;
    private ImageAnimation baseAttackAnimation;
    private ImageAnimation damageAnimation;
    private ImageAnimation deadAnimation;

    // текстуры
    private Texture baseStanceTextrue;
    private Texture walkTexture;
    private Texture jumpTexture;
    private Texture goUpDownTexture;
    private Texture battleStancePrepareTexture;
    private Texture battleStanceTexture;
    private Texture baseAttackTexture;
    private Texture damageTexture;
    private Texture deadTexture;

    // тригеры умений
    private boolean isBaseAttack;

    // вспомогательные контролеры
    private int count;
    private int maxCount;
    private int counter;
    private int counterMax;
    private int timer;
    private int waitTime;
    private int deadCount;
    private int deadFrame;
    private int damageCount;
    private int damageFrame;
    private int currentFrame;
    private int sampleData;
    private int battleEvent;
    private int studyEvent;

    // вспомогательные элементы класса
    private boolean remap;
    private boolean action;
    private boolean pathCheck;
    private boolean moveClick;
    private boolean studyModeActive;
    private boolean preparingForBattle;
    private boolean firstBaseAttack;
    private boolean moveControl;
    private boolean waiting;
    private boolean playDead;
    private boolean prepareMove;
    private boolean autoControl;
    private boolean flowControl;
    private boolean choiceDirection;
    private Character allySavedCharacter;
    private Character enemySavedCharacter;
    private Cell targetPoint;
    private Vector3f targetPos;
    private Cell tempFinish;
    private RandomPosition randomPosition;

    // конструкторы класса
    // начало
    public AnarchyBandit(){
        super();
        init();
    }

    public AnarchyBandit(Character character, Vector3f position, Vector3f lagerPoint, float id, int recognition){
        super(character);
        init();
        setId(id);
        setGroup(GROUP_R);
        setRecognition(recognition);
        setPosition(position);
        setLagerPoint(lagerPoint);
        preset();
    }
    // конец

    // методы инициализации - начало
    private void init(){
        float widthSize = 3.5f;
        // базовая стойка
        baseStanceTextrue = new Texture2D();
        baseStanceTextrue.setup(null,baseStance.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        baseStanceAnimation = initAnimation(baseStanceTextrue,baseStance,widthSize);
        // ходьба
        walkTexture = new Texture2D();
        walkTexture.setup(null,walk.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        walkAnimation = initAnimation(walkTexture,walk,widthSize);
        // прыжок
        jumpTexture = new Texture2D();
        jumpTexture.setup(null,jump.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        jumpAnimation = initAnimation(jumpTexture,jump,widthSize);
        // подьем вверх вниз
        goUpDownTexture = new Texture2D();
        goUpDownTexture.setup(null,goUpDown.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        goUpDownAnimation = initAnimation(goUpDownTexture,goUpDown,widthSize);
        // подготовка боевой стойки
        battleStancePrepareTexture = new Texture2D();
        battleStancePrepareTexture.setup(null,battleStancePrepare.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        battleStancePrepareAnimation = initAnimation(battleStancePrepareTexture,battleStancePrepare,widthSize);
        // боевая стойка
        battleStanceTexture = new Texture2D();
        battleStanceTexture.setup(null,battleStance.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        battleStanceAnimation = initAnimation(battleStanceTexture,battleStance,widthSize);
        // базовая атака
        baseAttackTexture = new Texture2D();
        baseAttackTexture.setup(null,baseAttack.getPath(),GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        baseAttackAnimation = initAnimation(baseAttackTexture,baseAttack,widthSize);
        // получение урона
        damageTexture = new Texture2D();
        damageTexture.setup(null,damage.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        damageAnimation = initAnimation(damageTexture,damage,widthSize);
        // смерть
        deadTexture = new Texture2D();
        deadTexture.setup(null,dead.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        deadAnimation = initAnimation(deadTexture,dead,widthSize);
    }

    private void init(Character character){
        // texture
        setBaseStanceTextrue(character.getBaseStanceTextrue());
        setWalkTexture(character.getWalkTexture());
        setJumpTexture(character.getJumpTexture());
        setGoUpDownTexture(character.getGoUpDownTexture());
        setBattleStancePrepareTexture(character.getBattleStancePrepareTexture());
        setBattleStanceTexture(character.getBattleStanceTexture());
        setBaseAttackTexture(character.getBaseAttackTexture());
        // animation
        setBaseStanceAnimation(character.getBaseStanceAnimation());
        setWalkAnimation(character.getWalkAnimation());
        setJumpAnimation(character.getJumpAnimation());
        setGoUpDownAnimation(character.getGoUpDownAnimation());
        setBattleStancePrepareAnimation(character.getBattleStancePrepareAnimation());
        setBattleStanceAnimation(character.getBattleStanceAnimation());
        setBaseAttackAnimation(character.getBaseAttackAnimation());
    }

    private ImageAnimation initAnimation(Texture texture, TextureConfig textureConfig, float widthSize){
        // базовая стойка
        float texWid = texture.getWidth();
        float texHei = texture.getHeight();
        float objectHeight = (texHei / textureConfig.getColumn()) * widthSize / (texWid / textureConfig.getRow());
        return ImageAnimLoader.load(texture,textureConfig.getRow(),textureConfig.getColumn(),widthSize,objectHeight,null,0);
    }

    @Override
    public void setDefaultCharacteristic(){
        // Опыт и уровень
        getCharacteristic().setExperience(0);
        getCharacteristic().setLevel(1);
        // Инициатива
        getCharacteristic().setInitiativeCharge(15);
        // Здоровье
        getCharacteristic().setTotalHealth(40);
        getCharacteristic().setHealth(getCharacteristic().getTotalHealth());
        getCharacteristic().setHealthCharge(0);
        // Манна
        getCharacteristic().setTotalManna(30);
        getCharacteristic().setManna(getCharacteristic().getTotalManna());
        getCharacteristic().setMannaCharge(0);
        // Стамина
        getCharacteristic().setTotalStamina(120);
        getCharacteristic().setStamina(getCharacteristic().getTotalStamina());
        getCharacteristic().setStaminaCharge(15);
        // Характеристики
        getCharacteristic().setPhysicalPower(20);
        getCharacteristic().setMagicPower(10);
        // Движения
        getCharacteristic().setMove(5);
        getCharacteristic().setJump(1);
        getCharacteristic().setSpeed(2);
        // Обзор
        getCharacteristic().setVision(10);
    }
    // методы инициализации - конец

    @Override
    public void preset(){
        targetPoint = null;
        tempFinish = new Cell();
        // тригеры умений
        isBaseAttack = false;
        // доп инициализация
        battleEvent = PREPARED_AREA;
        studyEvent = CREATE_PATH;
        count = 0;
        maxCount = (int)(50.0f + Math.random() * 150.0f);
        counter = 0;
        counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
        timer = 0;
        waitTime = (int)(250.0f + Math.random() * 250.0f);
        currentFrame = 1;
        sampleData = Time.getSecond();
        deadCount = 0;
        deadFrame = 1;
        damageCount = 0;
        damageFrame = 1;
        // вспомогательные
        remap = false;
        action = false;
        pathCheck = false;
        moveClick = false;
        studyModeActive = false;
        preparingForBattle = true;
        firstBaseAttack = true;
        waiting = true;
        moveControl = true;
        playDead = true;
        prepareMove = false;
        autoControl = false;
        flowControl = false;
        choiceDirection = false;
    }

    @Override
    public void resetSettings(){
        targetPoint = null;
        tempFinish = new Cell();
        // тригеры умений
        isBaseAttack = false;
        // доп инициализация
        battleEvent = PREPARED_AREA;
        studyEvent = CREATE_PATH;
        count = 0;
        maxCount = (int)(50.0f + Math.random() * 150.0f);
        counter = 0;
        counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
        timer = 0;
        waitTime = (int)(250.0f + Math.random() * 250.0f);
        currentFrame = 1;
        sampleData = Time.getSecond();
        if(!isDead()) {
            deadCount = 0;
            deadFrame = 1;
        }
        damageCount = 0;
        damageFrame = 1;
        // вспомогательные
        remap = false;
        action = false;
        pathCheck = false;
        moveClick = false;
        studyModeActive = false;
        preparingForBattle = true;
        firstBaseAttack = true;
        waiting = true;
        moveControl = true;
        prepareMove = false;
        autoControl = false;
        flowControl = false;
        choiceDirection = false;
        if(!isDead()) {
            playDead = true;
        }
    }

    @Override
    public void interaction(Cell[][]grid, Cell targetElement, Vector3f pixel, List<Character> enemies, List<Character> allies, BattleGround battleGround) {
        targetControl(pixel);

        if (getCharacteristic().getHealth() == 0) {
            setDead(true);
            grid[(int)getPosition().getX()][(int)getPosition().getZ()].setOccupied(true);
        }

        if(isDead()){
            if(playDead){
                deadCount++;
                if(deadCount > 40){
                    deadCount = 0;
                    deadFrame++;
                }
                if(deadFrame <= dead.getRow()){
                    deadAnimation.setFrames(deadFrame);
                    updateAnimation(deadTexture,deadAnimation);
                }else{
                    deadCount = 0;
                    playDead = false;
                }
            }else{
                updateAnimation(deadTexture,deadAnimation);
            }
        }else {
            blink();
            if (isBattle()) {
                updateAnimation(battleStanceTexture, battleStanceAnimation);
            } else {
                updateAnimation(baseStanceTextrue, baseStanceAnimation);
            }

            if (isSelected() && getRecognition() == ALLY && targetElement != null) targetPoint = targetElement;

            switch (getRecognition()) {
                case ALLY:
                    if (isBattle()) {
                        if (preparingForBattle) {
                            if (studyEvent == MOVEMENT_ANIMATION) {
                                if (moveControl) {
                                    float x = (int) Math.floor(getPosition().getX());
                                    float z = (int) Math.floor(getPosition().getZ());
                                    if(Math.round(getPosition().getX() - 1) >= 0) {
                                        grid[Math.round(getPosition().getX() - 1)][Math.round(getPosition().getZ())].setOccupied(false);
                                    }
                                    if(Math.round(getPosition().getX() + 1) < grid.length) {
                                        grid[Math.round(getPosition().getX() + 1)][Math.round(getPosition().getZ())].setOccupied(false);
                                    }
                                    grid[Math.round(getPosition().getX())][Math.round(getPosition().getZ())].setOccupied(false);
                                    if(Math.round(getPosition().getZ() - 1) >= 0) {
                                        grid[Math.round(getPosition().getX())][Math.round(getPosition().getZ() - 1)].setOccupied(false);
                                    }
                                    if(Math.round(getPosition().getZ() + 1) < grid[0].length) {
                                        grid[Math.round(getPosition().getX())][Math.round(getPosition().getZ() + 1)].setOccupied(false);
                                    }
                                    Vector3f currentPos = new Vector3f(x, 0.0f, z);
                                    int index = -1;
                                    for (int i = 0; i < getWayPoints().size(); i++) {
                                        if (getWayPoints().get(i).getPosition().equals(currentPos)) {
                                            index = i + 1;
                                        }
                                    }

                                    if (index != -1 && index < getWayPoints().size()) {
                                        List<Cell> newWayPoints = new ArrayList<>();
                                        for (int i = 0; i <= index; i++) {
                                            newWayPoints.add(getWayPoints().get(i));
                                        }
                                        getMotionAnimation().setWayPoints(newWayPoints);
                                    }
                                    moveControl = false;
                                } else {
                                    movementAnimation(battleGround);
                                }
                            } else {
                                counter++;
                                if (counter > 20) {
                                    currentFrame++;
                                    counter = 0;
                                }
                                if (currentFrame <= battleStancePrepare.getRow()) {
                                    battleStancePrepareAnimation.setFrames(currentFrame);
                                    updateAnimation(battleStancePrepareTexture, battleStancePrepareAnimation);
                                } else {
                                    grid[(int)getPosition().getX()][(int)getPosition().getZ()].setOccupied(true);
                                    currentFrame = 1;
                                    moveControl = true;
                                    preparingForBattle = false;
                                    counter = 0;
                                }
                            }
                        } else {
                            battleMode(battleGround, grid, allies, enemies);
                        }
                    } else {
                        if(battleEvent == MOVEMENT_ANIMATION){
                            battleMode(battleGround, grid, allies, enemies);
                        }else {
                            // Заплатка! Стамина расходуется в классе MotionAnimation
                            getCharacteristic().setCurentActionPoint(getCharacteristic().getTotalActionPoint());
                            getCharacteristic().updateIndicators();
                            researchMode(battleGround,grid);
                        }
                    }
                    break;
                case ENEMY:
                    if (isBattle()) {
                        if (preparingForBattle) {
                            if (studyEvent == MOVEMENT_ANIMATION) {
                                if (moveControl) {
                                    float x = (int) Math.floor(getPosition().getX());
                                    float z = (int) Math.floor(getPosition().getZ());
                                    if(Math.round(getPosition().getX() - 1) >= 0) {
                                        grid[Math.round(getPosition().getX() - 1)][Math.round(getPosition().getZ())].setOccupied(false);
                                    }
                                    if(Math.round(getPosition().getX() + 1) < grid.length) {
                                        grid[Math.round(getPosition().getX() + 1)][Math.round(getPosition().getZ())].setOccupied(false);
                                    }
                                    grid[Math.round(getPosition().getX())][Math.round(getPosition().getZ())].setOccupied(false);
                                    if(Math.round(getPosition().getZ() - 1) >= 0) {
                                        grid[Math.round(getPosition().getX())][Math.round(getPosition().getZ() - 1)].setOccupied(false);
                                    }
                                    if(Math.round(getPosition().getZ() + 1) < grid[0].length) {
                                        grid[Math.round(getPosition().getX())][Math.round(getPosition().getZ() + 1)].setOccupied(false);
                                    }
                                    Vector3f currentPos = new Vector3f(x, 0.0f, z);
                                    int index = -1;
                                    for (int i = 0; i < getWayPoints().size(); i++) {
                                        if (getWayPoints().get(i).getPosition().equals(currentPos)) {
                                            index = i + 1;
                                        }
                                    }

                                    if (index != -1 && index < getWayPoints().size()) {
                                        List<Cell> newWayPoints = new ArrayList<>();
                                        for (int i = 0; i <= index; i++) {
                                            newWayPoints.add(getWayPoints().get(i));
                                        }
                                        getMotionAnimation().setWayPoints(newWayPoints);
                                    }
                                    moveControl = false;
                                } else {
                                    movementAnimation(battleGround);
                                }
                            } else {
                                counter++;
                                if (counter > 20) {
                                    currentFrame++;
                                    counter = 0;
                                }
                                if (currentFrame <= battleStancePrepare.getRow()) {
                                    battleStancePrepareAnimation.setFrames(currentFrame);
                                    updateAnimation(battleStancePrepareTexture, battleStancePrepareAnimation);
                                } else {
                                    grid[(int)getPosition().getX()][(int)getPosition().getZ()].setOccupied(true);
                                    currentFrame = 1;
                                    moveControl = true;
                                    preparingForBattle = false;
                                    counter = 0;
                                }
                            }
                        } else {
                            autoBattleMode(battleGround, grid, allies, enemies);
                        }
                    } else {
                        getCharacteristic().setCurentActionPoint(getCharacteristic().getTotalActionPoint());
                        getCharacteristic().updateIndicators();
                        checkInvasion(enemies,battleGround,grid);
                        if (waiting) {
                            if(flowControl){
                                if(!randomPosition.isAlive()){
                                    if(targetPos.getX() != -1.0f && targetPos.getZ() != -1.0f){
                                        this.targetPoint = getElement(grid,targetPos);
                                        waiting = false;
                                        flowControl = false;
                                    }
                                }
                            }else{
                                timer++;
                                if (timer > waitTime) {
                                    timer = 0;
                                    waitTime = (int) (250.0f + Math.random() * 250.0f);
                                    flowControl = true;
                                    targetPos = new Vector3f(-1.0f,-1.0f,-1.0f);
                                    randomPosition = new RandomPosition(targetPos,getLagerPoint(),grid,5.0f,true);
                                    randomPosition.start();
                                    //this.targetPoint = getElement(grid, Generator.getRandomPos(grid, getLagerPoint(), 5.0f, true));
                                    //waiting = false;
                                }
                            }
                        } else {
                            switch (studyEvent) {
                                case CREATE_PATH:
                                    createPath(battleGround, grid);
                                    break;
                                case MOVEMENT_ANIMATION:
                                    movementAnimation(battleGround);
                                    break;
                            }
                        }
                    }
                    break;
                case NEUTRAL:
                    System.out.println("NOT WORK YET");
                    break;
            }
        }
    }

    @Override
    public void update() {
        getProjection().getModelMatrix().identity();
        getProjection().setTranslation(getPosition());
        getSelfIndicators().update(getPosition(),getCharacteristic());
        if(isTakeDamadge()){
            damageCount++;
            if(damageCount > 20){
                damageFrame++;
                damageCount = 0;
            }
            if(damageFrame <= damage.getRow()){
                damageAnimation.setFrames(damageFrame);
                updateAnimation(damageTexture,damageAnimation);
            }else{
                damageCount = 0;
                damageFrame = 1;
                setTakeDamadge(false);
            }
        }
    }

    @Override
    public void draw(Shader shader, boolean shadow) {
        super.draw(shader, shadow);
    }

    // дополнительные методы - начало
    private void blink(){
        if(isBattle()){
            count++;
            if(count > maxCount){
                battleStanceAnimation.nextFrame();
                count = 0;
                maxCount = 20;
            }
        }else {
            if (getBaseStanceAnimation().getCurrentFrame() == 2) {
                count += maxCount / 50.0f;
            }
            count++;
            if (count > maxCount) {
                baseStanceAnimation.nextFrame();
                count = 0;
                maxCount = (int) (50.0f + Math.random() * 150.0f);
            }
        }
    }

    private void targetControl(Vector3f pixel){
        if(pixel.getX() == getId()){
            setTarget(true);
        }else{
            setTarget(false);
        }
    }
    // дополнительные методы - конец

    // РАСЧЕТЫ И АНИМАЦИЯ - НАЧАЛО
    private void battleMode(BattleGround battleGround, Cell[][] grid, List<Character> allies, List<Character> enemies){
        setShowIndicators(false);
        if(Default.isWait()){
            if(action) {
                setShowIndicators(true);
                switch (battleEvent) {
                    case PREPARED_AREA:
                        runPathfindingAlgorithm();
                        getPathfindingAlgorithm().setup(getCharacteristic(), battleGround, grid, grid[(int)getPosition().getX()][(int)getPosition().getZ()]);
                        getPathfindingAlgorithm().start();
                        battleEvent = CREATE_PATH;
                        break;
                    case CREATE_PATH:
                        if(prepareMove){
                            if(!getPathfindingAlgorithm().isAlive()) {
                                if (!getWayPoints().isEmpty()) {
                                    grid[(int)getPosition().getX()][(int)getPosition().getZ()].setOccupied(false);
                                    getWayPoints().add(0,grid[(int)getPosition().getX()][(int)getPosition().getZ()]);
                                    getMotionAnimation().setup(getPosition(), getWayPoints());
                                    walkAnimation.setFrames(1);
                                    baseStanceAnimation.setFrames(1);
                                    jumpAnimation.setFrames(1);
                                    goUpDownAnimation.setFrames(1);
                                    battleEvent = MOVEMENT_ANIMATION;
                                    prepareMove = false;
                                    setJump(false);
                                    for (int x = 0; x < grid.length; x++) {
                                        for (int z = 0; z < grid[0].length; z++) {
                                            if (!grid[x][z].isWayPoint()) {
                                                grid[x][z].setVisible(false);
                                            }
                                        }
                                    }
                                }else{
                                    System.out.println("WayPoints is empty!!");
                                }
                            }
                        }else {
                            boolean action = actionControl(grid, enemies);
                            if (!action) {
                                Cell finish = getFinishCell(grid);
                                if (finish == null) {
                                    for (int x = 0; x < grid.length; x++) {
                                        for (int z = 0; z < grid[0].length; z++) {
                                            if (grid[x][z].isVisible()) {
                                                grid[x][z].setWayPoint(false);
                                            }
                                        }
                                    }
                                } else {
                                    if (finish.getId() != tempFinish.getId()) {
                                        if (!getPathfindingAlgorithm().isAlive()) {
                                            runPathfindingAlgorithm();
                                            getPathfindingAlgorithm().setup(getCharacteristic(), battleGround, grid, getWayPoints(), grid[(int) getPosition().getX()][(int) getPosition().getZ()], finish, false);
                                            getPathfindingAlgorithm().start();
                                        }
                                    }

                                    // ЗАПЛАТКА!!!!
                                    boolean block = false;
                                    for (Character character : enemies) {
                                        if (Pixel.getPixel().getX() == character.getId()) {
                                            block = true;
                                        }
                                    }
                                    // ЗАПЛАТКА!!!!

                                    if (GameController.getInstance().isLeftClick() && !block) {
                                        prepareMove = true;
                                    }
                                }

                                if(finish != null) {
                                    tempFinish = finish;
                                }else{
                                    tempFinish = new Cell();
                                }
                            }
                        }
                        break;
                    case MOVEMENT_ANIMATION:
                        Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
                        int motion = getMotionAnimation().motion(this, battleGround, position, getPosition(), getCharacteristic(), jumpAnimation, goUpDownAnimation, walkAnimation);
                        if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
                            setPosition(position);
                        }

                        if (motion == 0) {
                            preparingForBattle = true;
                            Cell currentElement = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                            currentElement.setOccupied(true);
                            if (currentElement.isBlueZona()) {
                                getCharacteristic().setCurentActionPoint(getCharacteristic().getCurentActionPoint() - 1);
                            } else if (currentElement.isGoldZona()) {
                                getCharacteristic().setCurentActionPoint(getCharacteristic().getCurentActionPoint() - 2);
                            }
                            updateAnimation(baseStanceTextrue,baseStanceAnimation);
                            setJump(false);
                            for(int x=0; x<grid.length; x++){
                                for(int z=0; z<grid[0].length; z++){
                                    grid[x][z].setGrayZona();
                                    grid[x][z].setVisible(false);
                                    grid[x][z].setWayPoint(false);
                                }
                            }
                            if (getCharacteristic().getCurentActionPoint() > 0) {
                                if (getCharacteristic().getStamina() > 0) {
                                    battleEvent = PREPARED_AREA;
                                } else {
                                    battleEvent = CHOICE_DIRECTION;
                                }
                            } else {
                                battleEvent = CHOICE_DIRECTION;
                            }
                        }else if (motion == 1) {
                            setJump(false);
                            updateAnimation(walkTexture,walkAnimation);
                        }else if (motion == 2) {
                            setJump(true);
                            updateAnimation(jumpTexture,jumpAnimation);
                        }else if (motion == 3) {
                            setJump(false);
                            updateAnimation(goUpDownTexture,goUpDownAnimation);
                        }else if(motion == 4){
                            setJump(false);
                            updateAnimation(baseStanceTextrue,baseStanceAnimation);
                        }
                        break;
                    case ATTACK_ANIMATION:
                        if(isBaseAttack){
                            if(enemySavedCharacter != null) {
                                if(firstBaseAttack){
                                    // ОБРОБОТКА ПОВОРОТА - НАЧАЛО
                                    Vector3f enemyPos = new Vector3f(enemySavedCharacter.getPosition()); enemyPos.setY(0.0f);
                                    Vector3f currentPos = new Vector3f(getPosition()); currentPos.setY(0.0f);
                                    Vector3f direction = new Vector3f(enemyPos.sub(currentPos)).normalize();
                                    if(direction.getZ() >= 0.5f){ // NORTH
                                        setLook(NORTH);
                                    }else if(direction.getX() >= 0.5f){ // WEST
                                        setLook(WEST);
                                    }else if(direction.getZ() <= -0.5f){ // SOUTH
                                        setLook(SOUTH);
                                    }else if(direction.getX() <= -0.5f){ // EAST
                                        setLook(EAST);
                                    }
                                    // ОБРОБОТКА ПОВОРОТА - КОНЕЦ
                                    firstBaseAttack = false;
                                }else {
                                    counter++;
                                    if (counter > 10) {
                                        currentFrame++;
                                        counter = 0;
                                    }
                                    if (currentFrame <= baseAttack.getRow()) {
                                        baseAttackAnimation.setFrames(currentFrame);
                                        updateAnimation(baseAttackTexture, baseAttackAnimation);
                                    } else {
                                        counter = 0;
                                        currentFrame = 1;
                                        isBaseAttack = false;
                                        firstBaseAttack = true;
                                        getCharacteristic().setCurentActionPoint(getCharacteristic().getCurentActionPoint() - 1);
                                        getCharacteristic().setStamina(getCharacteristic().getStamina() - 10);
                                        enemySavedCharacter.getCharacteristic().setHealth(enemySavedCharacter.getCharacteristic().getHealth() - getCharacteristic().getPhysicalPower());
                                        enemySavedCharacter.setTakeDamadge(true);
                                        if(enemySavedCharacter.getCharacteristic().getHealth() < 0){
                                            enemySavedCharacter.getCharacteristic().setHealth(0);
                                        }
                                        if (getCharacteristic().getCurentActionPoint() > 0) {
                                            if (getCharacteristic().getStamina() > 0) {
                                                battleEvent = PREPARED_AREA;
                                            } else {
                                                battleEvent = CHOICE_DIRECTION;
                                            }
                                        } else {
                                            battleEvent = CHOICE_DIRECTION;
                                        }
                                    }
                                }
                            }else{
                                System.out.println("нет врага переменна пустая ... ");
                            }
                        }
                        break;
                    case CHOICE_DIRECTION:
                        if(choiceDirection){
                            boolean end = false;
                            Cell tempCell = null;
                            for(int x=0; x<grid.length; x++){
                                for(int z=0; z<grid[0].length; z++){
                                    if(grid[x][z].isTarget()){
                                        grid[x][z].setGreenZona();
                                        // ОБРОБОТКА ПОВОРОТА - НАЧАЛО
                                        Vector3f lookPos = new Vector3f(grid[x][z].getPosition()); lookPos.setY(0.0f);
                                        Vector3f currentPos = new Vector3f(getPosition()); currentPos.setY(0.0f);
                                        Vector3f direction = new Vector3f(lookPos.sub(currentPos)).normalize();
                                        if(direction.getZ() >= 0.5f){ // NORTH
                                            setLook(NORTH);
                                        }else if(direction.getX() >= 0.5f){ // WEST
                                            setLook(WEST);
                                        }else if(direction.getZ() <= -0.5f){ // SOUTH
                                            setLook(SOUTH);
                                        }else if(direction.getX() <= -0.5f){ // EAST
                                            setLook(EAST);
                                        }
                                        // ОБРОБОТКА ПОВОРОТА - КОНЕЦ
                                        setTurn();
                                        tempCell = grid[x][z];
                                        break;
                                    }
                                }
                            }

                            if(GameController.getInstance().isLeftClick() && tempCell != null){
                                end = true;
                            }

                            if(end) {
                                for(int x=0; x<grid.length; x++){
                                    for(int z=0; z<grid[0].length; z++){
                                        grid[x][z].setGrayZona();
                                        grid[x][z].setWayPoint(false);
                                        grid[x][z].setVisible(false);
                                    }
                                }
                                setStop(false);
                                choiceDirection = false;
                                battleEvent = PREPARED_AREA;
                                Default.setWait(false);
                                this.action = false;
                            }
                        }else{
                            setStop(true);
                            Map<Integer,Vector3f> directions = new HashMap<>();
                            directions.put(0,new Vector3f(0.0f,0.0f,1.0f));
                            directions.put(1,new Vector3f(-1.0f,0.0f,0.0f));
                            directions.put(2,new Vector3f(0.0f,0.0f,-1.0f));
                            directions.put(3,new Vector3f(1.0f,0.0f,0.0f));
                            for(int key = 0; key < 4; key++){
                                Cell cell = getElement(grid,new Vector3f(getPosition()).add(directions.get(key)));
                                if(cell != null){
                                    cell.setGrayZona();
                                    cell.setVisible(true);
                                }
                            }
                            choiceDirection = true;
                        }
                        break;
                }
            }
        }else{
            if(sampleData != Time.getSecond()){
                getCharacteristic().setInitiative(getCharacteristic().getInitiative() + getCharacteristic().getInitiativeCharge());
                if(getCharacteristic().getInitiative() >= 100){
                    getCharacteristic().setCurentActionPoint(getCharacteristic().getTotalActionPoint());
                    getCharacteristic().setInitiative(0);
                    getCharacteristic().updateIndicators();
                    this.action = true;
                    battleEvent = PREPARED_AREA;
                    Default.setWait(true);
                }
            }
            sampleData = Time.getSecond();
        }
    }

    private void autoBattleMode(BattleGround battleGround, Cell[][] grid, List<Character> allies, List<Character> enemies){
        setShowIndicators(false);
        if(Default.isWait()){
            if(action) {
                setShowIndicators(true);
                switch (battleEvent) {
                    case PREPARED_AREA:
                        SimpleAI.dataLoading(grid,this,allies,enemies);
                        SimpleAI.dataAnalyze();
                        counter = 0;
                        runPathfindingAlgorithm();
                        getPathfindingAlgorithm().setup(getCharacteristic(), battleGround, grid, grid[(int)getPosition().getX()][(int)getPosition().getZ()]);
                        getPathfindingAlgorithm().start();
                        battleEvent = CREATE_PATH;
                        autoControl = false;
                        break;
                    case CREATE_PATH:
                        if(autoControl){
                            if (!getPathfindingAlgorithm().isAlive()) {
                                if (!getWayPoints().isEmpty()) {
                                    grid[(int)getPosition().getX()][(int)getPosition().getZ()].setOccupied(false);
                                    List<Cell>wayPoints = new ArrayList<>();
                                    boolean last = false;
                                    for(int i=0; i<getWayPoints().size(); i++){
                                        if(last){
                                            getWayPoints().get(i).setWayPoint(false);
                                        }else {
                                            if (getWayPoints().get(i).isBlueZona() || getWayPoints().get(i).isGoldZona()) {
                                                wayPoints.add(getWayPoints().get(i));
                                            } else {
                                                getWayPoints().get(i).setWayPoint(false);
                                                last = true;
                                            }
                                        }
                                    }
                                    wayPoints.add(0,grid[(int)getPosition().getX()][(int)getPosition().getZ()]);
                                    getMotionAnimation().setup(getPosition(), wayPoints);
                                    walkAnimation.setFrames(1);
                                    baseStanceAnimation.setFrames(1);
                                    jumpAnimation.setFrames(1);
                                    goUpDownAnimation.setFrames(1);
                                    battleEvent = MOVEMENT_ANIMATION;
                                    prepareMove = false;
                                    setJump(false);
                                    for (int x = 0; x < grid.length; x++) {
                                        for (int z = 0; z < grid[0].length; z++) {
                                            if (!grid[x][z].isWayPoint()) {
                                                grid[x][z].setVisible(false);
                                            }
                                        }
                                    }
                                }else{
                                    System.out.println("WayPoints is empty!!");
                                    for(int x=0; x<grid.length; x++){
                                        for(int z=0; z<grid[0].length; z++){
                                            grid[x][z].setGrayZona();
                                            grid[x][z].setVisible(false);
                                            grid[x][z].setWayPoint(false);
                                        }
                                    }
                                    battleEvent = PREPARED_AREA;
                                    Default.setWait(false);
                                    this.action = false;
                                }
                            }
                        }else{
                            counter++;
                            if(counter > 200) {
                                boolean action = actionControl(grid,SimpleAI.getTargetCharacter());
                                if(!action) {
                                    if (!getPathfindingAlgorithm().isAlive()) {
                                        runPathfindingAlgorithm();
                                        getPathfindingAlgorithm().setup(getCharacteristic(), battleGround, grid, getWayPoints(), grid[(int) getPosition().getX()][(int) getPosition().getZ()], SimpleAI.getCloserCell(), true);
                                        getPathfindingAlgorithm().start();
                                        autoControl = true;
                                        counter = 0;
                                    }
                                }
                            }
                        }
                        break;
                    case MOVEMENT_ANIMATION:
                        Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
                        int motion = getMotionAnimation().motion(this, battleGround, position, getPosition(), getCharacteristic(), jumpAnimation, goUpDownAnimation, walkAnimation);
                        if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
                            setPosition(position);
                        }

                        if (motion == 0) {
                            preparingForBattle = true;
                            Cell currentElement = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                            currentElement.setOccupied(true);
                            if (currentElement.isBlueZona()) {
                                getCharacteristic().setCurentActionPoint(getCharacteristic().getCurentActionPoint() - 1);
                            } else if (currentElement.isGoldZona()) {
                                getCharacteristic().setCurentActionPoint(getCharacteristic().getCurentActionPoint() - 2);
                            }
                            updateAnimation(baseStanceTextrue,baseStanceAnimation);
                            setJump(false);
                            for(int x=0; x<grid.length; x++){
                                for(int z=0; z<grid[0].length; z++){
                                    grid[x][z].setGrayZona();
                                    grid[x][z].setVisible(false);
                                    grid[x][z].setWayPoint(false);
                                }
                            }
                            if (getCharacteristic().getCurentActionPoint() > 0) {
                                if (getCharacteristic().getStamina() > 0) {
                                    battleEvent = PREPARED_AREA;
                                } else {
                                    battleEvent = PREPARED_AREA;
                                    Default.setWait(false);
                                    this.action = false;
                                }
                            } else {
                                battleEvent = PREPARED_AREA;
                                Default.setWait(false);
                                this.action = false;
                            }
                        }else if (motion == 1) {
                            setJump(false);
                            updateAnimation(walkTexture,walkAnimation);
                        }else if (motion == 2) {
                            setJump(true);
                            updateAnimation(jumpTexture,jumpAnimation);
                        }else if (motion == 3) {
                            setJump(false);
                            updateAnimation(goUpDownTexture,goUpDownAnimation);
                        }else if(motion == 4){
                            setJump(false);
                            updateAnimation(baseStanceTextrue,baseStanceAnimation);
                        }
                        break;
                    case ATTACK_ANIMATION:
                        if(isBaseAttack){
                            if(enemySavedCharacter != null) {
                                if(firstBaseAttack){
                                    // ОБРОБОТКА ПОВОРОТА - НАЧАЛО
                                    Vector3f enemyPos = new Vector3f(enemySavedCharacter.getPosition()); enemyPos.setY(0.0f);
                                    Vector3f currentPos = new Vector3f(getPosition()); currentPos.setY(0.0f);
                                    Vector3f direction = new Vector3f(enemyPos.sub(currentPos)).normalize();
                                    if(direction.getZ() >= 0.5f){ // NORTH
                                        setLook(NORTH);
                                    }else if(direction.getX() >= 0.5f){ // WEST
                                        setLook(WEST);
                                    }else if(direction.getZ() <= -0.5f){ // SOUTH
                                        setLook(SOUTH);
                                    }else if(direction.getX() <= -0.5f){ // EAST
                                        setLook(EAST);
                                    }
                                    // ОБРОБОТКА ПОВОРОТА - КОНЕЦ
                                    firstBaseAttack = false;
                                }else {
                                    counter++;
                                    if (counter > 10) {
                                        currentFrame++;
                                        counter = 0;
                                    }

                                    if (currentFrame <= baseAttack.getRow()) {
                                        baseAttackAnimation.setFrames(currentFrame);
                                        updateAnimation(baseAttackTexture, baseAttackAnimation);
                                    } else {
                                        counter = 0;
                                        currentFrame = 1;
                                        isBaseAttack = false;
                                        firstBaseAttack = true;
                                        getCharacteristic().setCurentActionPoint(getCharacteristic().getCurentActionPoint() - 1);
                                        getCharacteristic().setStamina(getCharacteristic().getStamina() - 10);
                                        enemySavedCharacter.getCharacteristic().setHealth(enemySavedCharacter.getCharacteristic().getHealth() - getCharacteristic().getPhysicalPower());
                                        enemySavedCharacter.setTakeDamadge(true);
                                        if (enemySavedCharacter.getCharacteristic().getHealth() < 0) {
                                            enemySavedCharacter.getCharacteristic().setHealth(0);
                                        }
                                        if (getCharacteristic().getCurentActionPoint() > 0) {
                                            if (getCharacteristic().getStamina() > 0) {
                                                battleEvent = PREPARED_AREA;
                                            } else {
                                                battleEvent = PREPARED_AREA;
                                                Default.setWait(false);
                                                this.action = false;
                                            }
                                        } else {
                                            battleEvent = PREPARED_AREA;
                                            Default.setWait(false);
                                            this.action = false;
                                        }
                                    }
                                }
                            }else{
                                System.out.println("нет врага переменная пустая ... ");
                            }
                        }
                        break;
                }
            }
        }else{
            if(sampleData != Time.getSecond()){
                getCharacteristic().setInitiative(getCharacteristic().getInitiative() + getCharacteristic().getInitiativeCharge());
                if(getCharacteristic().getInitiative() >= 100){
                    getCharacteristic().setCurentActionPoint(getCharacteristic().getTotalActionPoint());
                    getCharacteristic().setInitiative(0);
                    getCharacteristic().updateIndicators();
                    this.action = true;
                    battleEvent = PREPARED_AREA;
                    Default.setWait(true);
                }
            }
            sampleData = Time.getSecond();
        }
    }

    private boolean actionControl(Cell[][] grid, List<Character> enemy){
        isBaseAttack = false;
        boolean action = false;
        enemySavedCharacter = null;
        if(GameController.getInstance().isLeftClick()){
            Vector3f pixel = Pixel.getPixel();
            for(Character character : enemy){
                if(character.getId() == pixel.getX() && !character.isDead()){
                    if(getPosition().getX() - 1.0f <= character.getPosition().getX() && character.getPosition().getX() <= getPosition().getX() + 1.0f) {
                        if (getPosition().getZ() - 1.0f <= character.getPosition().getZ() && character.getPosition().getZ() <= getPosition().getZ() + 1.0f) {
                            if (Math.abs(character.getPosition().getY() - getPosition().getY()) <= 0.5f) {
                                enemySavedCharacter = character;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(enemySavedCharacter != null){
            battleEvent = ATTACK_ANIMATION;
            action = true;
            isBaseAttack = true;
            for(int x=0; x<grid.length; x++){
                for(int z=0; z<grid[0].length; z++){
                    grid[x][z].setVisible(false);
                }
            }
        }
        return action;
    }

    private boolean actionControl(Cell[][] grid, Character enemy){
        isBaseAttack = false;
        boolean fastAttack = false;
        enemySavedCharacter = null;

        if(enemy != null){
            if(!enemy.isDead()){
                if(getPosition().getX() - 1.0f <= enemy.getPosition().getX() && enemy.getPosition().getX() <= getPosition().getX() + 1.0f) {
                    if (getPosition().getZ() - 1.0f <= enemy.getPosition().getZ() && enemy.getPosition().getZ() <= getPosition().getZ() + 1.0f) {
                        if (Math.abs(enemy.getPosition().getY() - getPosition().getY()) <= 0.5f) {
                            enemySavedCharacter = enemy;
                        }
                    }
                }
            }
        }

        if(enemySavedCharacter != null){
            battleEvent = ATTACK_ANIMATION;
            fastAttack = true;
            isBaseAttack = true;
            for(int x=0; x<grid.length; x++){
                for(int z=0; z<grid[0].length; z++){
                    grid[x][z].setVisible(false);
                }
            }
        }
        return fastAttack;
    }

    private void researchMode(BattleGround battleGround, Cell[][] grid){
        if(remap){
            studyEventMode(battleGround, grid);
        }else {
            if (isSelected()) {
                studyEventMode(battleGround, grid);
            } else {
                if (studyEvent == MOVEMENT_ANIMATION) {
                    movementAnimation(battleGround);
                }
            }
        }
    }

    private void studyEventMode(BattleGround battleGround, Cell[][] grid){
        switch (studyEvent){
            case CREATE_PATH:
                createPath(battleGround, grid);
                break;
            case MOVEMENT_ANIMATION:
                if(GameController.getInstance().isLeftClick()){
                    moveClick = true;
                    Vector3f currentPos = new Vector3f(getPosition());
                    currentPos.setY(0.0f);
                    int lastIndex = -1;
                    for(int i=0; i<getWayPoints().size(); i++){
                        Vector3f somePos = new Vector3f(getWayPoints().get(i).getPosition());
                        somePos.setY(0.0f);
                        if(Math.abs(currentPos.sub(somePos).length()) <= 1.0f){
                            lastIndex = i;
                        }
                    }

                    if(lastIndex != -1){
                        List<Cell> newWayPoints = new ArrayList<>();
                        for(int i=0; i<=lastIndex; i++){
                            newWayPoints.add(getWayPoints().get(i));
                        }
                        getMotionAnimation().setWayPoints(newWayPoints);
                    }
                }
                movementAnimation(battleGround);
                break;
        }
    }

    private void createPath(BattleGround battleGround, Cell[][] grid){
        if (remap) {
            if (targetPoint != null && !pathCheck) {
                bypass(battleGround, grid);
            }
        } else {
            if (targetPoint != null && !pathCheck) {
                path(battleGround, grid);
            }
        }

        if(pathCheck){
            if(!getPathfindingAlgorithm().isAlive()){
                pathCheck = false;
                remap = false;
                studyModeActive = true;

                Vector3f temp = this.targetPoint.getModifiedPosition();

                if(getWayPoints().size() == 0){
                    studyModeActive = false;
                    waiting = true;
                }else{
                    Cell point = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                    getWayPoints().add(0,point);
                }
                this.targetPoint = grid[(int)temp.getX()][(int)temp.getZ()];
            }
        }

        if (studyModeActive) {
            // Инициируем стартовые данные для анимации движенния и механики передвижения
            getMotionAnimation().setup(getPosition(), getWayPoints());
            walkAnimation.setFrames(1);
            baseStanceAnimation.setFrames(1);
            jumpAnimation.setFrames(1);
            goUpDownAnimation.setFrames(1);
            studyEvent = MOVEMENT_ANIMATION;
            setJump(false);
            studyModeActive = false;
            counter = 0;
        }
    }

    private void path(BattleGround battleGround, Cell[][] grid){
        this.targetPoint.setParent(null);
        runPathfindingAlgorithm();
        getPathfindingAlgorithm().setup(getCharacteristic(), battleGround, grid, getWayPoints(), grid[(int) getPosition().getX()][(int) getPosition().getZ()], targetPoint, false);
        getPathfindingAlgorithm().start();
        pathCheck = true;
    }

    private void bypass(BattleGround battleGround, Cell[][] grid){
        this.targetPoint.setParent(null);
        this.targetPoint.setOccupied(false);
        runPathfindingAlgorithm();
        getPathfindingAlgorithm().setup(getCharacteristic(), battleGround, grid, getWayPoints(), grid[(int) getPosition().getX()][(int) getPosition().getZ()], targetPoint, false);
        getPathfindingAlgorithm().start();
        pathCheck = true;
    }

    private void movementAnimation(BattleGround battleGround){
        Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
        int motion = getMotionAnimation().motion(this, battleGround, position, getPosition(), getCharacteristic(), jumpAnimation, goUpDownAnimation, walkAnimation);

        if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
            setPosition(position);
        }

        if (motion == 0) {
            if(moveClick){
                remap = true;
                moveClick = false;
            }else {
                this.targetPoint = null;
            }
            counter = 0;
            updateAnimation(baseStanceTextrue, baseStanceAnimation);
            setJump(false);
            studyEvent = CREATE_PATH;
            waiting = true;
        }else if (motion == 1) {
            counter = 0;
            setJump(false);
            updateAnimation(walkTexture,walkAnimation);
        }else if (motion == 2) {
            counter = 0;
            setJump(true);
            updateAnimation(jumpTexture,jumpAnimation);
        }else if (motion == 3) {
            counter = 0;
            setJump(false);
            updateAnimation(goUpDownTexture,goUpDownAnimation);
        }else if(motion == 4){
            counter++;
            if(counter > counterMax) {
                Vector3f currentPos = new Vector3f(getPosition());
                currentPos.setY(0.0f);
                Vector3f wayPointPos = new Vector3f(getWayPoints().get(getWayPoints().size() - 1).getModifiedPosition());
                wayPointPos.setY(0.0f);
                if(currentPos.sub(wayPointPos).length() >= 3.0f) {
                    studyEvent = CREATE_PATH;
                    this.targetPoint = getWayPoints().get(getWayPoints().size() - 1);
                    remap = true;
                    counter = 0;
                    counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
                }else{
                    this.targetPoint = null;
                    updateAnimation(baseStanceTextrue,baseStanceAnimation);
                    setJump(false);
                    studyEvent = CREATE_PATH;
                    waiting = true;
                    counter = 0;
                    counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
                }
            }else {
                updateAnimation(baseStanceTextrue,baseStanceAnimation);
                setJump(false);
            }
        }

        if(getWayPoints().isEmpty()){
            System.out.println("Error");
            if(moveClick){
                remap = true;
                moveClick = false;
            }else {
                this.targetPoint = null;
            }
            counter = 0;
            updateAnimation(baseStanceTextrue, baseStanceAnimation);
            setJump(false);
            studyEvent = CREATE_PATH;
            waiting = true;
        }
    }

    private void checkInvasion(List<Character> enemies, BattleGround battleGrounds, Cell[][] grid){
        Vector3f mainPos = new Vector3f(getPosition()); mainPos.setY(0.0f);
        for (Character character : enemies) {
            if(!character.isDead()) {
                Vector3f enemyPos = new Vector3f(character.getPosition());
                enemyPos.setY(0.0f);
                if (Math.abs(mainPos.sub(enemyPos).length()) <= getCharacteristic().getVision()) { // Если в зоне видимости!
                    Vector3f lagerPos = new Vector3f(getLagerPoint());
                    lagerPos.setY(0.0f);
                    if (mainPos.sub(lagerPos).length() <= getCharacteristic().getVision()) { // если персонаж в зоне видимости лагеря
                        Vector3f direction = new Vector3f(character.getPosition().sub(getPosition())).normalize();
                        float distance = Math.abs(new Vector3f(character.getPosition().sub(getPosition())).length() / 2.0f);
                        Vector3f lagerPoint = new Vector3f(getPosition().add(direction.mul(distance)));
                        int x = Math.round(lagerPoint.getX());
                        int z = Math.round(lagerPoint.getZ());
                        lagerPoint = grid[x][z].getModifiedPosition();
                        battleGrounds.setActive(true);
                        battleGrounds.setLocalPoint(lagerPoint);
                        battleGrounds.setRadius(15);
                    } else { // если персонаж вне зоне видимости своего лагеря
                        Vector3f direction = new Vector3f(character.getPosition().sub(getPosition())).normalize();
                        float distance = Math.abs(new Vector3f(character.getPosition().sub(getPosition())).length() / 2.0f);
                        Vector3f lagerPoint = new Vector3f(getPosition().add(direction.mul(distance)));
                        int x = Math.round(lagerPoint.getX());
                        int z = Math.round(lagerPoint.getZ());
                        lagerPoint = grid[x][z].getModifiedPosition();
                        battleGrounds.setActive(true);
                        battleGrounds.setLocalPoint(lagerPoint);
                        battleGrounds.setRadius(10);
                    }
                    break;
                }
            }
        }
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

    private Cell getFinishCell(Cell[][]grid){
        Cell finish = null;
        // ВРЕМЕННО! НАДО ПОДУМАТЬ!!!
        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                if(grid[x][z].isTarget() && !grid[x][z].isBlocked() && !grid[x][z].isOccupied()){
                    if(grid[x][z].isBlueZona() || grid[x][z].isGoldZona()) {
                        finish = grid[x][z];
                        break;
                    }
                }
            }
        }

        return finish;
    }
    // РАСЧЕТЫ И АНИМАЦИЯ - КОНЕЦ

    // методы гетеры и сетеры - начало
    // текстуры
    @Override
    public Texture getBaseStanceTextrue() {
        return baseStanceTextrue;
    }

    @Override
    public void setBaseStanceTextrue(Texture baseStanceTextrue) {
        this.baseStanceTextrue = baseStanceTextrue;
    }

    @Override
    public Texture getWalkTexture() {
        return walkTexture;
    }

    @Override
    public void setWalkTexture(Texture walkTexture) {
        this.walkTexture = walkTexture;
    }

    @Override
    public Texture getJumpTexture() {
        return jumpTexture;
    }

    @Override
    public void setJumpTexture(Texture jumpTexture) {
        this.jumpTexture = jumpTexture;
    }

    @Override
    public Texture getGoUpDownTexture() {
        return goUpDownTexture;
    }

    @Override
    public void setGoUpDownTexture(Texture goUpDownTexture) {
        this.goUpDownTexture = goUpDownTexture;
    }

    @Override
    public Texture getBattleStancePrepareTexture() {
        return battleStancePrepareTexture;
    }

    @Override
    public void setBattleStancePrepareTexture(Texture battleStancePrepareTexture) {
        this.battleStancePrepareTexture = battleStancePrepareTexture;
    }

    @Override
    public Texture getBattleStanceTexture() {
        return battleStanceTexture;
    }

    @Override
    public void setBattleStanceTexture(Texture battleStanceTexture) {
        this.battleStanceTexture = battleStanceTexture;
    }

    @Override
    public Texture getBaseAttackTexture() {
        return baseAttackTexture;
    }

    @Override
    public void setBaseAttackTexture(Texture baseAttackTexture) {
        this.baseAttackTexture = baseAttackTexture;
    }

    @Override
    public Texture getBackstabTexture() {
        return null;
    }

    @Override
    public void setBackstabTexture(Texture backstabTexture) {

    }

    // анимация
    @Override
    public ImageAnimation getBaseStanceAnimation() {
        return baseStanceAnimation;
    }

    @Override
    public void setBaseStanceAnimation(ImageAnimation baseStanceAnimation) {
        this.baseStanceAnimation = baseStanceAnimation;
    }

    @Override
    public ImageAnimation getWalkAnimation() {
        return walkAnimation;
    }

    @Override
    public void setWalkAnimation(ImageAnimation walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    @Override
    public ImageAnimation getJumpAnimation() {
        return jumpAnimation;
    }

    @Override
    public void setJumpAnimation(ImageAnimation jumpAnimation) {
        this.jumpAnimation = jumpAnimation;
    }

    @Override
    public ImageAnimation getGoUpDownAnimation() {
        return goUpDownAnimation;
    }

    @Override
    public void setGoUpDownAnimation(ImageAnimation goUpDownAnimation) {
        this.goUpDownAnimation = goUpDownAnimation;
    }

    @Override
    public ImageAnimation getBattleStancePrepareAnimation() {
        return battleStancePrepareAnimation;
    }

    @Override
    public void setBattleStancePrepareAnimation(ImageAnimation battleStancePrepareAnimation) {
        this.battleStancePrepareAnimation = battleStancePrepareAnimation;
    }

    @Override
    public ImageAnimation getBattleStanceAnimation() {
        return battleStanceAnimation;
    }

    @Override
    public void setBattleStanceAnimation(ImageAnimation battleStanceAnimation) {
        this.battleStanceAnimation = battleStanceAnimation;
    }

    @Override
    public ImageAnimation getBaseAttackAnimation() {
        return baseAttackAnimation;
    }

    @Override
    public void setBaseAttackAnimation(ImageAnimation baseAttackAnimation) {
        this.baseAttackAnimation = baseAttackAnimation;
    }

    @Override
    public ImageAnimation getBackstabAttackAnimation() {
        return null;
    }

    @Override
    public void setBackstabAttackAnimation(ImageAnimation backstabAttackAnimation) {

    }
    // методы гетеры и сетеры - конец
}
