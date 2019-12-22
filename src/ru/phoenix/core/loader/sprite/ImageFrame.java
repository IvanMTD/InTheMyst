package ru.phoenix.core.loader.sprite;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.util.SortArray;

import java.util.ArrayList;
import java.util.List;

public class ImageFrame {
    private VertexBufferObject vbo;

    private int row;
    private int column;

    private float[] pos;
    private float[] tex;
    private int[] ind;
    private Matrix4f[] mat;

    public ImageFrame(int row, int column){
        vbo = new MeshConfig();
        this.row = row;
        this.column = column;
    }

    public void init(float[] positions, float[] textureCoords, int[] indices){
        pos = positions;
        tex = textureCoords;
        ind = indices;
        vbo.allocate(positions,null,textureCoords,null,null,null,null,null,indices);
    }

    public void init(float[] positions, float[] textureCoords, int[] indices, Matrix4f[] matrix){
        pos = positions;
        tex = textureCoords;
        ind = indices;
        mat = matrix;
        vbo.allocate(positions,null,textureCoords,null,null,null,null,matrix,indices);
    }

    public void draw(){
        vbo.draw();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public VertexBufferObject getVbo(){
        return vbo;
    }
}
