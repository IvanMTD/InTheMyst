package ru.phoenix.core.loader.text;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

public class Symbol {
    private VertexBufferObject vbo;
    private char description;

    private Projection projection;
    private Vector3f position;
    private Vector3f color;
    private float size;

    private boolean empty;

    Symbol(float[] pos, float[] tex, int[] indices, char description){
        this.description = description;
        vbo = new MeshConfig();
        vbo.allocate(pos,null,tex,null,null,null,null,null,indices);
        projection = new Projection();
        position = new Vector3f();
        color = new Vector3f();
        size = 1.0f;
        empty = false;
    }

    Symbol(Symbol symbol){
        this.vbo = symbol.getVbo();
        this.description = symbol.getDescription();
        this.projection = symbol.getProjection();
        this.position = symbol.getPosition();
        this.color = symbol.getColor();
        this.size = symbol.getSize();
        this.empty = symbol.isEmpty();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setSize(float size){
        this.size = size;
    }

    public char getDescription() {
        return description;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public VertexBufferObject getVbo() {
        return vbo;
    }

    public Projection getProjection() {
        return projection;
    }

    public float getSize() {
        return size;
    }

    public void draw(Shader shader){
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
        projection.setScaling(size);
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("empty",isEmpty() ? 1 : 0);
        shader.setUniform("color",color);
        vbo.draw();
    }
}
