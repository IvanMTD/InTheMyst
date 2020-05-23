package ru.phoenix.game.datafile;

import ru.phoenix.core.math.Vector3f;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class StructData implements Externalizable {

    private static final long serialVersionUID = 1L;

    private float currentHeight;
    private float currentOriginalHeight;
    private Vector3f position;
    private boolean road;

    public StructData(){
        currentHeight = 0.0f;
        currentOriginalHeight = 0.0f;
        position = new Vector3f();
        road = false;
    }

    public StructData(float currentHeight, float currentOriginalHeight, Vector3f position, boolean road) {
        this.currentHeight = currentHeight;
        this.currentOriginalHeight = currentOriginalHeight;
        this.position = position;
        this.road = road;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

    public float getCurrentOriginalHeight() {
        return currentOriginalHeight;
    }

    public void setCurrentOriginalHeight(float currentOriginalHeight) {
        this.currentOriginalHeight = currentOriginalHeight;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public boolean isRoad() {
        return road;
    }

    public void setRoad(boolean road) {
        this.road = road;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(currentHeight);
        out.writeObject(currentOriginalHeight);
        position.writeExternal(out);
        out.writeObject(road);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setCurrentHeight((float)in.readObject());
        setCurrentOriginalHeight((float)in.readObject());
        position.readExternal(in);
        setRoad((boolean)in.readObject());
    }
}
