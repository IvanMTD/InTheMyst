package ru.phoenix.game.content.block.type;

import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

public class SnowRock extends BlockControl implements Block {
    // конструкторы
    public SnowRock(){
        super();
        setMeshs("./data/content/block/rock/snow_rock.obj");
    }

    public SnowRock(SnowRock rock){
        super();
        setMeshs(rock.getMeshes());
    }
}
