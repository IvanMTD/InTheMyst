package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_DIRT;

public class Dirt extends BlockControl implements Block {
    // описание

    // конструкторы
    public Dirt(){
        super();
        setMeshs("./data/content/block/dirt/block_dirt.obj");
        setType(BLOCK_DIRT);
    }

    public Dirt(Texture texture){
        super();
        setMeshs("./data/content/block/dirt/block_dirt.obj",texture);
        setType(BLOCK_DIRT);
    }

    public Dirt(Dirt block){
        super();
        if(block.getTexture() != null) {
            setMeshs(block.getMeshes(), block.getTexture());
        }else{
            setMeshs(block.getMeshes());
        }
        setType(block.getType());
    }
}
