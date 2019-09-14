package ru.phoenix.game.content.object.active.property;

public class Characteristic {
    private int initiative;
    private int speed;

    public Characteristic(){
        initiative = 0;
        speed = 15;
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
}
