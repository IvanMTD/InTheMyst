package ru.phoenix.core.config;

import static org.lwjgl.opengl.GL11.*;
import static ru.phoenix.core.config.Constants.ID_PERSON_GEHARD;

public class Default {

    private static boolean wait;
    private static float offset;
    private static boolean wait;

    private static final String GH_IDLE_STAND = "./data/content/texture/person/idle_gh_stand.png";

    public static void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void clearScreen() {
        glClearColor(0.2f,0.4f,0.5f,1.0f);
        glClearDepth(1.0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static boolean isWait() {
        return wait;
    }

    public static void setWait(boolean wait) {
        Default.wait = wait;
    }

    public static float getOffset() {
        return offset;
    }

    public static void setOffset(float offset) {
        Default.offset = offset;
    }

    public static boolean isWait() {
        return wait;
    }

    public static void setWait(boolean wait) {
        Default.wait = wait;
    }

    public static String getStandIdle(float id){
        if(id == ID_PERSON_GEHARD){
            return GH_IDLE_STAND;
        }
        return null;
    }
}
