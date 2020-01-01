package ru.phoenix.core.config;

import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyThief;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;

import static org.lwjgl.opengl.GL11.*;

public class Default {

    // communis
    private static Character gehard;
    private static Character communisArcher;
    // anarchy
    private static Character anarchyThief;

    private static float offset;
    private static boolean wait;
    private static float cursorAngle;

    public static void init() {
        // communis
        gehard = new Gehard();
        communisArcher = new CommunisArcher();
        // anarchy
        anarchyThief = new AnarchyThief();

        cursorAngle = 0.0f;
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void clearScreen() {
        //glClearColor(0.2f,0.4f,0.5f,1.0f);
        glClearColor(0.0f,0.0f,0.0f,1.0f);
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

    public static float getCursorAngle() {
        return cursorAngle;
    }

    public static void setCursorAngle(float cursorAngle) {
        Default.cursorAngle = cursorAngle;
    }

    // COMMUNIS
    public static Character getGehard() {
        return gehard;
    }

    public static Character getCommunisArcher() {
        return communisArcher;
    }

    // ANARCHY
    public static Character getAnarchyThief() {
        return anarchyThief;
    }
}
