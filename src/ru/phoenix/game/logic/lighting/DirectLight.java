package ru.phoenix.game.logic.lighting;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;

public class DirectLight implements Light {
    private Vector3f position;

    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;

    private Matrix4f[] lightSpaceMatrix;
    private Matrix4f directLightViewMatrix;
    private int shadowMapSize;

    private int mapX;
    private int mapZ;

    public DirectLight(Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular, int shadowMapSize, int mapX, int mapZ){
        lightSpaceMatrix = new Matrix4f[]{
                new Matrix4f()
        };
        directLightViewMatrix = new Matrix4f();
        this.shadowMapSize = shadowMapSize;
        setPosition(position);
        setAmbient(ambient);
        setDiffuse(diffuse);
        setSpecular(specular);
        setMapX(mapX);
        setMapZ(mapZ);
        createLightSpaceMatrix();
    }

    private void createLightSpaceMatrix(){
        Projection projection = new Projection();
        projection.setOrtho(-shadowMapSize,shadowMapSize,-shadowMapSize,shadowMapSize, WindowConfig.getInstance().getNear(), mapX + mapZ);
        projection.setView(position,new Vector3f(mapX / 2,0.0f,mapZ / 2),new Vector3f(0.0f,1.0f,0.0f));
        directLightViewMatrix.setMatrix(projection.getViewMatrix());
        lightSpaceMatrix[0].setMatrix(projection.getProjection().mul(projection.getViewMatrix()));
    }

    @Override
    public void updateLightSpaceMatrix() {
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
        return null;
    }

    @Override
    public void setStrength(Vector3f strength) {

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

    @Override
    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public Vector3f getSpecular() {
        return specular;
    }

    @Override
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
        return directLightViewMatrix;
    }

    @Override
    public void setDirectLightViewMatrix(Matrix4f directLightViewMatrix) {
        this.directLightViewMatrix = directLightViewMatrix;
    }

    private void setMapX(int mapX) {
        this.mapX = mapX;
    }

    private void setMapZ(int mapZ) {
        this.mapZ = mapZ;
    }
}
