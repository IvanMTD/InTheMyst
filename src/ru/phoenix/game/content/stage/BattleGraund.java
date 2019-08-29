package ru.phoenix.game.content.stage;

import ru.phoenix.core.shader.Shader;

public interface BattleGraund {
    public void draw(Shader shader);
    public int getMapX();
    public int getMapZ();
}
