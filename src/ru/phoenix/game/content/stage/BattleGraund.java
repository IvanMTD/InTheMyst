package ru.phoenix.game.content.stage;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface BattleGraund {
    void update();
    void draw(Shader shader);
    void drawSprites(Shader shader);
    void drawWater(Shader shader);
    void drawShadowSprites(Shader shader);
    int getMapX();
    int getMapZ();
    List<GridElement> getGridElements();
    List<Object> getSprites();
    List<Light> getDirectLight();
}
