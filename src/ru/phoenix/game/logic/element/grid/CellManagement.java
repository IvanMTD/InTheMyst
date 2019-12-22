package ru.phoenix.game.logic.element.grid;

import ru.phoenix.core.math.Vector3f;

abstract class CellManagement {
    // контроль
    private float currentHeight;
    private Vector3f position;
    private float id;
    private Cell parent;
    private int cellCost;
    private int step;
    private int cost;
    private float distance;
    private float bevelAngle;
    // conditions - состояния
    // -- состояния отрисовки --
    private boolean target;
    private boolean visible;
    private boolean wayPoint;
    private boolean cursor;
    private boolean blueZona;
    private boolean goldZona;
    // -- состояние клетки --
    private boolean bevel;
    private boolean grass;
    private boolean blocked;
    private boolean water;
    private boolean battleGraund;
    private boolean exitBattleGraund;
    // -- вспомогательные --
    private boolean occupied;
    private boolean skip;

    // дефолтный конструктор
    CellManagement(){
        // контроль
        setCurrentHeight(0.0f);
        setPosition(new Vector3f());
        setId(0.0f);
        setParent(null);
        setCellCost(1);
        setStep(0);
        setCost(0);
        setDistance(0.0f);
        setBevelAngle(0.0f);
        // отрисовка
        setTarget(false);
        setVisible(false);
        setWayPoint(false);
        setCursor(false);
        setBlueZona(false);
        setGoldZona(false);
        // состояния
        setBevel(false);
        setGrass(false);
        setBlocked(false);
        setWater(false);
        setBattleGraund(false);
        setExitBattleGraund(false);
        // вспомогательные
        setOccupied(false);
        setSkip(false);
    }

    CellManagement(Cell cell){
        // контроль
        setCurrentHeight(cell.getCurrentHeight());
        setPosition(cell.getPosition());
        setId(cell.getId());
        setParent(cell.getParent());
        setCellCost(cell.getCellCost());
        setStep(cell.getStep());
        setCost(cell.getCost());
        setDistance(cell.getDistance());
        setBevelAngle(cell.getBevelAngle());
        // отрисовка
        setTarget(cell.isTarget());
        setVisible(cell.isVisible());
        setWayPoint(cell.isWayPoint());
        setCursor(cell.isCursor());
        setBlueZona(cell.isBlueZona());
        setGoldZona(cell.isGoldZona());
        // состояния
        setBevel(cell.isBevel());
        setGrass(cell.isGrass());
        setBlocked(cell.isBlocked());
        setWater(cell.isWater());
        setBattleGraund(cell.isBattleGraund());
        setExitBattleGraund(cell.isExitBattleGraund());
        // вспомогательные
        setOccupied(cell.isOccupied());
        setSkip(cell.isSkip());
    }

    // методы быстрого контроля
    // начало
    public void disable(){
        setTarget(false);
        setVisible(false);
        setWayPoint(false);
        setCursor(false);
        setBlueZona(false);
        setGoldZona(false);
    }
    // конец

    // методы управления контролем клетки
    // начало
    public float getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

    public Vector3f getModifiedPosition(){
        return new Vector3f(getPosition().getX(),getCurrentHeight(),getPosition().getZ());
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public int getCellCost() {
        return cellCost;
    }

    public void setCellCost(int cellCost) {
        this.cellCost = cellCost;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getBevelAngle() {
        return bevelAngle;
    }

    public void setBevelAngle(float bevelAngle) {
        this.bevelAngle = bevelAngle;
    }

    public int getEstimateFullPathLength(){
        return getStep() + getCost();
    }

    public int getTravelCost(){
        return isWater() ? getCellCost() * 3 : getCellCost();
    }
    // конец

    // методы установки и получения состояний
    // начало\
    // -- отрисовка --
    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isWayPoint() {
        return wayPoint;
    }

    public void setWayPoint(boolean wayPoint) {
        this.wayPoint = wayPoint;
    }

    public boolean isCursor() {
        return cursor;
    }

    public void setCursor(boolean cursor) {
        this.cursor = cursor;
    }

    public boolean isBlueZona() {
        return blueZona;
    }

    public void setBlueZona(boolean blueZona) {
        this.blueZona = blueZona;
    }

    public boolean isGoldZona() {
        return goldZona;
    }

    public void setGoldZona(boolean goldZona) {
        this.goldZona = goldZona;
    }

    // --состояния --
    public boolean isBevel() {
        return bevel;
    }

    public void setBevel(boolean bevel) {
        this.bevel = bevel;
    }

    public boolean isGrass() {
        return grass;
    }

    public void setGrass(boolean grass) {
        this.grass = grass;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isWater() {
        return water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isBattleGraund() {
        return battleGraund;
    }

    public void setBattleGraund(boolean battleGraund) {
        this.battleGraund = battleGraund;
    }

    public boolean isExitBattleGraund() {
        return exitBattleGraund;
    }

    public void setExitBattleGraund(boolean exitBattleGraund) {
        this.exitBattleGraund = exitBattleGraund;
    }

    // --вспомогательные--
    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
    // конец
}
