package ru.phoenix.game.content.object;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.generator.component.GridElement;

import java.util.List;

public interface Object {

    public void init(Matrix4f[] matrix);
    public void update();
    public List<Texture> getTextures();

    public float getDistance();
    public void setDistance(float distance);
    public Vector3f getPosition();
    public void setPosition(Vector3f position);
    public float getId();
    public boolean isOnTarget();
    public boolean isInstance();
    public boolean isShadow();
    public boolean isActive();
    public void draw(Shader shader);
    public boolean isBoard();
    public void setProjection(Projection projection);
}
