package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_DIRT_GRASS;

public class GrassBevel extends BlockControl implements Block {
    // конструкторы
    public GrassBevel(){
        super();
        setMeshs("./data/content/block/grass/dirt_grass_bevel.obj");
        setType(BLOCK_DIRT_GRASS);
    }

    public GrassBevel(GrassBevel block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
