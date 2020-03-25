package ru.phoenix.core.loader.texture;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.shader.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE0;

public class Skybox {
    private Shader shader;
    private Texture texture;
    private VertexBufferObject vbo;
    private Projection projection;

    private float[] vertices;
    private String[] path;

    public Skybox(){
        shader = new Shader();
        texture = new TextureCubeMap();
        vbo = new MeshConfig();
        projection = new Projection();
        createSkybox();
    }

    public void init(){
        texture.setup(path,null,GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);

        shader.createVertexShader("VS_skybox.glsl");
        shader.createFragmentShader("FS_skybox.glsl");
        shader.createProgram();

        vbo.allocate(vertices,null,null,null,null,null,null,null,null);
    }

    public void draw(){
        glDepthFunc(GL_LEQUAL);
        shader.useProgram();
        setUniform();
        vbo.draw();
        glDepthFunc(GL_LESS);
    }

    private void setUniform() {
        shader.setUniformBlock("matrices",0);
        // Передаем текстуру
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture.getTextureID());
        shader.setUniform("skybox",0);
    }

    private void createSkybox() {

        String SKYBOX_TEX_DIR = "./data/content/texture/skybox/";
        path = new String[]{
                SKYBOX_TEX_DIR + "right.png",
                SKYBOX_TEX_DIR + "left.png",
                SKYBOX_TEX_DIR + "top.png",
                SKYBOX_TEX_DIR + "bottom.png",
                SKYBOX_TEX_DIR + "front.png",
                SKYBOX_TEX_DIR + "back.png",
        };

        vertices = new float[]{
                -1.0f,  1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                -1.0f,  1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                1.0f, -1.0f,  1.0f
        };
    }

    public Projection getTransform() {
        return projection;
    }

    public int getCubeMap(){
        return texture.getTextureID();
    }
}
