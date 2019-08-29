package ru.phoenix.core.math;

public class Quaternion {
    // Переменные класса
    private float x;
    private float y;
    private float z;
    private float w;

    // Конструкторы
    public Quaternion(){
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Quaternion(Quaternion q){
        this.x = q.getX();
        this.y = q.getY();
        this.z = q.getZ();
        this.w = q.getW();
    }

    public Quaternion(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vector3f axis, float angle){
        float sinHalfAngle = (float)Math.sin(angle / 2);
        float cosHalfAngle = (float)Math.cos(angle / 2);

        this.x = axis.getX() * sinHalfAngle;
        this.y = axis.getY() * sinHalfAngle;
        this.z = axis.getZ() * sinHalfAngle;
        this.w = cosHalfAngle;
    }

    // Методы класса
    // Длинна Кватерниона
    public float length(){
        return (float)Math.sqrt(x * x + y * y + z * z + w * w);
    }
    // Нормализация Кватерниона
    public Quaternion normalized(){
        float length = length();
        return new Quaternion(x / length, y / length, z / length, w / length);
    }
    // Сопряженный Кватернион
    public Quaternion conjugate(){
        return new Quaternion(-x, -y, -z, w);
    }
    // Умножение на скаляр
    public Quaternion mul(float value){
        return new Quaternion(x * value, y * value, z * value, w * value);
    }
    // Умножение на Кватернион
    public Quaternion mul(Quaternion q){
        float w = this.w * q.getW() - this.x * q.getX() - this.y * q.getY() - this.z * q.getZ();
        float x = this.x * q.getW() + this.w * q.getX() + this.y * q.getZ() - this.z * q.getY();
        float y = this.y * q.getW() + this.w * q.getY() + this.z * q.getX() - this.x * q.getZ();
        float z = this.z * q.getW() + this.w * q.getZ() + this.x * q.getY() - this.y * q.getX();

        return new Quaternion(x, y, z, w);
    }
    // Умножение на вектор
    public Quaternion mul(Vector3f v){
        float w = -this.x * v.getX() - this.y * v.getY() - this.z * v.getZ();
        float x =  this.w * v.getX() + this.y * v.getZ() - this.z * v.getY();
        float y =  this.w * v.getY() + this.z * v.getX() - this.x * v.getZ();
        float z =  this.w * v.getZ() + this.x * v.getY() - this.y * v.getX();

        return new Quaternion(x, y, z, w);
    }
    // Вычитание Кватерниона
    public Quaternion sub(Quaternion q){
        return new Quaternion(x - q.getX(), y - q.getY(), z - q.getZ(), w - q.getW());
    }
    // Сложение Кватерниона
    public Quaternion add(Quaternion q){
        return new Quaternion(x + q.getX(), y + q.getY(), z + q.getZ(), w + q.getW());
    }
    // Из текущего Кватерниона в Матрицу Вращения
    public Matrix4f toRotationMatrix(){
        Vector3f forward =  new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
        Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
        Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));

        return new Matrix4f().setView(right.getX(),right.getY(),right.getZ(),up.getX(),up.getY(),up.getZ(),forward.getX(),forward.getY(),forward.getZ());
    }
    // Угол находиться через acos(dot prroduct)
    public float dot(Quaternion q){
        return x * q.getX() + y * q.getY() + z * q.getZ() + w * q.getW();
    }
    // Нормализованная Линейная Интерполяция
    public Quaternion nLerp(Quaternion dest, float lerpFactor, boolean shortest){
        Quaternion correctedDest = dest;
        if(shortest && this.dot(dest) < 0) {
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }
        return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
    }
    // Сферическая линейная интерполяция
    public Quaternion sLerp(Quaternion dest, float lerpFactor, boolean shortest){
        final float EPSILON = 1e3f;
        float cos = this.dot(dest);
        Quaternion correctedDest = dest;

        if(shortest && cos < 0) {
            cos = -cos;
            correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
        }

        if(Math.abs(cos) >= 1 - EPSILON) {
            return nLerp(correctedDest, lerpFactor, true);
        }

        float sin = (float)Math.sqrt(1.0f - cos * cos);
        float angle = (float)Math.atan2(sin, cos);
        float invSin =  1.0f/sin;

        float srcFactor = (float)Math.sin((1.0f - lerpFactor) * angle) * invSin;
        float destFactor = (float)Math.sin((lerpFactor) * angle) * invSin;

        return this.mul(srcFactor).add(correctedDest.mul(destFactor));
    }

    // Гетеры и сетеры
    public Vector3f getForward() {
        return new Vector3f(0,0,1).rotate(this);
    }

    public Vector3f getBack() {
        return new Vector3f(0,0,-1).rotate(this);
    }

    public Vector3f getUp() {
        return new Vector3f(0,1,0).rotate(this);
    }

    public Vector3f getDown() {
        return new Vector3f(0,-1,0).rotate(this);
    }

    public Vector3f getRight() {
        return new Vector3f(1,0,0).rotate(this);
    }

    public Vector3f getLeft() {
        return new Vector3f(-1,0,0).rotate(this);
    }

    public Quaternion set(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Quaternion set(Quaternion r){
        set(r.getX(), r.getY(), r.getZ(), r.getW());
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
    // Проверка на идентичность
    public boolean equals(Quaternion q) {
        return x == q.getX() && y == q.getY() && z == q.getZ() && w == q.getW();
    }
    // Информация о кватернионе для отладки
    public static void getQuaternionInfo(Quaternion q){
        System.out.println("Quaternion info: " + q.getX() + " " + q.getY() + " " + q.getZ() + " " + q.getW() + "\n");
    }
}
