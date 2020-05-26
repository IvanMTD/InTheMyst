package ru.phoenix.game.datafile;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class SaveElement implements Externalizable {

    private static final long serialVersionUID = 1L;

    private int arraySize;
    private List<Vector2f> blockedPos;
    private Vector3f elementPos;
    private int type;
    private int textureNum;
    private float currentHeight;
    private float objectWidth;
    private float objectHeight;

    private int gmSize;
    private Matrix4f[] grassMatrix;

    public SaveElement(){
        arraySize = 0;
        blockedPos = new ArrayList<>();
        elementPos = new Vector3f();
        type = 0;
        textureNum = 0;
        currentHeight = 0.0f;
        objectWidth = 0.0f;
        objectHeight = 0.0f;
        gmSize = 0;
        grassMatrix = new Matrix4f[0];
    }

    public SaveElement(List<Vector2f> blockedPos, Vector3f elementPos, int type, int textureNum, float currentHeight, float objectWidth, float objectHeight) {
        arraySize = blockedPos.size();
        this.blockedPos = blockedPos;
        this.elementPos = elementPos;
        this.type = type;
        this.textureNum = textureNum;
        this.currentHeight = currentHeight;
        this.objectWidth = objectWidth;
        this.objectHeight = objectHeight;
    }

    public SaveElement(Matrix4f[] matrix){
        gmSize = matrix.length;
        grassMatrix = matrix;
    }

    public List<Vector2f> getBlockedPos() {
        return blockedPos;
    }

    public void setBlockedPos(List<Vector2f> blockedPos) {
        arraySize = blockedPos.size();
        this.blockedPos = blockedPos;
    }

    public Vector3f getElementPos() {
        return elementPos;
    }

    public void setElementPos(Vector3f elementPos) {
        this.elementPos = elementPos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTextureNum() {
        return textureNum;
    }

    public void setTextureNum(int textureNum) {
        this.textureNum = textureNum;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

    public float getObjectWidth() {
        return objectWidth;
    }

    public void setObjectWidth(float objectWidth) {
        this.objectWidth = objectWidth;
    }

    public float getObjectHeight() {
        return objectHeight;
    }

    public void setObjectHeight(float objectHeight) {
        this.objectHeight = objectHeight;
    }

    public int getGmSize() {
        return gmSize;
    }

    public void setGmSize(int gmSize) {
        this.gmSize = gmSize;
    }

    public Matrix4f[] getGrassMatrix() {
        return grassMatrix;
    }

    public void setGrassMatrix(Matrix4f[] grassMatrix) {
        gmSize = grassMatrix.length;
        this.grassMatrix = grassMatrix;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(arraySize);
        for(Vector2f vector : blockedPos){
            vector.writeExternal(out);
        }
        elementPos.writeExternal(out);
        out.writeObject(type);
        out.writeObject(textureNum);
        out.writeObject(currentHeight);
        out.writeObject(objectWidth);
        out.writeObject(objectHeight);
        out.writeObject(gmSize);
        for(int i=0; i<gmSize; i++){
            grassMatrix[i].writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        arraySize = (int)in.readObject();
        blockedPos = new ArrayList<>();
        for(int i=0; i<arraySize; i++){
            Vector2f vector = new Vector2f();
            vector.readExternal(in);
            blockedPos.add(vector);
        }
        elementPos.readExternal(in);
        type = (int)in.readObject();
        textureNum = (int)in.readObject();
        currentHeight = (float)in.readObject();
        objectWidth = (float)in.readObject();
        objectHeight = (float)in.readObject();
        gmSize = (int)in.readObject();
        grassMatrix = new Matrix4f[gmSize];
        for(int i=0; i<gmSize; i++){
            grassMatrix[i] = new Matrix4f();
            grassMatrix[i].readExternal(in);
        }
    }
}
