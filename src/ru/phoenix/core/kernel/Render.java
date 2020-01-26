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

    private float angle;
    private float hyp;

    private int textureNum;

    Render(){
        textureNum = 3;
        window = Window.getInstance();
        baseRenderFrame = new BaseRenderFrame(textureNum);
        shadowRenderFrame = new ShadowRenderFrame();
        mapRenderFrame = new MapRenderFrame();
        stopRender = false;
        firstStart = true;

        angle = 0.0f;
    }

    public void init(){
        baseRenderFrame.init();
        shadowRenderFrame.init();
        mapRenderFrame.init();
    }

    public void render(Scene scene){
        if(SceneControl.isReinit()){
            shadowRenderFrame = new ShadowRenderFrame();
            shadowRenderFrame.init();
            baseRenderFrame = new BaseRenderFrame(textureNum);
            baseRenderFrame.init();
            stopRender = true;
            SceneControl.setLoading(true);
        }

        Default.clearScreen();

        if (!stopRender) {
            if (scene.getLights() != null) {
                /*Vector3f center = new Vector3f((float)scene.getBattleGraund().getMapX() / 2.0f,0.0f,(float)scene.getBattleGraund().getMapZ()/2.0f);

                int width = scene.getBattleGraund().getMapX();
                int height = scene.getBattleGraund().getMapZ();
                float hyp = (float)Math.sqrt(width * width + height * height);

                float x = center.getX() + ((float)Math.sin(Math.toRadians(angle)) * (float)width / 2.0f);
                float y = hyp;
                float z = center.getZ() + ((float)Math.cos(Math.toRadians(angle)) * (float)height / 2.0f);

                scene.getBattleGraund().getDirectLight().get(0).setPosition(new Vector3f(x,y,z));
                scene.getBattleGraund().getDirectLight().get(0).updateLightSpaceMatrix();

                angle += 0.1f;
                if(angle > 360.0f){
                    angle = 0.0f;
                }*/

                shadowRenderFrame.draw(scene);
                baseRenderFrame.setFbo(shadowRenderFrame.getFbo(), 0);
            }
            mapRenderFrame.draw(scene);
            baseRenderFrame.setFbo(mapRenderFrame.getFbo(),1);
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
