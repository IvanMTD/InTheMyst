package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.logic.element.Pixel;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class Cursor {
    private Texture texture;
    private Texture redCursor;
    private Texture handCursor;

    private Shader shader;

    private VertexBufferObject vbo;
    private Projection projection;
    private Vector3f position;

    public Cursor(){
        texture = new Texture2D();
        redCursor = new Texture2D();
        handCursor = new Texture2D();

        shader = new Shader();

        vbo = new MeshConfig();
        projection = new Projection();
        position = new Vector3f();
    }

    public void init(){
        shader.createVertexShader("VS_cursor.glsl");
        shader.createFragmentShader("FS_cursor.glsl");
        shader.createProgram();
        redCursor.setup(null,"./data/content/texture/hud/cursors/main_cursor.png",GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        handCursor.setup(null,"./data/content/texture/hud/cursors/main_cursor_hand.png",GL_SRGB_ALPHA, GL_CLAMP_TO_BORDER);
        float size = 40.0f;
        float[] position = new float[]{
                0.0f,  0.0f,  0.0f,
                0.0f,  size,  0.0f,
                size,  size,  0.0f,
                size,  0.0f,  0.0f
        };

        float[] texCoord = new float[]{
                0.0f,1.0f,
                0.0f,0.0f,
                1.0f,0.0f,
                1.0f,1.0f
        };

        int[] indices = new int[]{
                0,1,2,
                0,2,3
        };

        vbo.allocate(position,null,texCoord,null,null,null,null,null,indices);
        setHandCursor();
    }

    public void update(List<Character> enemies){
        if(enemies != null) {
            setHandCursor();
            for (Character enemy : enemies) {
                if (enemy.getId() == Pixel.getPixel().getR()) {
                    setRedCursor();
                }
            }
        }
        Vector2f cursorPos = new Vector2f(Input.getInstance().getCursorPosition());
        this.position = new Vector3f(cursorPos.getX(),cursorPos.getY(),0.0f);
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
    }

    public void setRedCursor(){
        texture = redCursor;
    }

    public void setHandCursor(){
        texture = handCursor;
    }

    public void draw(){
        setUniforms(shader);
        vbo.draw();
    }

    private void setUniforms(Shader shader){
        shader.useProgram();
        shader.setUniformBlock("matrices",0);
        shader.setUniform("model_m",projection.getModelMatrix());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image",0);
    }
}
