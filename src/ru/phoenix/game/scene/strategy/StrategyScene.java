package ru.phoenix.game.scene.strategy;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.scene.Scene;

import java.util.List;

public class StrategyScene implements Scene {
    // scene info
    private int days;
    private float[] currentDayBiom;

    // scene control
    private boolean active;

    public StrategyScene() {
        active = false;
    }

    public void init(){
        days = 100;
        currentDayBiom = new float[days];
        long seed = (long)(1 + Math.random() * 10000000000L);
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 20.0f;
        System.out.println("accuracy: " + accuracy);
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
            currentDayBiom[i] = num;
        }

        float diff = Math.abs(max - min);

        for(int i=0; i<days; i++){
            float num = currentDayBiom[i] - min;
            float percent = num * 100.0f / diff;
            float newNum = 1.0f * percent / 100.0f;
            currentDayBiom[i] = newNum;
        }

    }

    @Override
    public void start() {
        active = true;
        init();
    }

    @Override
    public void over() {
        active = false;
    }

    @Override
    public void update() {
        SceneControl.setLastScene(this);
        SceneControl.setStrategyGame(false);
        SceneControl.setTacticalGame(true);
        over();
    }

    @Override
    public void draw() {

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
