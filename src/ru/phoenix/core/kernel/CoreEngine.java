package ru.phoenix.core.kernel;

import org.lwjgl.glfw.GLFWErrorCallback;
import ru.phoenix.core.buffer.ubo.ProjectionUniforms;
import ru.phoenix.core.buffer.ubo.UniformBufferObject;
import ru.phoenix.core.config.*;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;
import ru.phoenix.game.scene.batlle.BattleScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class CoreEngine {
    private Render render;
    private UniformBufferObject uboProjection;

    private List<Scene> scenes;

    private Scene battleScene;

    private static int fps;
    private static float framerate = 200;
    private static float frameTime = 1.0f/framerate;
    private boolean isRunning;

    public void createWindow(WindowConfig wc){
        // init glfw and window
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        Window.getInstance().create(wc.getWidth(), wc.getHeight(), wc.isFullScreen());
        // init vatiable
        render = new Render();
        uboProjection = new ProjectionUniforms();

        battleScene = new BattleScene();

        scenes = new ArrayList<Scene>();
    }

    public void init(){
        Default.init();
        render.init();
        uboProjection.allocate(0);
        scenes = Arrays.asList(battleScene);
    }

    public void start(){
        if(isRunning){
            return;
        }
        run();
    }

    private void run(){
        this.isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning){

            Scene currentScene = SceneControl.getCurrentScene(scenes);

            boolean render = false;

            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) Constants.NANOSECOND;
            frameCounter += passedTime;

            while(unprocessedTime > frameTime) {

                render = true;
                unprocessedTime -= frameTime;

                if (Window.getInstance().isCloseRequested()) {
                    stop();
                }

                update(currentScene);

                if(frameCounter >= Constants.NANOSECOND) {
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render) {
                render(currentScene);
                frames++;
            }else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        cleanUp();
    }

    private void stop() {
        if(!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void update(Scene scene) {
        Time.update();
        Window.getInstance().titleUpdate(getFps(), Time.getCurrentTime());
        Input.getInstance().update();
        if(scene.isInit()) {
            scene.update();
        }
        uboProjection.totalUpdate();
    }

    private void render(Scene scene){
        render.render(scene);
    }

    private void cleanUp() {
        Window.getInstance().dispose();
        glfwTerminate();
    }

    private static int getFps() {
        return fps;
    }

    private static void setFps(int fps) {
        CoreEngine.fps = fps;
    }
}
