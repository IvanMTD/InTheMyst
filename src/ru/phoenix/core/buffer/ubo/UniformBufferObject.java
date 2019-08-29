package ru.phoenix.core.buffer.ubo;

public interface UniformBufferObject {
    public void allocate(int index);
    public void update();
    public void totalUpdate();
    public int getIndex();
}
