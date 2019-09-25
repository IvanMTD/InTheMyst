package ru.phoenix.game.content.block;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.shader.Shader;

public interface Block {
    void draw(Shader shader);
    void setInstanceMatrix(Matrix4f[] matrix);
    int getType();
}
