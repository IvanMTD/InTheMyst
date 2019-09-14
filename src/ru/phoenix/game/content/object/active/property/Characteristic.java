package ru.phoenix.game.content.object.active.property;

public class Characteristic {
    private int initiative;
    private int speed;
    private int actionPoint;
    private int jump;

    public Characteristic(){
        initiative = 0;
        speed = 15;
        actionPoint = 6;
        jump = 1;
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

    public int getActionPoint() {
        return actionPoint;
    }

    public void setActionPoint(int actionPoint) {
        this.actionPoint = actionPoint;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }
}
