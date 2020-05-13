package ru.phoenix.core.buffer.vbo;

import ru.phoenix.core.math.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class NormalizedDeviceCoordinates implements VertexBufferObject {
    private int vao;
    private List<Integer> vbo;
    private int ibo;
    private int size;

    public NormalizedDeviceCoordinates(){
        vao = glGenVertexArrays();
        vbo = new ArrayList<>();
        for(int i=0; i<2; i++){
            vbo.add(glGenBuffers());
        }
        ibo = glGenBuffers();
        size = 0;
    }

    @Override
    public void allocate(float[] positions, float[] normals, float[] textureCoords, float[] tangents, float[] biTangents, int[] boneIds, float[] boneWeights, Matrix4f[] instances, int[] indices){
        size = indices.length;
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
        glBufferData(GL_ARRAY_BUFFER, textureCoords, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindVertexArray(0);
    }

    @Override
    public void draw() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    @Override
    public void setNewPos(float[] pos) {

    }

    @Override
    public void setNewInstances(Matrix4f[] instances) {

    }

    @Override
    public void setNewTex() {

    }

    @Override
    public void setNewTex(float[] tex) {

    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public boolean isTangentSpace() {
        return false;
    }

    @Override
    public boolean isInstances() {
        return false;
    }

    @Override
    public void delete() {
        glBindVertexArray(vao);
        for (Integer temp : vbo) {
            glDeleteBuffers(temp);
        }
        glDeleteBuffers(ibo);
        glDeleteVertexArrays(vao);
        glBindVertexArray(0);
    }
}
