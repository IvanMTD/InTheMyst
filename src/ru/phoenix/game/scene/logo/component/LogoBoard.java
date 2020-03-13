package ru.phoenix.game.scene.logo.component;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.effects.Effects;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class LogoBoard {
    // render
    private VertexBufferObject vbo;
    private Texture texture;
    // positioning
    private Projection projection;
    private Vector3f position;
    // info variable
    private float sizeInPercent;
    private float width;
    private float height;
    private boolean tune;

    public LogoBoard(boolean tune){
        vbo = new MeshConfig();
        texture = new Texture2D();

        projection = new Projection();
        position = new Vector3f();

        this.tune = tune;
    }

    public void init(String texturePath, Vector3f position, float sizeInPercent){
        texture.setup(null,texturePath,GL_SRGB_ALPHA, GL_CLAMP_TO_EDGE);
        this.position = position;
        this.sizeInPercent = sizeInPercent;
        projection.setTranslation(position);
        createBoard(texture.getWidth(), texture.getHeight());
    }

    private void createBoard(int width, int height){

        float winHei = (float) Window.getInstance().getHeight();
        this.height = winHei * sizeInPercent / 100.0f;
        float percent = this.height * 100.0f / height;
        this.width = width * percent / 100.0f;
        float offsetW = this.width / 2;
        float offsetH = this.height / 2;

        float[] pos = new float[]{
                -offsetW,  offsetH, 0.0f,
                -offsetW, -offsetH, 0.0f,
                offsetW, -offsetH, 0.0f,
                offsetW,  offsetH, 0.0f
        };

        float[] tex = new float[]{
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };

        vbo.allocate(pos,null,tex,null,null,null,null,null,indices);
    }

    public void draw(Shader shader){
        setUniforms(shader);
        vbo.draw();
    }

    public Projection getProjection() {
        return projection;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position){
        this.position = position;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    private void setUniforms(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("projection",0);
        shader.setUniform("tune",tune ? 1 : 0);
        shader.setUniform("onTarget",0);
        shader.setUniform("id",0.0f);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        if(tune) {
            shader.setUniform("gamma", Effects.getGammaAttenuation(10.0f));
        }
    }
}
