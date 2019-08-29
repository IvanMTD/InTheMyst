package ru.phoenix.core.buffer.fbo;

import ru.phoenix.core.kernel.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.system.MemoryUtil.NULL;
import static ru.phoenix.core.config.Constants.*;

public class ShadowFrameBuffer implements FrameBufferObject {

    private int frameBuffer;
    private int texture[];
    private int config;

    public ShadowFrameBuffer(int numberOfTexture, int config){
        texture = new int[numberOfTexture];
        this.config = config;
        createFrameBuffer();
    }

    private void createFrameBuffer(){
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        for(int i=0; i<texture.length; i++) {
            createTexture(i);
            switch(config){
                case DIRECT_SHADOW:
                    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, texture[i], 0);
                    break;
                case POINT_SHADOW:
                    glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture[i], 0);
                    break;
                case SPOT_SHADOW:
                    glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture[i], 0);
                    break;
            }
        }
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
    }

    private void createTexture(int i){
        switch(config){
            case DIRECT_SHADOW:
                texture[i] = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, texture[i]);
                glTexImage2D(
                        GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                        Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                        Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                        0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL
                );
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
                float[] borderColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
                glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
                break;
            case POINT_SHADOW:
                texture[i] = glGenTextures();
                glBindTexture(GL_TEXTURE_CUBE_MAP, texture[i]);
                for (int j = 0; j < 6; j++) {
                    glTexImage2D(
                            GL_TEXTURE_CUBE_MAP_POSITIVE_X + j, 0, GL_DEPTH_COMPONENT,
                            Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                            Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                            0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL
                    );
                }
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
                break;
            case SPOT_SHADOW:
                texture[i] = glGenTextures();
                glBindTexture(GL_TEXTURE_CUBE_MAP, texture[i]);
                for (int j = 0; j < 6; j++) {
                    glTexImage2D(
                            GL_TEXTURE_CUBE_MAP_POSITIVE_X + j, 0, GL_DEPTH_COMPONENT,
                            Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                            Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                            0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL
                    );
                }
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
                break;
        }
    }

    @Override
    public int getFrameBuffer() {
        return frameBuffer;
    }

    @Override
    public int getTexture() {
        return texture[0];
    }

    @Override
    public int getTexture(int index) {
        return texture[index];
    }

    @Override
    public int getTextureSize() {
        return texture.length;
    }

    @Override
    public void delete() {
        glDeleteFramebuffers(frameBuffer);
        glDeleteTextures(texture);
    }
}
