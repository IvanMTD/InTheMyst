package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_DIRT_GRASS;

public class Grass extends BlockControl implements Block{
    // описание

    // конструкторы
    public Grass(){
        super();
        setMeshs("./data/content/block/grass/dirt_grass_block.obj");
        setType(BLOCK_DIRT_GRASS);
    }

    public Grass(Texture texture){
        super();
        setMeshs("./data/content/block/grass/dirt_grass_block.obj",texture);
        setType(BLOCK_DIRT_GRASS);
    }

    public Grass(Grass block){
        super();
        if(block.getTexture() != null) {
            setMeshs(block.getMeshes(), block.getTexture());
        }else{
            setMeshs(block.getMeshes());
        }
        setType(block.getType());
    }
}
