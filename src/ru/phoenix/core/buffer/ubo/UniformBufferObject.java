package ru.phoenix.core.buffer.ubo;

public interface UniformBufferObject {
    void allocate(int index);
    void update();
    void totalUpdate();
    int getIndex();
}
