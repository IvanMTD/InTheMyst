package ru.phoenix.core.loader.model;

import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;

public class Vertex {
    // variable
    private Vector3f position;
    private Vector3f normal;
    private Vector2f texCoords;
    private Vector3f tangents;
    private Vector3f bitangnets;

    // constructor
    public Vertex(){
        position = new Vector3f();
        normal = new Vector3f();
        texCoords = new Vector2f();
        tangents = new Vector3f();
        bitangnets = new Vector3f();
    }

    // methods
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public Vector2f getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(Vector2f texCoord) {
        this.texCoords = texCoord;
    }

    public Vector3f getTangents() {
        return tangents;
    }

    public void setTangents(Vector3f tangents) {
        this.tangents = tangents;
    }

    public Vector3f getBitangnets() {
        return bitangnets;
    }

    public void setBitangnets(Vector3f bitangnets) {
        this.bitangnets = bitangnets;
    }
}
