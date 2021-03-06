package ru.phoenix.game.hud.instance;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

public interface Board {
    void draw(Shader shader);
    void update(Vector3f currentPos);
    // control
    boolean isVisible();
    void setVisible(boolean visible);
    float getDiscatdControl();
    void setDiscatdControl(float discatdControl);
    boolean isDiscardReverse();
    void setDiscardReverse(boolean discardReverse);
}
