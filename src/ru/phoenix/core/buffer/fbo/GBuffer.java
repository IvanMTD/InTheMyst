package ru.phoenix.core.buffer.fbo;

import ru.phoenix.core.kernel.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GBuffer implements FrameBufferObject {
    private int gBuffer;
    private int renderBuffer;
    private int[] texture;

    public GBuffer(){
        texture = new int[3];
        createGBuffer();
    }

    private void createGBuffer(){
        gBuffer = glGenFramebuffers();
        // Создаем фреймбуфер
        glBindFramebuffer(GL_FRAMEBUFFER,gBuffer);
        createTextures();
        createRenderBuffer();
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
        int[] attachments = new int[texture.length];
        for(int i=0; i<attachments.length;i++){
            attachments[i] = GL_COLOR_ATTACHMENT0 + i;
        }
        glDrawBuffers(attachments);
        check();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void createTextures(){
        // буфер позиций
        texture[0] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture[0]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, GL_RGB, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glBindTexture(GL_TEXTURE_2D, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture[0], 0);
        // буфер нормалей
        texture[1] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture[1]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, GL_RGB, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, texture[1], 0);
        // буфер цвета + коифецент зеркального отражения
        texture[2] = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture[2]);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Window.getInstance().getWidth(), Window.getInstance().getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D, texture[2], 0);

    }

    private void createRenderBuffer(){
        renderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, Window.getInstance().getWidth(), Window.getInstance().getHeight());
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
    }

    private void check(){
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("Framebuffer not complete!");
        }
    }

    @Override
    public int getFrameBuffer() {
        return gBuffer;
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
        glDeleteFramebuffers(gBuffer);
        glDeleteTextures(texture);
    }
}
