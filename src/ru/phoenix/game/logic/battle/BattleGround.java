package ru.phoenix.game.logic.battle;

import ru.phoenix.core.math.Vector3f;

public class BattleGround {
    private final int RADIUS = 20;
    private Vector3f localPoint;
    private boolean active;

    private float minW;
    private float maxW;
    private float minH;
    private float maxH;

    public BattleGround() {
        setActive(false);
        localPoint = new Vector3f();
    }

    public Vector3f getLocalPoint() {
        return localPoint;
    }

    public void setLocalPoint(Vector3f localPoint) {
        this.localPoint = localPoint;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getMinW() {
        return minW;
    }

    public void setMinW(float minW) {
        this.minW = minW;
    }

    public float getMaxW() {
        return maxW;
    }

    public void setMaxW(float maxW) {
        this.maxW = maxW;
    }

    public float getMinH() {
        return minH;
    }

    public void setMinH(float minH) {
        this.minH = minH;
    }

    public float getMaxH() {
        return maxH;
    }

    public void setMaxH(float maxH) {
        this.maxH = maxH;
    }

    public int getRADIUS() {
        return RADIUS;
    }
}
