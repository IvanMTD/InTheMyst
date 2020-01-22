package ru.phoenix.core.loader.texture;

public interface Texture {
    void setup(String[] pathArray, String path, int rgbaConfig, int filter);
    void setup(float[][] heiMap, int rgbaConfig, int filter);
    int getTextureID();
    int getWidth();
    int getHeight();
}
