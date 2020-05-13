package ru.phoenix.game.content.block;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

public interface Block {
    void draw(Shader shader);
    void setInstanceMatrix(Matrix4f[] matrix);
    void updateInstanceMatrix(Matrix4f[] matrix);
    void setPosition(Vector3f position);
    public void updateProjection();
    int getType();
    int getCost();
    void setCost(int cost);
    boolean isSimple();
    void setSimple(boolean simple);
}
