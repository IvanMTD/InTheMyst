package ru.phoenix.core.kernel;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import ru.phoenix.core.config.Constants;
import ru.phoenix.core.math.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static Input instance = null;

    private boolean[] keys;
    private boolean[] buttons;

    private boolean cursorMove;
    private boolean click;
    private Vector2f cursorPosition;
    private float scrollOffset;
    private int counter;
    private int action;

    public static Input getInstance(){
        if(instance == null){
            instance = new Input();
        }
        return instance;
    }

    private Input(){
        init();
        GLFWKeyCallback keyCallback;
        glfwSetKeyCallback(Window.getInstance().getWindow(), new GLFWKeyCallback() {
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
        });

        GLFWMouseButtonCallback mouseButtonCallback;
        glfwSetMouseButtonCallback(Window.getInstance().getWindow(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    buttons[button] = true;
                } else if (action == GLFW_RELEASE) {
                    buttons[button] = false;
                }
            }
        });

        GLFWCursorPosCallback cursorPosCallback;
        glfwSetCursorPosCallback(Window.getInstance().getWindow(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                cursorPosition.setX((float) xpos);
                cursorPosition.setY((float) ypos);
                setCursorMove(true);
            }
        });

        glfwSetScrollCallback(Window.getInstance().getWindow(), new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                setScrollOffset((float) yoffset);
            }
        });
    }

    private void init(){
        action = Constants.NO_ACTION;
        counter = 0;
        click = false;
        cursorPosition = new Vector2f();
        scrollOffset = 0.0f;
        keys = new boolean[1024];
        buttons = new boolean[10];
        glfwSetInputMode(Window.getInstance().getWindow(),GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public void update(){
        setScrollOffset(0.0f);
        glfwPollEvents();
    }

    public int buttonActionVerification(boolean mouse, int key){
        if(mouse){
            if (isMousePressed(key)) {
                counter++;
                if(!click) {
                    click = true;
                    action = Constants.HOLD;
                }
            }else if(!isMousePressed(key)){
                action = Constants.NO_ACTION;
                if(click) {
                    if (counter <= 50) {
                        action = Constants.CLICK;
                    }
                    counter = 0;
                    click = false;
                }
            }
        }else{
            System.out.println("Not working yet!");
        }

        return action;
    }

    public boolean isPressed(int index){
        return keys[index];
    }

    private boolean isMousePressed(int index){
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
