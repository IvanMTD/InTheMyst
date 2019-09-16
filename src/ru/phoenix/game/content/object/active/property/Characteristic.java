package ru.phoenix.game.content.object.active.property;

public class Characteristic {
    private int initiative;
    private int speed;
    private int totalActionPoint;
    private int curentActionPoint;
    private int jump;

    public Characteristic(){
        initiative = 0;
        speed = 15;
        totalActionPoint = 6;
        curentActionPoint = 6;
        jump = 1;
    }

    public Characteristic(Characteristic characteristic){
        initiative = characteristic.getInitiative();
        speed = characteristic.getSpeed();
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }
}