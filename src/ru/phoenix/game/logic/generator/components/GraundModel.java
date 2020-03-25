package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.generator.Generator;


import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;

public class GraundModel {
    private Mesh mesh;
    private GraundTexture graundTexture;
    private Texture heightMap;

    public GraundModel(Mesh mesh, GraundTexture graundTexture){
        this.mesh = mesh;
        this.graundTexture = graundTexture;
        this.heightMap = Generator.getHeightMap();
    }

    public void draw(Shader shader){

        shader.setUniform("instance",mesh.getVbo().isInstances() ? 1 : 0);
        shader.setUniform("model_m",new Projection().getModelMatrix());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getGraundAtlas().getTextureID());
        shader.setUniform("material.diffuseMap", 0);

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getGraundAtlasNormalMap().getTextureID());
        shader.setUniform("material.normalMap", 1);

        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, heightMap.getTextureID());
        shader.setUniform("heightMap", 2);

        glActiveTexture(GL_TEXTURE10);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getDesert_low().getTextureID());
        shader.setUniform("material.desert_low", 10);

        glActiveTexture(GL_TEXTURE11);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getDesert_side().getTextureID());
        shader.setUniform("material.desert_side", 11);

        glActiveTexture(GL_TEXTURE12);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getDesert_up().getTextureID());
        shader.setUniform("material.desert_up", 12);

        glActiveTexture(GL_TEXTURE13);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getSteppe_low().getTextureID());
        shader.setUniform("material.steppe_low", 13);

        glActiveTexture(GL_TEXTURE14);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getSteppe_side().getTextureID());
        shader.setUniform("material.steppe_side", 14);

        glActiveTexture(GL_TEXTURE15);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getSteppe_up().getTextureID());
        shader.setUniform("material.steppe_up", 15);

        glActiveTexture(GL_TEXTURE16);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getPlain_low().getTextureID());
        shader.setUniform("material.plain_low", 16);

        glActiveTexture(GL_TEXTURE17);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getPlain_side().getTextureID());
        shader.setUniform("material.plain_side", 17);

        glActiveTexture(GL_TEXTURE18);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getPlain_up().getTextureID());
        shader.setUniform("material.plain_up", 18);

        glActiveTexture(GL_TEXTURE19);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getForest_low().getTextureID());
        shader.setUniform("material.forest_low", 19);

        glActiveTexture(GL_TEXTURE20);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getForest_side().getTextureID());
        shader.setUniform("material.forest_side", 20);

        glActiveTexture(GL_TEXTURE21);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getForest_up().getTextureID());
        shader.setUniform("material.forest_up", 21);

        glActiveTexture(GL_TEXTURE22);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getMountains_low().getTextureID());
        shader.setUniform("material.mountains_low", 22);

        glActiveTexture(GL_TEXTURE23);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getMountains_side().getTextureID());
        shader.setUniform("material.mountains_side", 23);

        glActiveTexture(GL_TEXTURE24);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getMountains_up().getTextureID());
        shader.setUniform("material.mountains_up", 24);

        glActiveTexture(GL_TEXTURE25);
        glBindTexture(GL_TEXTURE_2D, graundTexture.getRoad().getTextureID());
        shader.setUniform("material.road", 25);

        /*glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, lowDiffMap.getTextureID());
        shader.setUniform("material.lowDiffMap", 3);

        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, upDiffMap.getTextureID());
        shader.setUniform("material.upDiffMap", 4);*/

        mesh.draw();
    }
}
