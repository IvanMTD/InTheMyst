package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.LittleThing;
import ru.phoenix.game.content.object.water.WaterFlower;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.MOUNTAIN_AREA;
import static ru.phoenix.core.config.Constants.PLAIN_AREA;

public class ResourcesAndThings {
    private static Object littleThing_main      = null;
    private static Object waterFlower_main      = null;

    private static List<Object> sprites = new ArrayList<>();

    public static List<Object> scatter(Cell[][]grid, int w, int h, float currentHeight, SaveData saveData){
        sprites.clear();
        initSprites();

        if(currentHeight == 5.0f){
        }else if(currentHeight == 10.0f){
            currentHeight = 0.0f;
        }else if(currentHeight == 15.0f){
            currentHeight = currentHeight - 1.0f;
        }else if(currentHeight == 20.0f){
            currentHeight = 0.0f;
        }else if(currentHeight == 25.0f){
            currentHeight = currentHeight - 2.0f;
        }else if(currentHeight == 30.0f){
            currentHeight = 0.0f;
        }else if(currentHeight == 35.0f){
            currentHeight = 0.0f;
        }else if(currentHeight == 40.0f){
            currentHeight = 0.0f;
        }else{
            currentHeight = 0.0f;
        }

        for (int x = 0; x <= w; x++) {
            for (int z = 0; z <= h; z++) {
                if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad() && grid[x][z].getCurrentHeight() == grid[x][z].getPosition().getY()) {
                    if (grid[x][z].isWater()) { // установка водяных растений
                        Vector3f leftPos = new Vector3f(x - 1.0f, 0.0f, z);
                        Vector3f rightPos = new Vector3f(x + 1.0f, 0.0f, z);
                        Vector3f upPos = new Vector3f(x, 0.0f, z + 1);
                        Vector3f downPos = new Vector3f(x, 0.0f, z - 1);

                        int checkInfo = 0;

                        if (0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                            if (grid[x - 1][z].getPosition().equals(leftPos)) {
                                if (grid[x - 1][z].getCurrentHeight() >= 0) {
                                    checkInfo++;
                                }
                            }
                        }
                        if (0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                            if (grid[x + 1][z].getPosition().equals(rightPos)) {
                                if (grid[x + 1][z].getCurrentHeight() >= 0) {
                                    checkInfo++;
                                }
                            }
                        }
                        if (0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                            if (grid[x][z + 1].getPosition().equals(upPos)) {
                                if (grid[x][z + 1].getCurrentHeight() >= 0) {
                                    checkInfo++;
                                }
                            }
                        }
                        if (0 <= x - 1 && x + 1 <= w && 0 <= z - 1 && z + 1 <= h) {
                            if (grid[x][z - 1].getPosition().equals(downPos)) {
                                if (grid[x][z - 1].getCurrentHeight() >= 0) {
                                    checkInfo++;
                                }
                            }
                        }

                        if (checkInfo > 0 && grid[x][z].getPosition().getY() != -0.5f) {
                            if (Math.random() * 100.0f <= 5.0f) {
                                Object waterFlower = new WaterFlower((WaterFlower) waterFlower_main);
                                waterFlower.init(null);
                                waterFlower.setPosition(new Vector3f(x, currentHeight - 0.7499f, z));
                                sprites.add(waterFlower);
                            }
                        }
                    } else {
                        boolean option1 =  13.0f < grid[x][z].getCurrentOriginalHeight() && grid[x][z].getCurrentOriginalHeight() < 20.0f;
                        boolean option2 =  23.0f < grid[x][z].getCurrentOriginalHeight() && grid[x][z].getCurrentOriginalHeight() < 43.0f;
                        if(option1 || option2) {
                            if (Math.random() * 100.0f <= 1.0f) {
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

    public static List<Object> scatter(Cell[][]grid, SaveData saveData){
        sprites.clear();
        initSprites();



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
