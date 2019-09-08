package ru.phoenix.core.util;

import ru.phoenix.core.math.Matrix4f;

public class SortArray {
    public float condition;
    public Matrix4f matrix;

    public SortArray(float condition, Matrix4f matrix) {
        this.condition = condition;
        this.matrix = matrix;
    }

    public float getCondition() {
        return condition;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }
}
