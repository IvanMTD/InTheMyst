package ru.phoenix.game.scene.batlle;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.logic.generator.GraundGenerator;
import ru.phoenix.game.logic.lighting.DirectLight;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static ru.phoenix.core.config.Constants.MOUNTAIN_MAP;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;

public class BattleScene implements Scene {

    private Shader shader3D;

    private boolean active;
    private boolean init;

    private BattleGraund battleGraund;

    private boolean tapStop;

    private int index;

    private List<Light> lights;

    public BattleScene(){
        tapStop = false;
        active = false;
        init = false;
        shader3D = new Shader();
        index = 0;
        lights = new ArrayList<>();
    }

    @Override
    public void init(){
        init = true;
        shader3D.createVertexShader("VS_game_object.glsl");
        shader3D.createFragmentShader("FS_game_object.glsl");
        shader3D.createProgram();
        battleGraund = GraundGenerator.generateMap(PLAIN_MAP);

        Light directLight = new DirectLight(
                new Vector3f(5.0f,20.0f,5.0f), // position
                new Vector3f(0.2f,0.2f,0.2f), // ambient
                new Vector3f(1.0f,1.0f,1.0f), // diffuse
                new Vector3f(1.0f,1.0f,1.0f), // specular
                30
        );

        lights.add(directLight);
    }

    @Override
    public void start(){
        active = true;
    }

    @Override
    public void update(){
        Camera.getInstance().update(battleGraund.getMapX(),battleGraund.getMapZ(),false);

        boolean tap = false;

        if(!tapStop){
            tap = Input.getInstance().isPressed(GLFW_KEY_SPACE);
            tapStop = true;
        }

        if(!Input.getInstance().isPressed(GLFW_KEY_SPACE)){
            tapStop = false;
        }

        if(tap){
            if(index == 0) {

                lights.clear();
                Light directLight = new DirectLight(
                        new Vector3f(5.0f,20.0f,5.0f), // position
                        new Vector3f(0.2f,0.2f,0.2f), // ambient
                        new Vector3f(1.0f,1.0f,1.0f), // diffuse
                        new Vector3f(1.0f,1.0f,1.0f), // specular
                        30
                );

                lights.add(directLight);

                battleGraund = GraundGenerator.generateMap(MOUNTAIN_MAP);
            }else if(index == 1){

                lights.clear();
                float x = (float)((-battleGraund.getMapX() / 2) + Math.random() * ((battleGraund.getMapX() / 2) * 2));
                float y = (float)((-battleGraund.getMapZ() / 2) + Math.random() * ((battleGraund.getMapZ() / 2) * 2));
                Light directLight = new DirectLight(
                        new Vector3f(x,(float)(10.0f + Math.random() * 20.0f),y), // position
                        new Vector3f(0.2f,0.2f,0.2f), // ambient
                        new Vector3f(1.0f,1.0f,1.0f), // diffuse
                        new Vector3f(1.0f,1.0f,1.0f), // specular
                        30
                );

                lights.add(directLight);

                battleGraund = GraundGenerator.generateMap(PLAIN_MAP);
            }

            index++;
            if(index > 1){
                index = 0;
            }
        }
    }

    @Override
    public void draw(){
        battleGraund.draw(shader3D);
    }

    @Override
    public void draw(Shader shader){
        battleGraund.draw(shader);
    }

    @Override
    public void over(){
        active = false;
    }

    @Override
    public int getId(){
        return Constants.SCENE_BATTLE;
    }

    @Override
    public boolean isActive(){
        return active;
    }

    @Override
    public boolean isInit() {
        return init;
    }

    @Override
    public List<Light> getLights() {
        return lights;
    }

    @Override
    public Shader getShader() {
        return shader3D;
    }
}
