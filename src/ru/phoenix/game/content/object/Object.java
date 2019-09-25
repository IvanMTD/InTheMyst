package ru.phoenix.game.content.object;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.element.GridElement;

import java.util.List;

public interface Object {

    void init(Matrix4f[] matrix);
    void update(List<GridElement> gridElements);
    List<Texture> getTextures();

    float getDistance();
    void setDistance(float distance);
    Vector3f getPosition();
    void setPosition(Vector3f position);
    float getId();
    boolean isOnTarget();
    boolean isInstance();
    boolean isShadow();
    boolean isActive();
    void draw(Shader shader);
    boolean isBoard();
    void setProjection(Projection projection);
}
