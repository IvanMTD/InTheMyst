package ru.phoenix.core.debug;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class HowLong {

    private static String information;
    private static double timer;

    public static void setup(String info){
        information = info;
        timer = glfwGetTime();
    }

    public static void getInformation(){
        System.out.println("Обработка " + information + " заняло " + (glfwGetTime() - timer) + " секунд");
    }
}
