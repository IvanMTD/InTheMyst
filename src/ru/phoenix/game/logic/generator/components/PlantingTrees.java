package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.BigTree;
import ru.phoenix.game.content.object.passive.Bush;
import ru.phoenix.game.content.object.passive.Tree;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.datafile.SaveElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlantingTrees {

    private static List<Object> trees = new ArrayList<>();
    private static List<SaveElement> saveElements = new ArrayList<>();

    private static Object tree_main             = null;
    private static Object big_tree_main         = null;
    private static Object bush_main             = null;

    private static int width;
    private static int height;

    public static List<Object> start(Cell[][] grid, int w, int h, SaveData saveData){
        trees.clear();
        saveElements.clear();
        width = w;
        height = h;
        initTrees();

        for(int x = 1; x <= width - 1; x++){
            for(int z = 1; z <= height - 1; z++) {
                setupTrees(grid,x,z);
            }
        }

        saveData.setTreeElements(saveElements);

        return trees;
    }

    public static List<Object> start(Cell[][] grid, SaveData saveData){
        trees.clear();

        width = saveData.getSizeX() - 1;
        height = saveData.getSizeZ() - 1;

        initTrees();

        List<SaveElement> elements = new ArrayList<>(saveData.getTreeElements());

        for(SaveElement element : elements){
            switch (element.getType()){
                case 1:
                    Object bigTree = new BigTree((BigTree) big_tree_main,element.getCurrentHeight());
                    bigTree.init(null,element);
                    bigTree.setPosition(element.getElementPos());
                    for(Vector2f pos : element.getBlockedPos()){
                        grid[(int)pos.getX()][(int)pos.getY()].setBlocked(true);
                    }
                    trees.add(bigTree);
                    break;
                case 2:
                    Object tree = new Tree((Tree)tree_main,element.getCurrentHeight());
                    tree.init(null,element);
                    tree.setPosition(element.getElementPos());
                    for(Vector2f pos : element.getBlockedPos()){
                        grid[(int)pos.getX()][(int)pos.getY()].setBlocked(true);
                    }
                    trees.add(tree);
                    break;
                case 3:
                    Object bush = new Bush((Bush)bush_main,element.getCurrentHeight());
                    bush.init(null,element);
                    bush.setPosition(element.getElementPos());
                    for(Vector2f pos : element.getBlockedPos()){
                        grid[(int)pos.getX()][(int)pos.getY()].setBlocked(true);
                    }
                    trees.add(bush);
                    break;
            }
        }


        return trees;
    }

    private static void setupTrees(Cell[][] grid, int x, int z){

        int type = 0;
        List<Vector2f> blockedPos = new ArrayList<>();

        boolean isBigTree = false;
        boolean isSmallTree = false;
        float currentHeight = grid[x][z].getCurrentOriginalHeight();
        if (treeTest(grid, grid[x][z].getPosition(), true)) {
            float chance = (float)Math.round(currentHeight / 20.0f);
            if ((Math.random() * 100.0f) <= chance) {
                Object bigTree = new BigTree((BigTree) big_tree_main, currentHeight);
                if(bigTree.isApplying()) {
                    bigTree.init(null);
                    bigTree.setPosition(grid[x][z].getPosition().add(new Vector3f(0.5f, 0.0f, 0.5f)));
                    trees.add(bigTree);
                    grid[x][z].setBlocked(true);
                    blockedPos.add(new Vector2f(x,z));
                    grid[x + 1][z].setBlocked(true);
                    blockedPos.add(new Vector2f(x+1,z));
                    grid[x][z + 1].setBlocked(true);
                    blockedPos.add(new Vector2f(x,z+1));
                    grid[x + 1][z + 1].setBlocked(true);
                    blockedPos.add(new Vector2f(x+1,z+1));
                    isBigTree = true;
                    type = 1;
                    SaveElement saveElement = new SaveElement(blockedPos,bigTree.getPosition(),type,bigTree.getTextureNum(),currentHeight,bigTree.getObjectWidth(),bigTree.getObjectHeight());
                    saveElements.add(saveElement);
                }
            }
        }
        if (!isBigTree) {
            if (treeTest(grid, grid[x][z].getPosition(), false)) {
                float chance = (float)Math.round(currentHeight / 5.0f);
                if(currentHeight < 10.0f){
                    chance = 0.1f;
                }
                if ((Math.random() * 100.0f) <= chance) {
                    Object tree = new Tree((Tree) tree_main, currentHeight);
                    if(tree.isApplying()) {
                        tree.init(null);
                        tree.setPosition(grid[x][z].getPosition());
                        trees.add(tree);
                        grid[x][z].setBlocked(true);
                        blockedPos.add(new Vector2f(x,z));
                        isSmallTree = true;
                        type = 2;
                        SaveElement saveElement = new SaveElement(blockedPos,tree.getPosition(),type,tree.getTextureNum(),currentHeight,tree.getObjectWidth(),tree.getObjectHeight());
                        saveElements.add(saveElement);
                    }
                }
            }
            if (!isSmallTree) {
                if (treeTest(grid, grid[x][z].getPosition(), false)) {
                    float chance = (float)Math.round(currentHeight / 5.0f);
                    if(currentHeight < 10.0f){
                        chance = 0.1f;
                    }
                    if ((Math.random() * 100.0f) <= chance) {
                        Object bush = new Bush((Bush) bush_main, currentHeight);
                        if(bush.isApplying()) {
                            bush.init(null);
                            bush.setPosition(grid[x][z].getPosition());
                            trees.add(bush);
                            grid[x][z].setBlocked(true);
                            blockedPos.add(new Vector2f(x,z));
                            type = 3;
                            SaveElement saveElement = new SaveElement(blockedPos,bush.getPosition(),type,bush.getTextureNum(),currentHeight,bush.getObjectWidth(),bush.getObjectHeight());
                            saveElements.add(saveElement);
                        }
                    }
                }
            }
        }
    }

    private static void initTrees(){
        if(tree_main == null){
            tree_main = new Tree();
        }
        if(big_tree_main == null){
            big_tree_main = new BigTree();
        }
        if(bush_main == null){
            bush_main = new Bush();
        }
    }

    private static boolean treeTest(Cell[][]grid, Vector3f position, boolean big){
        boolean check = false;
        List<Vector3f> checkList = new ArrayList<>();
        Vector3f etalon = new Vector3f(position);
        Vector3f point = new Vector3f(position.getX(),0.0f,position.getZ());

        if(big) {
            Vector3f stydyPos1 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ() - 1.0f);
            Vector3f stydyPos2 = new Vector3f(point.getX(), point.getY(), point.getZ() - 1.0f);
            Vector3f stydyPos3 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ() - 1.0f);
            Vector3f stydyPos4 = new Vector3f(point.getX() + 2.0f, point.getY(), point.getZ() - 1.0f);

            Vector3f stydyPos5 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ());
            Vector3f stydyPos6 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ());
            Vector3f stydyPos7 = new Vector3f(point.getX() + 2.0f, point.getY(), point.getZ());

            Vector3f stydyPos8 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ() + 1.0f);
            Vector3f stydyPos9 = new Vector3f(point.getX(), point.getY(), point.getZ() + 1.0f);
            Vector3f stydyPos10 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ() + 1.0f);
            Vector3f stydyPos11 = new Vector3f(point.getX() + 2.0f, point.getY(), point.getZ() + 1.0f);

            Vector3f stydyPos12 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ() + 2.0f);
            Vector3f stydyPos13 = new Vector3f(point.getX(), point.getY(), point.getZ() + 2.0f);
            Vector3f stydyPos14 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ() + 2.0f);
            Vector3f stydyPos15 = new Vector3f(point.getX() + 2.0f, point.getY(), point.getZ() + 2.0f);

            checkList = new ArrayList<>(Arrays.asList(stydyPos1, stydyPos2, stydyPos3, stydyPos4, stydyPos5, stydyPos6, stydyPos7, stydyPos8, stydyPos9, stydyPos10, stydyPos11, stydyPos12, stydyPos13, stydyPos14, stydyPos15));
        }else{
            Vector3f stydyPos1 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ() - 1.0f);
            Vector3f stydyPos2 = new Vector3f(point.getX(), point.getY(), point.getZ() - 1.0f);
            Vector3f stydyPos3 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ() - 1.0f);

            Vector3f stydyPos4 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ());
            Vector3f stydyPos5 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ());

            Vector3f stydyPos6 = new Vector3f(point.getX() - 1.0f, point.getY(), point.getZ() + 1.0f);
            Vector3f stydyPos7 = new Vector3f(point.getX(), point.getY(), point.getZ() + 1.0f);
            Vector3f stydyPos8 = new Vector3f(point.getX() + 1.0f, point.getY(), point.getZ() + 1.0f);

            checkList = new ArrayList<>(Arrays.asList(stydyPos1, stydyPos2, stydyPos3, stydyPos4, stydyPos5, stydyPos6, stydyPos7, stydyPos8));
        }

        int test = 0;

        for(Vector3f studyPosition : checkList){
            if(checkPosition(grid,etalon,studyPosition)){
                test++;
            }
        }

        if(big ? test == 15 : test == 8){
            check = true;
        }

        return check;
    }

    private static boolean checkPosition(Cell[][]grid, Vector3f etalon, Vector3f studyPosition){
        boolean check = false;
        if((0 <= studyPosition.getX() && studyPosition.getX() <= width) && (0 <= studyPosition.getZ() && studyPosition.getZ() <= height)){
            if(!grid[(int)studyPosition.getX()][(int)studyPosition.getZ()].isBlocked() && !grid[(int)studyPosition.getX()][(int)studyPosition.getZ()].isRoad()) {
                if (grid[(int) studyPosition.getX()][(int) studyPosition.getZ()].getCurrentHeight() == etalon.getY()) {
                    check = true;
                }
            }
        }
        return check;
    }
}
