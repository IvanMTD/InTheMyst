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

    // generator seed
    public static final int PLAIN_MAP           = 0x13101;
    public static final int MOUNTAIN_MAP        = 0x13102;
    public static final int RIVER_MAP           = 0x13103;

    // scene indicators
    public static final int SCENE_LOGO          = 0x10000;
    public static final int SCENE_MAIN_MENU     = 0x11000;
    public static final int SCENE_GAME          = 0x12000;
    public static final int SCENE_BATTLE        = 0x13000;
    public static final int SCENE_LOADING       = 0x14000;

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
