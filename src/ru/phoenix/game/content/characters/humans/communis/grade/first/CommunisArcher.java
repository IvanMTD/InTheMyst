package ru.phoenix.game.content.characters.humans.communis.grade.first;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.ImageAnimLoader;
import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.sprite.TextureConfig;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.HumanDraw;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.property.GameController;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class CommunisArcher extends HumanDraw implements Character {
    // константы
    private final int PREPARED_AREA         = 0x90001;
    private final int CREATE_PATH           = 0x90002;
    private final int MOVEMENT_ANIMATION    = 0x90003;
    private final int ATTACK_ANIMATION      = 0x90004;

    // константы класса
    private final TextureConfig baseStance = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_stand.png",3,1);
    private final TextureConfig walk = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_walk.png",12,1);
    private final TextureConfig jump = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_jump.png",7,1);
    private final TextureConfig goUpDown = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_climbing.png",4,1);
    private final TextureConfig battleStancePrepare = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_bs_open.png",6,1);
    private final TextureConfig battleStance = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_bs.png",6,1);
    private final TextureConfig baseAttack = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_base_attack.png",10,1);
    private final TextureConfig damage = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_damage.png",6,1);
    private final TextureConfig dead = new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_dead.png",6,1);

    // константы боеприпасов
    private final TextureConfig simpleArrow = new TextureConfig("./data/content/texture/items/ammo/simple_arrow.png",2,1);

    // анимации
    // персонаж
    private ImageAnimation baseStanceAnimation;
    private ImageAnimation walkAnimation;
    private ImageAnimation jumpAnimation;
    private ImageAnimation goUpDownAnimation; // может быть стоит разделить?
    private ImageAnimation battleStancePrepareAnimation;
    private ImageAnimation battleStanceAnimation;
    private ImageAnimation baseAttackAnimation;
    private ImageAnimation damageAnimation;
    private ImageAnimation deadAnimation;
    // боеприпасы
    private ImageAnimation simpleArrowAnimation;

    // текстуры
    // персонаж
    private Texture baseStanceTextrue;
    private Texture walkTexture;
    private Texture jumpTexture;
    private Texture goUpDownTexture;
    private Texture battleStancePrepareTexture;
    private Texture battleStanceTexture;
    private Texture baseAttackTexture;
    private Texture damageTexture;
    private Texture deadTexture;
    // боеприпасы
    private Texture simpleArrowTexture;

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
    private boolean firstStart;
    private boolean moveControl;
    private boolean waiting;
    private boolean doubleAnimation;
    private boolean playDead;
    private Character allySavedCharacter;
    private Character enemySavedCharacter;
    private Cell targetPoint;
    private Vector3f tempFinish;
    private List<Vector3f> timeShift;
    private int index;
    private float lastY;

    // конструкторы класса
    // начало
    public CommunisArcher(){
        super();
        init();
    }

    public CommunisArcher(Character character, Vector3f position, Vector3f lagerPoint, float id, int recognition){
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

        // БОЕПРИПАСЫ
        simpleArrowTexture = new Texture2D();
        simpleArrowTexture.setup(null,simpleArrow.getPath(),GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        simpleArrowAnimation = initAnimation(simpleArrowTexture,simpleArrow,widthSize);
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
        getCharacteristic().setInitiativeCharge(14);
        // Здоровье
        getCharacteristic().setTotalHealth(25);
        getCharacteristic().setHealth(getCharacteristic().getTotalHealth());
        getCharacteristic().setHealthCharge(0);
        // Манна
        getCharacteristic().setTotalManna(30);
        getCharacteristic().setManna(getCharacteristic().getTotalManna());
        getCharacteristic().setMannaCharge(0);
        // Стамина
        getCharacteristic().setTotalStamina(100);
        getCharacteristic().setStamina(getCharacteristic().getTotalStamina());
        getCharacteristic().setStaminaCharge(14);
        // Характеристики
        getCharacteristic().setPhysicalPower(15);
        getCharacteristic().setMagicPower(10);
        // Движения
        getCharacteristic().setMove(4);
        getCharacteristic().setJump(2);
        getCharacteristic().setSpeed(2);
        // Обзор
        getCharacteristic().setVision(12);
    }

    @Override
    public void preset(){
        targetPoint = null;
        // тригеры умений
        isBaseAttack = false;
        // доп инициализация
        count = 0;
        maxCount = (int)(50.0f + Math.random() * 150.0f);
        battleEvent = PREPARED_AREA;
        studyEvent = CREATE_PATH;
        counter = 0;
        counterMax = (int)(200.0f + (float)Math.random() * 300.0f);
        timer = 0;
        waitTime = (int)(250.0f + Math.random() * 250.0f);
        currentFrame = 1;
        sampleData = Time.getSecond();
        tempFinish = new Vector3f(-1.0f,-1.0f,-1.0f);
        index = 0;
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
        firstStart = true;
        preparingForBattle = true;
        firstBaseAttack = true;
        waiting = true;
        moveControl = true;
        doubleAnimation = false;
        playDead = true;
    }
    // методы инициализации - конец

    @Override
    public void interaction(Cell[][]grid, Cell targetElement, Vector3f pixel, List<Character> enemy, List<Character> ally, BattleGround battleGround) {
        targetControl(pixel);
        if(isDead()){
            if(playDead){
                deadCount++;
                if(deadCount > 20){
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
            if(getCharacteristic().getHealth() == 0){
                setDead(true);
            }
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
                                    movementAnimation();
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
                                    currentFrame = 1;
                                    moveControl = true;
                                    preparingForBattle = false;
                                    counter = 0;
                                }
                            }
                        } else {
                            battleMode(grid, ally, enemy);
                        }
                    } else {
                        if(battleEvent == MOVEMENT_ANIMATION){
                            battleMode(grid,ally,enemy);
                        }else {
                            // Заплатка! Стамина расходуется в классе MotionAnimation
                            getCharacteristic().setCurentActionPoint(getCharacteristic().getTotalActionPoint());
                            getCharacteristic().updateIndicators();
                            researchMode(grid);
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
                                    movementAnimation();
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
                                    currentFrame = 1;
                                    moveControl = true;
                                    preparingForBattle = false;
                                    counter = 0;
                                }
                            }
                        } else {

                        }
                    } else {
                        getCharacteristic().setCurentActionPoint(getCharacteristic().getTotalActionPoint());
                        getCharacteristic().updateIndicators();
                        checkInvasion(enemy,battleGround,grid);
                        if (waiting) {
                            timer++;
                            if (timer > waitTime) {
                                this.targetPoint = getElement(grid, Generator.getRandomPos(grid, getLagerPoint(), 5.0f, true));
                                waiting = false;
                                timer = 0;
                                waitTime = (int) (250.0f + Math.random() * 250.0f);
                            }
                        } else {
                            switch (studyEvent) {
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
                case NEUTRAL:
                    System.out.println("NOT WORK YET");
                    break;
            }
            updateTemplate();
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
                maxCount = 40;
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
    private void battleMode(Cell[][] grid, List<Character> ally, List<Character> enemy){
        if(Default.isWait()){
            if(action) {
                switch (battleEvent) {
                    case PREPARED_AREA:
                        runPathfindingAlgorithm();
                        getPathfindingAlgorithm().setup(grid, getCharacteristic(), getPosition());
                        getPathfindingAlgorithm().start();
                        battleEvent = CREATE_PATH;
                        break;
                    case CREATE_PATH:
                        isBaseAttack = fastAttack(grid,enemy);
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
                                if (!getPathfindingAlgorithm().isAlive()) {
                                    runPathfindingAlgorithm();
                                    getPathfindingAlgorithm().setup(grid, getCharacteristic(), getPosition(), finish);
                                    getPathfindingAlgorithm().start();
                                }
                            }

                            // ЗАПЛАТКА!!!!
                            boolean block = false;
                            for(Character character : enemy){
                                if(Pixel.getPixel().getX() == character.getId()){
                                    block = true;
                                }
                            }
                            // ЗАПЛАТКА!!!!

                            if (GameController.getInstance().isLeftClick() && !block) {
                                getWayPoints().clear();
                                Cell finishElement = getFinishElement(grid);
                                if (finishElement != null) {
                                    List<Cell> reverse = new ArrayList<>();
                                    while (finishElement.getParent() != null) {
                                        reverse.add(finishElement);
                                        finishElement = finishElement.getParent();
                                    }
                                    for (int i = reverse.size() - 1; i >= 0; i--) {
                                        getWayPoints().add(reverse.get(i));
                                    }

                                    Cell point = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                                    getWayPoints().add(0,point);

                                    if (!getWayPoints().isEmpty()) {
                                        getMotionAnimation().setup(getPosition(), getWayPoints());
                                        walkAnimation.setFrames(1);
                                        baseStanceAnimation.setFrames(1);
                                        jumpAnimation.setFrames(1);
                                        goUpDownAnimation.setFrames(1);
                                        battleEvent = MOVEMENT_ANIMATION;
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
                            int motion = getMotionAnimation().motion(position, getPosition(), getCharacteristic(), jumpAnimation, goUpDownAnimation, walkAnimation);
                            if (!position.equals(new Vector3f(-1.0f, -1.0f, -1.0f))) {
                                setPosition(position);
                            }

                            if (motion == 0) {
                                preparingForBattle = true;
                                Cell currentElement = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
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
                                        firstStart = true;
                                    } else {
                                        battleEvent = PREPARED_AREA;
                                        firstStart = true;
                                        Default.setWait(false);
                                        this.action = false;
                                    }
                                } else {
                                    battleEvent = PREPARED_AREA;
                                    firstStart = true;
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
                        }
                        break;
                    case ATTACK_ANIMATION:
                        if(isBaseAttack){
                            if(enemySavedCharacter != null) {
                                if(doubleAnimation){
                                    if(index < timeShift.size()) {
                                        counter++;
                                        setAdditionalProjection(timeShift.get(index));
                                        if(timeShift.get(index).getY() < lastY){
                                            simpleArrowAnimation.setFrames(2);
                                        }
                                        baseAttackAnimation.setFrames(baseAttack.getRow());
                                        updateAnimation(baseAttackTexture,baseAttackAnimation);
                                        updateAdditionalAnimation(simpleArrowTexture,simpleArrowAnimation);
                                        lastY = timeShift.get(index).getY();
                                        if (counter > 2) {
                                            counter = 0;
                                            index++;
                                        }
                                    }else{
                                        // контролеры
                                        index = 0;
                                        counter = 0;
                                        currentFrame = 1;
                                        isBaseAttack = false;
                                        firstBaseAttack = true;
                                        doubleAnimation = false;
                                        useAdditionalAnimation(false);
                                        // результаты
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
                                                firstStart = true;
                                            } else {
                                                battleEvent = PREPARED_AREA;
                                                firstStart = true;
                                                Default.setWait(false);
                                                this.action = false;
                                            }
                                        } else {
                                            battleEvent = PREPARED_AREA;
                                            firstStart = true;
                                            Default.setWait(false);
                                            this.action = false;
                                        }
                                    }
                                }else {
                                    if (firstBaseAttack) {
                                        // ОБРОБОТКА ПОВОРОТА - НАЧАЛО
                                        float mainXOffset = getXOffsetOnScreen(getPosition());
                                        float enemyXOffset = getXOffsetOnScreen(enemySavedCharacter.getPosition());
                                        if (mainXOffset < enemyXOffset) {
                                            setTurn(true);
                                            float yaw = Camera.getInstance().getYaw();
                                            if (90.0f < yaw && yaw < 270.0f) {
                                                setTurn(isTurn());
                                            } else {
                                                setTurn(!isTurn());
                                            }
                                        } else {
                                            setTurn(false);
                                            float yaw = Camera.getInstance().getYaw();
                                            if (90.0f < yaw && yaw < 270.0f) {
                                                setTurn(isTurn());
                                            } else {
                                                setTurn(!isTurn());
                                            }
                                        }
                                        // ОБРОБОТКА ПОВОРОТА - КОНЕЦ


                                        firstBaseAttack = false;
                                    } else {
                                        counter++;
                                        if (counter > 20) {
                                            currentFrame++;
                                            counter = 0;
                                        }
                                        if (currentFrame <= baseAttack.getRow()) {
                                            baseAttackAnimation.setFrames(currentFrame);
                                            updateAnimation(baseAttackTexture, baseAttackAnimation);
                                        } else {
                                            doubleAnimation = true;
                                            currentFrame = 1;
                                            counter = 0;
                                            index = 0;
                                            useAdditionalAnimation(true);
                                            setAdditionalProjection(timeShift.get(0));
                                            simpleArrowAnimation.setFrames(1);
                                            updateAdditionalAnimation(simpleArrowTexture,simpleArrowAnimation);
                                            lastY = timeShift.get(0).getY();
                                        }
                                    }
                                }
                            }else{
                                System.out.println("нет врага переменна пустая ... ");
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
                    this.firstStart = true;
                    battleEvent = PREPARED_AREA;
                    Default.setWait(true);
                }
            }
            sampleData = Time.getSecond();
        }
    }

    private boolean fastAttack(Cell[][] grid, List<Character> enemy){
        boolean fastAttack = false;
        enemySavedCharacter = null;
        if(GameController.getInstance().isLeftClick()) {
            Vector3f pixel = Pixel.getPixel();
            for (Character character : enemy) {
                if (character.getId() == pixel.getX() && !character.isDead()) {
                    if (getPosition().getX() - getCharacteristic().getVision() <= character.getPosition().getX() && character.getPosition().getX() <= getPosition().getX() + getCharacteristic().getVision()) {
                        if (getPosition().getZ() - getCharacteristic().getVision() <= character.getPosition().getZ() && character.getPosition().getZ() <= getPosition().getZ() + getCharacteristic().getVision()) {
                            // начало проверки на высоту
                            Vector3f direction = new Vector3f(character.getPosition().sub(getPosition())).normalize();
                            float distance = Math.abs(new Vector3f(character.getPosition().sub(getPosition())).length());
                            float halfDistance = distance / 2.0f;
                            Vector3f p0 = new Vector3f(getPosition());
                            Vector3f p1 = new Vector3f(getPosition().add(direction.mul(halfDistance)));
                            p1.setY(p1.getY() + 5.0f); // 5.0f это высота полета стрелы
                            Vector3f p2 = new Vector3f(character.getPosition());
                            timeShift = new ArrayList<>();
                            for(float t=0.0f; t<1.0f; t+= 1.0f / (distance * 5.0f)){
                                float x = (1-t) * p0.getX() + t * p2.getX();
                                float y = (float)Math.pow((1-t),2) * p0.getY() + 2 * t * (1-t) * p1.getY() + (float)Math.pow(t,2) * p2.getY();
                                float z = (1-t) * p0.getZ() + t * p2.getZ();
                                Vector3f time = new Vector3f(x,y,z);
                                Cell cell = grid[Math.round(x)][Math.round(z)];
                                //System.out.println(cell.getCurrentHeight() + " | " + time.toString());
                                if(!cell.isBlocked() && cell.getCurrentHeight() <= time.getY() + 1.0f) {
                                    timeShift.add(time);
                                }else{
                                    timeShift.clear();
                                    break;
                                }
                            }
                            //System.out.println("main pos: " + getPosition().toString());
                            //System.out.println("enemy pos: " + character.getPosition().toString());
                            if(timeShift.size() != 0){
                                enemySavedCharacter = character;
                                break;
                            }
                            // конец проверки
                        }
                    }
                }
            }
        }

        if(enemySavedCharacter != null){
            battleEvent = ATTACK_ANIMATION;
            fastAttack = true;
            for(int x=0; x<grid.length; x++){
                for(int z=0; z<grid[0].length; z++){
                    grid[x][z].setVisible(false);
                }
            }
        }
        return fastAttack;
    }

    private void researchMode(Cell[][] grid){
        // Заплатка! Стамина расходуется в классе MotionAnimation
        getCharacteristic().setStamina(getCharacteristic().getTotalStamina());
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
                movementAnimation();
                break;
        }
    }

    private void createPath(Cell[][] grid){
        if(Default.isCreatePath()) {
            if (remap) {
                if (targetPoint != null && !pathCheck) {
                    Default.setCreatePath(false);
                    bypass(grid);
                }
            } else {
                if (targetPoint != null && !pathCheck) {
                    Default.setCreatePath(false);
                    path(grid);
                }
            }
        }

        if(pathCheck){
            if(!getPathfindingAlgorithm().isAlive()){
                pathCheck = false;
                remap = false;
                studyModeActive = true;

                Vector3f temp = this.targetPoint.getModifiedPosition();

                if(getWayPoints().size() == 0){
                    Default.setCreatePath(true);
                    studyModeActive = false;
                    remap = true;
                }else{
                    Cell point = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
                    getWayPoints().add(0,point);
                }
                this.targetPoint = grid[(int)temp.getX()][(int)temp.getZ()];
            }
        }

        if (studyModeActive) {
            Default.setCreatePath(true);
            // Инициируем стартовые данные для анимации движенния и механики передвижения
            getMotionAnimation().setup(getPosition(), getWayPoints());
            walkAnimation.setFrames(1);
            baseStanceAnimation.setFrames(1);
            jumpAnimation.setFrames(1);
            goUpDownAnimation.setFrames(1);
            studyEvent = MOVEMENT_ANIMATION;
            setJump(false);
            studyModeActive = false;
            firstStart = true;
            counter = 0;
        }
    }

    private void path(Cell[][] grid){
        Cell startPosition = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
        this.targetPoint.setParent(null);
        runPathfindingAlgorithm();
        getPathfindingAlgorithm().setup(grid, getCharacteristic(), startPosition, this.targetPoint, getWayPoints(),true);
        getPathfindingAlgorithm().start();
        pathCheck = true;
    }

    private void bypass(Cell[][] grid){
        Cell startPosition = grid[(int)getPosition().getX()][(int)getPosition().getZ()];
        this.targetPoint.setParent(null);
        this.targetPoint.setOccupied(false);
        runPathfindingAlgorithm();
        getPathfindingAlgorithm().setup(grid, getCharacteristic(), startPosition, this.targetPoint, getWayPoints(),false);
        getPathfindingAlgorithm().start();
        pathCheck = true;
    }

    private void movementAnimation(){
        if (firstStart) {
            setTurn();
            firstStart = false;
        } else {
            Vector3f position = new Vector3f(-1.0f, -1.0f, -1.0f);
            int motion = getMotionAnimation().motion(position, getPosition(), getCharacteristic(), jumpAnimation, goUpDownAnimation, walkAnimation);

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
            Vector3f enemyPos = new Vector3f(character.getPosition());
            enemyPos.setY(0.0f);
            if (Math.abs(mainPos.sub(enemyPos).length()) <= getCharacteristic().getVision()) { // Если в зоне видимости!
                Vector3f lagerPos = new Vector3f(getLagerPoint()); lagerPos.setY(0.0f);
                if(mainPos.sub(lagerPos).length() <= getCharacteristic().getVision()){ // если персонаж в зоне видимости лагеря
                    battleGrounds.setActive(true);
                    battleGrounds.setLocalPoint(getLagerPoint());
                }else{ // если персонаж вне зоне видимости своего лагеря
                    Vector3f direction = new Vector3f(character.getPosition().sub(getPosition())).normalize();
                    float distance = Math.abs(new Vector3f(character.getPosition().sub(getPosition())).length() / 2.0f);
                    Vector3f lagerPoint = new Vector3f(getPosition().add(direction.mul(distance)));
                    int x = Math.round(lagerPoint.getX());
                    int z = Math.round(lagerPoint.getZ());
                    lagerPoint = grid[x][z].getModifiedPosition();
                    battleGrounds.setActive(true);
                    battleGrounds.setLocalPoint(lagerPoint);
                }
                break;
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

    private float getXOffsetOnScreen(Vector3f objectPos){
        Vector3f position = new Vector3f(objectPos);
        Matrix4f perspective = new Matrix4f(Camera.getInstance().getPerspective().getProjection());
        Matrix4f view = new Matrix4f(Camera.getInstance().getPerspective().getViewMatrix());
        Matrix4f world = new Matrix4f(perspective.mul(view));
        Vector3f ndcPosition = new Vector3f(world.mulOnVector(position));
        ndcPosition = new Vector3f(ndcPosition.getX() / ndcPosition.getZ(), ndcPosition.getY() / ndcPosition.getZ(), 0.0f);
        return (ndcPosition.getX() + 1.0f) * Window.getInstance().getWidth() / 2.0f;
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