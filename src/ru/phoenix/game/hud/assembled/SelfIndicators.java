package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.instance.Board;
import ru.phoenix.game.hud.instance.type.Indicator;

public class SelfIndicators {
    private Board mainBoard;
    private Board firstPointIndicator;
    private Board secondPointIndicator;

    public SelfIndicators(float width,Vector3f offsetPosition){
        mainBoard = new Indicator("./data/content/texture/boards/personal_indicator.png",width,offsetPosition);
        firstPointIndicator = new Indicator("./data/content/texture/boards/1_st_point.png",width,offsetPosition);
        secondPointIndicator = new Indicator("./data/content/texture/boards/2_st_point.png",width,offsetPosition);
    }

    public void update(Vector3f currentPosition){
        mainBoard.update(currentPosition);
        firstPointIndicator.update(currentPosition);
        secondPointIndicator.update(currentPosition);
    }

    public void draw(Shader shader){
        firstPointIndicator.draw(shader);
        secondPointIndicator.draw(shader);
        mainBoard.draw(shader);
    }
}
