package ru.phoenix.game.content.object.active.property;

public class Characteristic {

    private int initiative;
    private int initiativeCharge;
    private int totalActionPoint;
    private int curentActionPoint;
    private int move;
    private int staminaTotal;
    private int stamina;
    private int staminaCharge;
    private float jump;
    private float speed;

    public Characteristic(){
        setInitiative(0);
        setInitiativeCharge(15);
        setTotalActionPoint(2);
        setCurentActionPoint(2);
        setMove(6);
        setStaminaTotal(100);
        setStamina(100);
        setStaminaCharge(10);
        setJump(1.0f);
        setSpeed(0.012f);
    }

    public Characteristic(Characteristic characteristic) {
        setInitiative(characteristic.getInitiative());
        setInitiativeCharge(characteristic.getInitiativeCharge());
        setTotalActionPoint(characteristic.getTotalActionPoint());
        setCurentActionPoint(characteristic.getCurentActionPoint());
        setMove(characteristic.getMove());
        setStaminaTotal(characteristic.getStaminaTotal());
        setStamina(characteristic.getStamina());
        setStaminaCharge(characteristic.getStaminaCharge());
        setJump(characteristic.getJump());
        setSpeed(characteristic.getSpeed());
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getInitiativeCharge() {
        return initiativeCharge;
    }

    public void setInitiativeCharge(int initiativeCharge) {
        this.initiativeCharge = initiativeCharge;
    }

    public int getTotalActionPoint() {
        return totalActionPoint;
    }

    public void setTotalActionPoint(int totalActionPoint) {
        this.totalActionPoint = totalActionPoint;
    }

    public int getCurentActionPoint() {
        return curentActionPoint;
    }

    public void setCurentActionPoint(int curentActionPoint) {
        this.curentActionPoint = curentActionPoint;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getStaminaTotal() {
        return staminaTotal;
    }

    public void setStaminaTotal(int staminaTotal) {
        this.staminaTotal = staminaTotal;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getStaminaCharge() {
        return staminaCharge;
    }

    public void setStaminaCharge(int staminaCharge) {
        this.staminaCharge = staminaCharge;
    }

    public float getJump() {
        return jump;
    }

    public void setJump(float jump) {
        this.jump = jump;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}