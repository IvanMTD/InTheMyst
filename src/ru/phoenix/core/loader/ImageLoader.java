package ru.phoenix.core.loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageLoader {
    private static ByteBuffer buf;
    private static int width;
    private static int height;

    public static void load(String path) throws IOException {
        BufferedImage img;
        img = ImageIO.read(new File(path));

        buf = null;
        width = img.getWidth();
        height = img.getHeight();
        buf = ByteBuffer.allocateDirect(img.getHeight()*img.getWidth()*4);
        buf.limit(img.getHeight()*img.getWidth()*4);

        for(int y=0;y<img.getHeight();y++) {
            for(int x=0;x<img.getWidth();x++) {
                Object pixelIm = img.getRaster().getDataElements(x, y, null);
                buf.put((byte)img.getColorModel().getRed(pixelIm));
                buf.put((byte)img.getColorModel().getGreen(pixelIm));
                buf.put((byte)img.getColorModel().getBlue(pixelIm));
                buf.put((byte)img.getColorModel().getAlpha(pixelIm));
            }
        }

        buf.flip();
    }

    public static void load(float[][] heiMap) throws IOException {
        buf = null;
        width = heiMap.length - 1;
        height = heiMap[0].length - 1;
        buf = ByteBuffer.allocateDirect(height * width * 4);
        buf.limit(height * width * 4);

        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) { // 0 - 255
                float hei = heiMap[x][y];
                int color = (int)(hei * 255);
                buf.put((byte)color);
                buf.put((byte)color);
                buf.put((byte)color);
                buf.put((byte)255);
            }
        }

        buf.flip();
    }

    public static ByteBuffer getBuf() {
        return buf;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
