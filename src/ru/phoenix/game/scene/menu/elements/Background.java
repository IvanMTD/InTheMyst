package ru.phoenix.game.scene.menu.elements;

import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.stage.mainmenu.BackgroundArea;

public class Background {
    private Skybox skybox;
    private BackgroundArea backgroundArea;

    public Background(){
        skybox = new Skybox();
        backgroundArea = new BackgroundArea();
    }

    public void init(){
        skybox.init();
        backgroundArea.init();
    }

    public void update(){
        backgroundArea.getStudyArea().update(null,new Vector3f());
    }

    public void draw(){
        backgroundArea.draw();
        skybox.draw();
    }

    public void draw(Shader shader){
        backgroundArea.draw(shader);
    }

    public BackgroundArea getBackgroundArea() {
        return backgroundArea;
    }
}
