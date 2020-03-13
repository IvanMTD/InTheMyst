package ru.phoenix.core.kernel;

import ru.phoenix.core.config.*;
import ru.phoenix.core.frame.BaseRenderFrame;
import ru.phoenix.core.frame.Framework;
import ru.phoenix.core.frame.MapRenderFrame;
import ru.phoenix.core.frame.ShadowRenderFrame;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Render {
    private Window window;

    private Framework baseRenderFrame;
    private Framework shadowRenderFrame;
    private Framework mapRenderFrame;

    private boolean stopRender;
    private boolean firstStart;
    private float lastTime;
    private float timer;

    private int textureNum;

    private int index;

    Render(){
        textureNum = 3;
        window = Window.getInstance();
        baseRenderFrame = new BaseRenderFrame(textureNum);
        shadowRenderFrame = new ShadowRenderFrame();
        mapRenderFrame = new MapRenderFrame();
        stopRender = false;
        firstStart = true;
    }

    public void init(){
        baseRenderFrame.init();
        shadowRenderFrame.init();
        mapRenderFrame.init();
        index = 0;
    }

    public void render(Scene scene){
        if(SceneControl.isReinit()){
            shadowRenderFrame = new ShadowRenderFrame();
            shadowRenderFrame.init();
            baseRenderFrame = new BaseRenderFrame(textureNum);
            baseRenderFrame.init();
            mapRenderFrame = new MapRenderFrame();
            mapRenderFrame.init();
            stopRender = true;
            SceneControl.setLoading(true);
        }

        Default.clearScreen();

        if (!stopRender) {
            // рисуем карту теней и карту тумана войны
            if(scene.getSceneId() == Constants.SCENE_TACTICAL) {
                if (index == 0) {
                    shadowRenderFrame.draw(scene);
                } else {
                    mapRenderFrame.draw(scene);
                }
                // переводим текстуры в основной рендер
                baseRenderFrame.setFbo(shadowRenderFrame.getFbo(), 0);
                baseRenderFrame.setFbo(mapRenderFrame.getFbo(), 1);
            }
            // рисуем основной рендер
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

        index++;
        if(index > 1){
            index = 0;
        }
    }
}
