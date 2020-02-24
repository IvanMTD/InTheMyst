package ru.phoenix.game.content.block;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.LoadStaticModel;
import ru.phoenix.core.loader.model.Material;
import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class BlockControl {
    // описание
    private List<Mesh> meshes;
    private Projection projection;
    private Vector3f position;

    private Texture texture;

    private int type;
    private int cost;

    private boolean isMeshTexture;

    // конструкторы
    public BlockControl(){
        projection = new Projection();
        position = new Vector3f();
        cost = 1;
    }

    // методы
    protected void setMeshs(String path, Texture texture){
        isMeshTexture = false;
        meshes = LoadStaticModel.LoadModel(path,true);
        this.texture = texture;
    }

    protected void setMeshs(String path){
        isMeshTexture = true;
        meshes = LoadStaticModel.LoadModel(path,false);
    }

    protected Texture getTexture(){
        return texture;
    }

    protected void setMeshs(List<Mesh> meshes, Texture texture){
        isMeshTexture = false;
        this.meshes = new ArrayList<>(meshes);
        this.texture = texture;
    }

    protected void setMeshs(List<Mesh> meshes){
        isMeshTexture = true;
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
            /*shader.useProgram();
            // глобальный юниформ
            shader.setUniformBlock("matrices",0);*/
            // контролеры
            shader.setUniform("instance",mesh.getVbo().isInstances() ? 1 : 0);
            shader.setUniform("board",0);
            // доп данные
            shader.setUniform("model_m",projection.getModelMatrix());
            shader.setUniform("shininess",64.0f);
            shader.setUniform("group",GROUP_A);
            shader.setUniform("id",0.0f);
            shader.setUniform("onTarget", 0);
            // end
            shader.setUniform("instance",mesh.getVbo().isInstances() ? 1 : 0);
            // доп данные
            shader.setUniform("model_m",projection.getModelMatrix());

            if(isMeshTexture){
                int map = 0;
                for (Material material : mesh.getTextures()) {
                    if (Objects.equals(material.getType(), "ambientMap")) {
                        map = material.getId();
                    }
                }
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, map);
                shader.setUniform("material.ambientMap", 0);

                map = 0;
                for (Material material : mesh.getTextures()) {
                    if (Objects.equals(material.getType(), "diffuseMap")) {
                        map = material.getId();
                    }
                }
                glActiveTexture(GL_TEXTURE1);
                glBindTexture(GL_TEXTURE_2D, map);
                shader.setUniform("material.diffuseMap", 1);

                map = 0;
                for (Material material : mesh.getTextures()) {
                    if (Objects.equals(material.getType(), "specularMap")) {
                        map = material.getId();
                    }
                }
                glActiveTexture(GL_TEXTURE2);
                glBindTexture(GL_TEXTURE_2D, map);
                shader.setUniform("material.specularMap", 2);

                map = 0;
                for (Material material : mesh.getTextures()) {
                    if (Objects.equals(material.getType(), "normalMap")) {
                        map = material.getId();
                    }
                }
                glActiveTexture(GL_TEXTURE3);
                glBindTexture(GL_TEXTURE_2D, map);
                shader.setUniform("material.normalMap", 3);
            }else{
                glActiveTexture(GL_TEXTURE4);
                glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
                shader.setUniform("material.diffuseMap", 4);
            }

            mesh.draw();
        }
    }

    public int getType() {
        return type;
    }

    protected void setType(int type) {
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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
