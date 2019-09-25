package ru.phoenix.game.logic.lighting;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;

public interface Light {
    void updateLightSpaceMatrix();

    Vector3f getPosition();
    void setPosition(Vector3f position);

    Vector3f getDirection();
    void setDirection(Vector3f direction);

    Vector3f getStrength();
    void setStrength(Vector3f strength);

    Vector3f getAmbient();
    void setAmbient(Vector3f ambient);

    Vector3f getDiffuse();
    void setDiffuse(Vector3f diffuse);

    Vector3f getSpecular();
    void setSpecular(Vector3f specular);

    Matrix4f[] getLightSpaceMatrix();
    void setLightSpaceMatrix(Matrix4f[] lightSpaceMatrix);

    Matrix4f getDirectLightViewMatrix();
    void setDirectLightViewMatrix(Matrix4f directLightViewMatrix);
}
