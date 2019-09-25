package ru.phoenix.core.math;

public class Matrix4f {

    public static final int SIZE = 64;

    private float[][] m;

    public Matrix4f(){
        setMatrix(new float[4][4]);
    }

    public Matrix4f(Matrix4f matrix){
        setMatrix(matrix);
    }

    public Matrix4f zero() {
        m[0][0] = 0; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = 0; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 0; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 0;

        return this;
    }

    public Matrix4f identity() {
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f mul(Matrix4f matrix){
        Matrix4f result = new Matrix4f();
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                result.set(i, j, m[i][0] * matrix.get(0, j) + m[i][1] * matrix.get(1, j) + m[i][2] * matrix.get(2, j) + m[i][3] * matrix.get(3, j));
            }
        }

        m = new Matrix4f(result).getMatrix();

        return this;
    }

    public Matrix4f setScaling(float x, float y, float z){

        m[0][0] = x; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = y; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = z; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f setScaling(Vector3f v){

        m[0][0] = v.getX(); m[0][1] = 0;        m[0][2] = 0;        m[0][3] = 0;
        m[1][0] = 0;        m[1][1] = v.getY(); m[1][2] = 0;        m[1][3] = 0;
        m[2][0] = 0;        m[2][1] = 0;        m[2][2] = v.getZ(); m[2][3] = 0;
        m[3][0] = 0;        m[3][1] = 0;        m[3][2] = 0;        m[3][3] = 1;

        return this;
    }

    public Matrix4f setTranslation(float x, float y, float z) {

        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = x;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = y;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = z;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f setTranslation(Vector3f v) {

        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = v.getX();
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = v.getY();
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = v.getZ();
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f setRotation(float angle, float x, float y, float z) {

        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();

        //Вращение вокруг оси X:
        rx.m[0][0] = 1;      rx.m[0][1] = 0;                                             rx.m[0][2] = 0;                                              rx.m[0][3] = 0;
        rx.m[1][0] = 0;      rx.m[1][1] = (float)Math.cos((angle * x) * (Math.PI/180));  rx.m[1][2] = (float)-Math.sin((angle * x) * (Math.PI/180));  rx.m[1][3] = 0;
        rx.m[2][0] = 0;      rx.m[2][1] = (float)Math.sin((angle * x) * (Math.PI/180));  rx.m[2][2] = (float)Math.cos((angle * x) * (Math.PI/180));   rx.m[2][3] = 0;
        rx.m[3][0] = 0;      rx.m[3][1] = 0;                                             rx.m[3][2] = 0;                                              rx.m[3][3] = 1;
        //Вращение вокруг оси Y:
        ry.m[0][0] = (float)Math.cos((angle * y) * (Math.PI/180));   ry.m[0][1] = 0;      ry.m[0][2] = (float)Math.sin((angle * y) * (Math.PI/180));   ry.m[0][3] = 0;
        ry.m[1][0] = 0;                                              ry.m[1][1] = 1;      ry.m[1][2] = 0;                                              ry.m[1][3] = 0;
        ry.m[2][0] = (float)-Math.sin((angle * y) * (Math.PI/180));  ry.m[2][1] = 0;      ry.m[2][2] = (float)Math.cos((angle * y) * (Math.PI/180));   ry.m[2][3] = 0;
        ry.m[3][0] = 0;                                              ry.m[3][1] = 0;      ry.m[3][2] = 0;                                              ry.m[3][3] = 1;
        //Вращение вокруг оси Z:
        rz.m[0][0] = (float)Math.cos((angle * z) * (Math.PI/180));   rz.m[0][1] = (float)-Math.sin((angle * z) * (Math.PI/180));   rz.m[0][2] = 0;         rz.m[0][3] = 0;
        rz.m[1][0] = (float)Math.sin((angle * z) * (Math.PI/180));   rz.m[1][1] = (float)Math.cos((angle * z) * (Math.PI/180));    rz.m[1][2] = 0;         rz.m[1][3] = 0;
        rz.m[2][0] = 0;                                              rz.m[2][1] = 0;                                               rz.m[2][2] = 1;         rz.m[2][3] = 0;
        rz.m[3][0] = 0;                                              rz.m[3][1] = 0;                                               rz.m[3][2] = 0;         rz.m[3][3] = 1;

        m = new Matrix4f(rz.mul(ry.mul(rx))).getMatrix();

        return this;
    }

    public Matrix4f setRotation(float angle, Vector3f v) {

        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();

        //Вращение вокруг оси X:
        rx.m[0][0] = 1;      rx.m[0][1] = 0;                                                    rx.m[0][2] = 0;                                                     rx.m[0][3] = 0;
        rx.m[1][0] = 0;      rx.m[1][1] = (float)Math.cos((angle * v.getX()) * (Math.PI/180));  rx.m[1][2] = (float)-Math.sin((angle * v.getX()) * (Math.PI/180));  rx.m[1][3] = 0;
        rx.m[2][0] = 0;      rx.m[2][1] = (float)Math.sin((angle * v.getX()) * (Math.PI/180));  rx.m[2][2] = (float)Math.cos((angle * v.getX()) * (Math.PI/180));   rx.m[2][3] = 0;
        rx.m[3][0] = 0;      rx.m[3][1] = 0;                                                    rx.m[3][2] = 0;                                                     rx.m[3][3] = 1;
        //Вращение вокруг оси Y:
        ry.m[0][0] = (float)Math.cos((angle * v.getY()) * (Math.PI/180));   ry.m[0][1] = 0;      ry.m[0][2] = (float)Math.sin((angle * v.getY()) * (Math.PI/180));   ry.m[0][3] = 0;
        ry.m[1][0] = 0;                                                     ry.m[1][1] = 1;      ry.m[1][2] = 0;                                                     ry.m[1][3] = 0;
        ry.m[2][0] = (float)-Math.sin((angle * v.getY()) * (Math.PI/180));  ry.m[2][1] = 0;      ry.m[2][2] = (float)Math.cos((angle * v.getY()) * (Math.PI/180));   ry.m[2][3] = 0;
        ry.m[3][0] = 0;                                                     ry.m[3][1] = 0;      ry.m[3][2] = 0;                                                     ry.m[3][3] = 1;
        //Вращение вокруг оси Z:
        rz.m[0][0] = (float)Math.cos((angle * v.getZ()) * (Math.PI/180));   rz.m[0][1] = (float)-Math.sin((angle * v.getZ()) * (Math.PI/180));   rz.m[0][2] = 0;         rz.m[0][3] = 0;
        rz.m[1][0] = (float)Math.sin((angle * v.getZ()) * (Math.PI/180));   rz.m[1][1] = (float)Math.cos((angle * v.getZ()) * (Math.PI/180));    rz.m[1][2] = 0;         rz.m[1][3] = 0;
        rz.m[2][0] = 0;                                                     rz.m[2][1] = 0;                                                      rz.m[2][2] = 1;         rz.m[2][3] = 0;
        rz.m[3][0] = 0;                                                     rz.m[3][1] = 0;                                                      rz.m[3][2] = 0;         rz.m[3][3] = 1;

        m = new Matrix4f(rz.mul(ry.mul(rx))).getMatrix();

        return this;
    }

    public Matrix4f rotation(Quaternion quat) {

        float w2 = quat.getW() * quat.getW();
        float x2 = quat.getX() * quat.getX();
        float y2 = quat.getY() * quat.getY();
        float z2 = quat.getZ() * quat.getZ();
        float zw = quat.getZ() * quat.getW(), dzw = zw + zw;
        float xy = quat.getX() * quat.getY(), dxy = xy + xy;
        float xz = quat.getX() * quat.getZ(), dxz = xz + xz;
        float yw = quat.getY() * quat.getW(), dyw = yw + yw;
        float yz = quat.getY() * quat.getZ(), dyz = yz + yz;
        float xw = quat.getX() * quat.getW(), dxw = xw + xw;

        set(0,0,(w2 + x2 - z2 - y2));   set(0,1,(-dzw + dxy));              set(0,2,(dyw + dxz));
        set(1,0,(dxy + dzw));           set(1,1,(y2 - z2 + w2 - x2));       set(1,2,(dyz - dxw));
        set(2,0,(dxz - dyw));           set(2,1,(dyz + dxw));               set(2,2,(z2 - y2 - x2 + w2));

        return this;
    }

    public static Quaternion getRotationQuat(Matrix4f rot){

        Quaternion quat;
        float x, y, z, w;

        float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

        if(trace > 0) {
            float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
            w = 0.25f / s;
            x = (rot.get(1, 2) - rot.get(2, 1)) * s;
            y = (rot.get(2, 0) - rot.get(0, 2)) * s;
            z = (rot.get(0, 1) - rot.get(1, 0)) * s;
        }else{
            if(rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
                float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
                w = (rot.get(1, 2) - rot.get(2, 1)) / s;
                x = 0.25f * s;
                y = (rot.get(1, 0) + rot.get(0, 1)) / s;
                z = (rot.get(2, 0) + rot.get(0, 2)) / s;
            }
            else if(rot.get(1, 1) > rot.get(2, 2)) {
                float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
                w = (rot.get(2, 0) - rot.get(0, 2)) / s;
                x = (rot.get(1, 0) + rot.get(0, 1)) / s;
                y = 0.25f * s;
                z = (rot.get(2, 1) + rot.get(1, 2)) / s;
            }
            else {
                float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
                w = (rot.get(0, 1) - rot.get(1, 0) ) / s;
                x = (rot.get(2, 0) + rot.get(0, 2) ) / s;
                y = (rot.get(1, 2) + rot.get(2, 1) ) / s;
                z = 0.25f * s;
            }
        }

        float length = (float)Math.sqrt(x * x + y * y + z * z + w * w);
        x /= length;
        y /= length;
        z /= length;
        w /= length;

        quat = new Quaternion(x,y,z,w);

        return quat;
    }

    public Matrix4f transpose()
    {
        Matrix4f result = new Matrix4f();

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                result.set(i, j, get(j,i));
            }
        }
        return result;
    }

    public Matrix4f invert()
    {
        float s0 = get(0, 0) * get(1, 1) - get(1, 0) * get(0, 1);
        float s1 = get(0, 0) * get(1, 2) - get(1, 0) * get(0, 2);
        float s2 = get(0, 0) * get(1, 3) - get(1, 0) * get(0, 3);
        float s3 = get(0, 1) * get(1, 2) - get(1, 1) * get(0, 2);
        float s4 = get(0, 1) * get(1, 3) - get(1, 1) * get(0, 3);
        float s5 = get(0, 2) * get(1, 3) - get(1, 2) * get(0, 3);

        float c5 = get(2, 2) * get(3, 3) - get(3, 2) * get(2, 3);
        float c4 = get(2, 1) * get(3, 3) - get(3, 1) * get(2, 3);
        float c3 = get(2, 1) * get(3, 2) - get(3, 1) * get(2, 2);
        float c2 = get(2, 0) * get(3, 3) - get(3, 0) * get(2, 3);
        float c1 = get(2, 0) * get(3, 2) - get(3, 0) * get(2, 2);
        float c0 = get(2, 0) * get(3, 1) - get(3, 0) * get(2, 1);


        float div = (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0);
        if (div == 0) System.err.println("not invertible");

        float invdet = 1.0f / div;

        Matrix4f invM = new Matrix4f();

        invM.set(0, 0, (get(1, 1) * c5 - get(1, 2) * c4 + get(1, 3) * c3) * invdet);
        invM.set(0, 1, (-get(0, 1) * c5 + get(0, 2) * c4 - get(0, 3) * c3) * invdet);
        invM.set(0, 2, (get(3, 1) * s5 - get(3, 2) * s4 + get(3, 3) * s3) * invdet);
        invM.set(0, 3, (-get(2, 1) * s5 + get(2, 2) * s4 - get(2, 3) * s3) * invdet);

        invM.set(1, 0, (-get(1, 0) * c5 + get(1, 2) * c2 - get(1, 3) * c1) * invdet);
        invM.set(1, 1, (get(0, 0) * c5 - get(0, 2) * c2 + get(0, 3) * c1) * invdet);
        invM.set(1, 2, (-get(3, 0) * s5 + get(3, 2) * s2 - get(3, 3) * s1) * invdet);
        invM.set(1, 3, (get(2, 0) * s5 - get(2, 2) * s2 + get(2, 3) * s1) * invdet);

        invM.set(2, 0, (get(1, 0) * c4 - get(1, 1) * c2 + get(1, 3) * c0) * invdet);
        invM.set(2, 1, (-get(0, 0) * c4 + get(0, 1) * c2 - get(0, 3) * c0) * invdet);
        invM.set(2, 2, (get(3, 0) * s4 - get(3, 1) * s2 + get(3, 3) * s0) * invdet);
        invM.set(2, 3, (-get(2, 0) * s4 + get(2, 1) * s2 - get(2, 3) * s0) * invdet);

        invM.set(3, 0, (-get(1, 0) * c3 + get(1, 1) * c1 - get(1, 2) * c0) * invdet);
        invM.set(3, 1, (get(0, 0) * c3 - get(0, 1) * c1 + get(0, 2) * c0) * invdet);
        invM.set(3, 2, (-get(3, 0) * s3 + get(3, 1) * s1 - get(3, 2) * s0) * invdet);
        invM.set(3, 3, (get(2, 0) * s3 - get(2, 1) * s1 + get(2, 2) * s0) * invdet);

        return invM;
    }

    public Matrix4f setPerspective(float fovY, float aspect, float zNear, float zFar) {

        float range = (float)Math.tan((fovY / 2) * (Math.PI/180)) * zNear;
        float right = range * aspect;

        m[0][0] = zNear / right;               m[0][1] = 0; 		 	                m[0][2] = 0; 				                      m[0][3] = 0;
        m[1][0] = 0; 					       m[1][1] = zNear / range;	                m[1][2] = 0; 			 	                      m[1][3] = 0;
        m[2][0] = 0; 				 	       m[2][1] = 0; 		 	                m[2][2] = -(zFar + zNear) / (zFar - zNear);	      m[2][3] = -(2 * zFar * zNear) / (zFar - zNear);
        m[3][0] = 0; 				 	       m[3][1] = 0; 		 	                m[3][2] = -1;                                     m[3][3] = 0;

        return this;
    }

    public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar) {

        m[0][0] = 2 / (right - left);                m[0][1] = 0; 		 	                         m[0][2] = 0; 				                  m[0][3] = -(right + left) / (right - left);
        m[1][0] = 0; 					             m[1][1] = 2 / (top - bottom);                   m[1][2] = 0; 			 	                  m[1][3] = -(top + bottom) / (top - bottom);
        m[2][0] = 0; 				 	             m[2][1] = 0; 		 	                         m[2][2] = -2 / (zFar - zNear);	              m[2][3] = -(zFar + zNear) / (zFar - zNear);
        m[3][0] = 0;	                             m[3][1] = 0;	                                 m[3][2] = 0;                                 m[3][3] = 1;

        return this;
    }

    public Matrix4f setView(float rX, float rY, float rZ, float uX, float uY, float uZ, float dX, float dY, float dZ){

        m[0][0] = rX; m[0][1] = rY; m[0][2] = rZ; m[0][3] = 0;
        m[1][0] = uX; m[1][1] = uY; m[1][2] = uZ; m[1][3] = 0;
        m[2][0] = dX; m[2][1] = dY; m[2][2] = dZ; m[2][3] = 0;
        m[3][0] = 0;  m[3][1] = 0;  m[3][2] = 0;  m[3][3] = 1;

        return this;
    }

    public Vector4f mulOnVector(Vector4f vector){
        Vector4f result = new Vector4f();

        result.setX(get(0,0) * vector.getX() + get(0,1) * vector.getY() + get(0,2) * vector.getZ() + get(0,3) * vector.getW());
        result.setY(get(1,0) * vector.getX() + get(1,1) * vector.getY() + get(1,2) * vector.getZ() + get(1,3) * vector.getW());
        result.setZ(get(2,0) * vector.getX() + get(2,1) * vector.getY() + get(2,2) * vector.getZ() + get(2,3) * vector.getW());
        result.setW(get(3,0) * vector.getX() + get(3,1) * vector.getY() + get(3,2) * vector.getZ() + get(3,3) * vector.getW());

        return result;
    }

    public Vector3f mulOnVector(Vector3f vector){
        Vector3f result = new Vector3f();

        result.setX(get(0,0) * vector.getX() + get(0,1) * vector.getY() + get(0,2) * vector.getZ() + get(0,3) * 1.0f);
        result.setY(get(1,0) * vector.getX() + get(1,1) * vector.getY() + get(1,2) * vector.getZ() + get(1,3) * 1.0f);
        result.setZ(get(2,0) * vector.getX() + get(2,1) * vector.getY() + get(2,2) * vector.getZ() + get(2,3) * 1.0f);

        return result;
    }

    public void setMatrix(float[][] m) {
        this.m = m;
    }

    public void setMatrix(Matrix4f matrix){
        m = new float[4][4];
        for(int i=0; i<4; i++){
            for(int j=0;j<4;j++){
                m[i][j] = matrix.get(i,j);
            }
        }
    }

    public float[][] getMatrix(){
        return m;
    }

    public void set(int x, int y, float value){
        m[x][y] = value;
    }

    public float get(int x, int y){
        return m[x][y];
    }

    public Vector3f getPositionVector(){
        return new Vector3f(get(0,3),get(1,3),get(2,3));
    }

    public static void matrixInfo(Matrix4f matrix){
        System.out.println(matrix.get(0,0) + " " + matrix.get(0,1) + " " + matrix.get(0,2) + " " + matrix.get(0,3));
        System.out.println(matrix.get(1,0) + " " + matrix.get(1,1) + " " + matrix.get(1,2) + " " + matrix.get(1,3));
        System.out.println(matrix.get(2,0) + " " + matrix.get(2,1) + " " + matrix.get(2,2) + " " + matrix.get(2,3));
        System.out.println(matrix.get(3,0) + " " + matrix.get(3,1) + " " + matrix.get(3,2) + " " + matrix.get(3,3) + "\n");
    }
}
