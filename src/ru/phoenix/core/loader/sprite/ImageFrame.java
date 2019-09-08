package ru.phoenix.core.loader.sprite;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.util.SortArray;

import java.util.ArrayList;
import java.util.Comparator;
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

    public void updateInstanceMatrix(){
        List<SortArray> sortingData = new ArrayList<>();
        if(mat != null){
            for(int i=0; i<mat.length; i++){
                float distance = new Vector3f(Camera.getInstance().getPos().sub(mat[i].getPositionVector())).length();
                Matrix4f matrix = mat[i];
                sortingData.add(new SortArray(distance,matrix));
            }

            sortingData.sort(new Comparator<SortArray>() {
                @Override
                public int compare(SortArray o1, SortArray o2) {
                    return o1.getCondition() < o2.getCondition() ? 1 : -1;
                }
            });

            Matrix4f[] matrix = new Matrix4f[sortingData.size()];
            int i=0;
            for(SortArray sortData : sortingData){
                matrix[i] = sortData.getMatrix();
                i++;
            }

            vbo.allocate(pos,null,tex,null,null,null,null,matrix,ind);
        }
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
