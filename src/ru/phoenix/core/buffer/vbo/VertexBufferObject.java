package ru.phoenix.core.buffer.vbo;

import ru.phoenix.core.math.Matrix4f;

public interface VertexBufferObject {

    void allocate(float[] positions, float[] normals, float[] textureCoords, float[] tangents, float[] biTangents, int[] boneIds, float[] boneWeights, Matrix4f[] instances, int[] indices);
    void draw();
    void setNewPos(float[] pos);
    void setNewTex();
    void setNewTex(float[]tex);
    boolean isAnimated();
    boolean isTangentSpace();
    boolean isInstances();
    void delete();
}
