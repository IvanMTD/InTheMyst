package ru.phoenix.game.scene.menu;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.property.GameController;
import ru.phoenix.game.scene.Scene;
import ru.phoenix.game.scene.menu.elements.Background;
import ru.phoenix.game.scene.menu.struct.MainMenu;
import ru.phoenix.game.scene.menu.struct.SettingsMenu;

import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static ru.phoenix.game.scene.menu.struct.MainMenu.*;
import static ru.phoenix.game.scene.menu.struct.MainMenu.NEW_GAME_BUTTON;
import static ru.phoenix.game.scene.menu.struct.SettingsMenu.BACK_BUTTON;
import static ru.phoenix.game.scene.menu.struct.SettingsMenu.OK_BUTTON;

public class MenuScene implements Scene {
    private Shader shader;
    private List<Scene> scenes;

    private Cursor cursor;
    private Background background;
    private MainMenu mainMenu;
    private SettingsMenu settingsMenu;

    private boolean active;
    private boolean init;

    // Переменные метода анимации
    private float tempGamma;
    private float timer;
    private float lastTime;
    private boolean over;
    private boolean reverse;

    // Переменные контроля меню
    private float menuAction;
    private float settingsAction;

    public MenuScene() {
        shader = new Shader();

        active = false;
        init = false;

        // переменные метода анимации
        tempGamma = Window.getInstance().getGamma();
        timer = 0.0f;
        lastTime = 0.0f;
        over = false;
        reverse = false;

        // переменые контроля меню
        menuAction = NO_ACTION;
        settingsAction = NO_ACTION;
    }

    public void init(){
        Camera.getInstance().preset(25.0f,-90.0f,-10.0f);
        Camera.getInstance().setPos(new Vector3f(25.0f,27.0f,59.0f));
        Camera.getInstance().setFront(new Vector3f(0.0f,0.0f,-1.0f));
        Camera.getInstance().updateViewMatrix();
        // классы
        if(!init) {
            shader.createVertexShader("VS_hud.glsl");
            shader.createFragmentShader("FS_hud.glsl");
            shader.createProgram();

            cursor = new Cursor();
            cursor.init();

            background = new Background();
            background.init();

            mainMenu = new MainMenu();
            settingsMenu = new SettingsMenu();

            init = true;
        }
    }

    @Override
    public void start(List<Scene> scenes){
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void reInit() {
        Camera.getInstance().preset(25.0f,-90.0f,-10.0f);
        Camera.getInstance().setPos(new Vector3f(25.0f,27.0f,59.0f));
        Camera.getInstance().setFront(new Vector3f(0.0f,0.0f,-1.0f));
        Camera.getInstance().updateViewMatrix();
        mainMenu = new MainMenu();
        settingsMenu = new SettingsMenu();
        over = false;
        reverse = false;
        menuAction = NO_ACTION;
        settingsAction = NO_ACTION;
    }

    @Override
    public void over() {
        active = false;
        menuAction = NO_ACTION;
        settingsAction = NO_ACTION;
        timer = 0.0f;
        lastTime = 0.0f;
        over = false;
        reverse = false;
    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {

    }

    @Override
    public void update() {
        // обработка данных
        GameController.getInstance().update();
        Vector3f pixel = Pixel.getPixel();
        boolean leftClick = GameController.getInstance().isLeftClick();
        // постоянно обновляемые
        background.update();
        cursor.update(null);
        // контролируемое обновление
        if(menuAction == NO_ACTION){
            mainMenu.update(pixel,leftClick);
            menuAction = mainMenu.getAction();
        }else if(menuAction == NEW_GAME_BUTTON){
            newGameAnimation();
        }else if(menuAction == LOADING_BUTTON){

        }else if(menuAction == SETTINGS_BUTTON){
            if(settingsAnimation(reverse)){
                if(reverse){
                    settingsAction = NO_ACTION;
                    menuAction = NO_ACTION;
                    over = false;
                    reverse = false;
                }else {
                    if (settingsAction == NO_ACTION) {
                        settingsMenu.update(pixel, leftClick);
                        settingsAction = settingsMenu.getAction();
                    } else if (settingsAction == OK_BUTTON) {
                        over = false;
                        reverse = true;
                    } else if (settingsAction == BACK_BUTTON) {
                        over = false;
                        reverse = true;
                    }
                }
            }
        }
    }

    @Override
    public void draw() {
        background.draw();
        cursor.draw();
        // hud
        shader.useProgram();
        mainMenu.draw(shader);
        settingsMenu.draw(shader);
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
        return background.getBackgroundArea().getStudyArea().getDirectLight();
    }

    @Override
    public Shader getShader() {
        return background.getBackgroundArea().getShader3D();
    }

    private void newGameAnimation(){
        if(lastTime == 0.0f){
            lastTime = (float)glfwGetTime();
        }
        float currentTime = (float)glfwGetTime();
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        timer += deltaTime;
        Camera.getInstance().setPos(Camera.getInstance().getPos().add(new Vector3f(0.01f,0.0f,0.0f)));
        Camera.getInstance().updateViewMatrix();
        mainMenu.update(-2.0f);
        float g = Window.getInstance().getGamma() - 0.0025f;
        if(g < 0){
            g = 0.0f;
        }
        Window.getInstance().setGamma(g);
        if(timer > 7.0f){
            nextScene();
            timer = 0.0f;
            lastTime = 0.0f;
            Window.getInstance().setGamma(tempGamma);
        }
    }

    private boolean settingsAnimation(boolean reverse){
        if(!over){
            if(lastTime == 0.0f){
                lastTime = (float)glfwGetTime();
            }
            float currentTime = (float)glfwGetTime();
            float deltaTime = 0.01f;//currentTime - lastTime;
            lastTime = currentTime;
            timer += deltaTime;

            float S = Window.getInstance().getWidth();
            float t = 1.0f;

            float timePercent = deltaTime * 100.0f / t;
            float v = timePercent * S / 100.0f;

            float menuOffset = v;
            float xOffset = -1 * (v / 200.0f);
            if(reverse){
                xOffset = -xOffset;
                menuOffset = -menuOffset;
            }
            Camera.getInstance().setPos(Camera.getInstance().getPos().add(new Vector3f(xOffset,0.0f,0.0f)));
            Camera.getInstance().updateViewMatrix();
            mainMenu.update(menuOffset);
            settingsMenu.update(menuOffset);
            if(timer > t){
                timer = 0.0f;
                lastTime = 0.0f;
                over = true;
            }
        }
        return over;
    }

    private void nextScene(){
        SceneControl.setLastScene(this);
        for (Scene scene : scenes) {
            if (scene.getSceneId() == Constants.SCENE_STRATEGIC) {
                scene.start(scenes);
            }
        }
        over();
    }
}
