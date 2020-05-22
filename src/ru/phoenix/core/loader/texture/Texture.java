package ru.phoenix.core.loader.texture;

import ru.phoenix.core.math.Vector3f;

public interface Texture {
    void setup(String[] pathArray, String path, int rgbaConfig, int filter);
    void setup(float[][] heiMap, int rgbaConfig, int filter);
    void setup(Vector3f[][] heiMap, int rgbaConfig, int filter);
    void saveImage(String fileName);
    int getTextureID();
    int getWidth();
    int getHeight();
}
