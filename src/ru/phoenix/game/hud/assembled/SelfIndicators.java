package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.hud.instance.Board;
import ru.phoenix.game.hud.instance.type.Indicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelfIndicators {

    private List<Board> boardList;

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
        healthIndicator = new Indicator("./data/content/texture/boards/health.png",width * 63.5f / 100.0f,offsetPosition);
        mannaIndicator = new Indicator("./data/content/texture/boards/manna.png",width * 63.5f / 100.0f,offsetPosition);
        staminaIndicator = new Indicator("./data/content/texture/boards/stamina.png",width * 63.5f / 100.0f,offsetPosition);

        boardList = new ArrayList<>(Arrays.asList(healthIndicator,mannaIndicator,staminaIndicator,firstPointIndicator,secondPointIndicator,mainBoard));
    }

    public void update(Vector3f currentPos, Characteristic characteristic){
        // основной борд
        mainBoard.update(currentPos);
        // Индикаторы
        healthIndicator.update(currentPos);
        mannaIndicator.update(currentPos);
        staminaIndicator.update(currentPos);
        // Свичи
        switch (characteristic.getCurentActionPoint()){
            case 0:
                firstPointIndicator.setVisible(false);
                secondPointIndicator.setVisible(false);
                break;
            case 1:
                firstPointIndicator.setVisible(true);
                secondPointIndicator.setVisible(false);
                break;
            case 2:
                firstPointIndicator.setVisible(true);
                secondPointIndicator.setVisible(true);
                break;
        }
        firstPointIndicator.update(currentPos);
        secondPointIndicator.update(currentPos);
    }

    public void draw(Shader shader){
        for(Board board : boardList){
            if(board.isVisible()) {
                board.draw(shader);
            }
        }
    }
}
