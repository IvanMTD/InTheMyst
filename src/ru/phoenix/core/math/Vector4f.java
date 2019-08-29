package ru.phoenix.core.math;

public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;

    // Конструкторы
    public Vector4f(){
        setX(0.0f);
        setY(0.0f);
        setZ(0.0f);
        setW(0.0f);
    }

    public Vector4f(float x, float y, float z, float w){
        setX(x);
        setY(y);
        setZ(z);
        setW(w);
    }

    public Vector4f(Vector4f vector){
        setVector(vector);
    }

    /* Вектор на скаляр (сложение и вычитание доют новый вектор)
    (умножение и деление - уменьшают или увеличивают вектор)*/
    public Vector4f add(float value){
        float x,y,z,w;
        x = getX() + value;
        y = getY() + value;
        z = getZ() + value;
        w = getW() + value;
        return new Vector4f(x,y,z,w);
    }

    public Vector4f sub(float value){
        float x,y,z,w;
        x = getX() - value;
        y = getY() - value;
        z = getZ() - value;
        w = getW() - value;
        return new Vector4f(x,y,z,w);
    }

    public Vector4f mul(float value){
        float x,y,z,w;
        x = getX() * value;
        y = getY() * value;
        z = getZ() * value;
        w = getW() * value;
        return new Vector4f(x,y,z,w);
    }

    public Vector4f div(float value){
        float x,y,z,w;
        x = getX() / value;
        y = getY() / value;
        z = getZ() / value;
        w = getW() / value;
        return new Vector4f(x,y,z,w);
    }

    /* Вектор на вектор
    Сложение векторв дает новый вектор - Вычитание находит разницу между двумя векторами
    Кросирование дает ортогональный вектор - Дотирование дает косинус угла*/
    public Vector4f add(Vector4f vector){
        float x,y,z,w;
        x = getX() + vector.getX();
        y = getY() + vector.getY();
        z = getZ() + vector.getZ();
        w = getW() + vector.getW();
        return new Vector4f(x,y,z,w);
    }

    public Vector4f sub(Vector4f vector){
        float x,y,z,w;
        x = getX() + (-vector.getX());
        y = getY() + (-vector.getY());
        z = getZ() + (-vector.getZ());
        w = getW() + (-vector.getW());
        return new Vector4f(x,y,z,w);
    }

    public Vector4f cross(Vector4f vector){
        float x,y,z,w;
        x = getY() * vector.getZ() - getZ() * vector.getY();
        y = getZ() * vector.getX() - getX() * vector.getZ();
        z = getX() * vector.getY() - getY() * vector.getX();
        w = 1.0f;
        return new Vector4f(x,y,z,w);
    }

    public float dot(Vector4f vector){
        float result;
        result = getX() * vector.getX() + getY() * vector.getY() + getZ() * vector.getZ() + getW() * vector.getW();
        return result;
    }

    /*Поиск длинны по теореме пифогора - нормализация вектора приведение его к еденичной длинне*/
    public float length() {
        float result;
        result = (float)Math.sqrt(getX()*getX() + getY()*getY() + getZ()*getZ() + getW()*getW());
        return result;
    }

    public Vector4f normalize() {
        float length = length();
        float x, y, z, w;
        x = getX() / length;
        y = getY() / length;
        z = getZ() / length;
        w = getW() / length;
        return new Vector4f(x, y, z, w);
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

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setVector(Vector4f vector){
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
        this.w = vector.getW();
    }

    public static void vectorInfo(Vector4f v){
        System.out.println("Vector info: x: " + v.getX() + ", y: " + v.getY() + ", z: " + v.getZ() + ", w: " + v.getW() + "\n");
    }
}
