package ru.phoenix.core.buffer.fbo;

public interface FrameBufferObject {
    public int getFrameBuffer();
    public int getTexture();
    public int getTexture(int index);
    public int getTextureSize();
    public void delete();
}
