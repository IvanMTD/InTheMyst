package ru.phoenix.core.util;

public class CollectorStruct {
    private float[] position;
    private float[] textureCoord;
    private int[] indices;

    public CollectorStruct(float[] position, float[] textureCoord, int[] indices) {
        this.position = position;
        this.textureCoord = textureCoord;
        this.indices = indices;
    }

    public float[] getPosition() {
        return position;
    }

    public float[] getTextureCoord() {
        return textureCoord;
    }

    public int[] getIndices() {
        return indices;
    }
}
