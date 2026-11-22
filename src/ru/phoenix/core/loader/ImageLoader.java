package ru.phoenix.core.loader;

import ru.phoenix.core.math.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageLoader {
    private static ByteBuffer buf;
    private static Vector3f[] imageLine;
    private static int width;
    private static int height;

    public static void load(String path) throws IOException {
        BufferedImage img = ImageIO.read(new File(path));
        if (img == null) {
            throw new IOException("Image not found or unsupported: " + path);
        }
        fill(img, false);
    }

    /**
     * Загрузка изображения из памяти (например, карты высот сохранения из H2).
     * В отличие от загрузки с диска, запоминает пиксели, чтобы текстуру можно было снова сохранить.
     */
    public static void load(byte[] png) throws IOException {
        if (png == null || png.length == 0) {
            throw new IOException("Empty image data");
        }
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(png));
        if (img == null) {
            throw new IOException("Unsupported image data");
        }
        fill(img, true);
    }

    public static void load(float[][] heiMap) throws IOException {
        buf = null;
        width = heiMap.length - 1;
        height = heiMap[0].length - 1;
        buf = ByteBuffer.allocateDirect(height * width * 4);
        buf.limit(height * width * 4);
        imageLine = new Vector3f[height * width];

        int index = 0;
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) { // 0 - 255
                float hei = heiMap[x][y];
                int color = (int)(hei * 255);
                buf.put((byte)color);
                buf.put((byte)color);
                buf.put((byte)color);
                buf.put((byte)255);
                imageLine[index++] = new Vector3f(color,color,color);
            }
        }

        buf.flip();
    }

    private static void fill(BufferedImage img, boolean keepPixels) {
        buf = null;
        width = img.getWidth();
        height = img.getHeight();
        int pixels = width * height;
        buf = ByteBuffer.allocateDirect(pixels * 4);
        buf.limit(pixels * 4);
        imageLine = keepPixels ? new Vector3f[pixels] : null;

        int index = 0;
        for(int y=0;y<height;y++) {
            for(int x=0;x<width;x++) {
                Object pixelIm = img.getRaster().getDataElements(x, y, null);
                int r = img.getColorModel().getRed(pixelIm);
                int g = img.getColorModel().getGreen(pixelIm);
                int b = img.getColorModel().getBlue(pixelIm);
                int a = img.getColorModel().getAlpha(pixelIm);
                buf.put((byte)r);
                buf.put((byte)g);
                buf.put((byte)b);
                buf.put((byte)a);
                if (keepPixels) {
                    imageLine[index] = new Vector3f(r, g, b);
                }
                index++;
            }
        }

        buf.flip();
    }

    public static void load(Vector3f[][] blendMap) throws IOException {
        buf = null;
        width = blendMap.length - 1;
        height = blendMap[0].length - 1;
        buf = ByteBuffer.allocateDirect(height * width * 4);
        buf.limit(height * width * 4);
        imageLine = new Vector3f[height * width];

        int index = 0;
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                Vector3f color = blendMap[x][y];
                buf.put((byte)((int)(color.getR() * 255)));
                int R = (int)(color.getR() * 255);
                buf.put((byte)((int)(color.getG() * 255)));
                int G = (int)(color.getG() * 255);
                buf.put((byte)((int)(color.getB() * 255)));
                int B = (int)(color.getB() * 255);
                buf.put((byte)255);
                imageLine[index++] = new Vector3f(R,G,B);
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

    public static Vector3f[] getImageLine() {
        return imageLine;
    }
}
