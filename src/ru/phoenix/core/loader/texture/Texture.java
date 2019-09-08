package ru.phoenix.core.loader.texture;

public interface Texture {
    public void setup(String[] pathArray, String path, int rgbaConfig, int filter);
    public int getTextureID();
    public int getWidth();
    public int getHeight();
}
