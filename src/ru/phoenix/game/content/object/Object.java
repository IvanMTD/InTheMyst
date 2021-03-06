package ru.phoenix.game.content.object;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.Collection;
import java.util.List;

public interface Object {

    void init(Matrix4f[] matrix);
    void init(Matrix4f[] matrix, SaveElement saveElement);
    void update(Cell[][]grid, Vector3f pixel, Cell targetElement);
    boolean isApplying();
    List<Texture> getTextures();
    int getRecognition();
    void setRecognition(int recognition);
    boolean isBattle();
    void setBattle(boolean battle);
    int getTextureNum();
    float getObjectWidth();
    void setObjectWidth(float objectWidth);
    float getObjectHeight();
    void setObjectHeight(float objectHeight);

    void updateInstanceMatrix(Matrix4f[] matrix);
    float getDistance();
    void setDistance(float distance);
    Vector3f getPosition();
    void setPosition(Vector3f position);
    float getId();
    boolean isOnTarget();
    boolean isSelected();
    void setSelected(boolean selected);
    boolean isInstance();
    boolean isShadow();
    boolean isActive();
    void draw(Shader shader);
    void draw(Shader shader, boolean shadow);
    boolean isBoard();
    void setProjection(Projection projection);
    boolean isTree();
}
