package ru.phoenix.core.loader.model;

import ru.phoenix.core.shader.Shader;

import java.util.Map;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class AnimatedModel {
    private Mesh[] meshes;
    private Map<String, Animation> animations;
    private Animation currentAnimation;

    public AnimatedModel(Mesh[] meshes, Map<String, Animation> animations) {
        this.meshes = meshes;
        this.animations = animations;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.map(Map.Entry::getValue).orElse(null);
    }

    public AnimatedModel(AnimatedModel animatedModel) {
        this.meshes = animatedModel.getMeshes();
        this.animations = animatedModel.getAnimations();
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.map(Map.Entry::getValue).orElse(null);
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public void setCurrentAnimation(String name){
        currentAnimation = animations.get(name);
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public void draw(Shader shader){
        for(Mesh mesh : meshes){
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

    public Mesh[] getMeshes(){
        return meshes;
    }

    public Map<String,Animation> getAnimations(){
        return animations;
    }
}
