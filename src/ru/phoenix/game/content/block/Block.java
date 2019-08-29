package ru.phoenix.game.content.block;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.shader.Shader;

public interface Block {
    public void draw(Shader shader);
    public void setInstanceMatrix(Matrix4f[] matrix);
}
