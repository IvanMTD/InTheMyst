package ru.phoenix.core.util;

import ru.phoenix.core.math.Matrix4f;

public class SortArray {
    private float condition;
    private Matrix4f matrix;
    private int index;
    private float value;

    public SortArray(float condition, Matrix4f matrix) {
        this.condition = condition;
        this.matrix = matrix;
    }

    public SortArray(int index, float value){
        this.index = index;
        this.value = value;
    }

    public float getCondition() {
        return condition;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }

    public int getIndex() {
        return index;
    }

    public float getValue() {
        return value;
    }
}
