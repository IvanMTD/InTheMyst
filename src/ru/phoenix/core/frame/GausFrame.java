package ru.phoenix.core.frame;

import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.core.buffer.vbo.NormalizedDeviceCoordinates;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.shader.Shader;

import static jdk.nashorn.internal.runtime.JSType.toInteger;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GausFrame {

    private static GausFrame instance = null;

    private int[] fbo;
    private int[] texture;

    private Shader shader;

    private VertexBufferObject ndcVbo;
    private Shader ndcShader;

    private GausFrame(){
        fbo = new int[2];
        texture = new int[2];
        shader = new Shader();

        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();

        init();
    }

    private void init(){
        for(int i=0; i<2; i++){
            fbo[i] = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, fbo[i]);
            texture[i] = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture[i]);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, GL_RGBA, GL_FLOAT, NULL);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glFramebufferTexture2D(
                    GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture[i], 0
            );
            if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
                System.out.println("Framebuffer not complete!");
            }
        }
        glBindFramebuffer(GL_FRAMEBUFFER,0);

        shader.createVertexShader("VS_NDC.glsl");
        shader.createFragmentShader("FS_gaus.glsl");
        shader.createProgram();
        ndcShader.createVertexShader("VS_NDC.glsl");
        ndcShader.createFragmentShader("FS_NDC.glsl");
        ndcShader.createProgram();

        float[] position = new float[]{ // позиции на экране в НДС
                -1.0f,  1.0f, // левый верхний угол
                -1.0f, -1.0f, // левый нижний угол
                1.0f, -1.0f, // правый нижний угол
                1.0f,  1.0f // правый верхний угол
        };

        float[] texCoord = new float[]{ // координаты на поверхности
                0.0f,  1.0f, // левый верхний угол
                0.0f,  0.0f, // левый нижний угол
                1.0f,  0.0f, // правый нижний угол
                1.0f,  1.0f // правый верхний угол
        };

        int[] indices = new int[]{0,1,2,0,2,3}; // индексы для чтения

        ndcVbo.allocate(position,null,texCoord,null,null,null,null,null,indices);
    }

    public void useFrame(FrameBufferObject fbo){
        boolean horizontal = true;
        boolean first_iteration = true;
        int amount = 10;

        for (int i = 0; i < amount; i++){
            glBindFramebuffer(GL_FRAMEBUFFER, this.fbo[toInteger(horizontal)]);
            shader.useProgram();
            shader.setUniform("horizontal", toInteger(horizontal));
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, first_iteration ? fbo.getTexture(2) : texture[toInteger(!horizontal)]);
            shader.setUniform("image",0);
            ndcVbo.draw();
            horizontal = !horizontal;
            if (first_iteration) {
                first_iteration = false;
            }
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void reInit(){
        instance = new GausFrame();
    }

    public int getTexture(int index){
        return texture[index];
    }

    public static GausFrame getInstance(){
        if(instance == null){
            instance = new GausFrame();
        }
        return instance;
    }
}
