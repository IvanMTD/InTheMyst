package ru.phoenix.game.hud.elements;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

public interface Hud {
    void update(Vector3f pixel, boolean leftClick);
    void draw(Shader shader);

    Vector3f getPosition();
    void setPosition(Vector3f position);
    boolean isOnTarget();
    void setOnTarget(boolean onTarget);
    boolean isSelected();
    void setSelected(boolean selected);
    float getId();

}
