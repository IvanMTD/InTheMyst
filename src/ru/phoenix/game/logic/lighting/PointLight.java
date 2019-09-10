package ru.phoenix.game.logic.lighting;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;

public class PointLight implements Light {
    private Vector3f position;
    private Vector3f strength;

    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;

    private Matrix4f[] lightSpaceMatrix;

    public PointLight(Vector3f position, Vector3f strength, Vector3f ambient, Vector3f diffuse, Vector3f specular){
        lightSpaceMatrix = new Matrix4f[6];
        for(int i=0; i<6; i++){
            lightSpaceMatrix[i] = new Matrix4f();
        }
        setPosition(position);
        setStrength(strength);
        setAmbient(ambient);
        setDiffuse(diffuse);
        setSpecular(specular);
        createLightSpaceMatrix();
    }

    private void createLightSpaceMatrix(){
        float aspect = (Window.getInstance().getWidth() + Window.getInstance().getHeight()) * 1.0f/(Window.getInstance().getWidth() + Window.getInstance().getHeight());
        Projection projection = new Projection();
        //Право
        projection.setProjection(new Matrix4f().setPerspective(90.0f,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar()));
        projection.setView(position, position.add(new Vector3f(1.0f,0.0f,0.0f)),new Vector3f(0.0f,-1.0f,0.0f));
        lightSpaceMatrix[0] = projection.getProjection().mul(projection.getViewMatrix());
        //Лево
        projection.setProjection(new Matrix4f().setPerspective(90.0f,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar()));
        projection.setView(position, position.add(new Vector3f(-1.0f,0.0f,0.0f)),new Vector3f(0.0f,-1.0f,0.0f));
        lightSpaceMatrix[1] = projection.getProjection().mul(projection.getViewMatrix());
        //Верх
        projection.setProjection(new Matrix4f().setPerspective(90.0f,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar()));
        projection.setView(position, position.add(new Vector3f(0.0f,1.0f,0.0f)),new Vector3f(0.0f,0.0f,1.0f));
        lightSpaceMatrix[2] = projection.getProjection().mul(projection.getViewMatrix());
        //Низ
        projection.setProjection(new Matrix4f().setPerspective(90.0f,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar()));
        projection.setView(position, position.add(new Vector3f(0.0f,-1.0f,0.0f)),new Vector3f(0.0f,0.0f,-1.0f));
        lightSpaceMatrix[3] = projection.getProjection().mul(projection.getViewMatrix());
        //Ближняя грань
        projection.setProjection(new Matrix4f().setPerspective(90.0f,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar()));
        projection.setView(position, position.add(new Vector3f(0.0f,0.0f,1.0f)),new Vector3f(0.0f,-1.0f,0.0f));
        lightSpaceMatrix[4] = projection.getProjection().mul(projection.getViewMatrix());
        //Дальняя грань
        projection.setProjection(new Matrix4f().setPerspective(90.0f,aspect, WindowConfig.getInstance().getNear(), WindowConfig.getInstance().getFar()));
        projection.setView(position, position.add(new Vector3f(0.0f,0.0f,-1.0f)),new Vector3f(0.0f,-1.0f,0.0f));
        lightSpaceMatrix[5] = projection.getProjection().mul(projection.getViewMatrix());
    }

    @Override
    public void updateLightSpaceMatrix(){
        createLightSpaceMatrix();
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    @Override
    public Vector3f getDirection() {
        return null;
    }

    @Override
    public void setDirection(Vector3f direction) {

    }

    @Override
    public Vector3f getStrength() {
        return strength;
    }

    @Override
    public void setStrength(Vector3f strength) {
        this.strength = strength;
    }

    @Override
    public Vector3f getAmbient() {
        return ambient;
    }

    @Override
    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    @Override
    public Vector3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public Vector3f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }

    @Override
    public Matrix4f[] getLightSpaceMatrix() {
        return lightSpaceMatrix;
    }

    @Override
    public void setLightSpaceMatrix(Matrix4f[] lightSpaceMatrix) {
        this.lightSpaceMatrix = lightSpaceMatrix;
    }

    @Override
    public Matrix4f getDirectLightViewMatrix() {
        return null;
    }

    @Override
    public void setDirectLightViewMatrix(Matrix4f directLightViewMatrix) {

    }
}
