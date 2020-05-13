package ru.phoenix.game.hud.elements;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

public interface HeadsUpDisplay {
    void update(Vector3f pixel);
    void selected();
    void draw(Shader shader);

    Vector3f getPosition();
    void setPosition(Vector3f position);
    float getId();
    boolean isTarget();
}
