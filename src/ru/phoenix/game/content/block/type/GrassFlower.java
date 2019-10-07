package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_GRASS_FLOWER;

public class GrassFlower extends BlockControl implements Block {

    // конструкторы

    public GrassFlower(){
        super();
        setMeshs("./data/content/block/grass/grass_flower.obj");
        setType(BLOCK_GRASS_FLOWER);
    }

    public GrassFlower(Texture texture){
        super();
        setMeshs("./data/content/block/grass/grass_flower.obj",texture);
        setType(BLOCK_GRASS_FLOWER);
    }

    public GrassFlower(GrassFlower block){
        super();
        if(block.getTexture() != null) {
            setMeshs(block.getMeshes(), block.getTexture());
        }else{
            setMeshs(block.getMeshes());
        }
        setType(block.getType());
    }
}
