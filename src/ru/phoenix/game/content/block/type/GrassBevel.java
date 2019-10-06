package ru.phoenix.game.content.block.type;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.BLOCK_DIRT_GRASS;

public class GrassBevel extends BlockControl implements Block {
    // конструкторы
    public GrassBevel(){
        super();
        setMeshs("./data/content/block/bevel.obj");
        setType(BLOCK_DIRT_GRASS);
    }

    public GrassBevel(Texture texture){
        super();
        setMeshs("./data/content/block/bevel.obj",texture);
        setType(BLOCK_DIRT_GRASS);
    }

    public GrassBevel(GrassBevel block){
        super();
        if(block.getTexture() != null) {
            setMeshs(block.getMeshes(), block.getTexture());
        }else{
            setMeshs(block.getMeshes());
        }
        setType(block.getType());
    }
}
