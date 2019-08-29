package ru.phoenix.core.buffer.fbo;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;

public class MultisampleFrameBuffer implements FrameBufferObject {

    private int frameBuffer;
    private int renderBuffer;
    private int[] texture;

    public MultisampleFrameBuffer(int numberOfTexture){
        texture = new int[numberOfTexture];
        createFrameBuffer();
    }

    private void createFrameBuffer(){
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
        for(int i=0; i<texture.length;i++) {
            createTexture(i);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D_MULTISAMPLE, texture[i], 0);
        }
        createRenderBuffer();
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
        //int[] attachments = new int[]{GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1};
        int[] attachments = new int[texture.length];
        for(int i=0; i<attachments.length; i++){
            attachments[i] = GL_COLOR_ATTACHMENT0 + i;
        }
        glDrawBuffers(attachments);
        check();

    }

    private void createTexture(int i){
        texture[i] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, texture[i]);
        glTexImage2DMultisample(
                GL_TEXTURE_2D_MULTISAMPLE, WindowConfig.getInstance().getSamples(), GL_RGBA32F,
                Window.getInstance().getWidth(), Window.getInstance().getHeight(), true
        );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, 0);
    }

    private void createRenderBuffer(){
        renderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
        glRenderbufferStorageMultisample(
                GL_RENDERBUFFER, WindowConfig.getInstance().getSamples(), GL_DEPTH24_STENCIL8,
                Window.getInstance().getWidth(), Window.getInstance().getHeight()
        );
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
    }

    private void check(){
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("Framebuffer not complete!");
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
    public int getTextureSize(){
        return texture.length;
    }

    @Override
    public void delete() {
        glDeleteFramebuffers(frameBuffer);
        glDeleteRenderbuffers(renderBuffer);
        glDeleteTextures(texture);
    }
}
