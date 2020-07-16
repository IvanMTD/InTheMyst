package ru.phoenix.core.frame;

import org.lwjgl.BufferUtils;
import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.core.buffer.fbo.MultisampleFrameBuffer;
import ru.phoenix.core.buffer.fbo.OutputFrameBuffer;
import ru.phoenix.core.buffer.vbo.NormalizedDeviceCoordinates;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.property.GameController;
import ru.phoenix.game.scene.Scene;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static org.lwjgl.opengl.GL30.*;

public class BaseRenderFrame implements Framework {
    private FrameBufferObject multisample;
    private FrameBufferObject render;
    private FrameBufferObject shadow;
    private FrameBufferObject map;

    private VertexBufferObject ndcVbo;
    private Shader ndcShader;

    private int count;
    private int index;
    private int random;
    private Texture texture;
    private Texture w1;
    private Texture w2;
    private Texture w3;
    private Texture w4;
    private Texture w5;
    private Texture w6;

    public BaseRenderFrame(){
        multisample = new MultisampleFrameBuffer(1);
        render = new OutputFrameBuffer(1);
        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();

        count = 0;
        index = 0;
        texture = new Texture2D();
        w1 = new Texture2D();
        w2 = new Texture2D();
        w3 = new Texture2D();
        w4 = new Texture2D();
        w5 = new Texture2D();
        w6 = new Texture2D();
    }

    public BaseRenderFrame(int num_of_fbo_texture){
        multisample = new MultisampleFrameBuffer(num_of_fbo_texture);
        render = new OutputFrameBuffer(num_of_fbo_texture);
        ndcVbo = new NormalizedDeviceCoordinates();
        ndcShader = new Shader();

        count = 0;
        index = 0;
        texture = new Texture2D();
        w1 = new Texture2D();
        w2 = new Texture2D();
        w3 = new Texture2D();
        w4 = new Texture2D();
        w5 = new Texture2D();
        w6 = new Texture2D();
    }

    @Override
    public void init(){
        ndcShader.createVertexShader("VS_NDC.glsl");
        ndcShader.createFragmentShader("FS_NDC.glsl");
        ndcShader.createProgram();
        random = (int)(5.0f + Math.random() * 10.0f);

        w1.setup(null,"./data/content/texture/hud/warfog/wf1.png",GL_SRGB_ALPHA,GL_REPEAT);
        w2.setup(null,"./data/content/texture/hud/warfog/wf2.png",GL_SRGB_ALPHA,GL_REPEAT);
        w3.setup(null,"./data/content/texture/hud/warfog/wf3.png",GL_SRGB_ALPHA,GL_REPEAT);
        w4.setup(null,"./data/content/texture/hud/warfog/wf4.png",GL_SRGB_ALPHA,GL_REPEAT);
        w5.setup(null,"./data/content/texture/hud/warfog/wf5.png",GL_SRGB_ALPHA,GL_REPEAT);
        w6.setup(null,"./data/content/texture/hud/warfog/wf6.png",GL_SRGB_ALPHA,GL_REPEAT);
        texture = w1;

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
            scene.getShader().setUniform("viewPos", Camera.getInstance().getPos());
            glActiveTexture(GL_TEXTURE6);
            glBindTexture(GL_TEXTURE_2D, Default.getMapTextureId());
            scene.getShader().setUniform("map",6);
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
        ndcShader.setUniform("contrast",Window.getInstance().getContrast());
        if(scene.getSceneId() == Constants.SCENE_LOGO){
            ndcShader.setUniform("contrast",0.0f);
        }
        // main texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, render.getTexture());
        ndcShader.setUniform("main_texture",0);

        glActiveTexture(GL_TEXTURE1);
        /*if(GameController.getInstance().isTabHold()){
            glBindTexture(GL_TEXTURE_2D, Generator.getHeightMap().getTextureID()); // отладочный для проверки heimap
            //glBindTexture(GL_TEXTURE_2D, map.getTexture()); // отладочный для проверки war fog
            //glBindTexture(GL_TEXTURE_2D, shadow.getTexture()); // отладочный для проверки карты теней
            //glBindTexture(GL_TEXTURE_2D, render.getTexture(1)); // отладочный для проверки выборочного фреймбуфера
            ndcShader.setUniform("shadow",1);
        }else{*/
            glBindTexture(GL_TEXTURE_2D, GausFrame.getInstance().getTexture(1)); // Основной рендер
            ndcShader.setUniform("shadow",2);
        //}
        //glBindTexture(GL_TEXTURE_2D, GausFrame.getInstance().getTexture(1)); // Основной рендер
        //glBindTexture(GL_TEXTURE_2D, render.getTexture(1)); // отладочный для проверки выборочного фреймбуфера
        //glBindTexture(GL_TEXTURE_2D, shadow.getTexture()); // отладочный для проверки карты теней
        ndcShader.setUniform("blur_texture",1);
        glActiveTexture(GL_TEXTURE2);
        if(count > random){
            count = 0;
            random = (int)(5.0f + Math.random() * 10.0f);
            index++;
            if (index > 5){
                index = 0;
            }
            if(index == 0){
                texture = w1;
            }else if(index == 1){
                texture = w2;
            }else if(index == 2){
                texture = w3;
            }else if(index == 3){
                texture = w4;
            }else if(index == 4){
                texture = w5;
            }else if(index == 5){
                texture = w6;
            }
        }
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        ndcShader.setUniform("warFog",2);

        ndcVbo.draw();
        count++;
    }

    @Override
    public void setFbo(FrameBufferObject fbo, int config) {
        if(config == 0) {
            shadow = fbo;
        }else{
            map = fbo;
        }
    }

    @Override
    public FrameBufferObject getFbo() {
        return null;
    }
}
