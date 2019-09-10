package ru.phoenix.core.loader;

import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.sprite.ImageFrame;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;

public class ImageAnimLoader {
    public static ImageAnimation load(Texture texture, int row, int column, float sprite_width, float sprite_height, Matrix4f[] matrix, boolean centerPos){
        ImageAnimation imageAnimation = new ImageAnimation();

        int totalWidth = texture.getWidth();
        int totalHeight = texture.getHeight();

        float pictureWidth = (float)totalWidth / (float)row;
        float pictureHeight = (float)totalHeight / (float)column;

        float baseX = pictureWidth * 1.0f / totalWidth;
        float baseY = pictureHeight * 1.0f / totalHeight;

        for(int c = 1; c <= column; c++){
            for(int r = 1; r <= row; r++){
                ImageFrame frame = new ImageFrame(r,c);
                float[] pos = null;
                if(centerPos){
                    pos = new float[]{
                            -sprite_width / 2.0f, 0.0f,  sprite_height / 2.0f,
                            -sprite_width / 2.0f, 0.0f, -sprite_height / 2.0f,
                             sprite_width / 2.0f, 0.0f, -sprite_height / 2.0f,
                             sprite_width / 2.0f, 0.0f,  sprite_height / 2.0f
                    };
                }else {
                    pos = new float[]{
                            -sprite_width / 2.0f, sprite_height, 0.0f,
                            -sprite_width / 2.0f, 0.0f, 0.0f,
                             sprite_width / 2.0f, 0.0f, 0.0f,
                             sprite_width / 2.0f, sprite_height, 0.0f
                    };
                }

                float x = (pictureWidth * r) * 1.0f / totalWidth;
                float y = (pictureHeight * c) * 1.0f / totalHeight;

                float[] tex = new float[]{
                        x - baseX,  y - baseY,
                        x - baseX,  y,
                        x,          y,
                        x,          y - baseY
                };

                int[] indices = new int[]{
                        0,1,2,
                        0,2,3
                };
                if(matrix != null){
                    frame.init(pos,tex,indices,matrix);
                }else{
                    frame.init(pos,tex,indices);
                }
                imageAnimation.addFrame(frame);
            }
        }

        return imageAnimation;
    }
}
