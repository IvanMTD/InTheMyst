package ru.phoenix.game.content.characters;

import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.math.Vector4f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.property.Characteristic;
import ru.phoenix.game.hud.assembled.SelfIndicators;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.List;

public interface Character {
    // main class
    void setDefaultCharacteristic();
    void preset();
    void resetSettings();
    void interaction(Cell[][]grid, Cell targetElement, Vector3f pixel, List<Character> enemy, List<Character> ally, BattleGround battleGround);
    void update();
    void draw(Shader shader, boolean shadow);
    // методы гетеры и сетеры - начало
    // текстуры
    Texture getBaseStanceTextrue();
    void setBaseStanceTextrue(Texture baseStanceTextrue);
    Texture getWalkTexture();
    void setWalkTexture(Texture walkTexture);
    Texture getJumpTexture();
    void setJumpTexture(Texture jumpTexture);
    Texture getGoUpDownTexture();
    void setGoUpDownTexture(Texture goUpDownTexture);
    Texture getBattleStancePrepareTexture();
    void setBattleStancePrepareTexture(Texture battleStancePrepareTexture);
    Texture getBattleStanceTexture();
    void setBattleStanceTexture(Texture battleStanceTexture);
    Texture getBaseAttackTexture();
    void setBaseAttackTexture(Texture baseAttackTexture);
    Texture getBackstabTexture();
    void setBackstabTexture(Texture backstabTexture);
    // анимация
    ImageAnimation getBaseStanceAnimation();
    void setBaseStanceAnimation(ImageAnimation baseStanceAnimation);
    ImageAnimation getWalkAnimation();
    void setWalkAnimation(ImageAnimation walkAnimation);
    ImageAnimation getJumpAnimation();
    void setJumpAnimation(ImageAnimation jumpAnimation);
    ImageAnimation getGoUpDownAnimation();
    void setGoUpDownAnimation(ImageAnimation goUpDownAnimation);
    ImageAnimation getBattleStancePrepareAnimation();
    void setBattleStancePrepareAnimation(ImageAnimation battleStancePrepareAnimation);
    ImageAnimation getBattleStanceAnimation();
    void setBattleStanceAnimation(ImageAnimation battleStanceAnimation);
    ImageAnimation getBaseAttackAnimation();
    void setBaseAttackAnimation(ImageAnimation baseAttackAnimation);
    ImageAnimation getBackstabAttackAnimation();
    void setBackstabAttackAnimation(ImageAnimation backstabAttackAnimation);

    // draw class
    void updateAnimation(Texture texture, ImageAnimation animation);
    ImageAnimation getAnimation();
    void setAnimation(ImageAnimation animation);
    Texture getTexture();
    void setTexture(Texture texture);
    Projection getProjection();
    void setProjection(Projection projection);

    // control class
    // характеристики
    Characteristic getCharacteristic();
    void setCharacteristic(Characteristic characteristic);
    // контрль положения персонажа
    Vector3f getPosition();
    void setPosition(Vector3f position);
    Vector3f getLagerPoint();
    void setLagerPoint(Vector3f lagerPoint);
    boolean isTurn();
    void setTurn(boolean turn);
    void updateTemplate();
    Vector3f getPositionTemplate();
    // индификаторы персонажа
    SelfIndicators getSelfIndicators();
    void setSelfIndicators(SelfIndicators selfIndicators);
    float getId();
    void setId(float id);
    int getGroup();
    void setGroup(int group);
    int getRecognition();
    void setRecognition(int recognition);
    // контроль состояний персонажа
    boolean isTarget();
    void setTarget(boolean target);
    boolean isMarker();
    void setMarker(boolean marker);
    boolean isSelected();
    void setSelected(boolean selected);
    boolean isBattle();
    void setBattle(boolean battle);
    boolean isJump();
    void setJump(boolean jump);
    boolean isTakeDamadge();
    void setTakeDamadge(boolean takeDamadge);
    boolean isDead();
    void setDead(boolean dead);
    // вспомогательные
    int getPriority(Character character);
}
