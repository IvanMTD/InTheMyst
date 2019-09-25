package ru.phoenix.core.frame;

import ru.phoenix.core.buffer.fbo.FrameBufferObject;
import ru.phoenix.game.scene.Scene;

public interface Framework {
    void init();
    void draw(Scene scene);
    void setFbo(FrameBufferObject fbo);
    FrameBufferObject getFbo();
}
