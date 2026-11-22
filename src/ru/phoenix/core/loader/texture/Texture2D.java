package ru.phoenix.core.loader.texture;

import ru.phoenix.core.loader.ImageLoader;
import ru.phoenix.core.math.Vector3f;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture2D implements Texture {
    private int textureID;
    private int width;
    private int height;
    private Vector3f[] imageLine;

    public Texture2D(){
        textureID = glGenTextures();
    }

    @Override
    public void setup(String[] pathArray, String path, int rgbaConfig, int filter) {
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            ImageLoader.load(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        upload(rgbaConfig, filter);
    }

    @Override
    public void setup(float[][] heiMap, int rgbaConfig, int filter) {
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            ImageLoader.load(heiMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        upload(rgbaConfig, filter);
    }

    @Override
    public void setup(Vector3f[][] heiMap, int rgbaConfig, int filter) {
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            ImageLoader.load(heiMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        upload(rgbaConfig, filter);
    }

    /**
     * Создание текстуры из PNG в памяти (карта высот из H2).
     * Запоминает пиксели, чтобы текстуру можно было снова сохранить при перезаписи слота.
     */
    public void setupPng(byte[] png, int rgbaConfig, int filter) {
        glBindTexture(GL_TEXTURE_2D, textureID);
        try {
            ImageLoader.load(png);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load heightmap image", e);
        }
        upload(rgbaConfig, filter);
    }

    private void upload(int rgbaConfig, int filter) {
        glTexImage2D(GL_TEXTURE_2D, 0, rgbaConfig, ImageLoader.getWidth(), ImageLoader.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, ImageLoader.getBuf());
        glGenerateMipmap(GL_TEXTURE_2D);

        setFilter(filter);

        width = ImageLoader.getWidth();
        height = ImageLoader.getHeight();
        imageLine = ImageLoader.getImageLine();

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void saveImage(String fileName){
        File fileDirection = new File("./data/save/image");
        File saveData = new File(fileDirection,fileName);

        try {
            Files.write(saveData.toPath(), toPng());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] toPng() {
        if (imageLine == null || width <= 0 || height <= 0) {
            throw new IllegalStateException("Texture has no pixel data to export as PNG");
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int index = 0;
        for(int h=0; h<height; h++){
            for(int w=0; w<width; w++){
                Vector3f pixel = imageLine[index++];
                Color color = new Color((int)pixel.getR(), (int)pixel.getG(), (int)pixel.getB());
                bufferedImage.setRGB(w, h, color.getRGB());
            }
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to encode texture as PNG", e);
        }
    }

    private void setFilter(int filter) {
        // GL_REPEAT (повторение текстуры), GL_MIRRORED_REPEAT (зеркальное повторение текстуры), GL_CLAMP_TO_EDGE(обрезать у празрачных текстур края)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    @Override
    public int getTextureID(){
        return textureID;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    /** Создать текстуру из массивки пикселей (ARGB формат) */
    public static Texture2D createTextureFromPixels(int[] pixels, int width, int height) {
        Texture2D texture = new Texture2D();
        texture.width = width;
        texture.height = height;

        // Конвертируем ARGB int[] в ByteBuffer
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(width * height * 4);
        buffer.order(java.nio.ByteOrder.nativeOrder());
        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
            buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
            buffer.put((byte) (pixel & 0xFF));          // B
            buffer.put((byte) ((pixel >> 24) & 0xFF));  // A
        }
        buffer.flip();

        glBindTexture(GL_TEXTURE_2D, texture.textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glBindTexture(GL_TEXTURE_2D, 0);

        return texture;
    }
}
