package ru.phoenix.core.config;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyArcher;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyBandit;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyThief;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisPartisan;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;

import static org.lwjgl.opengl.GL11.*;

public class Default {

    // communis
    private static Character gehard;
    private static Character communisArcher;
    private static Character communisPartisan;
    // anarchy
    private static Character anarchyBandit;
    private static Character anarchyThief;
    private static Character anarchyArcher;

    private static float radiance;
    private static float offset;
    private static boolean wait;
    private static boolean showAlpha;
    private static float cursorAngle;

    private static boolean mapFrameStart;
    private static int mapTextureId;

    public static void init() {
        showAlpha = false;
        // communis
        gehard = new Gehard();
        communisArcher = new CommunisArcher();
        communisPartisan = new CommunisPartisan();
        // anarchy
        anarchyBandit = new AnarchyBandit();
        anarchyThief = new AnarchyThief();
        anarchyArcher = new AnarchyArcher();

        mapFrameStart = false;
        mapTextureId = 0;

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

    public static float getRadiance() {
        return radiance;
    }

    public static void setRadiance(float radiance) {
        Default.radiance = radiance;
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

    public static boolean isShowAlpha() {
        return showAlpha;
    }

    public static void setShowAlpha(boolean showAlpha) {
        Default.showAlpha = showAlpha;
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

    public static Character getCommunisPartisan() {
        return communisPartisan;
    }

    // ANARCHY
    public static Character getAnarchyBandit() {
        return anarchyBandit;
    }

    public static Character getAnarchyThief() {
        return anarchyThief;
    }

    public static Character getAnarchyArcher() {
        return anarchyArcher;
    }

    // control

    public static boolean isMapFrameStart() {
        return mapFrameStart;
    }

    public static void setMapFrameStart(boolean mapFrameStart) {
        Default.mapFrameStart = mapFrameStart;
    }

    public static int getMapTextureId() {
        return mapTextureId;
    }

    public static void setMapTextureId(int mapTextureId) {
        Default.mapTextureId = mapTextureId;
    }
}
