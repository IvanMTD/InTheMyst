package ru.phoenix.core.loader.model;

import ru.phoenix.core.math.Matrix4f;

public class Bone {
    private final int id;
    private final String name;
    private Matrix4f offsetMatrix;

    public Bone(int id, String name, Matrix4f offsetMatrix) {
        this.id = id;
        this.name = name;
        this.offsetMatrix = offsetMatrix;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Matrix4f getOffsetMatrix() {
        return offsetMatrix;
    }
}
