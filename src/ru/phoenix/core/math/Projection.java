package ru.phoenix.core.math;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Window;

public class Projection {
    //Матрица проекции
    private Matrix4f projection;
    // Матрица View
    private Matrix4f view;
    //Матрица модели
    private Matrix4f model;
    //Матрицы изменений
    private Matrix4f translation;
    private Matrix4f scaling;
    private Matrix4f rotation;

    public Projection(){
        translation = new Matrix4f().identity();
        scaling = new Matrix4f().identity();
        rotation = new Matrix4f().identity();
        model = new Matrix4f().identity();
        view = new Matrix4f().identity();
        projection = new Matrix4f().identity();
    }

    public Projection(Projection projection){
        translation = projection.getTranslation();
        scaling = projection.getScaling();
        rotation = projection.getRotation();
        model = projection.getModelMatrix();
        view = projection.getViewMatrix();
        this.projection = projection.getProjection();
    }

    public void setPerspective(float fov){
        float aspect = Window.getInstance().getWidth() * 1.0f / Window.getInstance().getHeight();
        projection.setPerspective(fov,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar());
    }

    public void setOrtho(float left, float right, float bottom, float top, float zNear, float zFar){
        projection.setOrtho(left, right, bottom, top, zNear, zFar);
    }

    public void setView(float px, float py, float pz, float fx, float fy, float fz, float ux, float uy, float uz){
        Vector3f position = new Vector3f(px,py,pz);
        Vector3f target = new Vector3f(fx,fy,fz);
        Vector3f up = new Vector3f(ux,uy,uz);

        Vector3f cameraDirection = new Vector3f(position.sub(target)).normalize();
        Vector3f cameraRight = new Vector3f(up.cross(cameraDirection)).normalize();
        Vector3f cameraUp = new Vector3f(cameraDirection.cross(cameraRight));

        Vector3f pos = position.mul(-1);
        view.setView(
                cameraRight.getX(),cameraRight.getY(),cameraRight.getZ(),
                cameraUp.getX(),cameraUp.getY(),cameraUp.getZ(),
                cameraDirection.getX(),cameraDirection.getY(),cameraDirection.getZ()
        ).mul(new Matrix4f().setTranslation(pos.getX(),pos.getY(),pos.getZ()));
    }

    public void setView(Vector3f position, Vector3f target, Vector3f up){
        Vector3f cameraDirection = new Vector3f(position.sub(target)).normalize();
        Vector3f cameraRight = new Vector3f(up.cross(cameraDirection)).normalize();
        Vector3f cameraUp = new Vector3f(cameraDirection.cross(cameraRight));

        Vector3f pos = position.mul(-1);
        view.setView(
                cameraRight.getX(),cameraRight.getY(),cameraRight.getZ(),
                cameraUp.getX(),cameraUp.getY(),cameraUp.getZ(),
                cameraDirection.getX(),cameraDirection.getY(),cameraDirection.getZ()
        ).mul(new Matrix4f().setTranslation(pos.getX(),pos.getY(),pos.getZ()));
    }

    public void setTranslation(Vector3f v){
        translation.setTranslation(v.getX(),v.getY(),v.getZ());
        updateModelMatrix(getModelMatrix().mul(translation.mul(scaling.identity().mul(rotation.identity()))));
    }

    public void setScaling(Vector3f v){
        scaling.setScaling(v.getX(),v.getY(),v.getZ());
        updateModelMatrix(getModelMatrix().mul(translation.identity().mul(scaling.mul(rotation.identity()))));
    }

    public void setScaling(float value){
        scaling.setScaling(value,value,value);
        updateModelMatrix(getModelMatrix().mul(translation.identity().mul(scaling.mul(rotation.identity()))));
    }

    public void setRotation(float angle, Vector3f v){
        rotation.setRotation(angle,v.getX(),v.getY(),v.getZ());
        updateModelMatrix(getModelMatrix().mul(translation.identity().mul(scaling.identity().mul(rotation))));
    }

    public Vector3f transformVector(Matrix4f m, Vector3f v, float val){
        float x = m.get(0,0) * v.getX() + m.get(0,1) * v.getY() + m.get(0,2) * v.getZ() + m.get(0,3) * val;
        float y = m.get(1,0) * v.getX() + m.get(1,1) * v.getY() + m.get(1,2) * v.getZ() + m.get(1,3) * val;
        float z = m.get(2,0) * v.getX() + m.get(2,1) * v.getY() + m.get(2,2) * v.getZ() + m.get(2,3) * val;

        return new Vector3f(x,y,z);
    }

    private void updateModelMatrix(Matrix4f m){
        model = m;
    }

    public Matrix4f getTranslation(){
        return translation;
    }

    public Matrix4f getScaling(){
        return scaling;
    }

    public Matrix4f getRotation(){
        return rotation;
    }

    public Matrix4f getModelMatrix(){
        return model;
    }

    public void setModelMatrix(Matrix4f model){
        this.model = model;
    }

    public Matrix4f getProjection(){
        return projection;
    }

    public void setProjection(Matrix4f projection) {
        this.projection = projection;
    }

    public Matrix4f getViewMatrix(){
        return view;
    }
}
