package ru.phoenix.game.content.stage;

import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleGraundControl {
    private List<Block> blocks;
    private int mapX;
    private int mapZ;

    public BattleGraundControl(){
        blocks = new ArrayList<>();
    }

    protected void setBlocks(List<Block> blocks, int mapX, int mapZ){
        this.blocks = new ArrayList<>(blocks);
        this.mapX = mapX;
        this.mapZ = mapZ;
    }

    public void draw(Shader shader){
        for(Block block : blocks){
            block.draw(shader);
        }
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapZ() {
        return mapZ;
    }
}
