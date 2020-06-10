package ru.phoenix.game.scene.cut;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.SCENE_CUT;

public class CutScene implements Scene {

    public static final int FIRST_SCENE = 0x15100;
    public static final int LAGER_SCENE = 0x15100;

    private int sceneControl;

    private List<Scene> scenes;
    private List<Character> allies;
    private float currentHeight;

    private Skybox skybox;
    private Cursor cursor;

    private Shader shader3D;
    private Shader shaderSprite;

    private boolean active;
    private boolean init;

    private StudyArea studyArea;

    public CutScene(int sceneControl){
        active = false;
        init = false;

        this.sceneControl = sceneControl;

        scenes = new ArrayList<>();
        allies = new ArrayList<>();

        skybox = new Skybox();
        cursor = new Cursor();
    }

    public void init(){
        if(sceneControl == FIRST_SCENE){
            Camera.getInstance().preset(20.0f,46.0f,-30.0f);
            if(init){

            }else{
                shader3D = getIniShader("game_object.glsl");
                shaderSprite = getIniShader("sprite.glsl");
                skybox.init();
                cursor.init();
                init = true;
            }
        }else if(sceneControl == LAGER_SCENE){

        }
    }

    private Shader getIniShader(String name){
        Shader shader = new Shader();
        shader.createVertexShader("VS_" + name);
        shader.createGeometryShader("GS_" + name);
        shader.createFragmentShader("FS_" + name);
        shader.createProgram();
        return shader;
    }

    @Override
    public void start(List<Scene> scenes) {
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void reInit() {

    }

    @Override
    public void over() {

    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {
        this.currentHeight = currentHeight;
        this.allies = allies;
        for(int i=0; i<this.allies.size(); i++){
            if(this.allies.get(i).isDead()){
                this.allies.remove(i);
                i--;
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {

    }

    @Override
    public void draw(Shader shader, boolean isShadow) {

    }

    @Override
    public int getSceneId() {
        return SCENE_CUT;
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
