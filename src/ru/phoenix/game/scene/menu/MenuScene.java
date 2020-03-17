package ru.phoenix.game.scene.menu;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;

import java.util.List;

public class MenuScene implements Scene {
    private List<Scene> scenes;

    private boolean active;

    public MenuScene() {
        active = false;
    }

    public void init(){

    }

    @Override
    public void start(List<Scene> scenes){
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void over() {
        active = false;
    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {

    }

    @Override
    public void update() {
        SceneControl.setLastScene(this);
        for(Scene scene : scenes){
            if(scene.getSceneId() == Constants.SCENE_STRATEGIC){
                scene.start(scenes);
            }
        }
        over();
    }

    @Override
    public void draw() {

    }

    @Override
    public void draw(Shader shader, boolean isShadow) {

    }

    @Override
    public int getSceneId() {
        return Constants.SCENE_MAIN_MENU;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public List<Light> getLights() {
        return null;
    }

    @Override
    public Shader getShader() {
        return null;
    }
}
