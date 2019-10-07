package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_DIRT_SNOW;

public class Snow extends BlockControl implements Block {
    // конструкторы
    public Snow(){
        super();
        setMeshs("./data/content/block/snow/snow.obj");
        setType(BLOCK_DIRT_SNOW);
    }

    public Snow(Texture texture){
        super();
        setMeshs("./data/content/block/snow/snow.obj",texture);
        setType(BLOCK_DIRT_SNOW);
    }

    public Snow(Snow block){
        super();
        if(block.getTexture() != null) {
            setMeshs(block.getMeshes(), block.getTexture());
        }else{
            setMeshs(block.getMeshes());
        }
        setType(block.getType());
    }
}
