package ru.phoenix.core.loader.model;

import java.util.List;

public class Animation {
    private int currentFrame;
    private List<AnimatedFrame> frames;
    private String name;
    private double duration;

    public Animation(String name, List<AnimatedFrame> frames, double duration) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        this.duration = duration;
    }

    public AnimatedFrame getCurrentFrame() {
        return this.frames.get(currentFrame);
    }

    public AnimatedFrame getCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        return this.frames.get(currentFrame);
    }

    public double getDuration() {
        return this.duration;
    }

    public List<AnimatedFrame> getFrames() {
        return frames;
    }

    public String getName() {
        return name;
    }

    public AnimatedFrame getNextFrame() {
        int nextFrame = currentFrame + 1;
        if(nextFrame > frames.size()){
            nextFrame = 0;
        }
        return this.frames.get(nextFrame);
    }

    public void nextFrame() {
        int nextFrame = currentFrame + 1;
        if (nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }
    }
}
