package ru.phoenix.game.scene.batlle;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.logic.generator.GraundGenerator;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static ru.phoenix.core.config.Constants.MOUNTAIN_MAP;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;

public class BattleScene implements Scene {

    private Shader shader3D;
    private Shader shaderSprite;

    private boolean active;
    private boolean init;

    private BattleGraund battleGraund;

    private boolean tapStop;

    private int index;

    public BattleScene(){
        tapStop = false;
        active = false;
        init = false;
        shader3D = new Shader();
        shaderSprite = new Shader();
        index = 0;
    }

    @Override
    public void init(){
        init = true;

        shader3D.createVertexShader("VS_game_object.glsl");
        shader3D.createFragmentShader("FS_game_object.glsl");
        shader3D.createProgram();

        shaderSprite.createVertexShader("VS_sprite.glsl");
        shaderSprite.createFragmentShader("FS_sprite.glsl");
        shaderSprite.createProgram();

        battleGraund = GraundGenerator.useMapGenerator(PLAIN_MAP);
    }

    @Override
    public void start(){
        active = true;
    }

    @Override
    public void update(){
        Camera.getInstance().update(battleGraund.getMapX(),battleGraund.getMapZ(),false, battleGraund.getGridElements());

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
                battleGraund = GraundGenerator.useMapGenerator(MOUNTAIN_MAP);
            }else if(index == 1){
                battleGraund = GraundGenerator.useMapGenerator(PLAIN_MAP);
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
        battleGraund.drawSprites(shaderSprite);
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
        return battleGraund.getDirectLight();
    }

    @Override
    public Shader getShader() {
        return shader3D;
    }
}
