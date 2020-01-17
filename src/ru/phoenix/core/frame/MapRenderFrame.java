package ru.phoenix.core.frame;

import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.core.buffer.fbo.MultisampleFrameBuffer;
import ru.phoenix.core.buffer.fbo.OutputFrameBuffer;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.scene.Scene;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES30.GL_DRAW_FRAMEBUFFER;

public class MapRenderFrame implements Framework {
    private FrameBufferObject multisample;
    private FrameBufferObject render;
    private Shader shader;
    private int start;

    public MapRenderFrame(){
        multisample = new MultisampleFrameBuffer(1);
        render = new OutputFrameBuffer(1);
        shader = new Shader();
        start = 0;
    }

    @Override
    public void init() {
        shader.createVertexShader("VS_map.glsl");
        shader.createFragmentShader("FS_map.glsl");
        shader.createProgram();
    }

    @Override
    public void draw(Scene scene) {
        glBindFramebuffer(GL_FRAMEBUFFER,multisample.getFrameBuffer());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.useProgram();
        shader.setUniform("start",start);
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, render.getTexture());
        shader.setUniform("map",5);
        scene.draw(shader);
        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glBindFramebuffer(GL_READ_FRAMEBUFFER, multisample.getFrameBuffer());
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, render.getFrameBuffer());
        for(int i=0; i<multisample.getTextureSize();i++) {
            glReadBuffer(GL_COLOR_ATTACHMENT0 + i);
            glDrawBuffer(GL_COLOR_ATTACHMENT0 + i);
            glBlitFramebuffer(
                    0, 0, Window.getInstance().getWidth(), Window.getInstance().getHeight(),
                    0, 0,  Window.getInstance().getWidth(), Window.getInstance().getHeight(),
                    GL_COLOR_BUFFER_BIT, GL_LINEAR
            );
        }
        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glBindFramebuffer(GL_FRAMEBUFFER, render.getFrameBuffer());
        glReadBuffer(GL_COLOR_ATTACHMENT1);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        start = 1;
    }

    @Override
    public void setFbo(FrameBufferObject fbo, int config) {

    }

    public void copyFrameBuffer(FrameBufferObject fbo) {

    }

    public void draw(FrameBufferObject fbo) {

    }

    @Override
    public FrameBufferObject getFbo() {
        return render;
    }
}
