package ru.phoenix.core.frame;

import org.lwjgl.BufferUtils;
import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.core.buffer.fbo.MultisampleFrameBuffer;
import ru.phoenix.core.buffer.fbo.OutputFrameBuffer;
import ru.phoenix.core.buffer.vbo.NormalizedDeviceCoordinates;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.scene.Scene;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES30.GL_DRAW_FRAMEBUFFER;

public class BaseRenderFrame implements Framework {
    private FrameBufferObject multisample;
    private FrameBufferObject render;
    private FrameBufferObject shadow;

    private VertexBufferObject ndcVbo;
    private Shader ndcShader;

    public BaseRenderFrame(){
        multisample = new MultisampleFrameBuffer(1);
        render = new OutputFrameBuffer(1);
        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();
    }

    public BaseRenderFrame(int num_of_fbo_texture){
        multisample = new MultisampleFrameBuffer(num_of_fbo_texture);
        render = new OutputFrameBuffer(num_of_fbo_texture);
        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();
    }

    @Override
    public void init(){
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

        GausFrame.getInstance().reInit();
    }

    @Override
    public void draw(Scene scene){
        // ЗАПИСЬ В МУЛЬТИСЕМПЕЛ БУФЕР
        glBindFramebuffer(GL_FRAMEBUFFER, multisample.getFrameBuffer());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(scene.getShader() != null && shadow != null) {
            scene.getShader().useProgram();
            scene.getShader().setUniformBlock("matrices", 0);
            scene.getShader().setUniform("viewPos", Camera.getInstance().getPerspective().getViewMatrix());
            Matrix4f[] matrix = null;
            if (scene.getLights() != null) {
                matrix = scene.getLights().get(0).getLightSpaceMatrix();
            }
            assert matrix != null;
            scene.getShader().setUniform("lightSpaceMatrix", matrix[0]);
            glActiveTexture(GL_TEXTURE5);
            glBindTexture(GL_TEXTURE_2D, shadow.getTexture());
            scene.getShader().setUniform("shadowMap", 5);
            scene.getShader().setUniform("directLight.position", scene.getLights().get(0).getPosition());
            scene.getShader().setUniform("directLight.ambient", scene.getLights().get(0).getAmbient());
            scene.getShader().setUniform("directLight.diffuse", scene.getLights().get(0).getDiffuse());
            scene.getShader().setUniform("directLight.specular", scene.getLights().get(0).getSpecular());
        }

        scene.draw();

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        // КОПИРОВАНИЕ ИЗ МУЛЬТИСЕМПЛА В РЕНДЕР БУФЕР
        glBindFramebuffer(GL_READ_FRAMEBUFFER, multisample.getFrameBuffer());
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.render.getFrameBuffer());
        for(int i=0; i<multisample.getTextureSize();i++) {
            glReadBuffer(GL_COLOR_ATTACHMENT0 + i);
            glDrawBuffer(GL_COLOR_ATTACHMENT0 + i);
            glBlitFramebuffer(
                    0, 0, Window.getInstance().getWidth(), Window.getInstance().getHeight(),
                    0, 0, Window.getInstance().getWidth(), Window.getInstance().getHeight(),
                    GL_COLOR_BUFFER_BIT, GL_LINEAR
            );
        }
        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glBindFramebuffer(GL_FRAMEBUFFER, render.getFrameBuffer());
        glReadBuffer(GL_COLOR_ATTACHMENT1);

        int[] viewport = new int[4];
        glGetIntegerv(GL_VIEWPORT,viewport);
        FloatBuffer data = BufferUtils.createFloatBuffer(4);
        glReadPixels(
                (int) Input.getInstance().getCursorPosition().getX(),
                viewport[3] - (int) Input.getInstance().getCursorPosition().getY(),
                1,1,GL_RGBA,GL_FLOAT,data
        );

        Vector3f pixel = new Vector3f(data.get(0),data.get(1),data.get(2));
        Pixel.setPixel(pixel);
        GausFrame.getInstance().useFrame(render);

        // ВЫВОД НА НДС ЭКРАН
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        ndcShader.useProgram();
        ndcShader.setUniform("gamma", Window.getInstance().getGamma());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, render.getTexture());
        ndcShader.setUniform("main_texture",0);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, GausFrame.getInstance().getTexture(1));
        ndcShader.setUniform("blur_texture",1);
        ndcVbo.draw();
    }

    @Override
    public void setFbo(FrameBufferObject fbo) {
        shadow = fbo;
    }

    @Override
    public FrameBufferObject getFbo() {
        return null;
    }
}
