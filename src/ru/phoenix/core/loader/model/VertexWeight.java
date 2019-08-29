package ru.phoenix.core.loader.model;

public class VertexWeight {

    private int boneID;
    private int vertexID;
    private float weight;

    public VertexWeight(int boneID, int vertexID, float weight) {
        this.boneID = boneID;
        this.vertexID = vertexID;
        this.weight = weight;
    }

    public int getBoneID() {
        return boneID;
    }

    public void setBoneID(int boneID) {
        this.boneID = boneID;
    }

    public int getVertexID() {
        return vertexID;
    }

    public void setVertexID(int vertexID) {
        this.vertexID = vertexID;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
