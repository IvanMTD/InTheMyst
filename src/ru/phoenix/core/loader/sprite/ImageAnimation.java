package ru.phoenix.core.loader.sprite;

import ru.phoenix.core.buffer.vbo.VertexBufferObject;

import java.util.ArrayList;
import java.util.List;

public class ImageAnimation {
    private List<ImageFrame> frames;

    private int currentFrame;
    private int currentAnimation;

    private int condition;
    private boolean block;

    public ImageAnimation(){
        frames = new ArrayList<>();
        currentFrame = 1;
        currentAnimation = 1;
        condition = 20;
        block = false;
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

    public void setFrames(int currentFrame){
        this.currentFrame = currentFrame;
    }

    public void draw(){
        for(ImageFrame frame : frames){
            if(frame.getColumn() == currentAnimation && frame.getRow() == currentFrame){
                frame.draw();
            }
        }
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

    public VertexBufferObject getVbo(){
        return frames.get(0).getVbo();
    }

    public void setCondition(int condition){
        this.condition = condition;
    }

    public int getCondition(){
        return condition;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}
