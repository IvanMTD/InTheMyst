package ru.phoenix.game.scene;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface Scene {
    public void init();
    public void start();
    public void update();
    public void draw();
    public void draw(Shader shader);
    public void over();
    public int getId();
    public boolean isActive();
    public boolean isInit();
    public List<Light> getLights();
    public Shader getShader();
}
