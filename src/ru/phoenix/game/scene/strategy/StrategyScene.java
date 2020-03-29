package ru.phoenix.game.scene.strategy;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisPartisan;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;
import ru.phoenix.game.content.stage.strategy.StrategicScreen;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.ALLY;

public class StrategyScene implements Scene {
    private StrategicScreen strategicScreen;
    // обще игровые переменные
    private List<Character> allies;
    private List<Scene> scenes;
    // переменные контроля игры
    private int days;
    private float[] heights;
    private int currentDay;
    // контроль сцены
    private boolean active;
    private boolean init;
    // скайбокс
    private Skybox skybox;

    public StrategyScene() {
        strategicScreen = new StrategicScreen();
        skybox = new Skybox();
        allies = new ArrayList<>();
        scenes = new ArrayList<>();
        active = false;
        init = false;
    }

    public void init(){
        Camera.getInstance().preset(45.0f,-90.0f,-10.0f);
        Camera.getInstance().setPos(new Vector3f(100.0f,10.0f,200.0f));
        Camera.getInstance().setFront(new Vector3f(0.0f,0.0f,1.0f));
        Camera.getInstance().updateViewMatrix();
        if(!init){
            init = true;
            createAllies();
            createHeights();
            skybox.init();
            strategicScreen.init();
        }
    }

    private void createAllies(){
        Character mainCharacter = new Gehard();
        mainCharacter.setRecognition(ALLY);
        mainCharacter.preset();
        mainCharacter.setDefaultCharacteristic();
        allies.add(mainCharacter);

        //TEST
        int random = 1 + (int)Math.round(Math.random() * 4.0f);
        for(int i=0; i<random; i++){
            int chance = (int)Math.round(Math.random() * 10.0f);
            if(chance < 5){
                Character character = new CommunisPartisan();
                character.setRecognition(ALLY);
                character.preset();
                character.setDefaultCharacteristic();
                allies.add(character);
            }else{
                Character character = new CommunisArcher();
                character.setRecognition(ALLY);
                character.preset();
                character.setDefaultCharacteristic();
                allies.add(character);
            }
        }
        //TEST
    }

    private void createHeights(){
        days = 100;
        currentDay = 0;
        heights = new float[days];
        long seed = (long)(1 + Math.random() * 10000000000L);
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 20.0f;
        float dayMap[][] = new float[days][days];
        for(int x=0; x<days; x++){
            for(int y=0; y<days; y++){
                float value = perlin.getNoise(x/accuracy,y/accuracy,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                dayMap[x][y] = result;
            }
        }
        float min = dayMap[days / 2][0];
        float max = dayMap[days / 2][0];
        for (int i=0; i<days; i++){
            float num = dayMap[days / 2][i];
            if(num < min){
                min = num;
            }
            if(num > max){
                max = num;
            }
            heights[i] = num;
        }

        float diff = Math.abs(max - min);

        for(int i=0; i<days; i++){
            float height = heights[i] - min;
            float percent = height * 100.0f / diff;
            float newHeight = 1.0f * percent / 100.0f;
            heights[i] = newHeight * 50.0f;
        }
    }

    @Override
    public void start(List<Scene> scenes){
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void over() {
        active = false;
    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {
        this.allies = allies;
    }

    @Override
    public void update() {
        Camera.getInstance().update(0.0f,200.0f,0.0f,200.0f,null);
        //strategicScreen.update();
        /*SceneControl.setLastScene(this);
        for(Scene scene : scenes){
            if(scene.getSceneId() == Constants.SCENE_TACTICAL){
                float num = getHeight((float)(Math.random() * 50.0f));//getHeight(heights[currentDay]);
                scene.preset(getHeight(heights[currentDay]),allies);
                scene.start(scenes);
            }
        }
        over();
        currentDay++;
        if(currentDay > 100){
            currentDay = 0;
        }*/
    }

    private float getHeight(float h){
        float result = 0.0f;

        if(h < 5.0f){
            result = 5.0f;
        }else if(h < 10.0f){
            result = 10.0f;
        }else if(h < 15.0f){
            result = 15.0f;
        }else if(h < 20.0f){
            result = 20.0f;
        }else if(h < 25.0f){
            result = 25.0f;
        }else if(h < 30.0f){
            result = 30.0f;
        }else if(h < 35.0f){
            result = 35.0f;
        }else if(h < 40.0f){
            result = 40.0f;
        }else{
            result = 45.0f;
        }

        return result;
    }

    @Override
    public void draw() {
        strategicScreen.draw();
        skybox.draw();
    }

    @Override
    public void draw(Shader shader, boolean isShadow) {

    }

    @Override
    public int getSceneId() {
        return Constants.SCENE_STRATEGIC;
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
