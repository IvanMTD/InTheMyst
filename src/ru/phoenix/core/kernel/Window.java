package ru.phoenix.core.kernel;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.loader.ImageLoader;

import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    // экземпляр класса
    private static Window instance = null;
    private static final String TITLE = "Phoenix Engine version " + Constants.ENGINE_VERSION + " - " + Constants.GAME_NAME;
    // описатели окна
    private long window;
    private int width;
    private int height;
    private float gamma;

    public static Window getInstance(){
        if(instance == null){
            instance = new Window();
        }
        return instance;
    }

    public void create (int width, int height, boolean isFullScrean){

        setWidth(width);
        setHeight(height);
        setGamma(WindowConfig.getInstance().getGamma());

        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);

        if(isFullScrean) {
            window = glfwCreateWindow(width, height, TITLE, glfwGetPrimaryMonitor(), NULL);
        }else{
            window = glfwCreateWindow(width, height, TITLE, NULL, NULL);
        }

        if(window == NULL) {
            throw new RuntimeException("The window was not created!");
        }

        setupLogo(Constants.LOGO_PATH);

        try(MemoryStack stack = stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window,pWidth,pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidmode != null;
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0))/2,(vidmode.height() - pHeight.get(0))/2);
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwShowWindow(window);
    }

    private void setupLogo(String path){
        try {
            ImageLoader.load(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GLFWImage image = GLFWImage.malloc();

        image.set(32, 32, ImageLoader.getBuf());

        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, image);

        glfwSetWindowIcon(window, images);
    }

    public void titleUpdate(int fps, String time){
        glfwSetWindowTitle(window,TITLE + " | " + time + " | FPS: " + fps);
    }

    public void setWindowSize(int width, int height){
        glfwSetWindowSize(Window.getInstance().getWindow(),width,height);
    }

    public void setScreenMode(boolean fullScreen){
        WindowConfig.getInstance().setFullScreen(fullScreen);
        if(fullScreen){
            glfwSetWindowMonitor(getWindow(),glfwGetPrimaryMonitor(),0,0,getWidth(),getHeight(),GLFW_DONT_CARE);
        }else{
            glfwSetWindowMonitor(getWindow(),NULL,0,0,getWidth(),getHeight(),GLFW_DONT_CARE);
            try(MemoryStack stack = stackPush()){
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);
                glfwGetWindowSize(window,pWidth,pHeight);
                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
                assert vidmode != null;
                glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0))/2,(vidmode.height() - pHeight.get(0))/2);
            }
        }
    }

    public void setViewport(int width, int height){
        glViewport(0,0,width,height);
    }

    public boolean isCloseRequested() {
        return glfwWindowShouldClose(window);
    }

    public void render() {
        glfwSwapBuffers(window);
    }

    public void dispose() {
        glfwDestroyWindow(window);
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        WindowConfig.getInstance().setWidth(width);
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        WindowConfig.getInstance().setHeight(height);
        this.height = height;
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }
}
