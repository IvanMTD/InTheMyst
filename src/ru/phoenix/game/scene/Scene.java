package ru.phoenix.game.scene;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface Scene {
    void start(List<Scene> scenes);
    void over();
    void preset(float currentHeight, List<Character> allies);
    void update();
    void draw();
    void draw(Shader shader, boolean isShadow);
    int getSceneId();
    boolean isActive();
    List<Light> getLights();
    Shader getShader();
}
