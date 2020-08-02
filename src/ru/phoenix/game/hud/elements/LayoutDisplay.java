package ru.phoenix.game.hud.elements;

import ru.phoenix.core.buffer.vbo.MeshConfig;
import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.core.util.CollectorStruct;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public abstract class LayoutDisplay extends HudControl {
    private VertexBufferObject vbo;
    private Texture texture;
    private Projection projection;

    private float lastTime;
    private float timer;

    protected LayoutDisplay(){
        super();
        vbo = new MeshConfig();
        texture = new Texture2D();
        projection = new Projection();

        lastTime = 0.0f;
        timer = 0.0f;
    }

    protected void setTexture(Texture texture){
        this.texture = texture;
    }

    protected void setVbo(CollectorStruct cs){
        vbo.allocate(cs.getPosition(),null,cs.getTextureCoord(),null,null,null,null,null,cs.getIndices());
    }

    protected void updateProjection(){
        projection.getModelMatrix().identity();
        projection.setTranslation(getPosition());
        projection.setScaling(getSize());
    }

    protected void draw(Shader shader){
        if(!isHide()) {
            setUniforms(shader);
            vbo.draw();
        }
    }

    private void setUniforms(Shader shader){
        shader.setUniform("model_m",projection.getModelMatrix());
        shader.setUniform("tune",0);
        shader.setUniform("gamma", Window.getInstance().getGamma());
        shader.setUniform("onTarget",isFakeTarget() ? 0 : isTarget() ? 1 : 0);
        shader.setUniform("group", getGroup());
        shader.setUniform("id",getId());
        shader.setUniform("discardControl", isDiscard() ? 1 : 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        shader.setUniform("image", 0);
        if(getSize() != 1.0f){
            if(lastTime == 0.0f){
                lastTime = (float)glfwGetTime();
            }
            float currentTime = (float)glfwGetTime();
            float deltaTime = currentTime - lastTime;
            timer += deltaTime;
            lastTime = currentTime;
            if(timer > 0.2f){
                setSize(1.0f);
                timer = 0.0f;
                lastTime = 0.0f;
            }
        }
    }
}
