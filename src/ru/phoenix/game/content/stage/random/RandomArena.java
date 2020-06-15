package ru.phoenix.game.content.stage.random;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.content.stage.StudyAreaControl;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.components.GraundModel;
import ru.phoenix.game.logic.generator.components.Reservoir;

import java.util.List;

public class RandomArena extends StudyAreaControl implements StudyArea {
    public RandomArena(Cell[][] grid, GraundModel model, Reservoir waterReservoir, List<Block> blocks, List<Object> sprites, int mapX, int mapZ, float biom){
        super();
        setup(grid, model, waterReservoir, blocks, sprites, mapX, mapZ);
        setBiom(biom);
        initLight();
    }
}
