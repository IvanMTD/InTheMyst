package ru.phoenix.core.loader.texture;

import ru.phoenix.core.loader.ImageLoader;
import ru.phoenix.core.math.Vector3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
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
        //GL_SRGB_ALPHA
        glTexImage2D(GL_TEXTURE_2D, 0, rgbaConfig, ImageLoader.getWidth(), ImageLoader.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, ImageLoader.getBuf());
        glGenerateMipmap(GL_TEXTURE_2D);

        setFilter(filter);

        width = ImageLoader.getWidth();
        height = ImageLoader.getHeight();

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void setup(float[][] heiMap, int rgbaConfig, int filter) {
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            ImageLoader.load(heiMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //GL_SRGB_ALPHA
        glTexImage2D(GL_TEXTURE_2D, 0, rgbaConfig, ImageLoader.getWidth(), ImageLoader.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, ImageLoader.getBuf());
        glGenerateMipmap(GL_TEXTURE_2D);

        setFilter(filter);

        width = ImageLoader.getWidth();
        height = ImageLoader.getHeight();
        imageLine = ImageLoader.getImageLine();

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void setup(Vector3f[][] heiMap, int rgbaConfig, int filter) {
        glBindTexture(GL_TEXTURE_2D, textureID);

        try {
            ImageLoader.load(heiMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //GL_SRGB_ALPHA
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

        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        int index = 0;
        for(int h=0;h<height; h++){
            for(int w=0; w<width; w++){
                Color color = new Color((int)imageLine[index].getR(),(int)imageLine[index].getG(),(int)imageLine[index].getB());
                bufferedImage.setRGB(w, h,color.getRGB());
                index++;
            }
        }

        try {
            ImageIO.write(bufferedImage,"png",saveData);
        } catch (IOException e) {
            e.printStackTrace();
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
}


