package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_DIRT;

public class Dirt extends BlockControl implements Block {
    // описание

    // конструкторы
    public Dirt(){
        super();
        setMeshs("./data/content/block/dirt/block_dirt.obj");
        setType(BLOCK_DIRT);
    }

    public Dirt(Dirt block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
