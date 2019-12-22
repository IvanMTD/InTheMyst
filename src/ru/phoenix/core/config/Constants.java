package ru.phoenix.core.config;

public class Constants {

    // block types
    public static final int BLOCK_DIRT          = 0x13200;
    public static final int BLOCK_DIRT_GRASS    = 0x13201;
    public static final int BLOCK_DIRT_SNOW     = 0x13202;
    public static final int BLOCK_COLD_DIRT     = 0x13203;
    public static final int BLOCK_ROCK          = 0x13204;
    public static final int BLOCK_ROCK_SNOW     = 0x13205;
    public static final int BLOCK_GRASS_FLOWER  = 0x13206;
    // block stone
    public static final int BLOCK_STONE_SMALL   = 0x13300;
    public static final int BLOCK_STONE_MEDIUM  = 0x13301;
    public static final int BLOCK_STONE_BIG     = 0x13302;

    // map info
    public static final int LEFT_BOARD          = 0x00001;
    public static final int RIGHT_BOARD         = 0x00002;
    public static final int UP_BOARD            = 0x00003;
    public static final int DOWN_BOARD          = 0x00004;
    public static final int CENTER_BOARD        = 0x00005;

    // generator seed
    public static final int PLAIN_MAP           = 0x13101;
    public static final int MOUNTAIN_MAP        = 0x13102;
    public static final int RIVER_MAP           = 0x13103;
    // area info
    public static final int PLAIN_AREA          = 0x13101;
    public static final int MOUNTAIN_AREA       = 0x13102;

    // scene indicators
    public static final int SCENE_LOGO          = 0x10000;
    public static final int SCENE_MAIN_MENU     = 0x11000;
    public static final int SCENE_GAME          = 0x12000;
    public static final int SCENE_BATTLE        = 0x13000;
    public static final int SCENE_LOADING       = 0x14000;

    // recognition
    public static final int ALLY                = 0x13100;
    public static final int ENEMY               = 0x13101;
    public static final int NEUTRAL             = 0x13102;

    // person id
    // communists
    public static final int ID_COMMUNIST_GEHARD  = 0x12101;
    public static final int ID_COMMUNIST_CHEMIST = 0x12102;
    public static final int ID_COMMUNIST_ARCHER  = 0x12103;
    // anarchists
    public static final int ID_ANARCHY_THIEF   = 0x12201;
    public static final int ID_ANARCHY_BANDIT  = 0x12202;
    public static final int ID_ANARCHY_ARCHER  = 0x12203;
    // imperial
    public static final int ID_IMPERIAL_ARCHER = 0x12301;

    // click or hold
    public static final int NO_ACTION           = 0x30000;
    public static final int CLICK               = 0x30001;
    public static final int HOLD                = 0x30002;
    public static final int PRESS               = 0x30003;

    // group RGB
    public static final int GROUP_R             = 0x20001;
    public static final int GROUP_G             = 0x20002;
    public static final int GROUP_B             = 0x20003;
    public static final int GROUP_A             = 0x20004;

    // window tittle information and logo
    public static final String ENGINE_VERSION = "1.0.0 alpha";
    public static final String GAME_NAME = "\"IN THE MYST\"";
    public static final String LOGO_PATH = "./data/logo/phoenix_logo_32.png";
    // System control
    public static final long NANOSECOND = 1000000000L;
    // Shadow indificators
    public static final int DIRECT_SHADOW = 0x001;
    public static final int POINT_SHADOW = 0x002;
    public static final int SPOT_SHADOW = 0x003;
}
