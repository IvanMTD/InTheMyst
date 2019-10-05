package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_ROCK;

public class Rock extends BlockControl implements Block {
    // конструкторы
    public Rock(Texture texture){
        super();
        setMeshs("./data/content/block/rock.obj");
        setType(BLOCK_ROCK);
    }

    public Rock(Rock block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
