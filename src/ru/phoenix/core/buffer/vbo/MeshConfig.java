package ru.phoenix.core.buffer.vbo;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.util.BufferUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class MeshConfig implements VertexBufferObject {

    private int vao;
    private List<Integer> vbo;
    private int ibo;

    private int sizeIndices;
    private int sizeInstance;
    private int size;

    private boolean skybox;
    private boolean animated;
    private boolean tangent;
    private boolean instances;

    private float[]tex;
    private float tic;
    private float tic2;

    private int drawInfo;
    private int drawMethod;

    public MeshConfig(){
        vao = glGenVertexArrays();
        vbo = new ArrayList<>();
        for(int i=0; i<10; i++){
            vbo.add(glGenBuffers());
        }
        ibo = glGenBuffers();

        sizeIndices = 0;
        sizeInstance = 0;

        animated = false;
        tangent = false;
        instances = false;

        tic = 0.0f;
        tic2 = 0.0f;
        drawInfo = GL_STATIC_DRAW;
        drawMethod = GL_TRIANGLES;
        skybox = false;
    }

    public MeshConfig(int drawInfo, int drawMethod){
        vao = glGenVertexArrays();
        vbo = new ArrayList<>();
        for(int i=0; i<10; i++){
            vbo.add(glGenBuffers());
        }
        ibo = glGenBuffers();

        sizeIndices = 0;
        sizeInstance = 0;

        animated = false;
        tangent = false;
        instances = false;

        tic = 0.0f;
        tic2 = 0.0f;
        this.drawInfo = drawInfo;
        this.drawMethod = drawMethod;
        skybox = false;
    }

    @Override
    public void allocate(float[] positions, float[] normals, float[] textureCoords, float[] tangents, float[] biTangents, int[] boneIds, float[] boneWeights, Matrix4f[] instances, int[] indices){
        if(indices != null) {
            this.tex = textureCoords;

            int v4size = (int) Math.pow(Float.BYTES, 2);

            setBufferControl(tangents, biTangents, boneIds, boneWeights, instances);

            sizeIndices = indices.length;
            sizeInstance = instances != null ? instances.length : 0;

            if (animated) {
                glBindVertexArray(vao);
                // Записываем все позиции в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
                if (positions != null) {
                    glBufferData(GL_ARRAY_BUFFER, positions, drawInfo);
                }
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                // Записываем все нормали в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
                if (normals != null) {
                    glBufferData(GL_ARRAY_BUFFER, normals, drawInfo);
                }
                glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
                // Записываем все текстуры в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(2));
                if (textureCoords != null) {
                    glBufferData(GL_ARRAY_BUFFER, textureCoords, drawInfo);
                }
                glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                // Записываем все тангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(3));
                if (tangents != null) {
                    glBufferData(GL_ARRAY_BUFFER, tangents, drawInfo);
                }
                glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
                // Записываем все битангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(4));
                if (biTangents != null) {
                    glBufferData(GL_ARRAY_BUFFER, biTangents, drawInfo);
                }
                glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);
                // Записываем все айди костей
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(5));
                if (boneIds != null) {
                    glBufferData(GL_ARRAY_BUFFER, boneIds, drawInfo);
                }
                glVertexAttribPointer(5, 4, GL_FLOAT, false, 0, 0);
                // Записываем все веса костей
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(6));
                if (boneWeights != null) {
                    glBufferData(GL_ARRAY_BUFFER, boneWeights, drawInfo);
                }
                glVertexAttribPointer(6, 4, GL_FLOAT, false, 0, 0);

                if (instances != null) {
                    glBindBuffer(GL_ARRAY_BUFFER, vbo.get(7));
                    glBufferData(GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(instances), drawInfo);
                    glVertexAttribPointer(7, 4, GL_FLOAT, false, Float.BYTES * v4size, 0);
                    glVertexAttribPointer(8, 4, GL_FLOAT, false, Float.BYTES * v4size, v4size);
                    glVertexAttribPointer(9, 4, GL_FLOAT, false, Float.BYTES * v4size, 2 * v4size);
                    glVertexAttribPointer(10, 4, GL_FLOAT, false, Float.BYTES * v4size, 3 * v4size);
                    glBindBuffer(GL_ARRAY_BUFFER, 0);
                    glVertexAttribDivisor(7, 1);
                    glVertexAttribDivisor(8, 1);
                    glVertexAttribDivisor(9, 1);
                    glVertexAttribDivisor(10, 1);
                }

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, drawInfo);
                glBindVertexArray(0);
            } else {
                glBindVertexArray(vao);
                // Записываем все позиции в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
                if (positions != null) {
                    glBufferData(GL_ARRAY_BUFFER, positions, drawInfo);
                }
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
                // Записываем все нормали в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
                if (normals != null) {
                    glBufferData(GL_ARRAY_BUFFER, normals, drawInfo);
                }
                glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
                // Записываем все текстуры в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(2));
                if (textureCoords != null) {
                    glBufferData(GL_ARRAY_BUFFER, textureCoords, drawInfo);
                }
                glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
                // Записываем все тангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(3));
                if (tangents != null) {
                    glBufferData(GL_ARRAY_BUFFER, tangents, drawInfo);
                }
                glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
                // Записываем все битангенсы в буфер
                glBindBuffer(GL_ARRAY_BUFFER, vbo.get(4));
                if (biTangents != null) {
                    glBufferData(GL_ARRAY_BUFFER, biTangents, drawInfo);
                }
                glVertexAttribPointer(4, 3, GL_FLOAT, false, 0, 0);

                if (instances != null) {
                    glBindBuffer(GL_ARRAY_BUFFER, vbo.get(7));
                    glBufferData(GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(instances), drawInfo);
                    glVertexAttribPointer(7, 4, GL_FLOAT, false, Float.BYTES * v4size, 0);
                    glVertexAttribPointer(8, 4, GL_FLOAT, false, Float.BYTES * v4size, v4size);
                    glVertexAttribPointer(9, 4, GL_FLOAT, false, Float.BYTES * v4size, 2 * v4size);
                    glVertexAttribPointer(10, 4, GL_FLOAT, false, Float.BYTES * v4size, 3 * v4size);
                    glBindBuffer(GL_ARRAY_BUFFER, 0);
                    glVertexAttribDivisor(7, 1);
                    glVertexAttribDivisor(8, 1);
                    glVertexAttribDivisor(9, 1);
                    glVertexAttribDivisor(10, 1);
                }

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, drawInfo);
                glBindVertexArray(0);
            }
        }else{
            skybox = true;
            size = positions.length / 3;
            glBindVertexArray(vao);

            glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
            glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);

            glVertexAttribPointer(0,3,GL_FLOAT,false,Float.BYTES * 3,0);
            glEnableVertexAttribArray(0);
        }
    }

    @Override
    public void draw(){
        if(skybox){
            glBindVertexArray(vao);
            glDrawArrays(GL_TRIANGLES, 0, size);
            glBindVertexArray(0);
        }else {
            if (instances) {
                if (animated) {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glEnableVertexAttribArray(5);
                    glEnableVertexAttribArray(6);
                    glEnableVertexAttribArray(7);
                    glEnableVertexAttribArray(8);
                    glEnableVertexAttribArray(9);
                    glEnableVertexAttribArray(10);
                    glDrawElementsInstanced(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0, sizeInstance);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glDisableVertexAttribArray(5);
                    glDisableVertexAttribArray(6);
                    glDisableVertexAttribArray(7);
                    glDisableVertexAttribArray(8);
                    glDisableVertexAttribArray(9);
                    glDisableVertexAttribArray(10);
                    glBindVertexArray(0);
                } else {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glEnableVertexAttribArray(7);
                    glEnableVertexAttribArray(8);
                    glEnableVertexAttribArray(9);
                    glEnableVertexAttribArray(10);
                    glDrawElementsInstanced(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0, sizeInstance);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glDisableVertexAttribArray(7);
                    glDisableVertexAttribArray(8);
                    glDisableVertexAttribArray(9);
                    glDisableVertexAttribArray(10);
                    glBindVertexArray(0);
                }
            } else {
                if (animated) {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glEnableVertexAttribArray(5);
                    glEnableVertexAttribArray(6);
                    glDrawElements(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glDisableVertexAttribArray(5);
                    glDisableVertexAttribArray(6);
                    glBindVertexArray(0);
                } else {
                    glBindVertexArray(vao);
                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);
                    glEnableVertexAttribArray(2);
                    glEnableVertexAttribArray(3);
                    glEnableVertexAttribArray(4);
                    glDrawElements(drawMethod, sizeIndices, GL_UNSIGNED_INT, 0);
                    glDisableVertexAttribArray(0);
                    glDisableVertexAttribArray(1);
                    glDisableVertexAttribArray(2);
                    glDisableVertexAttribArray(3);
                    glDisableVertexAttribArray(4);
                    glBindVertexArray(0);
                }
            }
        }
    }

    private void setBufferControl(float[] tangents, float[] biTangents, int[] boneIds, float[] boneWeights, Matrix4f[] instances){
        if(tangents != null && biTangents != null){
            this.tangent = true;
        }
        if(boneIds != null && boneWeights != null){
            this.animated = true;
        }
        if(instances != null){
            this.instances = true;
        }
    }

    @Override
    public void setNewPos(float[] pos){
        glBindVertexArray(vao);
        // Записываем все позиции в буфер
        glDeleteBuffers(vbo.get(0));
        vbo.set(0,glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        if(pos != null) {
            glBufferData(GL_ARRAY_BUFFER, pos, drawInfo);
        }
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    @Override
    public void setNewInstances(Matrix4f[] instances){
        int v4size = (int) Math.pow(Float.BYTES, 2);
        glBindVertexArray(vao);
        // Записываем все позиции в буфер
        glDeleteBuffers(vbo.get(7));
        vbo.set(7,glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(7));
        if(instances != null) {
            glBufferData(GL_ARRAY_BUFFER, BufferUtil.createFlippedBuffer(instances), drawInfo);
        }
        glVertexAttribPointer(7, 4, GL_FLOAT, false, Float.BYTES * v4size, 0);
        glVertexAttribPointer(8, 4, GL_FLOAT, false, Float.BYTES * v4size, v4size);
        glVertexAttribPointer(9, 4, GL_FLOAT, false, Float.BYTES * v4size, 2 * v4size);
        glVertexAttribPointer(10, 4, GL_FLOAT, false, Float.BYTES * v4size, 3 * v4size);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glVertexAttribDivisor(7, 1);
        glVertexAttribDivisor(8, 1);
        glVertexAttribDivisor(9, 1);
        glVertexAttribDivisor(10, 1);
    }

    @Override
    public void setNewTex(){

        tic += 0.005f;
        tic2 += 0.001f;
        float[] temp = new float[this.tex.length];
        System.arraycopy(this.tex, 0, temp, 0, temp.length);

        if(tic > 1.0f){
            tic = 0.0f;
        }

        if(tic2 > 1.0f){
            tic2 = 0.0f;
        }

        for(int i=1; i<this.tex.length; i+=2){
            temp[i-1] = temp[i-1] + tic2;
            temp[i] = temp[i] + tic;
        }

        glBindVertexArray(vao);
        // Записываем все текстуры в буфер
        glDeleteBuffers(vbo.get(2));
        vbo.set(2,glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(2));
        if(tex != null) {
            glBufferData(GL_ARRAY_BUFFER, temp, drawInfo);
        }
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
    }

    @Override
    public void setNewTex(float[] tex) {
        glBindVertexArray(vao);
        // Записываем все текстуры в буфер
        glDeleteBuffers(vbo.get(2));
        vbo.set(2,glGenBuffers());
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get(2));
        if(tex != null) {
            glBufferData(GL_ARRAY_BUFFER, tex, drawInfo);
        }
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public boolean isTangentSpace() {
        return tangent;
    }

    @Override
    public boolean isInstances() {
        return instances;
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
