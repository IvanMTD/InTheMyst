package ru.phoenix.core.kernel;

import ru.phoenix.core.config.*;
import ru.phoenix.core.frame.BaseRenderFrame;
import ru.phoenix.core.frame.Framework;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Render {
    private Window window;

    private Framework baseRenderFrame;

    private boolean stopRender;
    private boolean firstStart;
    private float lastTime;
    private float timer;

    Render(){
        window = Window.getInstance();
        baseRenderFrame = new BaseRenderFrame(3);
        stopRender = false;
        firstStart = true;
    }

    public void init(){
        baseRenderFrame.init();
    }

    public void render(Scene scene){
        if(SceneControl.isReinit()){
            baseRenderFrame = new BaseRenderFrame(3);
            baseRenderFrame.init();
            stopRender = true;
            SceneControl.setLoading(true);
        }

        Default.clearScreen();

        if (!stopRender) {
            baseRenderFrame.draw(scene);
        } else {
            if (firstStart) {
                lastTime = (float) glfwGetTime();
                timer = 0.0f;
                firstStart = false;
            }
            float currentTime = (float) glfwGetTime();
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;
            timer += deltaTime;
            if (timer > 5.0f) {
                firstStart = true;
                stopRender = false;
            }
        }

        window.render();
    }
}
