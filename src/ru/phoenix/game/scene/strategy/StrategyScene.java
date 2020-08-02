package ru.phoenix.game.scene.strategy;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.stage.strategy.StrategicScreen;
import ru.phoenix.game.datafile.SaveGame;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.Scene;
import ru.phoenix.game.scene.cut.CutScene;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.SCENE_CUT;
import static ru.phoenix.core.config.Constants.WEST;

public class StrategyScene implements Scene {

    private int currentScene;
    private int currentEvent;

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
    private boolean start;
    private boolean event;
    private float currentGamma;
    // скайбокс
    private Skybox skybox;

    private float person_counter;
    private int dialog_counter;
    private int random_person;

    private List<Float> eventList;

    public StrategyScene() {
        km = 100;
        m = 0.0f;
        allies = new ArrayList<>();
        scenes = new ArrayList<>();
        active = false;
        init = false;
        currentGamma = WindowConfig.getInstance().getGamma();
        eventList = new ArrayList<>();
        dialog_counter = 0;
        person_counter = 0.0f;
    }

    public void init(){
        Camera.getInstance().preset(25.0f,-90.0f,-10.0f);
        Camera.getInstance().setPos(new Vector3f(100.0f,3.0f,213.0f));
        Camera.getInstance().setFront(new Vector3f(0.0f,0.0f,-1.0f));
        Camera.getInstance().updateViewMatrix();
        start = true;
        event = false;

        setPersonOnScreen();

        if(!init){
            createBioms();
            if(Default.getCurrentData() != null){
                currentKm = Default.getCurrentData().getCurrentKm();
                strategicScreen = new StrategicScreen();
                strategicScreen.init(Default.getCurrentData().getCurrentBiom()); // getBiom(bioms[currentKm])
            }else {
                currentKm = 0;
                strategicScreen = new StrategicScreen();
                strategicScreen.init(bioms[currentKm]); // getBiom(bioms[currentKm])
            }

            updateEventList();

            skybox = new Skybox();
            cursor = new Cursor();
            skybox.init();
            cursor.init();
            init = true;
        }
    }

    private void setPersonOnScreen(){
        float xOffset = allies.size() / 2.0f;
        float offset = 0.0f;
        for(Character ally : allies){
            Vector3f position = new Vector3f(100.0f + xOffset - offset,0.0f,197.0f + (-2.0f + (float)Math.random() * 4.0f));
            ally.preset();
            ally.setTarget(false);
            ally.setSelected(false);
            ally.setPosition(position);
            ally.setTempPos(position);
            ally.setLook(WEST);
            offset ++;
        }
    }

