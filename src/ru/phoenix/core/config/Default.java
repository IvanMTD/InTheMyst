package ru.phoenix.core.config;

import ru.phoenix.core.loader.sprite.TextureConfig;

import static org.lwjgl.opengl.GL11.*;
import static ru.phoenix.core.config.Constants.ID_PERSON_GEHARD;

public class Default {

    private static float offset;
    private static boolean wait;

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

    public static TextureConfig getStandIdle(float id){
        if(id == ID_PERSON_GEHARD){
            return new TextureConfig("./data/content/texture/person/idle_gh_stand.png",3,1);
        }
        return null;
    }

    public static TextureConfig getWalkIdle(float id){
        if(id == ID_PERSON_GEHARD){
            return new TextureConfig("./data/content/texture/person/idle_gh_walk.png",12,1);
        }
        return null;
    }

    public static TextureConfig getJumpIdle(float id){
        if(id == ID_PERSON_GEHARD){
            return new TextureConfig("./data/content/texture/person/idle_gh_jump.png",7,1);
        }
        return null;
    }
}
