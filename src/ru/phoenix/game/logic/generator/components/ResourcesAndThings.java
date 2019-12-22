package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.LittleThing;
import ru.phoenix.game.content.object.water.WaterFlower;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.MOUNTAIN_AREA;
import static ru.phoenix.core.config.Constants.PLAIN_AREA;

public class ResourcesAndThings {
    private static Object littleThing_main      = null;
    private static Object waterFlower_main      = null;

    private static List<Object> sprites = new ArrayList<>();

    public static List<Object> scatter(Cell[][]grid,int w, int h, int currentArea){
        sprites.clear();
        initSprites();

        for(int x=0; x<=w; x++){
            for(int z=0; z<=h; z++){
                if(currentArea == PLAIN_AREA){
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && grid[x][z].getCurrentHeight() == grid[x][z].getPosition().getY()){
                        if(grid[x][z].getPosition().getY() < 0){
                            Vector3f leftPos = new Vector3f(x - 1.0f, 0.0f, z);
                            Vector3f rightPos = new Vector3f(x + 1.0f, 0.0f, z);
                            Vector3f upPos = new Vector3f(x, 0.0f, z + 1);
                            Vector3f downPos = new Vector3f(x, 0.0f, z - 1);

                            int checkInfo = 0;

                            if(0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                                if (grid[x - 1][z].getPosition().equals(leftPos)) {
                                    if (grid[x - 1][z].getCurrentHeight() >= 0) {
                                        checkInfo++;
                                    }
                                }
                            }
                            if(0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                                if (grid[x + 1][z].getPosition().equals(rightPos)) {
                                    if (grid[x + 1][z].getCurrentHeight() >= 0) {
                                        checkInfo++;
                                    }
                                }
                            }
                            if(0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                                if (grid[x][z + 1].getPosition().equals(upPos)) {
                                    if (grid[x][z + 1].getCurrentHeight() >= 0) {
                                        checkInfo++;
                                    }
                                }
                            }
                            if(0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                                if (grid[x][z - 1].getPosition().equals(downPos)) {
                                    if (grid[x][z - 1].getCurrentHeight() >= 0) {
                                        checkInfo++;
                                    }
                                }
                            }

                            if(checkInfo > 0 && grid[x][z].getPosition().getY() != -0.5f){
                                if(Math.random() * 100.0f <= 30.0f) {
                                    Object waterFlower = new WaterFlower((WaterFlower) waterFlower_main);
                                    waterFlower.init(null);
                                    waterFlower.setPosition(new Vector3f(x, -0.7499f, z));
                                    sprites.add(waterFlower);
                                }
                            }
                        }else{
                            if(Math.random() * 100.0f <= 1.0f){
                                Object littleThing = new LittleThing((LittleThing) littleThing_main);
                                littleThing.init(null);
                                littleThing.setPosition(grid[x][z].getPosition());
                                sprites.add(littleThing);
                            }
                        }
                    }
                }else if(currentArea == MOUNTAIN_AREA){
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && grid[x][z].getCurrentHeight() == grid[x][z].getPosition().getY()){
                        if(grid[x][z].getPosition().getY() < -1.0f){
                            if(Math.random() * 100.0f <= 1.0f){
                                Object littleThing = new LittleThing((LittleThing) littleThing_main);
                                littleThing.init(null);
                                littleThing.setPosition(grid[x][z].getPosition());
                                sprites.add(littleThing);
                            }
                        }
                    }
                }
            }
        }

        return sprites;
    }

    private static void initSprites(){
        if(littleThing_main == null){
            littleThing_main = new LittleThing();
        }
        if(waterFlower_main == null){
            waterFlower_main = new WaterFlower();
        }
    }
}
