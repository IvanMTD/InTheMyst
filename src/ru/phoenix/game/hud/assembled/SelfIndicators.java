package ru.phoenix.game.hud.assembled;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.property.Characteristic;
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
    private Board emptyHealthIndicator;
    private Board emptyMannaIndicator;
    private Board emptyStaminaIndicator;

    public SelfIndicators(float width,Vector3f offsetPosition){
        mainBoard = new Indicator("./data/content/texture/boards/personal_indicator.png",width,offsetPosition);
        firstPointIndicator = new Indicator("./data/content/texture/boards/1_st_point.png",width,offsetPosition);
        secondPointIndicator = new Indicator("./data/content/texture/boards/2_st_point.png",width,offsetPosition);
        healthIndicator = new Indicator("./data/content/texture/boards/health.png",width * 59.0f / 100.0f,offsetPosition.add(new Vector3f(width * 5.0f / 100.0f,0.0f,0.0f)));
        mannaIndicator = new Indicator("./data/content/texture/boards/manna.png",width * 59.0f / 100.0f,offsetPosition.add(new Vector3f(width * 5.0f / 100.0f,0.0f,0.0f)));
        staminaIndicator = new Indicator("./data/content/texture/boards/stamina.png",width * 59.0f / 100.0f,offsetPosition.add(new Vector3f(width * 5.0f / 100.0f,0.0f,0.0f)));
        emptyHealthIndicator = new Indicator("./data/content/texture/boards/empty_health.png",width * 59.0f / 100.0f,offsetPosition.add(new Vector3f(width * 5.0f / 100.0f,0.0f,0.0f)));
        emptyHealthIndicator.setDiscardReverse(true);
        emptyMannaIndicator = new Indicator("./data/content/texture/boards/empty_manna.png",width * 59.0f / 100.0f,offsetPosition.add(new Vector3f(width * 5.0f / 100.0f,0.0f,0.0f)));
        emptyMannaIndicator.setDiscardReverse(true);
        emptyStaminaIndicator = new Indicator("./data/content/texture/boards/empty_stamina.png",width * 59.0f / 100.0f,offsetPosition.add(new Vector3f(width * 5.0f / 100.0f,0.0f,0.0f)));
        emptyStaminaIndicator.setDiscardReverse(true);

        boardList = new ArrayList<>(Arrays.asList(healthIndicator,mannaIndicator,staminaIndicator,emptyHealthIndicator,emptyMannaIndicator,emptyStaminaIndicator,firstPointIndicator,secondPointIndicator,mainBoard));
    }

    public void update(Vector3f currentPos, Characteristic characteristic){
        /*float h = currentPos.getY();
        characteristic.setVision((int)(characteristic.getFinalVision() + h));*/
        // основной борд
        mainBoard.update(currentPos);
        // Индикаторы
        healthIndicator.setDiscatdControl((characteristic.getHealth() * 100.0f / characteristic.getTotalHealth()) / 100.0f);
        emptyHealthIndicator.setDiscatdControl(healthIndicator.getDiscatdControl());
        healthIndicator.update(currentPos);
        emptyHealthIndicator.update(currentPos);
        mannaIndicator.setDiscatdControl((characteristic.getManna() * 100.0f / characteristic.getTotalManna()) / 100.0f);
        emptyMannaIndicator.setDiscatdControl(mannaIndicator.getDiscatdControl());
        mannaIndicator.update(currentPos);
        emptyMannaIndicator.update(currentPos);
        staminaIndicator.setDiscatdControl((characteristic.getStamina() * 100.0f / characteristic.getTotalStamina()) / 100.0f);
        emptyStaminaIndicator.setDiscatdControl(staminaIndicator.getDiscatdControl());
        staminaIndicator.update(currentPos);
        emptyStaminaIndicator.update(currentPos);
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
