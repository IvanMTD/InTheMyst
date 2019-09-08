package ru.phoenix.game.logic.generator.component;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;

public class GridElement {
    private Vector3f position;
    private Block block;
    private float currentHeight;
    private boolean bevel;
    private boolean blocked;

    public GridElement(Vector3f position, Block block, boolean bevel, boolean blocked) {
        setBlock(block);
        setPosition(position);
        setCurrentHeight(position.getY());
        setBevel(bevel);
        setBlocked(blocked);
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

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

    public boolean isBevel() {
        return bevel;
    }

    private void setBevel(boolean bevel) {
        this.bevel = bevel;
    }

    public boolean isBlocked() {
        return blocked;
    }

    private void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
