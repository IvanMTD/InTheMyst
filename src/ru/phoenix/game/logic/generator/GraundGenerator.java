package ru.phoenix.game.logic.generator;

import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.Dirt;
import ru.phoenix.game.content.block.type.Grass;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.random.RandomArena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.MOUNTAIN_MAP;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;
import static ru.phoenix.core.config.Constants.RIVER_MAP;

public class GraundGenerator {

    private static Block dirt_main = null;
    private static Block grass_main = null;

    private static BattleGraund battleGraund;
    private static int mapWidthOffset;
    private static int mapHeightOffset;
    private static int pits;
    private static int hills;
    private static boolean tilt;
    private static int tiltType;

    public static BattleGraund generateMap(int seed){

        if(dirt_main == null || grass_main == null){
            dirt_main = new Dirt();
            grass_main = new Grass();
        }

        List<Block> blocks = new ArrayList<>();

        generateMapSize();

        if(seed == PLAIN_MAP){

            generatePitsHills((int)(0.0f + Math.random() * 3.9f));

            if(hills != 0){

            }

            Block dirt = new Dirt((Dirt)dirt_main);
            Block grass = new Grass((Grass)grass_main);
            List<Matrix4f>dirtInstanceMatrix = new ArrayList<>();
            List<Matrix4f>grassInstanceMatrix = new ArrayList<>();

            for(int z = -mapHeightOffset; z <= mapHeightOffset; z++){
                for(int x= -mapWidthOffset; x <= mapWidthOffset; x++){

                    int y = 0;
                    Projection projection = new Projection();
                    Vector3f position = new Vector3f(x, y, z);
                    projection.setTranslation(position);

                    if(y >= 0) {
                        grassInstanceMatrix.add(projection.getModelMatrix());
                    }else{
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                    }
                }
            }

            Matrix4f[] dirtMatrix = new Matrix4f[dirtInstanceMatrix.size()];
            for(int i=0; i<dirtMatrix.length; i++){
                dirtMatrix[i] = dirtInstanceMatrix.get(i);
            }

            Matrix4f[] grassMatrix = new Matrix4f[grassInstanceMatrix.size()];
            for(int i=0; i<grassMatrix.length; i++){
                grassMatrix[i] = grassInstanceMatrix.get(i);
            }

            dirt.setInstanceMatrix(dirtMatrix);
            grass.setInstanceMatrix(grassMatrix);
            blocks = new ArrayList<>(Arrays.asList(dirt,grass));
        }else if(seed == MOUNTAIN_MAP){

        }else if(seed == RIVER_MAP){

        }

        int mapX = mapWidthOffset * 2 + 1;
        int mapZ = mapHeightOffset * 2 + 1;
        return battleGraund = new RandomArena(blocks,mapX,mapZ);
    }

    private static void generateMapSize(){
        mapWidthOffset = (int)(5.0f + Math.random() * 5.0f);
        mapHeightOffset = (int)(5.0f + Math.random() * 5.0f);
    }

    private static void generatePitsHills(int maxAmount){

        int pits = 0;
        int hills = 0;

        if(maxAmount != 0) {
            int finalAmount = (int) (0.0f + Math.random() * (maxAmount + 0.9f));
            pits = (int) (0.0f + Math.random() * (finalAmount + 0.9f));
            hills = finalAmount - pits;
        }

        GraundGenerator.pits = pits;
        GraundGenerator.hills = hills;
    }
}
