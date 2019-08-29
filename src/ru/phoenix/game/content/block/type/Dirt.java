package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

public class Dirt extends BlockControl implements Block {
    // описание

    // конструкторы
    public Dirt(){
        super();
        setMeshs("./data/content/block/dirt/block_dirt.obj");
    }

    public Dirt(Dirt dirt){
        super();
        setMeshs(dirt.getMeshes());
    }
}
