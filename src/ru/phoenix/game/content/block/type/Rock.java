package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

public class Rock extends BlockControl implements Block {
    // конструкторы
    public Rock(){
        super();
        setMeshs("./data/content/block/rock/rock.obj");
    }

    public Rock(Rock rock){
        super();
        setMeshs(rock.getMeshes());
    }
}
