package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

public class MountainDirt extends BlockControl implements Block {
    // описание

    // конструкторы
    public MountainDirt(){
        super();
        setMeshs("./data/content/block/dirt/mountain_dirt.obj");
    }

    public MountainDirt(MountainDirt mountainDirt){
        super();
        setMeshs(mountainDirt.getMeshes());
    }
}
