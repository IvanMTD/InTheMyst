package ru.phoenix.core.kernel;

import org.lwjgl.glfw.GLFWErrorCallback;
import ru.phoenix.core.buffer.ubo.ProjectionUniforms;
import ru.phoenix.core.buffer.ubo.UniformBufferObject;
import ru.phoenix.core.config.*;
import ru.phoenix.core.debug.Logger;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;
import ru.phoenix.game.scene.cut.CutScene;
import ru.phoenix.game.scene.logo.LogoScene;
import ru.phoenix.game.scene.menu.MenuScene;
import ru.phoenix.game.scene.strategy.StrategyScene;
import ru.phoenix.game.scene.tactic.TacticalScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class CoreEngine {
    private Render render;
    private UniformBufferObject uboProjection;

    private Scene logoScene;
    private Scene menuScene;
    private Scene cutScene;
    private Scene strategyScene;
    private Scene tacticalScene;

    private List<Scene> scenes;

    private static int fps;
    private static float framerate = 200;
    private static float frameTime = 1.0f/framerate;
    private boolean isRunning;

    public void createWindow(WindowConfig wc){
        // init glfw and window
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()){
            Logger.error("Unable to initialize GLFW");
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        Window.getInstance().create(wc.getWidth(), wc.getHeight(), wc.isFullScreen());
        // init vatiable
        render = new Render();
        uboProjection = new ProjectionUniforms();

        logoScene = new LogoScene();
        menuScene = new MenuScene();
        cutScene = new CutScene();
        strategyScene = new StrategyScene();
        tacticalScene = new TacticalScene();

        scenes = new ArrayList<>();
        Logger.info("Engine initialized, scenes created");
    }

    public void init(){
        Default.init();
        render.init();
        uboProjection.allocate(0);
        scenes = Arrays.asList(logoScene,menuScene,cutScene,strategyScene,tacticalScene);
        logoScene.start(scenes);
        Logger.info("Engine init complete, starting logo scene");
    }

    public void start(){
        if(isRunning){
            return;
        }
        run();
    }

    private void run(){
        this.isRunning = true;
        Logger.info("Game loop started");

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning){
            Scene currentScene = SceneControl.getCurrentScene(scenes);

            if(SceneControl.isReinit()){
                Logger.warn("Reinitializing engine and scene...");
                render = new Render();
                render.init();
                currentScene.reInit();
                SceneControl.setReinit(false);
            }else {

                boolean render = false;

                long startTime = System.nanoTime();
                long passedTime = startTime - lastTime;
                lastTime = startTime;

                unprocessedTime += passedTime / (double) Constants.NANOSECOND;
                frameCounter += passedTime;

                while (unprocessedTime > frameTime) {

                    render = true;
                    unprocessedTime -= frameTime;

                    if (Window.getInstance().isCloseRequested()) {
                        Logger.info("Window close requested, stopping...");
                        stop();
                    }

                    update(currentScene);

                    if (frameCounter >= Constants.NANOSECOND) {
                        setFps(frames);
                        frames = 0;
                        frameCounter = 0;
                    }
                }

                if (render) {
                    render(currentScene);
                    frames++;
                } else {
                    // Убрали Thread.sleep для более плавного цикла
                    // Если CPU загрузка слишком высокая, можно добавить короткую паузу
                    // Thread.yield() вместо sleep для лучшей отзывчивости
                    Thread.yield();
                }
            }
        }

        Logger.info("Game loop finished, cleaning up...");
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
        if (scene.isActive()) {
            scene.update();
        }
        uboProjection.totalUpdate();
    }

    private void render(Scene scene){
        render.render(scene);
    }

    private void cleanUp() {
        Logger.info("Cleaning up engine resources...");
        Window.getInstance().dispose();
        glfwTerminate();
        Logger.shutdown();
    }

    public static int getFps() {
        return fps;
    }

    private static void setFps(int fps) {
        CoreEngine.fps = fps;
    }
}
