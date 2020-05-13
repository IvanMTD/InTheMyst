package ru.phoenix.core.util;

public class Collector {
    public static CollectorStruct getStruct(float width, float height){
        float x = width / 2.0f;
        float y = height / 2.0f;
        float[] position = new float[]{
                -x,  y,  0.0f,
                -x, -y,  0.0f,
                 x, -y,  0.0f,
                 x,  y,  0.0f
        };

        float[] texCoord = new float[]{
                0.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
                1.0f,1.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };
        return new CollectorStruct(position,texCoord,indices);
    }
}
