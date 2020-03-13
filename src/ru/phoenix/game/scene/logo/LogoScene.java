package ru.phoenix.game.scene.logo;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;
import ru.phoenix.game.scene.logo.component.LogoBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class LogoScene implements Scene {
    private final String MAIN_LOGO_PATH = "./data/logo/cat_logo_683.png";
    private final String ENGINE_LOGO_PATH = "./data/logo/phoenix_version_logo.png";

    private Shader shader;

    private List<LogoBoard> boards;
    private LogoBoard mainLogo;
    private LogoBoard engineLogo;

    private boolean active;

    public LogoScene(){
        active = false;
        shader = new Shader();
        mainLogo = new LogoBoard(true);
        engineLogo = new LogoBoard(false);
        boards = new ArrayList<LogoBoard>();
    }

    public void init(){
        // init shader
        shader.createVertexShader("VS_hud.glsl");
        shader.createFragmentShader("FS_hud.glsl");
        shader.createProgram();
        // main logo init
        Vector3f pos = new Vector3f();
        pos.setX(Window.getInstance().getWidth()/2);
        pos.setY(Window.getInstance().getHeight()/2);
        pos.setZ(0.0f);
        mainLogo.init(MAIN_LOGO_PATH, pos, 45.0f);
        // engine logo init
        engineLogo.init(ENGINE_LOGO_PATH, new Vector3f(),8.0f);
        float x = engineLogo.getWidth();
        float y = engineLogo.getHeight();
        pos.setX(3.0f + (x / 2));
        pos.setY((Window.getInstance().getHeight() - 3.0f) - (y / 2));
        pos.setZ(0.0f);
        engineLogo.getProjection().getModelMatrix().identity();
        engineLogo.getProjection().setTranslation(pos);
        engineLogo.setPosition(pos);

        boards = Arrays.asList(mainLogo, engineLogo);
        SceneControl.setLastScene(this);
    }

    @Override
    public void start() {
        init();
        active = true;
    }

    @Override
    public void over() {
        active = false;
    }

    @Override
    public void update(){
        if (Time.getSecond() > 10.0f || Input.getInstance().isPressed(GLFW_KEY_SPACE) || Input.getInstance().isPressed(GLFW_KEY_ESCAPE)) {
            SceneControl.setLastScene(this);
            SceneControl.setLogo(false);
            SceneControl.setMainMenu(true);
            over();
        }
    }

    @Override
    public void draw(){
        for(LogoBoard board : boards){
            board.draw(shader);
        }
    }

    @Override
    public void draw(Shader shader, boolean isShadow) {

    }

    @Override
    public int getSceneId() {
        return Constants.SCENE_LOGO;
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
        return shader;
    }
}