    private void createBioms(){
        if(Default.getCurrentData() != null){
            bioms = Default.getCurrentData().getBioms();
        }else {
            bioms = new float[km];
            long seed = (long) (1 + Math.random() * 10000000000L);
            Perlin2D perlin = new Perlin2D(seed);
            float accuracy = 10.0f;
            float dayMap[] = new float[km];
            for (int y = 0; y < km; y++) {
                float value = perlin.getNoise(1 / accuracy, y / accuracy, 8, 0.5f);
                int n = (int) (value * 255 + 128) & 255;
                float result = ((float) n / 255.0f);
                dayMap[y] = result;
            }

            float min = 0.5f;
            float max = 0.5f;

            for (int i = 0; i < km; i++) {
                float num = dayMap[i];
                if (num < min) {
                    min = num;
                }
                if (num > max) {
                    max = num;
                }
                bioms[i] = num;
            }

            float diff = Math.abs(max - min);

            for (int i = 0; i < km / 2; i++) {
                float biom = bioms[i * 2] - min;
                float percent = biom * 100.0f / diff;
                float newBiom = 1.0f * percent / 100.0f;
                biom = getBiom(newBiom * 50.0f);
                if (i == 0) {
                    bioms[i] = 25.0f;
                } else {
                    float previous = bioms[(i * 2) - 2];
                    if (previous > biom) {
                        bioms[(i * 2) - 1] = previous - 5;
                        bioms[i * 2] = previous - 10;
                        if (i == 49) {
                            bioms[(i * 2) + 1] = previous - 10;
                        }
                    } else if (previous < biom) {
                        bioms[(i * 2) - 1] = previous + 5;
                        bioms[i * 2] = previous + 10;
                        if (i == 49) {
                            bioms[(i * 2) + 1] = previous + 10;
                        }
                    } else {
                        bioms[(i * 2) - 1] = biom;
                        bioms[i * 2] = biom;
                        if (i == 49) {
                            bioms[(i * 2) + 1] = biom;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 100; i++) {
            System.out.println("День " + i + ": " + getInfo(bioms[i]));
        }
        System.out.println(getInfo(bioms[currentKm]));
    }

    @Override
    public void start(List<Scene> scenes){
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void reInit() {

    }

    @Override
    public void over() {
        active = false;
    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {
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
        cursor.update(null);
        if(start){
            Window.getInstance().setGamma(Window.getInstance().getGamma() + 0.003f);
            for(Character ally : allies){
                ally.update(0);
                ally.update();
            }
            if(Window.getInstance().getGamma() > currentGamma){
                Window.getInstance().setGamma(currentGamma);
                start = false;
            }
        }else if(event){
            dialog_counter++;
            allies.get(random_person).setText("I see something!");
            currentEvent = CutScene.CAMP_SCENE;
            currentScene = SCENE_CUT;
            for (Character ally : allies) {
                ally.update(0);
                ally.update();
            }
            if(dialog_counter > 300) {
                allies.get(random_person).clearText();
                Window.getInstance().setGamma(Window.getInstance().getGamma() - 0.002f);
                if (Window.getInstance().getGamma() <= 0.0f) {
                    Window.getInstance().setGamma(0.0f);
                    nextScene();
                    dialog_counter = 0;
                }
            }
        }else {
            m += 0.1f;
            if (m > 300.0f) {
                m = 0.0f;
                currentKm++;
                if (currentKm >= bioms.length) {
                    currentKm = 0;
                }
                System.out.println(getInfo(bioms[currentKm]));

                updateEventList();
            }
            float biom = bioms[currentKm];
            strategicScreen.update(biom);
            person_counter += 0.02f;
            for(Character ally : allies){
                ally.setPosition(new Vector3f(ally.getPosition().add(new Vector3f(0.02f,0.0f,0.0f))));
                if(person_counter > 1.0f){
                    ally.setPosition(ally.getTempPos());
                }
                ally.update(1);
                ally.update();
            }
            if(person_counter > 1.0f){
                person_counter = 0.0f;
            }

            if (checkEvent()) {
                if(Math.random() * 100.0f < 50.0f) {
                    random_person = Math.round((float)Math.random() * (allies.size() - 1));
                    event = true;
                }
            }
        }
    }

    private void updateEventList(){
        eventList.clear();
        int amount = (int)Math.round(1.0f + Math.random() * 2.0f);

        for(int m = 300/amount; m < 300; m += 300/amount){
            float point = m + (-15.0f + (float)Math.random() * 30.0f);
            eventList.add(point);
        }
    }

    private boolean checkEvent(){
        boolean check = false;
        for(float point : eventList){
            if((point - 0.1f) < m && m < (point + 0.1)){
                eventList.remove(point);
                check = true;
                break;
            }
        }
        return check;
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

        return result;
    }

    @Override
    public void draw() {
        strategicScreen.draw();
        skybox.draw();

        strategicScreen.getSpriteShader().useProgram();
        for(Character ally : allies){
            ally.draw(strategicScreen.getSpriteShader(),false);
        }

        TextDisplay.getInstance().getShader().useProgram();
        for(Character ally : allies){
            ally.drawText(TextDisplay.getInstance().getShader());
        }

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

    @Override
    public void setCurrentInnerScene(int currentInnerScene) {

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

    private void nextScene(){
        SaveGame currentData = new SaveGame();
        currentData.setCurrentBiom(strategicScreen.getCurrentBiom());
        currentData.setCurrentKm(currentKm);
        currentData.setBioms(bioms);
        Default.setCurrentData(currentData);
        SceneControl.setLastScene(this);
        for (Scene scene : scenes) {
            if (scene.getSceneId() == currentScene) {
                scene.preset(strategicScreen.getCurrentBiom(), allies);
                scene.setCurrentInnerScene(currentEvent);
                scene.start(scenes);
            }
        }
        over();
    }

}
