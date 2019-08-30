package ru.phoenix.game.logic.generator;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.*;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.random.RandomArena;
import ru.phoenix.game.logic.generator.component.TerraElement;

import java.util.*;

import static ru.phoenix.core.config.Constants.MOUNTAIN_MAP;
import static ru.phoenix.core.config.Constants.PLAIN_MAP;
import static ru.phoenix.core.config.Constants.RIVER_MAP;

public class GraundGenerator {

    private static List<Vector3f> gridPositions = new ArrayList<>();

    private static Block dirt_main = null;
    private static Block grass_main = null;
    private static Block snow_main = null;
    private static Block mountain_dirt_main = null;
    private static Block snow_rock_main = null;
    private static Block rock_main = null;

    private static int mapWidthOffset;
    private static int mapHeightOffset;

    public static BattleGraund generateMap(int seed){

        initBlocks();
        generateMapSize();

        int area = (mapWidthOffset * 2) * (mapHeightOffset * 2);

        List<Block> blocks = new ArrayList<>();

        if(seed == PLAIN_MAP){
            gridPositions.clear();
            int maxAmount = (int)Math.round(1.0f + Math.random() * 3.0f);
            int maxRadius = area / (int)Math.round(10 + Math.random() * 30);
            int maxHeight = 1;
            List<TerraElement> elements = new ArrayList<>(generateTerraElement(maxAmount,maxRadius,maxHeight));

            Block dirt = new Dirt((Dirt)dirt_main);
            Block grass = new Grass((Grass)grass_main);
            List<Matrix4f>dirtInstanceMatrix = new ArrayList<>();
            List<Matrix4f>grassInstanceMatrix = new ArrayList<>();

            for(int z = -mapHeightOffset; z <= mapHeightOffset; z++){
                for(int x= -mapWidthOffset; x <= mapWidthOffset; x++){
                    Vector3f blockPosition = new Vector3f(x,0.0f,z);
                    // находим ближайший элемент
                    TerraElement closestElement = getClosestElement(elements,blockPosition);

                    Projection projection = new Projection();
                    Vector3f position = closestElement.getTerraEffect(blockPosition);
                    projection.setTranslation(position);

                    if(position.getY() >= 0) {
                        grassInstanceMatrix.add(projection.getModelMatrix());
                    }else{
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                    }

                    gridPositions.add(position);
                }
            }

            for(Vector3f gridPosition : gridPositions){
                for(int y = (int)gridPosition.getY() - 1; y >= -3; y--){
                    Projection projection = new Projection();
                    Vector3f position = new Vector3f(gridPosition.getX(),y,gridPosition.getZ());
                    projection.setTranslation(position);
                    dirtInstanceMatrix.add(projection.getModelMatrix());
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
            gridPositions.clear();
            int maxAmount = (int)Math.round(2.0f + Math.random() * 5.0f);
            int maxRadius = area / (int)Math.round(5 + Math.random() * 20);
            int maxHeight = 3;
            List<TerraElement> elements = new ArrayList<>(generateTerraElement(maxAmount,maxRadius,maxHeight));

            Block dirt = new MountainDirt((MountainDirt)mountain_dirt_main);
            Block snow = new Snow((Snow)snow_main);
            Block snowRock = new SnowRock((SnowRock)snow_rock_main);
            Block rock = new Rock((Rock)rock_main);

            List<Matrix4f>dirtInstanceMatrix = new ArrayList<>();
            List<Matrix4f>snowInstanceMatrix = new ArrayList<>();
            List<Matrix4f>snowRockInstanceMatrix = new ArrayList<>();
            List<Matrix4f>rockInstanceMatrix = new ArrayList<>();

            for(int z = -mapHeightOffset; z <= mapHeightOffset; z++){
                for(int x= -mapWidthOffset; x <= mapWidthOffset; x++){
                    Vector3f blockPosition = new Vector3f(x,0.0f,z);
                    // находим ближайший элемент
                    TerraElement closestElement = getClosestElement(elements,blockPosition);

                    Projection projection = new Projection();
                    Vector3f position = closestElement.getTerraEffect(blockPosition);
                    projection.setTranslation(position);

                    if(position.getY() >= -1) {
                        if(position.getY() >= 1){
                            snowRockInstanceMatrix.add(projection.getModelMatrix());
                        }else {
                            snowInstanceMatrix.add(projection.getModelMatrix());
                        }
                    }else{
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                    }

                    gridPositions.add(position);
                }
            }

            for(Vector3f gridPosition : gridPositions){
                for(int y = (int)gridPosition.getY() - 1; y >= -3; y--){
                    Projection projection = new Projection();
                    Vector3f position = new Vector3f(gridPosition.getX(),y,gridPosition.getZ());
                    projection.setTranslation(position);
                    if(y > -1){
                        rockInstanceMatrix.add(projection.getModelMatrix());
                    }else {
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                    }
                }
            }

            Matrix4f[] dirtMatrix = new Matrix4f[dirtInstanceMatrix.size()];
            for(int i=0; i<dirtMatrix.length; i++){
                dirtMatrix[i] = dirtInstanceMatrix.get(i);
            }

            Matrix4f[] snowMatrix = new Matrix4f[snowInstanceMatrix.size()];
            for(int i=0; i<snowMatrix.length; i++){
                snowMatrix[i] = snowInstanceMatrix.get(i);
            }

            Matrix4f[] snowRockMatrix = new Matrix4f[snowRockInstanceMatrix.size()];
            for(int i=0; i<snowRockMatrix.length; i++){
                snowRockMatrix[i] = snowRockInstanceMatrix.get(i);
            }

            Matrix4f[] rockMatrix = new Matrix4f[rockInstanceMatrix.size()];
            for(int i=0; i<rockMatrix.length; i++){
                rockMatrix[i] = rockInstanceMatrix.get(i);
            }

            dirt.setInstanceMatrix(dirtMatrix);
            snow.setInstanceMatrix(snowMatrix);
            snowRock.setInstanceMatrix(snowRockMatrix);
            rock.setInstanceMatrix(rockMatrix);
            blocks = new ArrayList<>(Arrays.asList(dirt,snow,snowRock,rock));
        }else if(seed == RIVER_MAP){

        }

        int mapX = mapWidthOffset * 2 + 1;
        int mapZ = mapHeightOffset * 2 + 1;
        return new RandomArena(blocks,mapX,mapZ);
    }

    private static void initBlocks(){
        if(dirt_main == null){
            dirt_main = new Dirt();
        }
        if(grass_main == null){
            grass_main = new Grass();
        }
        if(snow_main == null){
            snow_main = new Snow();
        }
        if(mountain_dirt_main == null){
            mountain_dirt_main = new MountainDirt();
        }
        if(snow_rock_main == null){
            snow_rock_main = new SnowRock();
        }
        if(rock_main == null){
            rock_main = new Rock();
        }
    }

    private static void generateMapSize(){
        mapWidthOffset = (int)(5.0f + Math.random() * 10.0f);
        mapHeightOffset = (int)(5.0f + Math.random() * 10.0f);
    }

    private static List<TerraElement> generateTerraElement(int maxAmount, int maxRadius, int maxHeight){

        List<TerraElement> terraElements = new ArrayList<>();
        for(int i=0; i<maxAmount; i++){
            int radius = (int)Math.round(1.0f + Math.random() * maxRadius);
            int height = 0;
            int coin = (int)Math.round(Math.random());
            switch (coin){
                case 0:
                    height = -(int)Math.ceil(Math.random() * maxHeight);
                    break;
                case 1:
                    height = (int)Math.ceil(Math.random() * maxHeight);
                    break;
            }
            terraElements.add(new TerraElement(getRandomPos(terraElements),radius,height));
        }

        return terraElements;
    }

    private static Vector3f getRandomPos(List<TerraElement> terraElements){
        int half_w = mapWidthOffset;
        int half_h = mapHeightOffset;
        float x = (float) Math.floor(-half_w + (float) Math.random() * (half_w * 2));
        float z = (float) Math.floor(-half_h + (float) Math.random() * (half_h * 2));
        Vector3f randomPos = new Vector3f(x, 0.0f, z);

        if(terraElements.size() != 0){
            for(TerraElement element : terraElements){
                if(randomPos.equals(element.getPosition())){
                    return getRandomPos(terraElements);
                }else{
                    return randomPos;
                }
            }
        }else{
            return randomPos;
        }

        return new Vector3f();
    }

    private static TerraElement getClosestElement(List<TerraElement> elements, Vector3f position){

        for(TerraElement element: elements){
            element.setDistance(element.getPosition().sub(position).length());
        }

        elements.sort(new Comparator<TerraElement>() {
            @Override
            public int compare(TerraElement o1, TerraElement o2) {
                return o1.getDistance() > o2.getDistance() ? 1 : -1;
            }
        });

        return elements.get(0);
    }
}
