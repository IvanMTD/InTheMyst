package ru.phoenix.game.content.stage;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.lighting.Light;

import java.util.List;

public interface StudyArea {
    void update(Cell targetElement, Vector3f pixel);
    void draw(Shader shader);
    void drawSprites(Shader shader);
    void drawTrees(Shader shader);
    void drawPersons(Shader shader);
    void drawMask(Shader shader);
    void drawWater(Shader shader);
    void drawShadowSprites(Shader shader);
    void drawShadowPersons(Shader shader, boolean shadow);
    int getMapX();
    int getMapZ();
    Cell[][] getGrid();
    List<Object> getSprites();
    List<Character> getAllies();
    List<Character> getEnemies();
    List<Light> getDirectLight();
    boolean isWater();
    BattleGround getBattleGround();
}
