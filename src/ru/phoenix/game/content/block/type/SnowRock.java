package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_ROCK_SNOW;

public class SnowRock extends BlockControl implements Block {
    // конструкторы
    public SnowRock(Texture texture){
        super();
        setMeshs("./data/content/block/rock_snow.obj");
        setType(BLOCK_ROCK_SNOW);
    }

    public SnowRock(SnowRock block){
        super();
        setMeshs(block.getMeshes());
        setType(block.getType());
    }
}
