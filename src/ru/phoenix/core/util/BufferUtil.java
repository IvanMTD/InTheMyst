package ru.phoenix.core.util;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.BufferUtils.createIntBuffer;

public class BufferUtil {
    public static FloatBuffer createFlippedBuffer(Matrix4f matrix){

        FloatBuffer buffer = createFloatBuffer(4*4);

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                buffer.put(matrix.get(i,j));
            }
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Matrix4f[] matrix){

        FloatBuffer buffer = createFloatBuffer(matrix.length * 16);

        for (Matrix4f aMatrix : matrix) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    buffer.put(aMatrix.get(x, y));
                }
            }
        }

        buffer.flip();

        return buffer;
    }

    public static IntBuffer createFlippedBuffer(int... values) {

        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vector3f vector) {
        FloatBuffer buffer = createFloatBuffer(3);
        buffer.put(vector.getX());
        buffer.put(vector.getY());
        buffer.put(vector.getZ());
        buffer.flip();
        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vector3f[] vector) {
        FloatBuffer buffer = createFloatBuffer(vector.length * 3);

        for (Vector3f aVector : vector) {
            buffer.put(aVector.getX());
            buffer.put(aVector.getY());
            buffer.put(aVector.getZ());
        }

        buffer.flip();
        return buffer;
    }
}

