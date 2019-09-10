package ru.phoenix.game.logic.lighting;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;

public interface Light {
    public void updateLightSpaceMatrix();

    public Vector3f getPosition();
    public void setPosition(Vector3f position);

    public Vector3f getDirection();
    public void setDirection(Vector3f direction);

    public Vector3f getStrength();
    public void setStrength(Vector3f strength);

    public Vector3f getAmbient();
    public void setAmbient(Vector3f ambient);

    public Vector3f getDiffuse();
    public void setDiffuse(Vector3f diffuse);

    public Vector3f getSpecular();
    public void setSpecular(Vector3f specular);

    public Matrix4f[] getLightSpaceMatrix();
    public void setLightSpaceMatrix(Matrix4f[] lightSpaceMatrix);

    public Matrix4f getDirectLightViewMatrix();
    public void setDirectLightViewMatrix(Matrix4f directLightViewMatrix);
}
