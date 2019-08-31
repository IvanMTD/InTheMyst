package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_GRASS_FLOWER;

public class GrassFlower extends BlockControl implements Block {

    // конструкторы
    public GrassFlower(){
        super();
        setMeshs("./data/content/block/grass/grass_flower.obj");
        setType(BLOCK_GRASS_FLOWER);
    }

    public GrassFlower(GrassFlower block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
