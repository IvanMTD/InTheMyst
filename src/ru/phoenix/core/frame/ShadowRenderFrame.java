package ru.phoenix.core.frame;

import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.core.buffer.fbo.ShadowFrameBuffer;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.scene.Scene;

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static ru.phoenix.core.config.Constants.DIRECT_SHADOW;

public class ShadowRenderFrame implements Framework {

    private FrameBufferObject fbo;
    private Shader shader;

    public ShadowRenderFrame(){
        fbo = new ShadowFrameBuffer(1,DIRECT_SHADOW);
        shader = new Shader();
    }

    @Override
    public void init() {
        shader.createVertexShader("VS_direct_shadow.glsl");
        shader.createFragmentShader("FS_direct_shadow.glsl");
        shader.createProgram();
    }

    @Override
    public void draw(Scene scene) {
        glBindFramebuffer(GL_FRAMEBUFFER,fbo.getFrameBuffer());
        Window.getInstance().setViewport(
                Window.getInstance().getWidth() + Window.getInstance().getHeight(),
                Window.getInstance().getWidth() + Window.getInstance().getHeight()
        );
        glClear(GL_DEPTH_BUFFER_BIT);

        shader.useProgram();
        //Matrix4f[] matrix = scene.getLights().get(0).getLightSpaceMatrix();
        //shader.setUniform("lightSpaceMatrix",matrix[0]);
        //scene.drawShadow(shader);

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        Window.getInstance().setViewport(Window.getInstance().getWidth(), Window.getInstance().getHeight());
    }

    public void copyFrameBuffer(FrameBufferObject fbo) {

    }

    public void draw(FrameBufferObject fbo) {

    }

    @Override
    public void setFbo(FrameBufferObject fbo) {
        this.fbo = fbo;
    }

    @Override
    public FrameBufferObject getFbo() {
        return fbo;
    }
}
