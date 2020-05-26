package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.Weed;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

public class PlantGrass {

    private static Object grass_main             = null;

    private static List<Object> grasses = new ArrayList<>();

    public static List<Object> start(Cell[][] grid, int w, int h, float currentHeight, SaveData saveData){
        grasses.clear();
        initGrass();

        List<Matrix4f> weedInstanceList1 = new ArrayList<>(); List<Vector2f> wp1 = new ArrayList<>();
        List<Matrix4f> weedInstanceList2 = new ArrayList<>(); List<Vector2f> wp2 = new ArrayList<>();
        List<Matrix4f> weedInstanceList3 = new ArrayList<>(); List<Vector2f> wp3 = new ArrayList<>();
        List<Matrix4f> weedInstanceList4 = new ArrayList<>(); List<Vector2f> wp4 = new ArrayList<>();
        List<Matrix4f> weedInstanceList5 = new ArrayList<>(); List<Vector2f> wp5 = new ArrayList<>();
        List<Matrix4f> weedInstanceList6 = new ArrayList<>(); List<Vector2f> wp6 = new ArrayList<>();

        List<SaveElement> saveElements = new ArrayList<>();

        if(currentHeight == 5.0f){ // Пустыня
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 2.2f || height > 9.2f) {
                        type = 1;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 5.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index++;
                                if (index > 5) {
                                    index = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 10.0f){ // Пустыня --> Степь
            int type1 = 0;
            int type2 = 0;
            int index1 = 0;
            int index2 = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height > 10.2f && height < 12.0f) {
                        type1 = 1;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 5.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                }
                                index1++;
                                if (index1 > 2) {
                                    index1 = 0;
                                }
                            }
                        }
                    }else if(height > 14.0f){
                        type2 = 2;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index2++;
                                if (index2 > 2) {
                                    index2 = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type1);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 15.0f){ // Степь
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height > 12.0f){
                        type = 2;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 15.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index++;
                                if (index > 5) {
                                    index = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 20.0f){ // Степь --> Ровнина
            int type1 = 0;
            int type2 = 0;
            int index1 = 0;
            int index2 = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 22.0f){
                        type1 = 2;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                }
                                index1++;
                                if (index1 > 2) {
                                    index1 = 0;
                                }
                            }
                        }
                    }else {
                        type2 = 3;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 15.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index2++;
                                if (index2 > 2) {
                                    index2 = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type1);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 25.0f){ // Ровнина
            int type1 = 0;
            int type2 = 0;
            int index1 = 0;
            int index2 = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 22.0f){
                        type1 = 1;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                }
                                index1++;
                                if (index1 > 2) {
                                    index1 = 0;
                                }
                            }
                        }
                    }else if(height > 23.5f){
                        type2 = 4;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 25.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index2++;
                                if (index2 > 2) {
                                    index2 = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type1);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 30.0f){ // Ровнина --> Лес
            int type1 = 0;
            int type2 = 0;
            int index1 = 0;
            int index2 = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 30.0f){
                        type1 = 4;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 25.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                }
                                index1++;
                                if (index1 > 2) {
                                    index1 = 0;
                                }
                            }
                        }
                    }else if(height > 23.5f){
                        type2 = 5;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 25.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index2++;
                                if (index2 > 2) {
                                    index2 = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type1);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type1);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type2);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 35.0f){ // Лес
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    type = 4;
                    if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                        float chance = 25.0f;
                        if (Math.random() * 100.0f <= chance) {
                            Projection spriteProjection = new Projection();
                            spriteProjection.setTranslation(grid[x][z].getPosition());
                            grid[x][z].setGrass(true);
                            switch (index) {
                                case 0:
                                    weedInstanceList1.add(spriteProjection.getModelMatrix());
                                    wp1.add(new Vector2f(x,z));
                                    break;
                                case 1:
                                    weedInstanceList2.add(spriteProjection.getModelMatrix());
                                    wp2.add(new Vector2f(x,z));
                                    break;
                                case 2:
                                    weedInstanceList3.add(spriteProjection.getModelMatrix());
                                    wp3.add(new Vector2f(x,z));
                                    break;
                                case 3:
                                    weedInstanceList4.add(spriteProjection.getModelMatrix());
                                    wp4.add(new Vector2f(x,z));
                                    break;
                                case 4:
                                    weedInstanceList5.add(spriteProjection.getModelMatrix());
                                    wp5.add(new Vector2f(x,z));
                                    break;
                                case 5:
                                    weedInstanceList6.add(spriteProjection.getModelMatrix());
                                    wp6.add(new Vector2f(x,z));
                                    break;
                            }
                            index++;
                            if (index > 5) {
                                index = 0;
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else if(currentHeight == 40.0f){ // Лес --> Горы
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 40.0f) {
                        type = 4;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 15.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index++;
                                if (index > 5) {
                                    index = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }else{ // Горы
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 43.0f) {
                        type = 1;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel() && !grid[x][z].isRoad()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        wp1.add(new Vector2f(x,z));
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        wp2.add(new Vector2f(x,z));
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        wp3.add(new Vector2f(x,z));
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        wp4.add(new Vector2f(x,z));
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        wp5.add(new Vector2f(x,z));
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
                                        wp6.add(new Vector2f(x,z));
                                        break;
                                }
                                index++;
                                if (index > 5) {
                                    index = 0;
                                }
                            }
                        }
                    }
                }
            }

            Object weed1 = new Weed((Weed)grass_main, type);
            if(weed1.isApplying()) {
                weed1.init(getInstanceMatrix(weedInstanceList1));
                grasses.add(weed1);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp1);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList1));
                saveElements.add(saveElement);
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp2);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList2));
                saveElements.add(saveElement);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp3);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList3));
                saveElements.add(saveElement);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp4);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList4));
                saveElements.add(saveElement);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp5);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList5));
                saveElements.add(saveElement);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
                SaveElement saveElement = new SaveElement();
                saveElement.setType(type);
                saveElement.setBlockedPos(wp6);
                saveElement.setGrassMatrix(getInstanceMatrix(weedInstanceList6));
                saveElements.add(saveElement);
            }
        }

        saveData.setPlantElements(saveElements);

        return grasses;
    }

    public static List<Object> start(Cell[][] grid, SaveData saveData){

        grasses.clear();

        initGrass();

        System.out.println(saveData.getPlantElements().size());

        for(SaveElement element : saveData.getPlantElements()){
            Object weed = new Weed((Weed)grass_main,element.getType());
            weed.init(element.getGrassMatrix());
            grasses.add(weed);
            for(Vector2f info : element.getBlockedPos()){
                grid[(int)info.getX()][(int)info.getY()].setGrass(true);
            }
        }

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
