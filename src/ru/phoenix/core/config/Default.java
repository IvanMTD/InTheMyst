package ru.phoenix.core.config;

import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyArcher;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyBandit;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyThief;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisPartisan;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;
import ru.phoenix.game.datafile.SaveGame;

import static org.lwjgl.opengl.GL11.*;

public class Default {

    private static SaveGame currentData;
    private static int slot;

    // communis
    private static Character gehard;
    private static Character communisArcher;
    private static Character communisPartisan;
    // anarchy
    private static Character anarchyBandit;
    private static Character anarchyThief;
    private static Character anarchyArcher;

    private static int mapTextureId;
    private static boolean start;
    private static float radiance;
    private static float offset;
    private static boolean wait;
    private static boolean showAlpha;
    private static float cursorAngle;

    private static boolean mapFrameStart;

    private static int langueage;

    // game element trigers
    private static boolean campFireOn;

    public static void init() {
        slot = 0;
        start = false;
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

        langueage = WindowConfig.getInstance().getLangueage();

        // game element trigers
        campFireOn = false;

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

    public static SaveGame getCurrentData() {
        return currentData;
    }

    public static void setCurrentData(SaveGame currentData) {
        Default.currentData = currentData;
    }

    public static int getSlot() {
        return slot;
    }

    public static void setSlot(int slot) {
        Default.slot = slot;
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

    public static boolean isStart() {
        return start;
    }

    public static void setStart(boolean start) {
        Default.start = start;
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

    public static int getLangueage() {
        return langueage;
    }

    // game element trigers
    public static boolean isCampFireOn() {
        return campFireOn;
    }

    public static void setCampFireOn(boolean campFireOn) {
        Default.campFireOn = campFireOn;
    }
}
