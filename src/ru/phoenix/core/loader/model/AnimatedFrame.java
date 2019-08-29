package ru.phoenix.core.loader.model;

import ru.phoenix.core.math.Matrix4f;

import java.util.Arrays;

public class AnimatedFrame {
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f().identity();

    public static final int MAX_JOINTS = 44;
    public static final int MAX_TIMES = 200;

    private final Matrix4f[] jointMatrices;
    private double[] times;
    private int timeSize;

    public AnimatedFrame() {
        timeSize = 0;
        times = new double[MAX_TIMES];
        jointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(jointMatrices, IDENTITY_MATRIX);
    }

    public Matrix4f[] getJointMatrices() {
        return jointMatrices;
    }

    public void setMatrix(int pos, Matrix4f jointMatrix) {
        jointMatrices[pos] = jointMatrix;
    }

    public void addTime(int index, double time){
        times[index] = time;
        timeSize++;
    }

    public double[] getTimes(){
        return times;
    }

    public double getTimes(int index){
        return times[index];
    }

    public int getTimeSize(){
        return timeSize;
    }
}
