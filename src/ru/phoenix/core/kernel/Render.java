package ru.phoenix.core.kernel;

import ru.phoenix.core.config.*;
import ru.phoenix.core.frame.BaseRenderFrame;
import ru.phoenix.core.frame.Framework;
import ru.phoenix.core.frame.ShadowRenderFrame;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Render {
    private Window window;

    private Framework baseRenderFrame;
    private Framework shadowRenderFrame;

    private boolean stopRender;
    private boolean firstStart;
    private float lastTime;
    private float timer;

    Render(){
        window = Window.getInstance();
        baseRenderFrame = new BaseRenderFrame(3);
        shadowRenderFrame = new ShadowRenderFrame();
        stopRender = false;
        firstStart = true;
    }

    public void init(){
        baseRenderFrame.init();
        shadowRenderFrame.init();
    }

    public void render(Scene scene){
        if(SceneControl.isReinit()){
            shadowRenderFrame = new ShadowRenderFrame();
            shadowRenderFrame.init();
            baseRenderFrame = new BaseRenderFrame(3);
            baseRenderFrame.init();
            stopRender = true;
            SceneControl.setLoading(true);
        }

        Default.clearScreen();

        if (!stopRender) {
            if (scene.getLights() != null) {
                shadowRenderFrame.draw(scene);
                baseRenderFrame.setFbo(shadowRenderFrame.getFbo());
            }
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
