package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.Weed;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

public class PlantGrass {

    private static Object grass_main             = null;

    private static List<Object> grasses = new ArrayList<>();

    public static List<Object> start(Cell[][] grid, int w, int h, float currentHeight){
        grasses.clear();
        initGrass();

        List<Matrix4f> weedInstanceList1 = new ArrayList<>();
        List<Matrix4f> weedInstanceList2 = new ArrayList<>();
        List<Matrix4f> weedInstanceList3 = new ArrayList<>();
        List<Matrix4f> weedInstanceList4 = new ArrayList<>();
        List<Matrix4f> weedInstanceList5 = new ArrayList<>();
        List<Matrix4f> weedInstanceList6 = new ArrayList<>();

        if(currentHeight == 5.0f){ // Пустыня
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 2.2f || height > 9.2f) {
                        type = 1;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 5.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 5.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
            }
        }else if(currentHeight == 15.0f){ // Степь
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height > 12.0f){
                        type = 2;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 15.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 15.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 25.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 25.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index1) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
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
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 25.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index2) {
                                    case 0:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type1);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type1);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type2);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type2);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type2);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
            }
        }else if(currentHeight == 35.0f){ // Лес
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    type = 4;
                    if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                        float chance = 25.0f;
                        if (Math.random() * 100.0f <= chance) {
                            Projection spriteProjection = new Projection();
                            spriteProjection.setTranslation(grid[x][z].getPosition());
                            grid[x][z].setGrass(true);
                            switch (index) {
                                case 0:
                                    weedInstanceList1.add(spriteProjection.getModelMatrix());
                                    break;
                                case 1:
                                    weedInstanceList2.add(spriteProjection.getModelMatrix());
                                    break;
                                case 2:
                                    weedInstanceList3.add(spriteProjection.getModelMatrix());
                                    break;
                                case 3:
                                    weedInstanceList4.add(spriteProjection.getModelMatrix());
                                    break;
                                case 4:
                                    weedInstanceList5.add(spriteProjection.getModelMatrix());
                                    break;
                                case 5:
                                    weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
            }
        }else if(currentHeight == 40.0f){ // Лес --> Горы
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 40.0f) {
                        type = 4;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 15.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
            }
        }else{ // Горы
            int type = 0;
            int index = 0;
            for(int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    float height = grid[x][z].getCurrentOriginalHeight();
                    if(height < 43.0f) {
                        type = 1;
                        if (!grid[x][z].isBlocked() && !grid[x][z].isBevel()) {
                            float chance = 10.0f;
                            if (Math.random() * 100.0f <= chance) {
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(grid[x][z].getPosition());
                                grid[x][z].setGrass(true);
                                switch (index) {
                                    case 0:
                                        weedInstanceList1.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 1:
                                        weedInstanceList2.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 2:
                                        weedInstanceList3.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 3:
                                        weedInstanceList4.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 4:
                                        weedInstanceList5.add(spriteProjection.getModelMatrix());
                                        break;
                                    case 5:
                                        weedInstanceList6.add(spriteProjection.getModelMatrix());
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
            }

            Object weed2 = new Weed((Weed)grass_main,type);
            if(weed2.isApplying()) {
                weed2.init(getInstanceMatrix(weedInstanceList2));
                grasses.add(weed2);
            }

            Object weed3 = new Weed((Weed)grass_main,type);
            if(weed3.isApplying()) {
                weed3.init(getInstanceMatrix(weedInstanceList3));
                grasses.add(weed3);
            }

            Object weed4 = new Weed((Weed)grass_main,type);
            if(weed4.isApplying()) {
                weed4.init(getInstanceMatrix(weedInstanceList4));
                grasses.add(weed4);
            }

            Object weed5 = new Weed((Weed)grass_main,type);
            if(weed5.isApplying()) {
                weed5.init(getInstanceMatrix(weedInstanceList5));
                grasses.add(weed5);
            }

            Object weed6 = new Weed((Weed)grass_main,type);
            if(weed6.isApplying()) {
                weed6.init(getInstanceMatrix(weedInstanceList6));
                grasses.add(weed6);
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
