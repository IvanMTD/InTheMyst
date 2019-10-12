package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.instance.Board;
import ru.phoenix.game.hud.instance.type.Indicator;

public class SelfIndicators {
    private Board mainBoard;
    private Board firstPointIndicator;
    private Board secondPointIndicator;
    private Board healthIndicator;
    private Board mannaIndicator;
    private Board staminaIndicator;

    public SelfIndicators(float width,Vector3f offsetPosition){
        mainBoard = new Indicator("./data/content/texture/boards/personal_indicator.png",width,offsetPosition);
        firstPointIndicator = new Indicator("./data/content/texture/boards/1_st_point.png",width,offsetPosition);
        secondPointIndicator = new Indicator("./data/content/texture/boards/2_st_point.png",width,offsetPosition);
        healthIndicator = new Indicator("./data/content/texture/boards/health.png",width * 58.064f / 100.0f,offsetPosition.add(new Vector3f(0.061f,0.0f,0.0f)));
        mannaIndicator = new Indicator("./data/content/texture/boards/manna.png",width* 58.064f / 100.0f,offsetPosition.add(new Vector3f(0.061f,0.0f,0.0f)));
        staminaIndicator = new Indicator("./data/content/texture/boards/stamina.png",width* 58.064f / 100.0f,offsetPosition.add(new Vector3f(0.061f,0.0f,0.0f)));
    }

    public void update(Vector3f currentPosition){
        mainBoard.update(currentPosition);
        firstPointIndicator.update(currentPosition);
        secondPointIndicator.update(currentPosition);
        healthIndicator.update(currentPosition);
        mannaIndicator.update(currentPosition);
        staminaIndicator.update(currentPosition);
    }

    public void draw(Shader shader){
        healthIndicator.draw(shader);
        mannaIndicator.draw(shader);
        staminaIndicator.draw(shader);
        firstPointIndicator.draw(shader);
        secondPointIndicator.draw(shader);
        mainBoard.draw(shader);
    }
}
