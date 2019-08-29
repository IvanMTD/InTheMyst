package ru.phoenix.game.scene.batlle;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.logic.generator.GraundGenerator;
import ru.phoenix.game.scene.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;

public class BattleScene implements Scene {

    private Shader shader3D;

    private boolean active;
    private boolean init;

    private BattleGraund battleGraund;

    public BattleScene(){
        active = false;
        init = false;
        shader3D = new Shader();
    }

    @Override
    public void init(){
        init = true;
        shader3D.createVertexShader("VS_game_object.glsl");
        shader3D.createFragmentShader("FS_game_object.glsl");
        shader3D.createProgram();
        battleGraund = GraundGenerator.generateMap(PLAIN_MAP);
        System.out.println("Map X: " + battleGraund.getMapX());
        System.out.println("Map Z: " + battleGraund.getMapZ());
    }

    @Override
    public void start(){
        active = true;
    }

    @Override
    public void update(){
        Camera.getInstance().update(battleGraund.getMapX(),battleGraund.getMapZ(),false);
        if(Input.getInstance().isPressed(GLFW_KEY_SPACE)){
            battleGraund = GraundGenerator.generateMap(PLAIN_MAP);
        }
    }

    @Override
    public void draw(){
        battleGraund.draw(shader3D);
    }

    @Override
    public void draw(Shader shader){

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
}
