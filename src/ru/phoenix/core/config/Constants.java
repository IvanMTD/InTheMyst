package ru.phoenix.core.config;

public class Constants {
    // scene indicators
    public static final int SCENE_LOGO          = 0x10000;
    public static final int SCENE_MAIN_MENU     = 0x11000;
    public static final int SCENE_STRATEGIC     = 0x12000;
    public static final int SCENE_TACTICAL      = 0x13000;
    public static final int SCENE_LOADING       = 0x14000;
    public static final int SCENE_CUT           = 0x15000;

    // SCENE_BATTLE - BEGIN 0x13000;
    // area info
    public static final int PLAIN_AREA          = 0x13001;
    public static final int MOUNTAIN_AREA       = 0x13002;

    // map info
    public static final int LEFT_BOARD          = 0x13101;
    public static final int RIGHT_BOARD         = 0x13102;
    public static final int UP_BOARD            = 0x13103;
    public static final int DOWN_BOARD          = 0x13104;
    public static final int CENTER_BOARD        = 0x13105;

    // block stone
    public static final int BLOCK_STONE_SMALL   = 0x13201;
    public static final int BLOCK_STONE_MEDIUM  = 0x13202;
    public static final int BLOCK_STONE_BIG     = 0x13203;

    // recognition
    public static final int ALLY                = 0x13301;
    public static final int ENEMY               = 0x13302;
    public static final int NEUTRAL             = 0x13303;

    // direction
    public static final int NORTH               = 0x13401;
    public static final int WEST                = 0x13402;
    public static final int SOUTH               = 0x13403;
    public static final int EAST                = 0x13404;

    // AI model
    public static final int MELEE_COMBAT        = 0x13501;
    public static final int RANGE_COMBAT        = 0x13502;
    public static final int MIDDLE_COMBAT       = 0x13503;
    // SCENE_BATTLE - END

    // CONTROL - BEGIN
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
    // CONTROL - END

    // language
    public static final int RUSSIAN             = 0x40001;
    public static final int ENGLISH             = 0x40002;
    // language typing
    public static final int TYPING_LEFT         = 0x40011;
    public static final int TYPING_RIGHT        = 0x40012;
    public static final int TYPING_CENTER       = 0x40013;

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
