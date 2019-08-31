package ru.phoenix.game.logic.generator.component;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;

public class GridElement {
    private Vector3f position;
    private Block block;
    private float currentHeight;

    public GridElement(Vector3f position, Block block) {
        setBlock(block);
        setPosition(position);
        setCurrentHeight(position.getY());
    }

    public Vector3f getPosition() {
        return position;
    }

    private void setPosition(Vector3f position) {
        this.position = position;
    }

    public Block getBlock() {
        return block;
    }

    private void setBlock(Block block) {
        this.block = block;
    }

    public float getCurrentHeight() {
        return currentHeight;
    }

    private void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }
}
