package ru.phoenix.game.scene.strategy;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisPartisan;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;
import ru.phoenix.game.content.stage.strategy.StrategicScreen;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static ru.phoenix.core.config.Constants.ALLY;

public class StrategyScene implements Scene {
    private StrategicScreen strategicScreen;
    // обще игровые переменные
    private List<Character> allies;
    private List<Scene> scenes;
    private Cursor cursor;
    // переменные контроля игры
    private int km;
    private int currentKm;
    private float m;
    private float[] bioms;
    // контроль сцены
    private boolean active;
    private boolean init;
    // скайбокс
    private Skybox skybox;

    private boolean reverse;
    private float tempBiom = 5.0f;

    public StrategyScene() {
        km = 100;
        currentKm = 0;
        m = 0.0f;
        reverse = false;
        strategicScreen = new StrategicScreen();
        skybox = new Skybox();
        cursor = new Cursor();
        allies = new ArrayList<>();
        scenes = new ArrayList<>();
        active = false;
        init = false;
    }

    public void init(){
        Camera.getInstance().preset(25.0f,-90.0f,-10.0f);
        Camera.getInstance().setPos(new Vector3f(100.0f,3.0f,213.0f));
        Camera.getInstance().setFront(new Vector3f(0.0f,0.0f,-1.0f));
        Camera.getInstance().updateViewMatrix();
        if(!init){
            init = true;
            createAllies();
            createBioms();
            skybox.init();
            cursor.init();
            strategicScreen.init(bioms[currentKm]); // getBiom(bioms[currentKm])
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

    private void createBioms(){
        bioms = new float[km];
        long seed = (long)(1 + Math.random() * 10000000000L);
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 10.0f;
        float dayMap[] = new float[km];
        for(int y=0; y<km; y++){
            float value = perlin.getNoise(1/accuracy,y/accuracy,8,0.5f);
            int n = (int)(value * 255 + 128) & 255;
            float result = ((float)n / 255.0f);
            dayMap[y] = result;
        }

        float min = 0.5f;
        float max = 0.5f;

        for (int i=0; i<km; i++){
            float num = dayMap[i];
            if(num < min){
                min = num;
            }
            if(num > max){
                max = num;
            }
            bioms[i] = num;
        }

        float diff = Math.abs(max - min);

        for(int i=0; i < km/2; i++){
            float biom = bioms[i*2] - min;
            float percent = biom * 100.0f / diff;
            float newBiom = 1.0f * percent / 100.0f;
            biom = getBiom(newBiom * 50.0f);
            if(i == 0){
                    bioms[i] = biom;
            }else {
                float previous = bioms[(i * 2) - 2];
                if (previous > biom) {
                    bioms[(i * 2) - 1] = previous - 5;
                    bioms[i * 2] = previous - 10;
                    if(i == 49){
                        bioms[(i * 2) + 1] = previous - 10;
                    }
                } else if (previous < biom) {
                    bioms[(i * 2) - 1] = previous + 5;
                    bioms[i * 2] = previous + 10;
                    if(i == 49){
                        bioms[(i * 2) + 1] = previous + 10;
                    }
                } else {
                    bioms[(i * 2) - 1] = biom;
                    bioms[i * 2] = biom;
                    if(i == 49){
                        bioms[(i * 2) + 1] = biom;
                    }
                }
            }
        }

        for(int i=0; i<100; i++){
            System.out.println("День " + i + ": " + getInfo(bioms[i]));
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
        cursor.update(null);

        m += 0.1f;
        if(m > 300.0f){
            m = 0.0f;
            currentKm++;
            System.out.println(bioms[currentKm]);
            if(currentKm >= bioms.length){
                currentKm = 0;
            }
        }

        float biom = bioms[currentKm];
        strategicScreen.update(biom);

        if(Input.getInstance().isPressed(GLFW_KEY_SPACE)) {
            SceneControl.setLastScene(this);
            for(Scene scene : scenes){
                if(scene.getSceneId() == Constants.SCENE_TACTICAL){
                    scene.preset(strategicScreen.getCurrentBiom(),allies);
                    scene.start(scenes);
                }
            }
            over();
        }
    }

    private float getBiom(float b){
        float result = 0.0f;

        if(b < 10.0f){
            result = 5.0f;
        }else if(b < 20.0f){
            result = 15.0f;
        }else if(b < 30.0f){
            result = 25.0f;
        }else if(b < 40.0f){
            result = 35.0f;
        }else{
            result = 45.0f;
        }

        /*if(0.0f <= b && b < 7.5f){
            result = 5.0f;
        }else if(7.5f <= b && b < 12.5f){
            result = 10.0f;
        }else if(12.5f <= b && b < 17.5f){
            result = 15.0f;
        }else if(17.5f <= b && b < 22.5f){
            result = 20.0f;
        }else if(22.5f <= b && b < 27.5f){
            result = 25.0f;
        }else if(27.5f <= b && b < 32.5f){
            result = 30.0f;
        }else if(32.5f <= b && b < 37.5f){
            result = 35.0f;
        }else if(37.5f <= b && b < 42.5f){
            result = 40.0f;
        }else{
            result = 45.0f;
        }*/

        return result;
    }

    @Override
    public void draw() {
        strategicScreen.draw();
        skybox.draw();
        cursor.draw();
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

    private String getInfo(float biom){
        String title;

        if(biom == 5.0f){
            title = "Пустыня";
        }else if(biom == 10.0f){
            title = "Граница пустыни";
        }else if(biom == 15.0f){
            title = "Степь";
        }else if(biom == 20.0f){
            title = "Граница степь";
        }else if(biom == 25.0f){
            title = "Равнина";
        }else if(biom == 30.0f){
            title = "Опушка леса";
        }else if(biom == 35.0f){
            title = "Лес";
        }else if(biom == 40.0f){
            title = "Предгорье";
        }else{
            title = "Горы";
        }

        return title;
    }
}
