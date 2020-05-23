package ru.phoenix.core.math;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Vector2f implements Externalizable {

    private static final long serialVersionUID = 1L;

    private float x;
    private float y;

    // Конструкторы
    public Vector2f(){
        setX(0.0f);
        setY(0.0f);
    }

    public Vector2f(float x, float y){
        setX(x);
        setY(y);
    }

    public Vector2f(Vector2f vector){
        setX(vector.getX());
        setY(vector.getY());
    }

    // Вектор на скаляр
    public Vector2f add (float value){
        float x,y;
        x = getX() + value;
        y = getY() + value;
        return new Vector2f(x,y);
    }

    public Vector2f sub (float value){
        float x,y;
        x = getX() - value;
        y = getY() - value;
        return new Vector2f(x,y);
    }

    public Vector2f mul (float value){
        float x,y;
        x = getX() * value;
        y = getY() * value;
        return new Vector2f(x,y);
    }

    public Vector2f div (float value){
        float x,y;
        x = getX() / value;
        y = getY() / value;
        return new Vector2f(x,y);
    }

    // Вектор на вектор
    public Vector2f add(Vector2f vector){
        float x,y;
        x = getX() + vector.getX();
        y = getY() + vector.getY();
        return new Vector2f(x,y);
    }

    public Vector2f sub(Vector2f vector){
        float x,y ;
        x = getX() + (-vector.getX());
        y = getY() + (-vector.getY());
        return new Vector2f(x,y);
    }

    // Умножение векторов
    public float dot(Vector2f vector) {
        float result;
        result = getX() * vector.getX() + getY() * vector.getY();
        return result;
    }

    // Длинна векторов
    public float length() {
        float result;
        result = (float)Math.sqrt(getX() * getX() + getY() * getY());
        return result;
    }

    // Нормализация (приведение к еденичному вектору)
    public Vector2f normalize() {
        float length = length();
        this.x /= length;
        this.y /= length;
        return this;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // инфо отладка
    public static void vectorInfo(Vector2f v){
        System.out.println("Vector info: x: " + v.getX() + ", y: " + v.getY() + "\n");
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getX());
        out.writeObject(getY());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setX((float)in.readObject());
        setY((float)in.readObject());
    }
}
