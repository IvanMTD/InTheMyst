package ru.phoenix.game.content.block.type.stone;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_STONE_BIG;

public class BigStone extends BlockControl implements Block {
    // описание

    // конструкторы
    public BigStone(){
        super();
        setMeshs("./data/content/block/stone/big/big_rock.obj");
        setType(BLOCK_STONE_BIG);
    }

    public BigStone(BigStone block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }

    @Override
    public void update(Vector3f pixel, boolean leftClick) {

    }
}
