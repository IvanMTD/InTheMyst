package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.instance.Board;
import ru.phoenix.game.hud.instance.type.Indicator;

public class SelfIndicators {
    private Board mainBoard;
    private Board firstPointIndicator;

    public SelfIndicators(float width,Vector3f offsetPosition){
        mainBoard = new Indicator("./data/content/texture/boards/personal_indicator.png",width,offsetPosition);
        firstPointIndicator = new Indicator("./data/content/texture/boards/1_st_point.png",width,new Vector3f(offsetPosition.getX(),offsetPosition.getY(),offsetPosition.getZ() + 0.01f));
    }

    public void update(Vector3f currentPosition){
        mainBoard.update(currentPosition);
        firstPointIndicator.update(currentPosition);
    }

    public void draw(Shader shader){
        firstPointIndicator.draw(shader);
        mainBoard.draw(shader);
    }
}
