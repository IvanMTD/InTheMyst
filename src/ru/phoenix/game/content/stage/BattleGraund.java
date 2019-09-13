package ru.phoenix.game.content.stage;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.logic.generator.component.GridElement;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface BattleGraund {
    public void update();
    public void draw(Shader shader);
    public void drawSprites(Shader shader);
    public void drawWater(Shader shader);
    public void drawShadowSprites(Shader shader);
    public int getMapX();
    public int getMapZ();
    public List<GridElement> getGridElements();
    public List<Object> getSprites();
    public List<Light> getDirectLight();
}
