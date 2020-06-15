package ru.phoenix.game.content.stage.random;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.content.stage.StudyAreaControl;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.components.GraundModel;
import ru.phoenix.game.logic.generator.components.Reservoir;

import java.util.List;

public class PreparedArena extends StudyAreaControl implements StudyArea {
    public PreparedArena(Cell[][] grid, GraundModel model, Reservoir waterReservoir, List<Block> blocks, List<Object> sprites, int mapX, int mapZ,float biom){
        super();
        setup(grid, model, waterReservoir, blocks, sprites, mapX, mapZ);
        setBiom(biom);
        initLight(new Vector3f(25.0f,50.0f,-50.0f),new Vector3f(0.1f,0.1f,0.1f),new Vector3f(1.0f,1.0f,1.0f),new Vector3f(0.5f,0.5f,0.5f));
    }
}
