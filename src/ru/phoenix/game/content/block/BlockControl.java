package ru.phoenix.game.content.block;

import ru.phoenix.core.loader.LoadStaticModel;
import ru.phoenix.core.loader.model.Material;
import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class BlockControl {
    // описание
    private List<Mesh> meshes;
    private Projection projection;
    private Vector3f position;

    private int type;

    // конструкторы
    public BlockControl(){
        projection = new Projection();
        position = new Vector3f();
    }

    // методы
    protected void setMeshs(String path){
        meshes = LoadStaticModel.LoadModel(path,false);
    }

    protected void setMeshs(List<Mesh> meshes){
        this.meshes = new ArrayList<>(meshes);
    }

    public void setPosition(Vector3f position){
        this.position = position;
    }

    public void updateProjection(){
        projection.getModelMatrix().identity();
        projection.setTranslation(position);
    }

    public void draw(Shader shader){
        for(Mesh mesh : meshes){
            // setUniforms
            shader.useProgram();
            // глобальный юниформ
            shader.setUniformBlock("matrices",0);
            // контролеры
            shader.setUniform("animated",0);
            shader.setUniform("instance",mesh.getVbo().isInstances() ? 1 : 0);
            shader.setUniform("board",0);
            // доп данные
            shader.setUniform("model_m",projection.getModelMatrix());
            shader.setUniform("shininess",64.0f);
            shader.setUniform("group",GROUP_A);
            shader.setUniform("id",0.0f);
            shader.setUniform("onTarget", 0);
            // end

            int map = 0;
            for (Material material : mesh.getTextures()) {
                if (material.getType() == "ambientMap") {
                    map = material.getId();
                }
            }
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, map);
            shader.setUniform("material.ambientMap", 0);

            map = 0;
            for (Material material : mesh.getTextures()) {
                if (material.getType() == "diffuseMap") {
                    map = material.getId();
                }
            }
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, map);
            shader.setUniform("material.diffuseMap", 1);

            map = 0;
            for (Material material : mesh.getTextures()) {
                if (material.getType() == "specularMap") {
                    map = material.getId();
                }
            }
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, map);
            shader.setUniform("material.specularMap", 2);

            map = 0;
            for (Material material : mesh.getTextures()) {
                if (material.getType() == "normalMap") {
                    map = material.getId();
                }
            }
            glActiveTexture(GL_TEXTURE3);
            glBindTexture(GL_TEXTURE_2D, map);
            shader.setUniform("material.normalMap", 3);

            glEnable(GL_CULL_FACE);
            glFrontFace(GL_CCW);
            glCullFace(GL_BACK);
            mesh.draw();
            glDisable(GL_CULL_FACE);
        }
    }

    public int getType() {
        return type;
    }

    protected void setType(int type) {
        this.type = type;
    }

    protected List<Mesh> getMeshes(){
        return meshes;
    }

    public void setInstanceMatrix(Matrix4f[] matrix){
        for(Mesh mesh : meshes){
            mesh.setupInstance(matrix);
        }
    }
}
