package ru.phoenix.game.scene;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface Scene {
    void init();
    void start();
    void update();
    void draw();
    void draw(Shader shader, boolean isShadow);
    void over();
    int getId();
    boolean isActive();
    boolean isInit();
    List<Light> getLights();
    Shader getShader();
}
