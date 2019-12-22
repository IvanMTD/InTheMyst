package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.loader.model.Vertex;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.water.WaterLine;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.*;
import static ru.phoenix.core.config.Constants.UP_BOARD;

public class WaterBay {

    private static Object water_line_main       = null;

    private static List<Object> water = new ArrayList<>();

    public static List<Object> fill(Cell[][]grid, int w, int h, int currentArea){
        water.clear();
        initWater();

        List<Matrix4f> waterLineInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineLeftInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineRightInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineUpInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineDownInstanceList = new ArrayList<>();

        if(currentArea == PLAIN_AREA) {
            for (int x = 0; x <= w; x++) {
                for (int z = 0; z <= h; z++) {
                    if (grid[x][z].getPosition().getY() < 0) {
                        Vector3f newPos = new Vector3f(x, -0.75f, z);
                        Projection newProjection = new Projection();
                        newProjection.setTranslation(newPos);
                        waterLineInstanceList.add(newProjection.getModelMatrix());
                        if(grid[x][z].getPosition().getY() < - 0.5f){
                            grid[x][z].setWater(true);
                        }

                        if(x == 0.0f){ // left
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f((float)x - 0.49f, someY, z);
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(-90.0f, new Vector3f(0.0f, 0.0f, 1.0f));
                                waterLineLeftInstanceList.add(rp.getModelMatrix());
                            }
                        }
                        if(x == w){ // right
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f((float)x + 0.49f, someY, z);
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(90.0f, new Vector3f(0.0f, 0.0f, 1.0f));
                                waterLineRightInstanceList.add(rp.getModelMatrix());
                            }
                        }
                        if(z == 0.0f){ // down
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f(x, someY, (float)z - 0.49f);
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(-90.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                                waterLineDownInstanceList.add(rp.getModelMatrix());
                            }
                        }
                        if(z == h){ // up
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f(x, someY, (float)z + 0.49f);
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(90.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                                waterLineUpInstanceList.add(rp.getModelMatrix());
                            }
                        }
                    }
                }
            }
        }

        water_line_main.init(getInstanceMatrix(waterLineInstanceList));
        Object waterLine_left = new WaterLine((WaterLine)water_line_main,LEFT_BOARD);
        waterLine_left.init(getInstanceMatrix(waterLineLeftInstanceList));
        Object waterLine_right = new WaterLine((WaterLine)water_line_main,RIGHT_BOARD);
        waterLine_right.init(getInstanceMatrix(waterLineRightInstanceList));
        Object waterLine_down = new WaterLine((WaterLine)water_line_main,DOWN_BOARD);
        waterLine_down.init(getInstanceMatrix(waterLineDownInstanceList));
        Object waterLine_up = new WaterLine((WaterLine)water_line_main,UP_BOARD);
        waterLine_up.init(getInstanceMatrix(waterLineUpInstanceList));
        water = new ArrayList<>(Arrays.asList(waterLine_left,waterLine_right,waterLine_down,waterLine_up,water_line_main));

        return water;
    }

    private static void initWater(){
        if(water_line_main == null){
            water_line_main = new WaterLine();
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
