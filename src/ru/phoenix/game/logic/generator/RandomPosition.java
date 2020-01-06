package ru.phoenix.game.logic.generator;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.element.grid.Cell;

public class RandomPosition extends Thread{
    // поток
    private Vector3f result;
    private Vector3f point;
    private Cell[][] grid;
    private float radius;
    private boolean occupied;

    public RandomPosition(Vector3f result, Vector3f point, Cell[][] grid, float radius, boolean occupied) {
        this.result = result;
        this.point = point;
        this.grid = grid;
        this.radius = radius;
        this.occupied = occupied;
    }

    @Override
    public void run(){
        Vector3f pos = getRandomPos(grid,point,radius,occupied);
        result.setVector(pos);
    }

    private Vector3f getRandomPos(Cell[][]grid, Vector3f point, float radius, boolean occupied){
        float x_min = (point.getX() - radius) < 0.0f ? 0.0f : (point.getX() - radius);
        float x_max = radius * 2;
        float x_random = (float)Math.random();
        int x = (int) Math.floor((x_min + x_random * x_max) >= grid.length ? grid.length - 1 : (x_min + x_random * x_max));

        float z_min = (point.getZ() - radius) < 0.0f ? 0.0f : (point.getZ() - radius);
        float z_max = radius * 2;
        float z_random = (float)Math.random();
        int z = (int) Math.floor((z_min + z_random * z_max) >= grid[0].length ? grid[0].length - 1 : (z_min + z_random * z_max));

        if(!grid[x][z].isBlocked() && !grid[x][z].isOccupied() && !grid[x][z].isBevel() && !grid[x][z].isWater()){
            grid[x][z].setOccupied(occupied);
            return new Vector3f(grid[x][z].getModifiedPosition());
        }else{
            return getRandomPos(grid,point,radius,occupied);
        }
    }
}
