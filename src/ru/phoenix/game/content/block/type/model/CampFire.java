package ru.phoenix.game.content.block.type.model;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.BlockControl;

import static ru.phoenix.core.config.Constants.GROUP_G;

public class CampFire extends BlockControl implements Block {
    // описание

    // конструкторы
    public CampFire(float id){
        super();
        setMeshs("./data/content/model/campfire/camp_fire.obj");
        setId(id);
        setGroup(GROUP_G);
    }

    public CampFire(CampFire campFire){
        super();
        setMeshs(campFire.getMeshes());
        setId(campFire.getId());
        setGroup(campFire.getGroup());
    }

    public CampFire(CampFire campFire, float id){
        super();
        setMeshs(campFire.getMeshes());
        setId(id);
        setGroup(GROUP_G);
    }

    @Override
    public void update(Vector3f pixel, boolean leftClick) {
        float id = pixel.getG();
        if(id == getId()){
            setTarget(true);
            if(leftClick){
                Default.setCampFireOn(true);
            }
        }else{
            setTarget(false);
        }
    }
}
