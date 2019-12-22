package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.Weed;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.MOUNTAIN_AREA;
import static ru.phoenix.core.config.Constants.PLAIN_AREA;

public class PlantGrass {

    private static Object grass_main             = null;

    private static List<Object> grasses = new ArrayList<>();

    public static List<Object> start(Cell[][] grid, int w, int h, int currentArea){
        grasses.clear();
        initGrass();

        List<Matrix4f> weedInstanceList1 = new ArrayList<>();
        List<Matrix4f> weedInstanceList2 = new ArrayList<>();
        List<Matrix4f> weedInstanceList3 = new ArrayList<>();
        List<Matrix4f> weedInstanceList4 = new ArrayList<>();
        List<Matrix4f> weedInstanceList5 = new ArrayList<>();
        List<Matrix4f> weedInstanceList6 = new ArrayList<>();

        int index = 0;

        for(int x = 0; x <= w; x++){
            for(int z = 0; z <= h; z++){
                if(currentArea == PLAIN_AREA){
                    if(grid[x][z].getCurrentHeight() < 0){
                        if(!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection weedProjection = new Projection();
                                weedProjection.setTranslation(grid[x][z].getPosition());
                                weedInstanceList1.add(weedProjection.getModelMatrix());
                                grid[x][z].setGrass(true);
                            }
                        }
                    }else{
                        if(!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 35.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 3:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 4:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        break;
                                }
                                index++;
                                if (index > 4) {
                                    index = 0;
                                }
                            }
                        }
                    }
                }else if(currentArea == MOUNTAIN_AREA){
                    if(grid[x][z].getCurrentHeight() < 1.0f){
                        if(!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 3:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 4:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        break;
                                }
                                index++;
                                if (index > 4) {
                                    index = 0;
                                }
                            }
                        }
                    }
                }
            }
        }

        Object weed1 = new Weed((Weed)grass_main,-1.0f);
        weed1.init(getInstanceMatrix(weedInstanceList1));
        grasses.add(weed1);

        Object weed2 = new Weed((Weed)grass_main,currentArea);
        weed2.init(getInstanceMatrix(weedInstanceList2));
        grasses.add(weed2);

        Object weed3 = new Weed((Weed)grass_main,currentArea);
        weed3.init(getInstanceMatrix(weedInstanceList3));
        grasses.add(weed3);

        Object weed4 = new Weed((Weed)grass_main,currentArea);
        weed4.init(getInstanceMatrix(weedInstanceList4));
        grasses.add(weed4);

        Object weed5 = new Weed((Weed)grass_main,currentArea);
        weed5.init(getInstanceMatrix(weedInstanceList5));
        grasses.add(weed5);

        Object weed6 = new Weed((Weed)grass_main,currentArea);
        weed6.init(getInstanceMatrix(weedInstanceList6));
        grasses.add(weed6);

        return grasses;
    }

    private static void initGrass(){
        if(grass_main == null){
            grass_main = new Weed();
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
