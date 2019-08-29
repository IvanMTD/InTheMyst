package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

public class Grass extends BlockControl implements Block{
    // описание

    // конструкторы
    public Grass(){
        super();
        setMeshs("./data/content/block/grass/dirt_grass_block.obj");
    }

    public Grass(Grass grass){
        super();
        setMeshs(grass.getMeshes());
    }
}
