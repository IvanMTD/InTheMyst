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

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.glCullFace;
import static ru.phoenix.core.config.Constants.ID_PERSON_GEHARD;
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

        // ГЕНЕРАЦИЯ
        battleGraund = GraundGenerator.useMapGenerator(PLAIN_MAP);

        Object person = new Person(ID_PERSON_GEHARD,GraundGenerator.getRandomPos());
        person.init(null);
        battleGraund.getSprites().add(person);
        Default.setWait(false);
        // КОНЕЦ ГЕНЕРАЦИИ
    }

    @Override
    public void start(){
        active = true;
    }

    @Override
    public void update(){
        // обновляем камеру
        Camera.getInstance().update(battleGraund.getMapX(),battleGraund.getMapZ(),battleGraund.getGridElements());

        // Обновляем информацию в сетке
        for(GridElement element : battleGraund.getGridElements()){
            if(element.isVisible()){
                float distance = new Vector3f(Camera.getInstance().getPos().sub(element.getPosition())).length();
                element.setDistance(distance);
                element.update();
            }
        }

        battleGraund.getGridElements().sort((o1, o2) -> o1.getDistance() < o2.getDistance() ? 0 : -1);

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
                Object person = new Person(ID_PERSON_GEHARD,GraundGenerator.getRandomPos());
                person.init(null);
                battleGraund.getSprites().add(person);
                Default.setWait(false);
            }else if(index == 1){
                battleGraund = GraundGenerator.useMapGenerator(PLAIN_MAP);
                Object person = new Person(ID_PERSON_GEHARD,GraundGenerator.getRandomPos());
                person.init(null);
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
        List<GridElement>temp = new ArrayList<>();
        List<GridElement>temp2 = new ArrayList<>();
        // рисуем поле и сетку
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CCW);
        glCullFace(GL_BACK);
        battleGraund.draw(shader3D);

        shaderSprite.useProgram();
        shaderSprite.setUniformBlock("matrices", 0);
        for(GridElement element : battleGraund.getGridElements()){
            if(element.isVisible()){
                if(element.getPosition().getY() == element.getCurrentHeight()){
                    element.draw(shaderSprite);
                }else{
                    if(element.getCurrentHeight() >= -0.5f) {
                        temp.add(element);
                    }else{
                        temp2.add(element);
                    }
                }
            }
        }
        glDisable(GL_CULL_FACE);
        // рисуем спрайты и воду
        battleGraund.drawSprites(shaderSprite);
        if(!temp2.isEmpty()){
            for(GridElement element : temp2){
                element.draw(shaderSprite);
            }
        }
        battleGraund.drawWater(shaderSprite);
        if(!temp.isEmpty()){
            for(GridElement element : temp){
                element.draw(shaderSprite);
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