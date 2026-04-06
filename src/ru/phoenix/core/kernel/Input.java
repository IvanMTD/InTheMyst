package ru.phoenix.core.kernel;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import ru.phoenix.core.config.Constants;
import ru.phoenix.core.math.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static ru.phoenix.core.config.Constants.NO_ACTION;

public class Input {
    private static Input instance = null;

    private boolean[] keys;
    private boolean[] buttons;

    private boolean cursorMove;
    private boolean click;
    private Vector2f cursorPosition;
    private float scrollOffset;
    private float counter;
    private int action;

    // Поля для хранения callback'ов, чтобы GC их не удалил
    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWScrollCallback scrollCallback;

    public static Input getInstance(){
        if(instance == null){
            instance = new Input();
        }
        return instance;
    }

    private Input(){
        init();
    }

    private void init(){
        action = NO_ACTION;
        counter = 0.0f;
        click = false;
        cursorPosition = new Vector2f();
        scrollOffset = 0.0f;
        keys = new boolean[1024];
        buttons = new boolean[10];
        
        // Скрываем системный курсор для использования кастомного курсора
        glfwSetInputMode(Window.getInstance().getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        
        // Создаем и сохраняем ссылки на callback'и, чтобы GC их не удалил
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(key >= 0) {
                    if (action == GLFW_PRESS) {
                        keys[key] = true;
                    } else if (action == GLFW_RELEASE) {
                        keys[key] = false;
                    }
                }
            }
        };
        glfwSetKeyCallback(Window.getInstance().getWindow(), keyCallback);

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    buttons[button] = true;
                } else if (action == GLFW_RELEASE) {
                    buttons[button] = false;
                }
            }
        };
        glfwSetMouseButtonCallback(Window.getInstance().getWindow(), mouseButtonCallback);

        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                cursorPosition.setX((float) xpos);
                cursorPosition.setY((float) ypos);
                setCursorMove(true);
            }
        };
        glfwSetCursorPosCallback(Window.getInstance().getWindow(), cursorPosCallback);

        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                setScrollOffset((float) yoffset);
            }
        };
        glfwSetScrollCallback(Window.getInstance().getWindow(), scrollCallback);
    }

    public void update(){
        cursorMove = false;
        setScrollOffset(0.0f);
        glfwPollEvents();
    }

    public boolean isPressed(int index){
        return keys[index];
    }

    public boolean isMousePressed(int index){
        return buttons[index];
    }

    public void setCursorMove(boolean value){
        cursorMove = value;
    }

    public boolean getCursorMove(){
        return cursorMove;
    }

    public Vector2f getCursorPosition(){
        return cursorPosition;
    }

    public float getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(float scroolOffset) {
        this.scrollOffset = scroolOffset;
    }
}
