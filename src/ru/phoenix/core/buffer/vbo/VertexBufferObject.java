package ru.phoenix.core.buffer.vbo;

import ru.phoenix.core.math.Matrix4f;

public interface VertexBufferObject {

    public void allocate(float[] positions, float[] normals, float[] textureCoords, float[] tangents, float[] biTangents, int[] boneIds, float[] boneWeights, Matrix4f[] instances, int[] indices);
    public void draw();
    public void setNewPos(float[] pos);
    public void setNewTex();
    public boolean isAnimated();
    public boolean isTangentSpace();
    public boolean isInstances();
    public void delete();
}
