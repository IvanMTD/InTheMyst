package ru.phoenix.game.content.block.type.stone;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_STONE_SMALL;

public class SmallStoneSnow extends BlockControl implements Block {
    // описание

    // конструкторы
    public SmallStoneSnow(){
        super();
        setMeshs("./data/content/block/stone/small/small_rock_snow.obj");
        setType(BLOCK_STONE_SMALL);
    }

    public SmallStoneSnow(SmallStoneSnow block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }

    @Override
    public void update(Vector3f pixel, boolean leftClick) {

    }
}
