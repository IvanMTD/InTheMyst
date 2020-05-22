package ru.phoenix.core.loader.texture;

import ru.phoenix.core.loader.ImageLoader;
import ru.phoenix.core.math.Vector3f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

public class TextureCubeMap implements Texture {
    private int textureID;

    public TextureCubeMap(){
        textureID = glGenTextures();
    }

    @Override
    public void setup(String[] pathArray, String path, int rgbaConfig, int filter){

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

        for(int i=0;i<pathArray.length; i++){
            try {
                ImageLoader.load(pathArray[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, rgbaConfig, ImageLoader.getWidth(), ImageLoader.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, ImageLoader.getBuf());
        }

        setFilter(filter);

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
    }

    @Override
    public void setup(float[][] heiMap, int rgbaConfig, int filter) {

    }

    @Override
    public void setup(Vector3f[][] heiMap, int rgbaConfig, int filter) {

    }

    @Override
    public void saveImage(String fileName) {

    }

    private void setFilter(int filter) {
        // GL_REPEAT (повторение текстуры), GL_MIRRORED_REPEAT (зеркальное повторение текстуры), GL_CLAMP_TO_EDGE(обрезать у празрачных текстур края)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, filter);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, filter);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, filter);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    @Override
    public int getTextureID(){
        return textureID;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}


