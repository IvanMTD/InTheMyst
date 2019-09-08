package ru.phoenix.game.content.block.type.stone;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_STONE_SMALL;

public class SmallStone extends BlockControl implements Block {
    // описание

    // конструкторы
    public SmallStone(){
        super();
        setMeshs("./data/content/block/stone/small/small_rock.obj");
        setType(BLOCK_STONE_SMALL);
    }

    public SmallStone(SmallStone block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
