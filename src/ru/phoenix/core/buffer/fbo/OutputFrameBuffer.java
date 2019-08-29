package ru.phoenix.core.buffer.fbo;

import ru.phoenix.core.kernel.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OutputFrameBuffer implements FrameBufferObject {

    private int frameBuffer;
    private int[] texture;

    public OutputFrameBuffer(int numberOfTexture){
        texture = new int[numberOfTexture];
        createFrameBuffer();
    }

    private void createFrameBuffer(){
        frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,frameBuffer);
        for(int i=0; i<texture.length;i++) {
            createTexture(i);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, texture[i], 0);
        }
        int[] attachments = new int[texture.length];
        for(int i=0; i<attachments.length;i++){
            attachments[i] = GL_COLOR_ATTACHMENT0 + i;
        }
        glDrawBuffers(attachments);
        check();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void createTexture(int i){
        texture[i] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture[i]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, GL_RGBA, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);
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
    public int getTexture(){
        return texture[0];
    }

    @Override
    public int getTexture(int index){
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
