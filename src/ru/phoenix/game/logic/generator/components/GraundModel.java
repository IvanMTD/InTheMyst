package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.shader.Shader;


import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class GraundModel {
    private Mesh mesh;
    private Texture diffuseMap;
    private Texture normalMap;

    public GraundModel(Mesh mesh, Texture diffuseMap, Texture normalMap){
        this.mesh = mesh;
        this.diffuseMap = diffuseMap;
        this.normalMap = normalMap;
    }

    public void draw(Shader shader){

        shader.setUniform("instance",mesh.getVbo().isInstances() ? 1 : 0);
        shader.setUniform("model_m",new Projection().getModelMatrix());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, diffuseMap.getTextureID());
        shader.setUniform("material.diffuseMap", 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, normalMap.getTextureID());
        shader.setUniform("material.normalMap", 1);

        mesh.draw();
    }
}
