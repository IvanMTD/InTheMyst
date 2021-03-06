package ru.phoenix.game.hud.elements;

import ru.phoenix.core.math.Vector3f;

import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class HudControl {
    // сложные переменные
    private Vector3f position;
    // простые переменные
    private float size;
    private float id;
    private int group;

    // булиановские переменные
    private boolean discard;
    private boolean target;
    private boolean fakeTarget;
    private boolean hide;

    HudControl(){
        position = new Vector3f();

        size = 1.0f;
        id = -1.0f;
        group = GROUP_A;

        discard = false;
        target = false;
        fakeTarget = false;
        hide = false;
    }

    // Сложные переменные
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    // Простые переменные
    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getId() {
        return id;
    }

    protected void setId(float id) {
        this.id = id;
    }

    protected int getGroup() {
        return group;
    }

    protected void setGroup(int group) {
        this.group = group;
    }

    // Переменные контроля
    boolean isDiscard() {
        return discard;
    }

    protected void setDiscard(boolean discard) {
        this.discard = discard;
    }

    public boolean isTarget() {
        return target;
    }

    protected void setTarget(boolean target) {
        this.target = target;
    }

    protected boolean isFakeTarget() {
        return fakeTarget;
    }

    protected void setFakeTarget(boolean fakeTarget) {
        this.fakeTarget = fakeTarget;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}
