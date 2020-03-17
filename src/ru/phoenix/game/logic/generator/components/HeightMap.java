package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.debug.HowLong;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.element.grid.Cell;

public class HeightMap {

    private static float[][] heiMap;

    public static Cell[][] get(long seed, int width, int height, float currentHeight, boolean aligment){
        heiMap = new float[(width + 1) * 16][(height + 1) * 16];
        HowLong.setup("карты вершин");
        Cell[][] heightMap = new Cell[width + 1][height + 1];
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 20.0f + (float)Math.random() * 30.0f;
        int coin = (int)Math.round(Math.random() * 10.0f);
        if(coin < 5.0f){
            accuracy = accuracy * 5.0f;
        }
        float cellId = 0.0f;
        float[][]map = new float[width + 1][height + 1];
        for(int x = 0; x <= width; x++) {
            for(int z = 0; z <= height; z++) {
                float value = perlin.getNoise(x/accuracy,z/accuracy,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                map[x][z] = result;
            }
        }

        float min = map[0][0];
        float max = map[0][0];
        for(int x = 0; x < map.length; x++){
            for(int z = 0; z < map[0].length; z++){
                float num = map[x][z];
                if(num < min){
                    min = num;
                }
                if(num > max){
                    max = num;
                }
            }
        }

        float diff = Math.abs(max - min);
        System.out.println("min: " + min + " | " + " max: " + max + " | diff: " + diff);

        for(int x=0; x<heightMap.length; x++){
            for(int z=0; z<heightMap[0].length; z++){
                cellId += 0.001f; // max 40.0f
                float h = map[x][z] - min;
                float percent = h * 100.0f / diff;
                float y = currentHeight + (((1.0f * percent / 100.0f) + 0.5f - 1.0f) * (currentHeight / 3.5f));

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

        map = new float[heiMap.length][heiMap[0].length];
        for(int x=0; x<heiMap.length; x++){
            for(int z=0; z<heiMap[0].length; z++){
                float value = perlin.getNoise(x/(accuracy * 16.0f),z/(accuracy * 16.0f),8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                map[x][z] = result;
            }
        }

        min = map[0][0];
        max = map[0][0];
        for(int x = 0; x < map.length; x++){
            for(int z = 0; z < map[0].length; z++){
                float num = map[x][z];
                if(num < min){
                    min = num;
                }
                if(num > max){
                    max = num;
                }
            }
        }

        diff = Math.abs(max - min);

        for(int x=0; x<map.length; x++){
            for(int z=0; z<map[0].length; z++){
                float h = map[x][z] - min;
                float percent = h * 100.0f / diff;
                float y = 1.0f * percent / 100.0f;
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
