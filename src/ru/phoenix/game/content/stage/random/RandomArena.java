package ru.phoenix.game.content.stage.random;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.BattleGraundControl;
import ru.phoenix.game.logic.element.GridElement;

import java.util.List;

public class RandomArena extends BattleGraundControl implements BattleGraund {
    public RandomArena(List<GridElement> gridElements, List<Block> blocks, List<Object> sprites, List<Object> water, int mapX, int mapZ){
        super();
        setBlocks(gridElements, blocks, water, sprites, mapX, mapZ);
        initLight();
    }
}
