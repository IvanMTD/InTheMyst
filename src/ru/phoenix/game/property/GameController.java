package ru.phoenix.game.property;

import ru.phoenix.core.util.ActionVerification;

import static org.lwjgl.glfw.GLFW.*;
import static ru.phoenix.core.config.Constants.CLICK;
import static ru.phoenix.core.config.Constants.HOLD;

public class GameController {
    private static GameController instance = null;
    // контролеры мыши
    private ActionVerification mouse_left_button;
    private ActionVerification mouse_right_button;
    // контролеры клавиатуры
    private ActionVerification key_space;
    private ActionVerification key_tab;

    // определение нажатия
    // мыши
    private boolean leftClick;
    private boolean rightClick;
    // клавиатуры
    private boolean spaceClick;
    private boolean tabClick;

    // определение зажатия
    // мыши
    private boolean leftHold;
    private boolean rightHold;
    // клавиатуры
    private boolean spaceHold;
    private boolean tabHold;

    private GameController(){
        // контролеры мыши
        mouse_left_button = new ActionVerification();
        mouse_right_button = new ActionVerification();
        // контролеры клавиатуры
        key_space = new ActionVerification();
        key_tab = new ActionVerification();
    }

    public void update(){
        // обновление мыши
        leftClick = mouse_left_button.mouseButton(GLFW_MOUSE_BUTTON_LEFT) == CLICK;
        leftHold = mouse_left_button.mouseButton(GLFW_MOUSE_BUTTON_LEFT) == HOLD;
        rightClick = mouse_right_button.mouseButton(GLFW_MOUSE_BUTTON_RIGHT) == CLICK;
        rightHold = mouse_right_button.mouseButton(GLFW_MOUSE_BUTTON_RIGHT) == HOLD;
        // обновление клавиатуры
        spaceClick = key_space.keyboardButton(GLFW_KEY_SPACE) == CLICK;
        spaceHold = key_space.keyboardButton(GLFW_KEY_SPACE) == HOLD;
        tabClick = key_tab.keyboardButton(GLFW_KEY_TAB) == CLICK;
        tabHold = key_tab.keyboardButton(GLFW_KEY_TAB) == HOLD;
    }

    // результаты работы мыши
    public boolean isLeftClick() {
        return leftClick;
    }

    public boolean isRightClick() {
        return rightClick;
    }

    public boolean isLeftHold() {
        return leftHold;
    }

    public boolean isRightHold() {
        return rightHold;
    }

    // результаты работы клавиатуры
    public boolean isSpaceClick() {
        return spaceClick;
    }

    public boolean isSpaceHold() {
        return spaceHold;
    }

    public boolean isTabClick() {
        return tabClick;
    }

    public boolean isTabHold() {
        return tabHold;
    }

    public static GameController getInstance(){
        if(instance == null){
            instance = new GameController();
        }
        return instance;
    }
}
