package ru.phoenix.game.content.object.active.property;

public class Characteristic {
    private int initiative;
    private int initiativeCharge;
    private int totalActionPoint;
    private int curentActionPoint;
    private float jump;
    private float speed;

    public Characteristic(){
        initiative = 0;
        initiativeCharge = 15;
        totalActionPoint = 8;
        curentActionPoint = 8;
        jump = 0.5f;
        speed = 0.02f;
    }

    public Characteristic(Characteristic characteristic){
        initiative = characteristic.getInitiative();
        initiativeCharge = characteristic.getInitiativeCharge();
        totalActionPoint = characteristic.getTotalActionPoint();
        curentActionPoint = characteristic.getCurentActionPoint();
        jump = characteristic.getJump();
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

    public void setInitiativeCharge(int speed) {
        this.initiativeCharge = speed;
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

    public float getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}