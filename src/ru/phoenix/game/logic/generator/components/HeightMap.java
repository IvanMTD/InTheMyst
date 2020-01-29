package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.debug.HowLong;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.element.grid.Cell;

public class HeightMap {

    private static float[][] heiMap;

    public static Cell[][] get(long seed, int width, int height, int heightRange, boolean aligment){
        heiMap = new float[(width + 1) * 16][(height + 1) * 16];
        HowLong.setup("карты вершин");
        Cell[][] heightMap = new Cell[width + 1][height + 1];
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 20.0f + (float)Math.random() * 30.0f;
        float cellId = 0.0f;
        for(int x = 0; x <= width; x++) {
            for(int z = 0; z <= height; z++) {
                cellId += 0.001f; // max 40.0f
                float value = perlin.getNoise(x/accuracy,z/accuracy,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                float y = (result + result - 1.0f) * (float)heightRange * 2;
                if(y > heightRange){
                    y = heightRange;
                }else if(y < -4.0f){
                    y = -4.0f;
                }
                //heiMap[x][z] = y;
                Cell cell = new Cell();
                cell.setId(cellId);
                if(aligment){
                    float heightDifference = Math.abs(Math.round(y) - y);
                    if(0.35f <= heightDifference && heightDifference <= 0.65f){
                        if(x != 0 && x != width && z != 0 && z != height) {
                            cell.setCurrentHeight((float)Math.floor(y) + 0.5f);
                            cell.setPosition(new Vector3f(x,cell.getCurrentHeight(),z));
                        }else{
                            cell.setCurrentHeight((float)Math.floor(y));
                            cell.setPosition(new Vector3f(x,cell.getCurrentHeight(),z));
                        }
                    }else{
                        cell.setCurrentHeight((float)Math.round(y));
                        cell.setPosition(new Vector3f(x,cell.getCurrentHeight(),z));
                    }
                }else {
                    cell.setCurrentHeight(y);
                    cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                }
                heightMap[x][z] = cell;
            }
        }

        //perlin = new Perlin2D(seed);
        for(int x = 0; x < heiMap.length; x++) {
            for(int z = 0; z < heiMap[0].length; z++) {
                float value = perlin.getNoise(x/accuracy,z/accuracy,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                float y = (result + result - 1.0f) * (float)heightRange * 2;
                if(y > heightRange){
                    y = heightRange;
                }else if(y < -4.0f){
                    y = -4.0f;
                }
                heiMap[x][z] = y;
            }
        }
        HowLong.getInformation();
        return heightMap;
    }

    public static float[][] getHeiMap() {
        return heiMap;
    }
}
