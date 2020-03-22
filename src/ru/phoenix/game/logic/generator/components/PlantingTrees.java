package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.BigTree;
import ru.phoenix.game.content.object.passive.Bush;
import ru.phoenix.game.content.object.passive.Tree;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlantingTrees {

    private static List<Object> trees = new ArrayList<>();

    private static Object tree_main             = null;
    private static Object big_tree_main         = null;
    private static Object bush_main             = null;

    private static int width;
    private static int height;

    public static List<Object> start(Cell[][] grid, int w, int h){
        trees.clear();
        width = w;
        height = h;
        initTrees();

        for(int x = 1; x <= width - 1; x++){
            for(int z = 1; z <= height - 1; z++) {
                setupTrees(grid,x,z);
            }
        }

        return trees;
    }

    private static void setupTrees(Cell[][] grid, int x, int z){
        boolean isBigTree = false;
        boolean isSmallTree = false;
        float currentHeight = grid[x][z].getCurrentOriginalHeight();
        if (treeTest(grid, grid[x][z].getPosition(), true)) {
            int chance = Math.round(currentHeight / 20.0f);
            if ((int) (Math.random() * 100.0f) <= chance) {
                Object bigTree = new BigTree((BigTree) big_tree_main, currentHeight);
                if(bigTree.isApplying()) {
                    bigTree.init(null);
                    bigTree.setPosition(grid[x][z].getPosition().add(new Vector3f(0.5f, 0.0f, 0.5f)));
                    trees.add(bigTree);
                    grid[x][z].setBlocked(true);
                    grid[x + 1][z].setBlocked(true);
                    grid[x][z + 1].setBlocked(true);
                    grid[x + 1][z + 1].setBlocked(true);
                    isBigTree = true;
                }
            }
        }
        if (!isBigTree) {
            if (treeTest(grid, grid[x][z].getPosition(), false)) {
                int chance = Math.round(currentHeight / 5.0f);;
                if ((int) (Math.random() * 100.0f) <= chance) {
                    Object tree = new Tree((Tree) tree_main, currentHeight);
                    if(tree.isApplying()) {
                        tree.init(null);
                        tree.setPosition(grid[x][z].getPosition());
                        trees.add(tree);
                        grid[x][z].setBlocked(true);
                        isSmallTree = true;
                    }
                }
            }
            if (!isSmallTree) {
                if (treeTest(grid, grid[x][z].getPosition(), false)) {
                    int chance = Math.round(currentHeight / 5.0f);;
                    if ((int) (Math.random() * 100.0f) <= chance) {
                        Object bush = new Bush((Bush) bush_main, currentHeight);
                        if(bush.isApplying()) {
                            bush.init(null);
                            bush.setPosition(grid[x][z].getPosition());
                            trees.add(bush);
                            grid[x][z].setBlocked(true);
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
            if(!grid[(int)studyPosition.getX()][(int)studyPosition.getZ()].isBlocked()) {
                if (grid[(int) studyPosition.getX()][(int) studyPosition.getZ()].getCurrentHeight() == etalon.getY()) {
                    check = true;
                }
            }
        }
        return check;
    }
}
