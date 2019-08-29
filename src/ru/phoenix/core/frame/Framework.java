package ru.phoenix.core.frame;

import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.game.scene.Scene;

public interface Framework {
    public void init();
    public void draw(Scene scene);
    public void setFbo(FrameBufferObject fbo);
    public FrameBufferObject getFbo();
}
