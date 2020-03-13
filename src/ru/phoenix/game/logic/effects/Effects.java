package ru.phoenix.game.logic.effects;

import ru.phoenix.core.kernel.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Effects {
    private static final float GAMMA_CORRECT = 1.0f;
    private static boolean firstStart = true;
    private static float lastTime;
    private static float timer;

    public static float getGammaAttenuation(float overTime){

        float gamma = 0.0f;

        if(firstStart){
            lastTime = (float)glfwGetTime();
            firstStart = false;
            timer = 0.0f;
        }

        float currentTime = (float)glfwGetTime();
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        timer += deltaTime;

        if(timer < overTime){
            if(timer <= overTime /2){
                float percent = (timer * 100.0f) / (overTime/2);
                gamma = GAMMA_CORRECT * percent / 100.0f;
            }else{
                float reverseTime = timer - overTime / 2;
                float percent = 100.0f - ((reverseTime * 100.0f) / (overTime / 2));
                gamma = GAMMA_CORRECT * percent / 100.0f;
            }
        }else{
            firstStart = true;
        }

        return gamma;
    }

    public static float getGammaFade(float overTime){

        float gamma = Window.getInstance().getGamma();

        if(firstStart){
            lastTime = (float)glfwGetTime();
            firstStart = false;
            timer = 0.0f;
        }

        float currentTime = (float)glfwGetTime();
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        timer += deltaTime;

        if(timer < overTime){
            float reverseTime = Math.abs(timer - overTime);
            float percent = 100.0f - ((reverseTime * 100.0f) / (overTime / 2));
            gamma = GAMMA_CORRECT * percent / 100.0f;

        }else{
            firstStart = true;
        }

        return gamma;
    }
}
