package ru.phoenix.game.content.stage.random;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.BattleGraundControl;

import java.util.List;

public class RandomArena extends BattleGraundControl implements BattleGraund {
    public RandomArena(List<Block> blocks, int mapX, int mapZ){
        setBlocks(blocks, mapX, mapZ);
    }
}
