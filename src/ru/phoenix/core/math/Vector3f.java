package ru.phoenix.core.math;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Vector3f implements Externalizable {
    private float x;
    private float y;
    private float z;

    // Конструкторы
    public Vector3f(){
        setX(0.0f);
        setY(0.0f);
        setZ(0.0f);
    }

    public Vector3f(float x, float y, float z){
        setX(x);
        setY(y);
        setZ(z);
    }

    public Vector3f(Vector3f vector){
        setVector(vector);
    }

    /* Вектор на скаляр (сложение и вычитание доют новый вектор)
    (умножение и деление - уменьшают или увеличивают вектор)*/
    public Vector3f add(float value){
        float x,y,z;
        x = getX() + value;
        y = getY() + value;
        z = getZ() + value;
        return new Vector3f(x,y,z);
    }

    public Vector3f sub(float value){
        float x,y,z;
        x = getX() - value;
        y = getY() - value;
        z = getZ() - value;
        return new Vector3f(x,y,z);
    }

    public Vector3f mul(float value){
        float x,y,z;
        x = getX() * value;
        y = getY() * value;
        z = getZ() * value;
        return new Vector3f(x,y,z);
    }

    public Vector3f div(float value){
        float x,y,z;
        x = getX() / value;
        y = getY() / value;
        z = getZ() / value;
        return new Vector3f(x,y,z);
    }

    /* Вектор на вектор
    Сложение векторв дает новый вектор - Вычитание находит разницу между двумя векторами
    Кросирование дает ортогональный вектор - Дотирование дает косинус угла*/
    public Vector3f add(Vector3f vector){
        float x,y,z;
        x = getX() + vector.getX();
        y = getY() + vector.getY();
        z = getZ() + vector.getZ();
        return new Vector3f(x,y,z);
    }

    public Vector3f sub(Vector3f vector){
        float x,y,z;
        x = getX() - vector.getX();
        y = getY() - vector.getY();
        z = getZ() - vector.getZ();
        return new Vector3f(x,y,z);
    }

    public Vector3f cross(Vector3f vector){
        float x,y,z;
        x = getY() * vector.getZ() - getZ() * vector.getY();
        y = getZ() * vector.getX() - getX() * vector.getZ();
        z = getX() * vector.getY() - getY() * vector.getX();
        return new Vector3f(x,y,z);
    }

    public float dot(Vector3f vector){
        float result;
        result = (getX() * vector.getX()) + (getY() * vector.getY()) + (getZ() * vector.getZ());
        return result;
    }

    /*Поиск длинны по теореме пифогора - нормализация вектора приведение его к еденичной длинне*/
    public float length() {
        float result;
        result = (float)Math.sqrt(getX()*getX() + getY()*getY() + getZ()*getZ());
        return result;
    }

    public Vector3f normalize() {
        float length = length();
        if(length != 0.0f) {
            float x, y, z;
            x = getX() / length;
            y = getY() / length;
            z = getZ() / length;
            return new Vector3f(x, y, z);
        }
        return new Vector3f();
    }

    public Vector3f rotate(Quaternion rotation) {
        Quaternion conjugate = rotation.conjugate();
        Quaternion w = rotation.mul(this).mul(conjugate);
        return new Vector3f(w.getX(), w.getY(), w.getZ());
    }

    public float getX() {
        return x;
    }

    public float getR(){
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public float getG() { return y; }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public float getB() { return z; }

    public void setZ(float z) {
        this.z = z;
    }

    public void setVector(Vector3f vector){
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    public boolean equals(Vector3f v){
        boolean equals = false;

        if(this.x == v.getX() && this.z == v.getZ()){
            equals = true;
        }

        return equals;
    }

    public static void vectorInfo(Vector3f v){
        System.out.println("Vector info: x: " + v.getX() + ", y: " + v.getY() + ", z: " + v.getZ() + "\n");
    }

    @Override
    public String toString(){
        return this.getX() + " " + this.getY() + " " + this.getZ();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getX());
        out.writeObject(getY());
        out.writeObject(getZ());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setX((float)in.readObject());
        setY((float)in.readObject());
        setZ((float)in.readObject());
    }
}
