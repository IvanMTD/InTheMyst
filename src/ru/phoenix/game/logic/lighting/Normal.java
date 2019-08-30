package ru.phoenix.game.logic.lighting;

import ru.phoenix.core.math.Vector3f;

public class Normal {

    private static Vector3f getTriangleNormal(Vector3f v1, Vector3f v2, Vector3f v3){
        Vector3f normal;
        Vector3f side1 = v2.sub(v1);
        Vector3f side2 = v3.sub(v1);
        normal = side2.cross(side1);
        return normal.normalize();
    }

    public static Vector3f getVertexNormalSquare(Vector3f h, Vector3f v0, Vector3f v1, Vector3f v2){
        Vector3f norm1 = getTriangleNormal(h,v0,v1);
        Vector3f norm2 = getTriangleNormal(h,v1,v2);
        return new Vector3f(norm1.add(norm2)).normalize();
    }

    public static Vector3f getVertexNormalCube(Vector3f h, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f v5){
        Vector3f norm1 = getTriangleNormal(h,v0,v1);
        Vector3f norm2 = getTriangleNormal(h,v1,v2);
        Vector3f norm3 = getTriangleNormal(h,v2,v3);
        Vector3f norm4 = getTriangleNormal(h,v3,v4);
        Vector3f norm5 = getTriangleNormal(h,v4,v5);
        Vector3f norm6 = getTriangleNormal(h,v5,v0);
        return new Vector3f(norm1.add(norm2.add(norm3.add(norm4.add(norm5.add(norm6))))).normalize());
    }

    public static Vector3f getVertexNormalTesseract(Vector3f h, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f v5, Vector3f v6, Vector3f v7){
        Vector3f norm1 = getTriangleNormal(h,v0,v1);
        Vector3f norm2 = getTriangleNormal(h,v1,v2);
        Vector3f norm3 = getTriangleNormal(h,v2,v3);
        Vector3f norm4 = getTriangleNormal(h,v3,v4);
        Vector3f norm5 = getTriangleNormal(h,v4,v5);
        Vector3f norm6 = getTriangleNormal(h,v5,v6);
        Vector3f norm7 = getTriangleNormal(h,v6,v7);
        Vector3f norm8 = getTriangleNormal(h,v7,v0);
        return new Vector3f(norm1.add(norm2.add(norm3.add(norm4.add(norm5.add(norm6.add(norm7.add(norm8))))))).normalize());
    }
}
