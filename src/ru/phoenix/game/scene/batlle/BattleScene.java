package ru.phoenix.game.scene.batlle;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.active.Person;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.logic.generator.GraundGenerator;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.scene.Scene;

import java.util.Comparator;
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

    private boolean switchControl;
    private float count;

    public BattleScene(){
        tapStop = false;
        active = false;
        init = false;
        shader3D = new Shader();
        shaderSprite = new Shader();
        index = 0;
        switchControl = false;
        count = 0.0f;
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

        Object person = new Person(Constants.ID_PERSON_GEHARD);
        person.init(null);
        Vector3f pos = GraundGenerator.getRandomPos();
        pos = new Vector3f(pos.getX(),pos.getY(),pos.getZ());
        person.setPosition(pos);
        battleGraund.getSprites().add(person);
        Default.setWait(false);
    }

    @Override
    public void start(){
        active = true;
    }

    @Override
    public void update(){
        // обновляем камеру
        Camera.getInstance().update(battleGraund.getMapX(),battleGraund.getMapZ(),false);

        // Обновляем информацию в сетке
        for(GridElement element : battleGraund.getGridElements()){
            if(element.isVisible()){
                float distance = new Vector3f(Camera.getInstance().getPos().sub(element.getPosition())).length();
                element.setDistance(distance);
                element.update();
            }
        }

        battleGraund.getGridElements().sort(new Comparator<GridElement>() {
            @Override
            public int compare(GridElement o1, GridElement o2) {
                return o1.getDistance() < o2.getDistance() ? 0 : -1;
            }
        });

        GraundGenerator.updateGridElements(battleGraund.getGridElements());

        // обнавляем движение волн
        if(switchControl){
            count+=0.0005f;
            Default.setOffset(count);
            if(count >= 0.05f){
                switchControl = !switchControl;
            }
        }else{
            count-=0.0005f;
            Default.setOffset(count);
            if(count <= -0.05f){
                switchControl = !switchControl;
            }
        }
        // обновляем все объекты сцены
        battleGraund.update();

        // ТЕСТОВЫЙ ТРИГЕР!!!
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
                Object person = new Person(Constants.ID_PERSON_GEHARD);
                person.init(null);
                Vector3f pos = GraundGenerator.getRandomPos();
                pos = new Vector3f(pos.getX(),pos.getY(),pos.getZ());
                person.setPosition(pos);
                battleGraund.getSprites().add(person);
                Default.setWait(false);
            }else if(index == 1){
                battleGraund = GraundGenerator.useMapGenerator(PLAIN_MAP);
                Object person = new Person(Constants.ID_PERSON_GEHARD);
                person.init(null);
                Vector3f pos = GraundGenerator.getRandomPos();
                pos = new Vector3f(pos.getX(),pos.getY(),pos.getZ());
                person.setPosition(pos);
                battleGraund.getSprites().add(person);
                Default.setWait(false);
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
        for(GridElement grid : battleGraund.getGridElements()){
            if(grid.getCurrentHeight() < -0.5f) {
                grid.draw(shaderSprite);
            }
        }
        battleGraund.drawSprites(shaderSprite);
        battleGraund.drawWater(shaderSprite);
        for(GridElement grid : battleGraund.getGridElements()){
            if(grid.getCurrentHeight() >= -0.5f) {
                grid.draw(shaderSprite);
            }
        }
    }

    @Override
    public void draw(Shader shader){
        battleGraund.draw(shader);
        battleGraund.drawShadowSprites(shader);
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
