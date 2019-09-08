package ru.phoenix.game.content.block.type.stone;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_STONE_MEDIUM;

public class MediumStone extends BlockControl implements Block {
    // описание

    // конструкторы
    public MediumStone(){
        super();
        setMeshs("./data/content/block/stone/medium/medium_rock.obj");
        setType(BLOCK_STONE_MEDIUM);
    }

    public MediumStone(MediumStone block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
