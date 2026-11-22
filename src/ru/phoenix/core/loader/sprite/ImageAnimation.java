package ru.phoenix.core.loader.sprite;

import ru.phoenix.core.buffer.vbo.VertexBufferObject;
import ru.phoenix.core.math.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ImageAnimation {
    private List<ImageFrame> frames;

    private int currentFrame;
    private int currentAnimation;

    /** Время в секундах между сменой кадров (по умолчанию 0.33с ≈ 30 FPS анимации) */
    private float frameInterval;
    private float accumulatedTime;
    
    private boolean block;

    public ImageAnimation(){
        frames = new ArrayList<>();
        currentFrame = 1;
        currentAnimation = 1;
        frameInterval = 0.33f; // ~30 FPS для анимации
        accumulatedTime = 0.0f;
        block = false;
    }

    public void addFrame(ImageFrame frame){
        frames.add(frame);
    }

    public void setCurrentAnimation(int animation){
        currentAnimation = animation;
    }

    /**
     * Обновляет анимацию с учётом deltaTime.
     * Автоматически переключает кадр когда накоплено достаточно времени.
     */
    public void update(float deltaTime) {
        if (block) return;
        accumulatedTime += deltaTime;
        if (accumulatedTime >= frameInterval) {
            accumulatedTime -= frameInterval;
            nextFrame();
        }
    }

    /** Переключает на следующий кадр (без учёта времени) */
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

    /** Устанавливает интервал между кадрами в секундах */
    public void setCondition(int condition){
        // Обратная совместимость: condition — это количество кадров при ~60 FPS
        // 60 FPS = 0.0167s на кадр, condition=20 → 20*0.0167 ≈ 0.33s
        this.frameInterval = condition / 60.0f;
    }

    public int getCondition(){
        return (int)(frameInterval * 60.0f);
    }

    /** Устанавливает интервал между кадрами в секундах */
    public void setFrameInterval(float interval) {
        this.frameInterval = interval;
    }

    public float getFrameInterval() {
        return frameInterval;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void updateInstanceMatrix(Matrix4f[] matrix){
        for(ImageFrame frame : frames){
            frame.updateInstanceMatrix(matrix);
        }
    }
}
