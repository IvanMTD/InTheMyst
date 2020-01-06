package ru.phoenix.game.content.characters.humans;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.logic.movement.PathSearchAlgorithm;
import ru.phoenix.game.property.Characteristic;
import ru.phoenix.game.hud.assembled.SelfIndicators;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.movement.MotionAnimation;
import ru.phoenix.game.logic.movement.PathfindingAlgorithm;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.*;

public abstract class HumanControl {
    // характеристики
    private Characteristic characteristic;
    // управление анимацией персонажа
    private MotionAnimation motionAnimation;
    private List<Cell> wayPoints;
    // управление положением персонажа
    private PathSearchAlgorithm pathSearchAlgorithm;
    private Vector3f position;
    private int look;
    private Vector3f lagerPoint;
    private boolean turn;
    private float tempX;
    private float tempZ;
    // индификаторы персонажа
    private SelfIndicators selfIndicators;
    private boolean showIndicators;
    private float id;
    private int group;
    private int recognition;
    // контроль состояний персонажа
    private boolean target;
    private boolean marker;
    private boolean selected;
    private boolean battle;
    private boolean jump;
    private boolean takeDamadge;
    private boolean dead;

    // конструкторы класса - начало
    HumanControl(){
        // характеристики
        characteristic = new Characteristic();
        // управление анимацией персонажа
        motionAnimation = new MotionAnimation();
        wayPoints = new ArrayList<>();
        // управление положением персонажа
        setPosition(new Vector3f());
        setLook(EAST);
        setLagerPoint(new Vector3f());
        setTurn(false);
        tempX = 0.0f;
        tempZ = 0.0f;
        // индификаторы персонажа
        selfIndicators = new SelfIndicators(1.2f,new Vector3f(0.0f,1.7f,0.0f));
        setShowIndicators(false);
        setId(0.0f);
        setGroup(GROUP_A);
        setRecognition(NEUTRAL);
        // контроль состояний персонажа
        setTarget(false);
        setMarker(false);
        setSelected(false);
        setBattle(false);
        setJump(false);
        setTakeDamadge(false);
        setDead(false);
    }

    HumanControl(Character character){
        // характеристики
        setCharacteristic(character.getCharacteristic());
        // управление анимацией персонажа
        motionAnimation = new MotionAnimation();
        wayPoints = new ArrayList<>();
        // управление положением персонажа
        setPosition(character.getPosition());
        setLook(character.getLook());
        setLagerPoint(character.getLagerPoint());
        setTurn(character.isTurn());
        tempX = 0.0f;
        tempZ = 0.0f;
        // индификаторы персонажа
        selfIndicators = new SelfIndicators(1.2f,new Vector3f(0.0f,1.7f,0.0f));
        setShowIndicators(false);
        setId(character.getId());
        setGroup(character.getGroup());
        setRecognition(character.getRecognition());
        // контроль состояний персонажа
        setTarget(character.isTarget());
        setMarker(character.isMarker());
        setSelected(character.isSelected());
        setBattle(character.isBattle());
        setJump(character.isJump());
        setTakeDamadge(false);
        setDead(character.isDead());
    }
    // конструкторы класса - конец

    // методы контроля - начало
    void setTurn(){
        Vector3f lookPoint = new Vector3f();
        switch (getLook()){
            case NORTH:
                lookPoint = new Vector3f(getPosition()).add(new Vector3f(0.0f,0.0f,1.0f));
                break;
            case WEST:
                lookPoint = new Vector3f(getPosition()).add(new Vector3f(1.0f,0.0f,0.0f));
                break;
            case SOUTH:
                lookPoint = new Vector3f(getPosition()).add(new Vector3f(0.0f,0.0f,-1.0f));
                break;
            case EAST:
                lookPoint = new Vector3f(getPosition()).add(new Vector3f(-1.0f,0.0f,0.0f));
                break;
            default:
                System.out.println("Соотношение не найденно, поворот отменен!");
                break;
        }

        float pos = getXOffsetOnScreen(getPosition());
        float look = getXOffsetOnScreen(lookPoint);
        if(pos < look){
            setTurn(false);
        }else{
            setTurn(true);
        }
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
    // методы контроля - конец

    // методы сетеры и гетеры - начало
    // характеристики
    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = new Characteristic(characteristic);
    }

    // управление анимацией персонажа
    protected MotionAnimation getMotionAnimation() {
        return motionAnimation;
    }

    protected List<Cell> getWayPoints() {
        return wayPoints;
    }

    // управление положением персонажа
    protected PathSearchAlgorithm getPathfindingAlgorithm() {
        return pathSearchAlgorithm;
    }

    protected void runPathfindingAlgorithm(){
        this.pathSearchAlgorithm = new PathSearchAlgorithm();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public Vector3f getLagerPoint() {
        return lagerPoint;
    }

    public void setLagerPoint(Vector3f lagerPoint) {
        this.lagerPoint = lagerPoint;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public Vector3f getPositionTemplate(){
        return new Vector3f(tempX,0.0f,tempZ);
    }

    // индификаторы персонажа
    public SelfIndicators getSelfIndicators() {
        return selfIndicators;
    }

    public void setSelfIndicators(SelfIndicators selfIndicators) {
        this.selfIndicators = selfIndicators;
    }

    public boolean isShowIndicators() {
        return showIndicators;
    }

    public void setShowIndicators(boolean showIndicators) {
        this.showIndicators = showIndicators;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getRecognition() {
        return recognition;
    }

    public void setRecognition(int recognition) {
        this.recognition = recognition;
    }

    // контроль состояний персонажа
    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public boolean isMarker() {
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isBattle() {
        return battle;
    }

    public void setBattle(boolean battle) {
        this.battle = battle;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isTakeDamadge() {
        return takeDamadge;
    }

    public void setTakeDamadge(boolean takeDamadge) {
        this.takeDamadge = takeDamadge;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    // методы сетеры и гетеры - конец

    // вспомогательные
    public int getPriority(Cell[][] grid, Character character){
        Cell enemyPoint = grid[(int)character.getPosition().getX()][(int)character.getPosition().getZ()];
        Vector3f mainPos = new Vector3f(getPosition()); mainPos.setY(0.0f);
        Vector3f studyPos = new Vector3f(character.getPosition());studyPos.setY(0.0f);
        // дистанция
        float d = Math.abs(new Vector3f(mainPos.sub(studyPos)).length());
        // враг
        int ea = getCharacteristic().getPhysicalPower();
        int eh = getCharacteristic().getHealth();
        // я
        int ia = character.getCharacteristic().getPhysicalPower();
        int ih = character.getCharacteristic().getHealth();

        int multiplier = 1;
        if(enemyPoint.isBlueZona()){
            multiplier = 1;
        }else if(enemyPoint.isGoldZona()){
            multiplier = 3;
        }else{
            multiplier = 5;
        }

        return (ih - ea) - (eh - ia) - Math.round(d * multiplier);
    }
}
