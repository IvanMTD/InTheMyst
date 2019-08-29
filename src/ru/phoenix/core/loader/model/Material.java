package ru.phoenix.core.loader.model;

import ru.phoenix.core.math.Vector3f;

public class Material {
    private int identifier;
    // variable
    private String type;
    private int id;
    private String path;
    private Vector3f material;

    // methods
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Vector3f getMaterial() {
        return material;
    }

    public void setMaterial(Vector3f material) {
        this.material = material;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
