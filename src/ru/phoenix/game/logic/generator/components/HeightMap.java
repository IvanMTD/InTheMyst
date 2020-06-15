package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.debug.HowLong;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.datafile.StructData;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.datafile.SaveData;

public class HeightMap {
    private static Vector3f[][] heiMap;

    public static Cell[][] get(long seed, int width, int height, float currentHeight, boolean aligment, float radius, SaveData saveData){
        heiMap = new Vector3f[(width + 1) * 16][(height + 1) * 16];
        HowLong.setup("карты вершин");
        Cell[][] heightMap = new Cell[width + 1][height + 1];
        StructData[][] structData = new StructData[width + 1][height + 1];
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 100.0f;

        float cellId = 0.0f;
        float[][]map = new float[width + 1][height + 1];
        Vector3f center = new Vector3f(width/2.0f,0.0f,height/2.0f);
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
                percent = (percent / 100.0f) + 0.5f - 1.0f;
                int pice = ((heightMap[0].length - 1) / 3);
                int special = (heightMap[0].length - 1) - pice;
                float proportion = 1.0f;
                if(z > special){
                    int num = z - special;
                    proportion = 1.0f - (float)num / (float)pice;
                }

                Vector3f currentPos = new Vector3f(x,0.0f,z);
                if((z > heightMap[0].length - 6) || ((Math.abs(center.sub(currentPos).length()) < radius + (-1.0f + (Math.random() * 2.0f))))){
                    proportion = 0.0f;
                }else{
                    proportion = 0.3f;
                }

                float oy = currentHeight + percent * 10.0f;
                percent = percent * proportion;

                float y = currentHeight + (percent * (currentHeight / 3.5f));
                if(currentHeight == 35.0f){
                    y = currentHeight + (percent * 3.0f);
                }

                float minY = currentHeight - 5.0f;
                float maxY = currentHeight + 5.0f;
                if(y < minY){
                    y = minY;
                }
                if(y > maxY){
                    y = maxY;
                }

                Cell cell = new Cell();
                cell.setId(cellId);
                if(aligment){
                    float heightDifference = Math.abs(Math.round(y) - y);
                    if(0.35f <= heightDifference && heightDifference <= 0.65f){
                        if(x != 0 && x != width && z != 0 && z != height) {
                            cell.setCurrentHeight((float) Math.floor(y) + 0.5f);
                            cell.setCurrentOriginalHeight(oy);
                            cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                        }else {
                            cell.setCurrentHeight((float) Math.floor(y));
                            cell.setCurrentOriginalHeight(oy);
                            cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                        }
                    }else{
                        cell.setCurrentHeight((float)Math.round(y));
                        cell.setCurrentOriginalHeight(oy);
                        cell.setPosition(new Vector3f(x,cell.getCurrentHeight(),z));
                    }

                    if(z >= heightMap[0].length - 4){
                        cell.setRoad(true);
                    }

                }else {
                    cell.setCurrentHeight(y);
                    cell.setCurrentOriginalHeight(oy);
                    cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                }

                structData[x][z] = new StructData(cell.getCurrentHeight(),cell.getCurrentOriginalHeight(),cell.getPosition(),cell.isRoad());

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
                int offset = (int)Math.round(14.0f + Math.random() * 2.0f);
                if(z > map[0].length - (4 * offset)){
                    float h = map[x][z] - min;
                    float percent = h * 100.0f / diff;
                    float y = percent / 100.0f;
                    heiMap[x][z] = new Vector3f(y, 0.0f, 0.0f);
                }else {
                    float h = map[x][z] - min;
                    float percent = h * 100.0f / diff;
                    float y = percent / 100.0f;
                    heiMap[x][z] = new Vector3f(y, y, y);
                }
            }
        }

        saveData.setStructData(structData);

        HowLong.getInformation();
        return heightMap;
    }

    public static Cell[][] get(long seed, int width, int height, float currentHeight, boolean aligment, SaveData saveData){
        heiMap = new Vector3f[(width + 1) * 16][(height + 1) * 16];
        HowLong.setup("карты вершин");
        Cell[][] heightMap = new Cell[width + 1][height + 1];
        StructData[][] structData = new StructData[width + 1][height + 1];
        Perlin2D perlin = new Perlin2D(seed);
        float accuracy = 20.0f + (float)Math.random() * 30.0f;
        int coin = (int)Math.round(Math.random() * 10.0f);
        if(coin < 5.0f){
            accuracy = accuracy * 5.0f;
        }

        /*if(currentHeight == 10.0f || currentHeight == 20.0f || currentHeight == 30.0f){
            accuracy = accuracy * 5.0f;
        }*/

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
                percent = (percent / 100.0f) + 0.5f - 1.0f;
                int pice = ((heightMap[0].length - 1) / 3);
                int special = (heightMap[0].length - 1) - pice;
                float proportion = 1.0f;
                if(z > special){
                    int num = z - special;
                    proportion = 1.0f - (float)num / (float)pice;
                }

                if(z > heightMap[0].length - 6){
                    proportion = 0.0f;
                }

                float oy = currentHeight + percent * 10.0f;
                percent = percent * proportion;

                float y = currentHeight + (percent * (currentHeight / 3.5f));
                if(currentHeight == 35.0f){
                    y = currentHeight + (percent * 3.0f);
                }

                float minY = currentHeight - 5.0f;
                float maxY = currentHeight + 5.0f;
                if(y < minY){
                    y = minY;
                }
                if(y > maxY){
                    y = maxY;
                }

                Cell cell = new Cell();
                cell.setId(cellId);
                if(aligment){
                    float heightDifference = Math.abs(Math.round(y) - y);
                    if(0.35f <= heightDifference && heightDifference <= 0.65f){
                        if(x != 0 && x != width && z != 0 && z != height) {
                            cell.setCurrentHeight((float) Math.floor(y) + 0.5f);
                            cell.setCurrentOriginalHeight(oy);
                            cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                        }else {
                            cell.setCurrentHeight((float) Math.floor(y));
                            cell.setCurrentOriginalHeight(oy);
                            cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                        }
                    }else{
                        cell.setCurrentHeight((float)Math.round(y));
                        cell.setCurrentOriginalHeight(oy);
                        cell.setPosition(new Vector3f(x,cell.getCurrentHeight(),z));
                    }

                    if(z >= heightMap[0].length - 4){
                        cell.setRoad(true);
                    }

                }else {
                    cell.setCurrentHeight(y);
                    cell.setCurrentOriginalHeight(oy);
                    cell.setPosition(new Vector3f(x, cell.getCurrentHeight(), z));
                }

                structData[x][z] = new StructData(cell.getCurrentHeight(),cell.getCurrentOriginalHeight(),cell.getPosition(),cell.isRoad());

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
                int offset = (int)Math.round(14.0f + Math.random() * 2.0f);
                if(z > map[0].length - (4 * offset)){
                    float h = map[x][z] - min;
                    float percent = h * 100.0f / diff;
                    float y = percent / 100.0f;
                    heiMap[x][z] = new Vector3f(y, 0.0f, 0.0f);
                }else {
                    float h = map[x][z] - min;
                    float percent = h * 100.0f / diff;
                    float y = percent / 100.0f;
                    heiMap[x][z] = new Vector3f(y, y, y);
                }
            }
        }

        saveData.setStructData(structData);

        HowLong.getInformation();
        return heightMap;
    }

    public static Cell[][] get(SaveData saveData){
        Cell[][] heightMap = new Cell[saveData.getSizeX()][saveData.getSizeZ()];
        for(int x = 0; x<heightMap.length; x++){
            for(int z=0; z<heightMap[0].length; z++){
                heightMap[x][z] = new Cell();
                heightMap[x][z].setCurrentHeight(saveData.getStructData()[x][z].getCurrentHeight());
                heightMap[x][z].setCurrentOriginalHeight(saveData.getStructData()[x][z].getCurrentOriginalHeight());
                heightMap[x][z].setPosition(new Vector3f(saveData.getStructData()[x][z].getPosition()));
                heightMap[x][z].setRoad(saveData.getStructData()[x][z].isRoad());
            }
        }
        return heightMap;
    }

    public static Vector3f[][] getHeiMap() {
        return heiMap;
    }
}