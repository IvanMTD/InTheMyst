package ru.phoenix.core.config;

import ru.phoenix.core.loader.sprite.TextureConfig;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyThief;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;

import static org.lwjgl.opengl.GL11.*;
import static ru.phoenix.core.config.Constants.*;

public class Default {

    // communis
    private static Character gehard;
    private static Character communisArcher;
    // anarchy
    private static Character anarchyThief;

    private static float offset;
    private static boolean wait;
    private static boolean battleMode;
    private static float cursorAngle;
    private static boolean createPath;

    public static void init() {
        // communis
        gehard = new Gehard();
        communisArcher = new CommunisArcher();
        // anarchy
        anarchyThief = new AnarchyThief();

        createPath = true;
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

    public static boolean isBattleMode() {
        return battleMode;
    }

    public static void setBattleMode(boolean battleMode) {
        Default.battleMode = battleMode;
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

    public static boolean isCreatePath() {
        return createPath;
    }

    public static void setCreatePath(boolean createPath) {
        Default.createPath = createPath;
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

    public static TextureConfig getStandIdle(int id){
        if(id == ID_COMMUNIST_GEHARD){
            return new TextureConfig("./data/content/texture/person/communists/gehard/idle_gh_stand.png",3,1);
        }else if(id == ID_COMMUNIST_CHEMIST){
            return new TextureConfig("./data/content/texture/person/communists/chemist/idle_chemist_stand.png",3,1);
        }else if(id == ID_COMMUNIST_ARCHER){
            return new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_stand.png",3,1);
        }else if(id == ID_ANARCHY_THIEF){
            return new TextureConfig("./data/content/texture/person/anarchists/thief/idle_thief_stand.png",3,1);
        }else if(id == ID_ANARCHY_BANDIT){
            return new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_stand.png",3,1);
        }else if(id == ID_ANARCHY_ARCHER){
            return new TextureConfig("./data/content/texture/person/anarchists/archer/idle_archer_stand.png",3,1);
        }else if(id == ID_IMPERIAL_ARCHER){
            return new TextureConfig("./data/content/texture/person/imperial/archer/idle_archer_stand.png",3,1);
        }
        return null;
    }

    public static TextureConfig getBattleStandIdle(int id){
        if(id == ID_COMMUNIST_GEHARD){
            return new TextureConfig("./data/content/texture/person/communists/gehard/idle_gh_battle_stand.png",11,1);
        }else if(id == ID_COMMUNIST_CHEMIST){
            return new TextureConfig("./data/content/texture/person/communists/chemist/idle_chemist_stand.png",3,1);
        }else if(id == ID_COMMUNIST_ARCHER){
            return new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_stand.png",3,1);
        }else if(id == ID_ANARCHY_THIEF){
            return new TextureConfig("./data/content/texture/person/anarchists/thief/idle_thief_stand.png",3,1);
        }else if(id == ID_ANARCHY_BANDIT){
            return new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_stand.png",3,1);
        }else if(id == ID_ANARCHY_ARCHER){
            return new TextureConfig("./data/content/texture/person/anarchists/archer/idle_archer_stand.png",3,1);
        }else if(id == ID_IMPERIAL_ARCHER){
            return new TextureConfig("./data/content/texture/person/imperial/archer/idle_archer_stand.png",3,1);
        }
        return null;
    }

    public static TextureConfig getAttackIdle(int id){
        if(id == ID_COMMUNIST_GEHARD){
            return new TextureConfig("./data/content/texture/person/communists/gehard/idle_gh_attack.png",3,1);
        }else if(id == ID_COMMUNIST_CHEMIST){
            return new TextureConfig("./data/content/texture/person/communists/chemist/idle_chemist_stand.png",3,1);
        }else if(id == ID_COMMUNIST_ARCHER){
            return new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_stand.png",3,1);
        }else if(id == ID_ANARCHY_THIEF){
            return new TextureConfig("./data/content/texture/person/anarchists/thief/idle_thief_stand.png",3,1);
        }else if(id == ID_ANARCHY_BANDIT){
            return new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_stand.png",3,1);
        }else if(id == ID_ANARCHY_ARCHER){
            return new TextureConfig("./data/content/texture/person/anarchists/archer/idle_archer_stand.png",3,1);
        }else if(id == ID_IMPERIAL_ARCHER){
            return new TextureConfig("./data/content/texture/person/imperial/archer/idle_archer_stand.png",3,1);
        }
        return null;
    }

    public static TextureConfig getWalkIdle(int id){
        if(id == ID_COMMUNIST_GEHARD){
            return new TextureConfig("./data/content/texture/person/communists/gehard/idle_gh_walk.png",12,1);
        }else if(id == ID_COMMUNIST_CHEMIST){
            return new TextureConfig("./data/content/texture/person/communists/chemist/idle_chemist_walk.png",12,1);
        }else if(id == ID_COMMUNIST_ARCHER){
            return new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_walk.png",12,1);
        }else if(id == ID_ANARCHY_THIEF){
            return new TextureConfig("./data/content/texture/person/anarchists/thief/idle_thief_walk.png",12,1);
        }else if(id == ID_ANARCHY_BANDIT){
            return new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_walk.png",12,1);
        }else if(id == ID_ANARCHY_ARCHER){
            return new TextureConfig("./data/content/texture/person/anarchists/archer/idle_archer_walk.png",12,1);
        }else if(id == ID_IMPERIAL_ARCHER){
            return new TextureConfig("./data/content/texture/person/imperial/archer/idle_archer_walk.png",12,1);
        }
        return null;
    }

    public static TextureConfig getJumpIdle(int id){
        if(id == ID_COMMUNIST_GEHARD){
            return new TextureConfig("./data/content/texture/person/communists/gehard/idle_gh_jump.png",7,1);
        }else if(id == ID_COMMUNIST_CHEMIST){
            return new TextureConfig("./data/content/texture/person/communists/chemist/idle_chemist_jump.png",7,1);
        }else if(id == ID_COMMUNIST_ARCHER){
            return new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_jump.png",7,1);
        }else if(id == ID_ANARCHY_THIEF){
            return new TextureConfig("./data/content/texture/person/anarchists/thief/idle_thief_jump.png",7,1);
        }else if(id == ID_ANARCHY_BANDIT){
            return new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_jump.png",7,1);
        }else if(id == ID_ANARCHY_ARCHER){
            return new TextureConfig("./data/content/texture/person/anarchists/archer/idle_archer_jump.png",7,1);
        }else if(id == ID_IMPERIAL_ARCHER){
            return new TextureConfig("./data/content/texture/person/imperial/archer/idle_archer_jump.png",7,1);
        }
        return null;
    }

    public static TextureConfig getClimbingIdle(int id){
        if(id == ID_COMMUNIST_GEHARD){
            return new TextureConfig("./data/content/texture/person/communists/gehard/idle_gh_climbing.png",4,1);
        }else if(id == ID_COMMUNIST_CHEMIST){
            return new TextureConfig("./data/content/texture/person/communists/chemist/idle_chemist_climbing.png",4,1);
        }else if(id == ID_COMMUNIST_ARCHER){
            return new TextureConfig("./data/content/texture/person/communists/archer/idle_archer_climbing.png",4,1);
        }else if(id == ID_ANARCHY_THIEF){
            return new TextureConfig("./data/content/texture/person/anarchists/thief/idle_thief_climbing.png",4,1);
        }else if(id == ID_ANARCHY_BANDIT){
            return new TextureConfig("./data/content/texture/person/anarchists/bandit/idle_bandit_climbing.png",4,1);
        }else if(id == ID_ANARCHY_ARCHER){
            return new TextureConfig("./data/content/texture/person/anarchists/archer/idle_archer_climbing.png",4,1);
        }else if(id == ID_IMPERIAL_ARCHER){
            return new TextureConfig("./data/content/texture/person/imperial/archer/idle_archer_climbing.png",4,1);
        }
        return null;
    }
}
