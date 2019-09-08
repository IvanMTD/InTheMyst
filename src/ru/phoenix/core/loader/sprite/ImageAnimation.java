package ru.phoenix.core.loader.sprite;

import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.math.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ImageAnimation {
    private List<ImageFrame> frames;

    private int currentFrame;
    private int currentAnimation;

    public ImageAnimation(){
        frames = new ArrayList<>();
        currentFrame = 1;
        currentAnimation = 1;
    }

    public void addFrame(ImageFrame frame){
        frames.add(frame);
    }

    public void setCurrentAnimation(int animation){
        currentAnimation = animation;
    }

    public void updateInstanceMatrix(){
        for(ImageFrame frame : frames){
            frame.updateInstanceMatrix();
        }
    }

    public void nextFrame(){
        int size = 0;
        for(ImageFrame frame : frames){
            if(frame.getColumn() == currentAnimation){
                size++;
            }
        }
        currentFrame++;
        if(currentFrame > size){
            currentFrame = 1;
        }
    }

    public void draw(){
        for(ImageFrame frame : frames){
            if(frame.getColumn() == currentAnimation && frame.getRow() == currentFrame){
                frame.draw();
            }
        }
    }

    public VertexBufferObject getVbo(){
        return frames.get(0).getVbo();
    }
}
