package ru.phoenix.game.hud.instance;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class BoardControl {
    private Texture texture;
    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f mainPosition;

    protected BoardControl(){
        texture = new Texture2D();
        vbo = new MeshConfig();
        projection = new Projection();
        mainPosition = new Vector3f();
    }

    protected void init(String texturePath, float width, Vector3f mainPosition){
        this.mainPosition = mainPosition;
        texture.setup(null,texturePath,GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        float height = width / (texture.getWidth() * 1.0f / texture.getHeight());
        createVbo(width,height);
    }

    private void createVbo(float width, float height){
        float[] pos = new float[]{
                0.0f + mainPosition.getX(),   height + mainPosition.getY(), 0.0f + mainPosition.getZ(),
                0.0f + mainPosition.getX(),   0.0f + mainPosition.getY(),   0.0f + mainPosition.getZ(),
                width + mainPosition.getX(),  0.0f + mainPosition.getY(),   0.0f + mainPosition.getZ(),
                width + mainPosition.getX(),  height + mainPosition.getY(), 0.0f + mainPosition.getZ()
        };

        float[] tex = new float[]{
                0.0f,0.0f,
                0.0f,1.0f,
                1.0f,1.0f,
                1.0f,0.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };

        vbo.allocate(pos,null,tex,null,null,null,null,null,indices);
    }

    public void update(Vector3f gamePosition){
        projection.getModelMatrix().identity();
        projection.setTranslation(gamePosition);
    }

    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){
        shader.setUniform("instance",0);
        shader.setUniform("animated",0);
        shader.setUniform("board",1);
        shader.setUniform("isActive",0);
        shader.setUniform("turn",0);
        // доп данные
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("xOffset",0);
        shader.setUniform("yOffset",0);
        shader.setUniform("zOffset",0);
        shader.setUniform("group",GROUP_A);
        shader.setUniform("id",0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("water", 0);
        // end
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image",0);
    }
}