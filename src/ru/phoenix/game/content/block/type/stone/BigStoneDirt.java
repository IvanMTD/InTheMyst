package ru.phoenix.game.content.block.type.stone;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_STONE_BIG;

public class BigStoneDirt extends BlockControl implements Block {
    // описание

    // конструкторы
    public BigStoneDirt(){
        super();
        setMeshs("./data/content/block/stone/big/big_dirt_stone.obj");
        setType(BLOCK_STONE_BIG);
    }

    public BigStoneDirt(BigStoneDirt block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
