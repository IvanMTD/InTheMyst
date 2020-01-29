package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.generator.Generator;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.GROUP_A;

public class GraundAim {
    private String path;
    private VertexBufferObject vbo;
    private Texture texture;
    private Projection projection;
    private Vector3f position;
    private float bevelAngle;
    private boolean bevel;
    private boolean visible;

    public GraundAim(String path){
        this.path = path;
        this.vbo = new MeshConfig();
        this.texture = new Texture2D();
        this.projection = new Projection();
        this.position = new Vector3f();
        this.bevelAngle = 0.0f;
        this.bevel = false;
        this.visible = false;
    }

    public void init(){
        texture.setup(null,path,GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        float[] position = new float[]{
                -0.4f,  0.0f,  0.4f,
                -0.4f,  0.0f, -0.4f,
                 0.4f,  0.0f, -0.4f,
                 0.4f,  0.0f,  0.4f
        };

        float[] textureCoord = new float[]{
                0.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
                1.0f,1.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };

        vbo.allocate(position,null,textureCoord,null,null,null,null,null,indices);
    }

    public void update(Vector3f position, boolean bevel, float bevelAngle){
        this.position = position;
        this.bevel = bevel;
        this.bevelAngle = bevelAngle;
    }

    public void draw(Shader shader){
        if(isVisible()) {
            setUniforms(shader);
            vbo.draw();
        }
    }

    private void setUniforms(Shader shader){
        Default.setCursorAngle(Default.getCursorAngle() + 1.0f);
        if(Default.getCursorAngle() > 359.0f){
            Default.setCursorAngle(0.0f);
        }
        projection.getModelMatrix().identity();
        projection.setTranslation(new Vector3f(position.getX(),position.getY() + 0.1f,position.getZ()));
        if(bevel) {
            projection.setRotation(bevelAngle, new Vector3f(0.0f, 1.0f, 0.0f));
            projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
        }
        projection.setRotation(Default.getCursorAngle(),new Vector3f(0.0f,1.0f,0.0f));
        projection.setScaling(0.8f);
        // контролеры
        shader.setUniform("instance", 0);
        shader.setUniform("animated", 0);
        shader.setUniform("board", 0);
        shader.setUniform("grid",1);
        shader.setUniform("isActive", 0);
        shader.setUniform("bigTree",0);
        shader.setUniform("turn",0);
        shader.setUniform("discardReverse", 0);
        shader.setUniform("discardControl", -1.0f);
        shader.setUniform("battlefield",0);
        shader.setUniform("indicator",1);
        // доп данные
        shader.setUniform("model_m", projection.getModelMatrix());
        shader.setUniform("xOffset", 0.0f);
        shader.setUniform("yOffset", 0.0f);
        shader.setUniform("zOffset", 0.0f);
        shader.setUniform("group", GROUP_A);
        shader.setUniform("id", 0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("water",0);
        // end

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, Generator.getHeightMap().getTextureID());
        shader.setUniform("heightMap", 1);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getX(){
        return (int)position.getX();
    }

    public int getZ(){
        return (int)position.getZ();
    }
}
