package ru.phoenix.game.content.block.type.stone;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_STONE_MEDIUM;

public class MediumStoneSnow extends BlockControl implements Block {
    // описание

    // конструкторы
    public MediumStoneSnow(){
        super();
        setMeshs("./data/content/block/stone/medium/medium_rock_snow.obj");
        setType(BLOCK_STONE_MEDIUM);
    }

    public MediumStoneSnow(MediumStoneSnow block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }

    @Override
    public void update(Vector3f pixel, boolean leftClick) {

    }
}
