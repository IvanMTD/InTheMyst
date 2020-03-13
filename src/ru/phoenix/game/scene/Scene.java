package ru.phoenix.game.scene;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface Scene {
    void start();
    void over();
    void update();
    void draw();
    void draw(Shader shader, boolean isShadow);
    int getSceneId();
    boolean isActive();
    List<Light> getLights();
    Shader getShader();
}
