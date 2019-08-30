package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

public class Snow extends BlockControl implements Block {
    // конструкторы
    public Snow(){
        super();
        setMeshs("./data/content/block/snow/snow.obj");
    }

    public Snow(Snow snow){
        super();
        setMeshs(snow.getMeshes());
    }
}
