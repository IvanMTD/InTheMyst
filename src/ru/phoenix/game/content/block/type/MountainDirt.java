package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_COLD_DIRT;

public class MountainDirt extends BlockControl implements Block {
    // описание

    // конструкторы
    public MountainDirt(){
        super();
        setMeshs("./data/content/block/dirt/mountain_dirt.obj");
        setType(BLOCK_COLD_DIRT);
    }

    public MountainDirt(MountainDirt block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
