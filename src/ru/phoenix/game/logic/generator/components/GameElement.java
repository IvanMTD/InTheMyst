package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.stone.*;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.MOUNTAIN_AREA;
import static ru.phoenix.core.config.Constants.PLAIN_AREA;

public class GameElement {

    private static Block stone_small_main       = null;
    private static Block stone_small_dirt_main  = null;
    private static Block stone_small_snow_main  = null;
    private static Block stone_medium_main      = null;
    private static Block stone_medium_dirt_main = null;
    private static Block stone_medium_snow_main = null;
    private static Block stone_big_main         = null;
    private static Block stone_big_dirt_main    = null;
    private static Block stone_big_snow_main    = null;

    private static List<Block> gameElements = new ArrayList<>();

    public static List<Block> setup(Cell[][] grid, int w, int h, float currentHeight, SaveData saveData){
        gameElements.clear();
        initRocks();

        List<Vector3f> gridInfo = new ArrayList<>();

        Block smallStone = new SmallStone((SmallStone)stone_small_main); SaveElement ss = new SaveElement();
        Block smallStoneDirt = new SmallStoneDirt((SmallStoneDirt)stone_small_dirt_main); SaveElement sds = new SaveElement();
        Block smallStoneSnow = new SmallStoneSnow((SmallStoneSnow)stone_small_snow_main); SaveElement sss = new SaveElement();
        Block mediumStone = new MediumStone((MediumStone)stone_medium_main); SaveElement ms = new SaveElement();
        Block mediumStoneDirt = new MediumStoneDirt((MediumStoneDirt)stone_medium_dirt_main); SaveElement mds = new SaveElement();
        Block mediumStoneSnow = new MediumStoneSnow((MediumStoneSnow)stone_medium_snow_main); SaveElement mss = new SaveElement();
        Block bigStone = new BigStone((BigStone)stone_big_main); SaveElement bs = new SaveElement();
        Block bigStoneDirt = new BigStoneDirt((BigStoneDirt)stone_big_dirt_main); SaveElement bds = new SaveElement();
        Block bigStoneSnow = new BigStoneSnow((BigStoneSnow)stone_big_snow_main); SaveElement bss = new SaveElement();

        List<Matrix4f>smallStoneInstanceList = new ArrayList<>();
        List<Matrix4f>smallStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>smallStoneSnowInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneSnowInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneSnowInstanceList = new ArrayList<>();

        if(currentHeight == 5.0f){ // Пустыня
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        if (Math.random() * 100.0f <= 1.0f) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            Projection projection = new Projection();
                            projection.setTranslation(grid[x][z].getPosition());
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            switch (random) {
                                case 0:
                                    smallStoneDirtInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 1:
                                    mediumStoneDirtInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 2:
                                    bigStoneDirtInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 10.0f){ // Пустыня --> Степь
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        float height = grid[x][z].getCurrentOriginalHeight();
                        if (height < 13.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        } else if (height > 14.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 15.0f){ // Степь
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        float height = grid[x][z].getCurrentOriginalHeight();
                        if (height < 11.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        } else if (height > 12.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 20.0f){ // Степь --> Равнина
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        if (Math.random() * 100.0f <= 1.0f) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            Projection projection = new Projection();
                            projection.setTranslation(grid[x][z].getPosition());
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            switch (random) {
                                case 0:
                                    smallStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 1:
                                    mediumStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 2:
                                    bigStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 25.0f){ // Равнина
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        float height = grid[x][z].getCurrentOriginalHeight();
                        if (height < 22.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneDirtInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        } else if (height > 23.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 30.0f){ // Равнина --> Лес
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        if (Math.random() * 100.0f <= 1.0f) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            Projection projection = new Projection();
                            projection.setTranslation(grid[x][z].getPosition());
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            switch (random) {
                                case 0:
                                    smallStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 1:
                                    mediumStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 2:
                                    bigStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 35.0f){ // Лес
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        if (Math.random() * 100.0f <= 1.0f) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            Projection projection = new Projection();
                            projection.setTranslation(grid[x][z].getPosition());
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            switch (random) {
                                case 0:
                                    smallStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 1:
                                    mediumStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 2:
                                    bigStoneInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                            }
                        }
                    }
                }
            }
        }else if(currentHeight == 40.0f){ // Лес --> Горы
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        float height = grid[x][z].getCurrentOriginalHeight();
                        if (height < 40.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        } else if (height > 43.0f) {
                            if (Math.random() * 100.0f <= 1.0f) {
                                int coin = (int) Math.round(Math.random() * 3.0);
                                float angle = 0.0f;
                                switch (coin) {
                                    case 0:
                                        angle = 0.0f;
                                        break;
                                    case 1:
                                        angle = 90.0f;
                                        break;
                                    case 2:
                                        angle = 180.0f;
                                        break;
                                    case 3:
                                        angle = 270.0f;
                                        break;
                                }
                                Projection projection = new Projection();
                                projection.setTranslation(grid[x][z].getPosition());
                                projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                                int random = (int) (Math.random() * 2.9f);
                                switch (random) {
                                    case 0:
                                        smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 1:
                                        mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                    case 2:
                                        bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                        grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                        gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }else{ // Горы
            for(int x=0; x<=w; x++) {
                for (int z = 0; z <= h; z++) {
                    if(!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isGrass() && !grid[x][z].isRoad()) {
                        if (Math.random() * 100.0f <= 1.0f) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            Projection projection = new Projection();
                            projection.setTranslation(grid[x][z].getPosition());
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            switch (random) {
                                case 0:
                                    smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 0.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 1:
                                    mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.0f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                                case 2:
                                    bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                    grid[x][z].setCurrentHeight(grid[x][z].getCurrentHeight() + 1.5f);
                                    gridInfo.add(new Vector3f(x,z,grid[x][z].getCurrentHeight()));
                                    break;
                            }
                        }
                    }
                }
            }
        }

        if(smallStoneInstanceList.size() != 0) {
            smallStone.setInstanceMatrix(getInstanceMatrix(smallStoneInstanceList));
            ss.setStoneMatrix(getInstanceMatrix(smallStoneInstanceList));
            gameElements.add(smallStone);
        }
        if(smallStoneDirtInstanceList.size() != 0) {
            smallStoneDirt.setInstanceMatrix(getInstanceMatrix(smallStoneDirtInstanceList));
            sds.setStoneMatrix(getInstanceMatrix(smallStoneDirtInstanceList));
            gameElements.add(smallStoneDirt);
        }
        if(smallStoneSnowInstanceList.size() != 0) {
            smallStoneSnow.setInstanceMatrix(getInstanceMatrix(smallStoneSnowInstanceList));
            sss.setStoneMatrix(getInstanceMatrix(smallStoneSnowInstanceList));
            gameElements.add(smallStoneSnow);
        }
        if(mediumStoneInstanceList.size() != 0) {
            mediumStone.setInstanceMatrix(getInstanceMatrix(mediumStoneInstanceList));
            ms.setStoneMatrix(getInstanceMatrix(mediumStoneInstanceList));
            gameElements.add(mediumStone);
        }
        if(mediumStoneDirtInstanceList.size() != 0) {
            mediumStoneDirt.setInstanceMatrix(getInstanceMatrix(mediumStoneDirtInstanceList));
            mds.setStoneMatrix(getInstanceMatrix(mediumStoneDirtInstanceList));
            gameElements.add(mediumStoneDirt);
        }
        if(mediumStoneSnowInstanceList.size() != 0) {
            mediumStoneSnow.setInstanceMatrix(getInstanceMatrix(mediumStoneSnowInstanceList));
            mss.setStoneMatrix(getInstanceMatrix(mediumStoneSnowInstanceList));
            gameElements.add(mediumStoneSnow);
        }
        if(bigStoneInstanceList.size() != 0) {
            bigStone.setInstanceMatrix(getInstanceMatrix(bigStoneInstanceList));
            bs.setStoneMatrix(getInstanceMatrix(bigStoneInstanceList));
            gameElements.add(bigStone);
        }
        if(bigStoneDirtInstanceList.size() != 0) {
            bigStoneDirt.setInstanceMatrix(getInstanceMatrix(bigStoneDirtInstanceList));
            bds.setStoneMatrix(getInstanceMatrix(bigStoneDirtInstanceList));
            gameElements.add(bigStoneDirt);
        }
        if(bigStoneSnowInstanceList.size() != 0) {
            bigStoneSnow.setInstanceMatrix(getInstanceMatrix(bigStoneSnowInstanceList));
            bss.setStoneMatrix(getInstanceMatrix(bigStoneSnowInstanceList));
            gameElements.add(bigStoneSnow);
        }

        saveData.setStoneElement(Arrays.asList(ss,sds,sss,ms,mds,mss,bs,bds,bss));
        saveData.setvList(gridInfo);

        return gameElements;
    }

    public static List<Block> setup(Cell[][] grid, SaveData saveData){
        gameElements.clear();
        initRocks();

        for(Vector3f info : saveData.getvList()){
            int x = (int)info.getX();
            int z = (int)info.getY();
            float currentHeight = info.getZ();
            grid[x][z].setCurrentHeight(currentHeight);
        }

        Block smallStone = new SmallStone((SmallStone)stone_small_main);
        smallStone.setInstanceMatrix(saveData.getStoneElement().get(0).getStoneMatrix());
        Block smallStoneDirt = new SmallStoneDirt((SmallStoneDirt)stone_small_dirt_main);
        smallStoneDirt.setInstanceMatrix(saveData.getStoneElement().get(1).getStoneMatrix());
        Block smallStoneSnow = new SmallStoneSnow((SmallStoneSnow)stone_small_snow_main);
        smallStoneSnow.setInstanceMatrix(saveData.getStoneElement().get(2).getStoneMatrix());
        Block mediumStone = new MediumStone((MediumStone)stone_medium_main);
        mediumStone.setInstanceMatrix(saveData.getStoneElement().get(3).getStoneMatrix());
        Block mediumStoneDirt = new MediumStoneDirt((MediumStoneDirt)stone_medium_dirt_main);
        mediumStoneDirt.setInstanceMatrix(saveData.getStoneElement().get(4).getStoneMatrix());
        Block mediumStoneSnow = new MediumStoneSnow((MediumStoneSnow)stone_medium_snow_main);
        mediumStoneSnow.setInstanceMatrix(saveData.getStoneElement().get(5).getStoneMatrix());
        Block bigStone = new BigStone((BigStone)stone_big_main);
        bigStone.setInstanceMatrix(saveData.getStoneElement().get(6).getStoneMatrix());
        Block bigStoneDirt = new BigStoneDirt((BigStoneDirt)stone_big_dirt_main);
        bigStoneDirt.setInstanceMatrix(saveData.getStoneElement().get(7).getStoneMatrix());
        Block bigStoneSnow = new BigStoneSnow((BigStoneSnow)stone_big_snow_main);
        bigStoneSnow.setInstanceMatrix(saveData.getStoneElement().get(8).getStoneMatrix());

        gameElements.addAll(Arrays.asList(
                smallStone,smallStoneDirt,smallStoneSnow,mediumStone,mediumStoneDirt,mediumStoneSnow,bigStone,bigStoneDirt,bigStoneSnow
        ));

        return gameElements;
    }

    private static void initRocks(){
        if(stone_small_main == null){
            stone_small_main = new SmallStone();
        }
        if(stone_small_dirt_main == null){
            stone_small_dirt_main = new SmallStoneDirt();
        }
        if(stone_medium_main == null){
            stone_medium_main = new MediumStone();
        }
        if(stone_medium_dirt_main == null){
            stone_medium_dirt_main = new MediumStoneDirt();
        }
        if(stone_big_main == null){
            stone_big_main = new BigStone();
        }
        if(stone_big_dirt_main == null){
            stone_big_dirt_main = new BigStoneDirt();
        }
        if(stone_small_snow_main == null){
            stone_small_snow_main = new SmallStoneSnow();
        }
        if(stone_medium_snow_main == null){
            stone_medium_snow_main = new MediumStoneSnow();
        }
        if(stone_big_snow_main == null){
            stone_big_snow_main = new BigStoneSnow();
        }
    }

    private static Matrix4f[] getInstanceMatrix(List<Matrix4f> matrixList){
        Matrix4f[] matrix = new Matrix4f[matrixList.size()];
        for(int i=0; i<matrix.length; i++){
            matrix[i] = matrixList.get(i);
        }
        return matrix;
    }
}
