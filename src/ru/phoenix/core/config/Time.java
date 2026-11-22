package ru.phoenix.core.config;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time{
    private static float lastTime = 0;
    private static float deltaTime = 0;
    private static boolean firstUpdate = true;

    private static int day = 0;
    private static int hour = 0;
    private static int minut = 0;
    private static float second = 0;

    public static void update(){
        float currentTime = (float)glfwGetTime();
        
        // Пропускаем первый кадр — deltaTime будет огромным из-за времени загрузки
        if (firstUpdate) {
            lastTime = currentTime;
            deltaTime = 0;
            firstUpdate = false;
            return;
        }
        
        deltaTime = currentTime - lastTime;
        // Ограничиваем deltaTime чтобы избежать скачков при лагах
        if (deltaTime > 0.1f) {
            deltaTime = 0.1f;
        }
        lastTime = currentTime;
        second += deltaTime;

        if(second >= 60){
            minut++;
            second = 0;
        }

        if(minut >= 60){
            hour++;
            minut = 0;
        }

        if(hour >= 24){
            day++;
            hour = 0;
        }
    }

    public static void setDay(int day) {
        Time.day = day;
    }

    public static void setHour(int hour) {
        Time.hour = hour;
    }

    public static void setMinut(int minut) {
        Time.minut = minut;
    }

    public static void setSecond(float second) {
        Time.second = second;
    }

    public static int getDay() {
        return day;
    }

    public static int getHour() {
        return hour;
    }

    public static int getMinut() {
        return minut;
    }

    public static int getSecond() {
        return (int)second;
    }

    /** Возвращает время в секундах, прошедшее с последнего кадра */
    public static float getDeltaTime() {
        return deltaTime;
    }

    public static String getCurrentTime(){
        String d = day > 9 ? Integer.toString(day) : "0" + day;
        String h = hour > 9 ? Integer.toString(hour) : "0" + hour;
        String m = minut > 9 ? Integer.toString(minut) : "0" + minut;
        String s = (int)second > 9 ? Integer.toString((int)second) : "0" + (int)second;
        return " day: " + d + " time: " + h + ":" + m + ":" + s;
    }
}
