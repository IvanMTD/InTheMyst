package ru.phoenix.game.logic.generator;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Perlin2D;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.*;
import ru.phoenix.game.content.block.type.stone.*;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.passive.Bush;
import ru.phoenix.game.content.object.passive.LittleThing;
import ru.phoenix.game.content.object.passive.Tree;
import ru.phoenix.game.content.object.passive.Weed;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.random.RandomArena;
import ru.phoenix.game.logic.generator.component.GridElement;
import ru.phoenix.game.logic.generator.component.TerraElement;
import ru.phoenix.game.logic.lighting.Light;

import java.util.*;

import static ru.phoenix.core.config.Constants.*;

public class GraundGenerator {

    private static List<GridElement> gridElements = new ArrayList<>();

    private static Block dirt_main              = null;
    private static Block grass_main             = null;
    private static Block snow_main              = null;
    private static Block mountain_dirt_main     = null;
    private static Block snow_rock_main         = null;
    private static Block rock_main              = null;
    private static Block grass_flower_main      = null;

    private static Block stone_small_main       = null;
    private static Block stone_small_dirt_main  = null;
    private static Block stone_small_snow_main  = null;
    private static Block stone_medium_main      = null;
    private static Block stone_medium_dirt_main = null;
    private static Block stone_medium_snow_main = null;
    private static Block stone_big_main         = null;
    private static Block stone_big_dirt_main    = null;
    private static Block stone_big_snow_main    = null;

    private static Object weed_main             = null;
    private static Object tree_main             = null;
    private static Object bush_main             = null;
    private static Object littleThing_main      = null;

    private static int mapWidthOffset;
    private static int mapHeightOffset;

    public static BattleGraund useMapGenerator(int seed){

        gridElements.clear();

        initBlocks();
        initSprite();
        generateMapSize();

        List<Block> blocks = new ArrayList<>();
        List<Object> sprites = new ArrayList<>();

        int height = 0;

        Block dirt = new Dirt((Dirt)dirt_main);
        Block grass = new Grass((Grass)grass_main);
        Block grassFlower = new GrassFlower((GrassFlower)grass_flower_main);
        Block coldDirt = new MountainDirt((MountainDirt)mountain_dirt_main);
        Block snow = new Snow((Snow)snow_main);
        Block rock = new Rock((Rock)rock_main);
        Block rockSnow = new SnowRock((SnowRock)snow_rock_main);

        Block smallStone = new SmallStone((SmallStone)stone_small_main);
        Block smallStoneDirt = new SmallStoneDirt((SmallStoneDirt)stone_small_dirt_main);
        Block smallStoneSnow = new SmallStoneSnow((SmallStoneSnow)stone_small_snow_main);
        Block mediumStone = new MediumStone((MediumStone)stone_medium_main);
        Block mediumStoneDirt = new MediumStoneDirt((MediumStoneDirt)stone_medium_dirt_main);
        Block mediumStoneSnow = new MediumStoneSnow((MediumStoneSnow)stone_medium_snow_main);
        Block bigStone = new BigStone((BigStone)stone_big_main);
        Block bigStoneDirt = new BigStoneDirt((BigStoneDirt)stone_big_dirt_main);
        Block bigStoneSnow = new BigStoneSnow((BigStoneSnow)stone_big_snow_main);

        if(seed == PLAIN_MAP){
            height = 1;
        }else if(seed == MOUNTAIN_MAP){
            height = 4;
        }

        long mapSeed = (long)(Math.random() * 10000000000L);
        List<Vector3f> finalPos = new ArrayList<>(createMap(mapSeed,height));

        List<Matrix4f>dirtInstanceList = new ArrayList<>();
        List<Matrix4f>grassInstanceList = new ArrayList<>();
        List<Matrix4f>grassFlowerInstanceList = new ArrayList<>();
        List<Matrix4f>coldDirtInstanceList = new ArrayList<>();
        List<Matrix4f>snowInstanceList = new ArrayList<>();
        List<Matrix4f>rockInstanceList = new ArrayList<>();
        List<Matrix4f>rockSnowInstanceList = new ArrayList<>();

        List<Matrix4f>smallStoneInstanceList = new ArrayList<>();
        List<Matrix4f>smallStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>smallStoneSnowInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneSnowInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneSnowInstanceList = new ArrayList<>();

        List<Matrix4f> weedInstanceList = new ArrayList<>();
        List<Matrix4f> treeInstanceList = new ArrayList<>();

        for(Vector3f position : finalPos){
            Projection projection = new Projection();
            float num = Math.abs(Math.round(position.getY()) - position.getY());
            boolean check = false;
            if(0.35f <= num && num <= 0.65f){
                position.setY((float)Math.floor(position.getY()) + 0.5f);
                check = true;
            }else{
                position.setY(Math.round(position.getY()));
            }
            if(checkMapBorder(position)){
                check = false;
            }
            projection.setTranslation(position);

            boolean bevel = false;

            if (seed == PLAIN_MAP) {
                if (position.getY() < 0) {
                    if(check){
                        bevel = tryGenerateTilt(position,finalPos,projection);
                    }
                    dirtInstanceList.add(projection.getModelMatrix());
                    gridElements.add(new GridElement(position, dirt, bevel,false));
                    if(!bevel && (Math.random() * 100.0f <= 1.0f)){
                        int coin = (int)Math.round(Math.random() * 3.0);
                        float angle = 0.0f;
                        switch (coin){
                            case 0:
                                angle = 0.0f;
                                break;
                            case 1:
                                angle = 90.0f;
                                break;
                            case 2:
                                angle = 180.0f;
                                break;
                            case 3:
                                angle = 270.0f;
                                break;
                        }
                        projection.setRotation(angle,new Vector3f(0.0f,1.0f,0.0f));
                        int random = (int)(Math.random() * 2.9f);
                        if(random == 0) {
                            smallStoneDirtInstanceList.add(projection.getModelMatrix());
                            for (GridElement grid : gridElements) {
                                if (grid.getPosition().equals(position)) {
                                    grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                }
                            }
                        }else if(random == 1){
                            mediumStoneDirtInstanceList.add(projection.getModelMatrix());
                            for (GridElement grid : gridElements) {
                                if (grid.getPosition().equals(position)) {
                                    grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                }
                            }
                        }else if(random == 2){
                            bigStoneDirtInstanceList.add(projection.getModelMatrix());
                            for (GridElement grid : gridElements) {
                                if (grid.getPosition().equals(position)) {
                                    grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                }
                            }
                        }
                    }
                } else {
                    float chance = 10.0f;
                    if (Math.random() * 100.0f <= chance) {
                        if(check){
                            bevel = tryGenerateTilt(position,finalPos,projection);
                        }
                        grassFlowerInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, grassFlower, bevel,false));
                        if(!bevel && (Math.random() * 100.0f <= 5.0f)){
                            int coin = (int)Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin){
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            projection.setRotation(angle,new Vector3f(0.0f,1.0f,0.0f));
                            int random = (int)(Math.random() * 2.9f);
                            if(random == 0) {
                                smallStoneInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                    }
                                }
                            }else if(random == 1){
                                mediumStoneInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                    }
                                }
                            }else if(random == 2){
                                bigStoneInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                    }
                                }
                            }
                        }
                    } else {
                        boolean tree = false;
                        if(check){
                            bevel = tryGenerateTilt(position,finalPos,projection);
                        }
                        if(!bevel) {
                            if (Math.random() * 100.0f <= Math.random() * 80.0f) {
                                Vector3f spritePos = new Vector3f(position.getX(), position.getY(), position.getZ());
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(spritePos);
                                //weedInstanceList.add(spriteProjection.getModelMatrix());
                                Object weed = new Weed((Weed) weed_main);
                                weed.init(null);
                                weed.setPosition(position);
                                sprites.add(weed);
                            }else if(Math.random() * 100.0f <= 1.0f){
                                Vector3f spritePos = new Vector3f(position.getX(), position.getY(), position.getZ());
                                Projection spriteProjection = new Projection();
                                spriteProjection.setTranslation(spritePos);
                                //weedInstanceList.add(spriteProjection.getModelMatrix());
                                Object littleThing = new LittleThing((LittleThing) littleThing_main);
                                littleThing.init(null);
                                littleThing.setPosition(position);
                                sprites.add(littleThing);
                            }else{
                                if((Math.random() * 100.0f <= 5.0f) && !checkMapBorder(position)){
                                    boolean test = true;
                                    for(GridElement element : gridElements) {
                                        if(element.isBlocked()){
                                            if(element.getPosition().sub(position).length() <= 2.0f){
                                                test = false;
                                            }
                                        }
                                    }
                                    if(test) {
                                        Vector3f spritePos = new Vector3f(position.getX(), position.getY(), position.getZ());
                                        Projection spriteProjection = new Projection();
                                        spriteProjection.setTranslation(spritePos);
                                        //treeInstanceList.add(spriteProjection.getModelMatrix());
                                        int coin = (int)Math.round(Math.random() * 1.0f);
                                        if(coin == 0) {
                                            Object wood = new Tree((Tree) tree_main,seed);
                                            wood.init(null);
                                            wood.setPosition(position);
                                            sprites.add(wood);
                                        }else{
                                            Object bush = new Bush((Bush) bush_main,seed);
                                            bush.init(null);
                                            bush.setPosition(position);
                                            sprites.add(bush);
                                        }
                                        tree = true;
                                    }
                                }
                            }
                        }
                        grassInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, grass, bevel, tree));
                    }
                }
            } else if (seed == MOUNTAIN_MAP) {
                boolean tree = false;
                if(check){
                    bevel = tryGenerateTilt(position,finalPos,projection);
                }
                if (position.getY() <= -2.0f) {
                    if((Math.random() * 100.0f <= 2.0f) && !checkMapBorder(position) && !bevel){
                        boolean test = true;
                        for(GridElement element : gridElements) {
                            if(element.isBlocked()){
                                if(element.getPosition().sub(position).length() <= 2.0f){
                                    test = false;
                                }
                            }
                        }
                        if(test) {
                            Vector3f spritePos = new Vector3f(position.getX(), position.getY(), position.getZ());
                            Projection spriteProjection = new Projection();
                            spriteProjection.setTranslation(spritePos);
                            //treeInstanceList.add(spriteProjection.getModelMatrix());
                            int coin = (int)Math.round(Math.random() * 1.0f);
                            if(coin == 0) {
                                Object wood = new Tree((Tree) tree_main,seed);
                                wood.init(null);
                                wood.setPosition(position);
                                sprites.add(wood);
                            }else{
                                Object bush = new Bush((Bush) bush_main,seed);
                                bush.init(null);
                                bush.setPosition(position);
                                sprites.add(bush);
                            }
                            tree = true;
                        }
                        coldDirtInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, coldDirt, false,tree));
                    }else if ((Math.random() * 100.0f <= Math.random() * 10.0f) && !bevel) {
                        Vector3f spritePos = new Vector3f(position.getX(), position.getY(), position.getZ());
                        Projection spriteProjection = new Projection();
                        spriteProjection.setTranslation(spritePos);
                        //weedInstanceList.add(spriteProjection.getModelMatrix());
                        Object weed = new Weed((Weed) weed_main,seed);
                        weed.init(null);
                        weed.setPosition(position);
                        sprites.add(weed);
                        coldDirtInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, coldDirt, false,false));
                    }else if(Math.random() * 100.0f <= 1.0f){
                        coldDirtInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, coldDirt, bevel,false));
                        if(!bevel) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            if (random == 0) {
                                smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                    }
                                }
                            } else if (random == 1) {
                                mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                    }
                                }
                            } else if (random == 2) {
                                bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                    }
                                }
                            }
                        }
                    }else{
                        coldDirtInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, coldDirt, bevel,false));
                    }
                } else if(-2.0f < position.getY() && position.getY() <= 0.0f){
                    if((Math.random() * 100.0f <= 2.0f) && !checkMapBorder(position) && !bevel){
                        boolean test = true;
                        for(GridElement element : gridElements) {
                            if(element.isBlocked()){
                                if(element.getPosition().sub(position).length() <= 2.0f){
                                    test = false;
                                }
                            }
                        }
                        if(test) {
                            Vector3f spritePos = new Vector3f(position.getX(), position.getY(), position.getZ());
                            Projection spriteProjection = new Projection();
                            spriteProjection.setTranslation(spritePos);
                            //treeInstanceList.add(spriteProjection.getModelMatrix());
                            int coin = (int)Math.round(Math.random() * 1.0f);
                            if(coin == 0) {
                                Object wood = new Tree((Tree) tree_main,seed);
                                wood.init(null);
                                wood.setPosition(position);
                                sprites.add(wood);
                            }else{
                                Object bush = new Bush((Bush) bush_main,seed);
                                bush.init(null);
                                bush.setPosition(position);
                                sprites.add(bush);
                            }
                            tree = true;
                        }
                        snowInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, snow, false,tree));
                    }else if(Math.random() * 100.0f <= 1.0f){
                        snowInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, snow, bevel,false));
                        if(!bevel) {
                            int coin = (int) Math.round(Math.random() * 3.0);
                            float angle = 0.0f;
                            switch (coin) {
                                case 0:
                                    angle = 0.0f;
                                    break;
                                case 1:
                                    angle = 90.0f;
                                    break;
                                case 2:
                                    angle = 180.0f;
                                    break;
                                case 3:
                                    angle = 270.0f;
                                    break;
                            }
                            projection.setRotation(angle, new Vector3f(0.0f, 1.0f, 0.0f));
                            int random = (int) (Math.random() * 2.9f);
                            if (random == 0) {
                                smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                    }
                                }
                            } else if (random == 1) {
                                mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                    }
                                }
                            } else if (random == 2) {
                                bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                    }
                                }
                            }
                        }
                    }else{
                        snowInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(position, snow, bevel,false));
                    }
                } else if(position.getY() > 0.0f){
                    rockSnowInstanceList.add(projection.getModelMatrix());
                    gridElements.add(new GridElement(position, rockSnow, bevel,false));
                    if(!bevel && Math.random() * 100.0f <= 1.0f){
                        int coin = (int)Math.round(Math.random() * 3.0);
                        float angle = 0.0f;
                        switch (coin){
                            case 0:
                                angle = 0.0f;
                                break;
                            case 1:
                                angle = 90.0f;
                                break;
                            case 2:
                                angle = 180.0f;
                                break;
                            case 3:
                                angle = 270.0f;
                                break;
                        }
                        projection.setRotation(angle,new Vector3f(0.0f,1.0f,0.0f));
                        int random = (int)(Math.random() * 2.9f);
                        if(random == 0) {
                            smallStoneSnowInstanceList.add(projection.getModelMatrix());
                            for (GridElement grid : gridElements) {
                                if (grid.getPosition().equals(position)) {
                                    grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                }
                            }
                        }else if(random == 1){
                            mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                            for (GridElement grid : gridElements) {
                                if (grid.getPosition().equals(position)) {
                                    grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                }
                            }
                        }else if(random == 2){
                            bigStoneSnowInstanceList.add(projection.getModelMatrix());
                            for (GridElement grid : gridElements) {
                                if (grid.getPosition().equals(position)) {
                                    grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int i=0; i<gridElements.size(); i++){
            if(gridElements.get(i).isBlocked()){
                for(int j=0; j<gridElements.size(); j++){
                    if(gridElements.get(j).isBevel()){
                        float distance = gridElements.get(i).getPosition().sub(gridElements.get(j).getPosition()).length();
                        if(distance <= 2.0f){
                            for(int k=0; k<sprites.size(); k++){
                                if(sprites.get(k).getPosition().equals(gridElements.get(i).getPosition())){
                                    sprites.remove(k);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int i=0; i<gridElements.size(); i++){
            for(int j=0; j<gridElements.size(); j++){
                if(gridElements.get(i).getPosition().getY() > gridElements.get(j).getPosition().getY()){
                    if(gridElements.get(j).isBlocked() && (gridElements.get(i).getPosition().sub(gridElements.get(j).getPosition()).length() <= 3.0f)){
                        for(int k=0; k<sprites.size();k++){
                            if(gridElements.get(j).getPosition().equals(sprites.get(k).getPosition())){
                                sprites.remove(k);
                                break;
                            }
                        }
                    }
                }
            }
        }

        for(GridElement grid : gridElements){
            for(float y = (int)Math.ceil(grid.getPosition().getY()) - 1.0f; y >= -4.0f; y--){
                Projection projection = new Projection();
                Vector3f position = new Vector3f(grid.getPosition().getX(), y, grid.getPosition().getZ());
                projection.setTranslation(position);
                if(grid.getBlock().getType() == BLOCK_DIRT_GRASS || grid.getBlock().getType() == BLOCK_DIRT || grid.getBlock().getType() == BLOCK_GRASS_FLOWER){
                    dirtInstanceList.add(projection.getModelMatrix());
                }else if(grid.getBlock().getType() == BLOCK_COLD_DIRT || grid.getBlock().getType() == BLOCK_DIRT_SNOW){
                    coldDirtInstanceList.add(projection.getModelMatrix());
                }else if(grid.getBlock().getType() == BLOCK_ROCK_SNOW){
                    rockInstanceList.add(projection.getModelMatrix());
                }
            }
        }

        dirt.setInstanceMatrix(getInstanceMatrix(dirtInstanceList));
        grass.setInstanceMatrix(getInstanceMatrix(grassInstanceList));
        grassFlower.setInstanceMatrix(getInstanceMatrix(grassFlowerInstanceList));
        coldDirt.setInstanceMatrix(getInstanceMatrix(coldDirtInstanceList));
        snow.setInstanceMatrix(getInstanceMatrix(snowInstanceList));
        rock.setInstanceMatrix(getInstanceMatrix(rockInstanceList));
        rockSnow.setInstanceMatrix(getInstanceMatrix(rockSnowInstanceList));

        smallStone.setInstanceMatrix(getInstanceMatrix(smallStoneInstanceList));
        smallStoneDirt.setInstanceMatrix(getInstanceMatrix(smallStoneDirtInstanceList));
        smallStoneSnow.setInstanceMatrix(getInstanceMatrix(smallStoneSnowInstanceList));
        mediumStone.setInstanceMatrix(getInstanceMatrix(mediumStoneInstanceList));
        mediumStoneDirt.setInstanceMatrix(getInstanceMatrix(mediumStoneDirtInstanceList));
        mediumStoneSnow.setInstanceMatrix(getInstanceMatrix(mediumStoneSnowInstanceList));
        bigStone.setInstanceMatrix(getInstanceMatrix(bigStoneInstanceList));
        bigStoneDirt.setInstanceMatrix(getInstanceMatrix(bigStoneDirtInstanceList));
        bigStoneSnow.setInstanceMatrix(getInstanceMatrix(bigStoneSnowInstanceList));

        weed_main.init(getInstanceMatrix(weedInstanceList));
        tree_main.init(getInstanceMatrix(treeInstanceList));

        blocks = new ArrayList<>(Arrays.asList(dirt,grass,grassFlower,coldDirt,snow,rock,rockSnow,smallStone,smallStoneDirt,mediumStone,mediumStoneDirt,bigStone,bigStoneDirt,smallStoneSnow,mediumStoneSnow,bigStoneSnow));
        //sprites = new ArrayList<>(Arrays.asList(tree_main,weed_main));

        int mapX = mapWidthOffset * 2 + 1;
        int mapZ = mapHeightOffset * 2 + 1;
        return new RandomArena(gridElements, blocks, sprites, mapX, mapZ);
    }

    public static BattleGraund generateMap(int seed){

        initBlocks();
        initSprite();
        generateMapSize();

        int area = (mapWidthOffset * 2) * (mapHeightOffset * 2);

        List<Block> blocks = new ArrayList<>();

        if(seed == PLAIN_MAP){
            gridElements.clear();
            int maxAmount = (int)Math.round(1.0f + Math.random() * 3.0f);
            int maxRadius = area / (int)Math.round(10.0f + Math.random() * 20.0f);
            int maxHeight = 1;
            List<TerraElement> elements = new ArrayList<>(generateTerraElement(maxAmount,maxRadius,maxHeight));

            Block dirt = new Dirt((Dirt)dirt_main);
            Block grass = new Grass((Grass)grass_main);
            Block grassFlower = new GrassFlower((GrassFlower) grass_flower_main);
            List<Matrix4f>dirtInstanceMatrix = new ArrayList<>();
            List<Matrix4f>grassInstanceMatrix = new ArrayList<>();
            List<Matrix4f>grassFlowerInstanceMatrix = new ArrayList<>();

            for(int z = -mapHeightOffset; z <= mapHeightOffset; z++){
                for(int x= -mapWidthOffset; x <= mapWidthOffset; x++){
                    Vector3f blockPosition = new Vector3f(x,0.0f,z);
                    // находим ближайший элемент
                    TerraElement closestElement = getClosestElement(elements,blockPosition);

                    Projection projection = new Projection();
                    Vector3f position = closestElement.getTerraEffect(blockPosition);
                    checkMapBorder(position);
                    projection.setTranslation(position);

                    //generateTilt(projection,position,closestElement);

                    int check = 0;
                    float chance = 1.0f + (float)Math.random() * 30.0f;
                    if(position.getY() >= 0) {
                        if(Math.random() * 100.0f <= chance){
                            check = 2;
                            float angle = 0.0f;
                            int random = (int)(Math.random() * 4.0f);
                            switch (random){
                                case 0:
                                    angle = 90.0f;
                                    break;
                                case 1:
                                    angle = 180.0f;
                                    break;
                                case 2:
                                    angle = 270.0f;
                                    break;
                                default:
                                    angle = 0.0f;
                            }
                            projection.setRotation(angle,new Vector3f(0.0f,1.0f,0.0f));
                            grassFlowerInstanceMatrix.add(projection.getModelMatrix());
                        }else {
                            check = 1;
                            grassInstanceMatrix.add(projection.getModelMatrix());
                        }
                    }else{
                        check = 0;
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                    }

                    if(check == 0){
                        gridElements.add(new GridElement(position,dirt,false,false));
                    }else if(check == 1){
                        gridElements.add(new GridElement(position,grass,false,false));
                    }else if(check == 2){
                        gridElements.add(new GridElement(position,grassFlower,false,false));
                    }
                }
            }

            for(GridElement grid : gridElements){
                for(float y = grid.getPosition().getY() - 1.0f; y >= -3.0f; y--){
                    Projection projection = new Projection();
                    Vector3f position = new Vector3f(grid.getPosition().getX(),y,grid.getPosition().getZ());
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

            Matrix4f[] grassFlowerMatrix = new Matrix4f[grassFlowerInstanceMatrix.size()];
            for(int i=0; i<grassFlowerMatrix.length; i++){
                grassFlowerMatrix[i] = grassFlowerInstanceMatrix.get(i);
            }

            dirt.setInstanceMatrix(dirtMatrix);
            grass.setInstanceMatrix(grassMatrix);
            grassFlower.setInstanceMatrix(grassFlowerMatrix);
            blocks = new ArrayList<>(Arrays.asList(dirt,grass,grassFlower));
        }else if(seed == MOUNTAIN_MAP){
            gridElements.clear();
            int maxAmount = (int)Math.round(4.0f + Math.random() * 6.0f);
            int maxRadius = area / (int)Math.round(10.0f + Math.random() * 50.0f);
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
                    checkMapBorder(position);
                    projection.setTranslation(position);

                    //generateTilt(projection,position,closestElement);

                    int check = 0;
                    if(position.getY() >= -2) {
                        if(position.getY() >= -1){
                            snowRockInstanceMatrix.add(projection.getModelMatrix());
                            check = 1;
                        }else {
                            snowInstanceMatrix.add(projection.getModelMatrix());
                            check = 2;
                        }
                    }else{
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                        check = 0;
                    }

                    if(check == 0){
                        gridElements.add(new GridElement(position,dirt,false,false));
                    }else if(check == 1){
                        gridElements.add(new GridElement(position,snowRock,false,false));
                    }else if(check == 2){
                        gridElements.add(new GridElement(position,snow,false,false));
                    }
                }
            }

            for(GridElement grid : gridElements){
                for(float y = grid.getPosition().getY() - 1.0f; y >= -3.0f; y--){
                    Projection projection = new Projection();
                    Vector3f position = new Vector3f(grid.getPosition().getX(), y, grid.getPosition().getZ());
                    projection.setTranslation(position);
                    if(grid.getBlock().getType() == BLOCK_COLD_DIRT || grid.getBlock().getType() == BLOCK_DIRT_SNOW){
                        dirtInstanceMatrix.add(projection.getModelMatrix());
                    }else if(grid.getBlock().getType() == BLOCK_ROCK_SNOW){
                        rockInstanceMatrix.add(projection.getModelMatrix());
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
        weed_main.init(null);
        List<Object> sprites = new ArrayList<>(Arrays.asList(weed_main));
        return new RandomArena(gridElements, blocks, sprites, mapX, mapZ);
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
        if(grass_flower_main == null){
            grass_flower_main = new GrassFlower();
        }

        if(stone_small_main == null){
            stone_small_main = new SmallStone();
        }
        if(stone_small_dirt_main == null){
            stone_small_dirt_main = new SmallStoneDirt();
        }
        if(stone_medium_main == null){
            stone_medium_main = new MediumStone();
        }
        if(stone_medium_dirt_main == null){
            stone_medium_dirt_main = new MediumStoneDirt();
        }
        if(stone_big_main == null){
            stone_big_main = new BigStone();
        }
        if(stone_big_dirt_main == null){
            stone_big_dirt_main = new BigStoneDirt();
        }
        if(stone_small_snow_main == null){
            stone_small_snow_main = new SmallStoneSnow();
        }
        if(stone_medium_snow_main == null){
            stone_medium_snow_main = new MediumStoneSnow();
        }
        if(stone_big_snow_main == null){
            stone_big_snow_main = new BigStoneSnow();
        }
    }

    private static void initSprite(){
        if(weed_main == null){
            weed_main = new Weed();
        }
        if(tree_main == null){
            tree_main = new Tree();
        }
        if(bush_main == null){
            bush_main = new Bush();
        }
        if(littleThing_main == null){
            littleThing_main = new LittleThing();
        }
    }

    private static void generateMapSize(){
        mapWidthOffset = (int)(10.0f + Math.random() * 50.0f);
        mapHeightOffset = (int)(10.0f + Math.random() * 50.0f);
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
                }else if(element.getPosition().getX() - 5.0f <= randomPos.getX() && randomPos.getX() <= element.getPosition().getX() + 5.0f) {
                    if(element.getPosition().getZ() - 5.0f <= randomPos.getZ() && randomPos.getZ() <= element.getPosition().getZ() + 5.0f){
                        return getRandomPos(terraElements);
                    }
                } else {
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

    private static void generateTilt(Projection projection, Vector3f position, TerraElement closestElement){
        int chacne = 50;
        if(Math.random() * 100.0f <= chacne) {
            if (Math.abs(position.getY() - Math.round(position.getY())) == 0.5f) {
                if(position.getX() < closestElement.getPosition().getX()){ // объекст с лева
                    projection.setScaling(new Vector3f(1.41f, 1.41f, 1.0f));
                    if(closestElement.getHeight() < 0) {
                        projection.setRotation(90.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }else if(closestElement.getHeight() > 0){
                        projection.setRotation(270.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }else if(position.getX() > closestElement.getPosition().getX()){ // объект справа
                    projection.setScaling(new Vector3f(1.41f, 1.41f, 1.0f));
                    if(closestElement.getHeight() < 0) {
                        projection.setRotation(270.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }else if(closestElement.getHeight() > 0){
                        projection.setRotation(90.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }else if(position.getZ() < closestElement.getPosition().getZ()){
                    projection.setScaling(new Vector3f(1.0f, 1.41f, 1.41f));
                    if(closestElement.getHeight() < 0) {
                        projection.setRotation(0.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }else if(closestElement.getHeight() > 0){
                        projection.setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }else if(position.getZ() > closestElement.getPosition().getZ()){
                    projection.setScaling(new Vector3f(1.0f, 1.41f, 1.41f));
                    if(closestElement.getHeight() < 0) {
                        projection.setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    }else if(closestElement.getHeight() > 0){
                        projection.setRotation(0.0f,new Vector3f(0.0f,1.0f,0.0f));
                    }
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
            }
        }
    }

    private static boolean checkMapBorder(Vector3f blockPosition){
        boolean check = false;
        int mx = mapWidthOffset;
        int mz = mapHeightOffset;
        int x = (int)blockPosition.getX();
        int z = (int)blockPosition.getZ();
        if(x == 0 || x == mx * 2 || z == 0 || z == mz * 2){
            check = true;
            float y = blockPosition.getY();
            if(Math.abs(Math.round(y) - y) == 0.5f){
                blockPosition.setY(blockPosition.getY() - 0.5f);
            }
        }
        return check;
    }

    private static Matrix4f[] getInstanceMatrix(List<Matrix4f> matrixList){
        Matrix4f[] matrix = new Matrix4f[matrixList.size()];
        for(int i=0; i<matrix.length; i++){
            matrix[i] = matrixList.get(i);
        }
        return matrix;
    }

    private static List<Vector3f> createMap(long seed, int height){
        List<Vector3f> map = new ArrayList<>();
        Perlin2D perlin = new Perlin2D(seed);

        for(int x = 0; x <= mapWidthOffset * 2; x++) {
            for(int z = 0; z <= mapHeightOffset * 2; z++) {
                float value = perlin.getNoise(x/50f,z/50f,8,0.5f);
                int n = (int)(value * 255 + 128) & 255;
                float result = ((float)n / 255.0f);
                float y = (result + result - 1.0f) * (float)height * 2;
                if(y > height){
                    y = height;
                }else if(y < -4.0f){
                    y = -4.0f;
                }
                map.add(new Vector3f(x,y,z));
            }
        }

        return map;
    }

    private static Map<Integer,List<Vector3f>> createMap(int height, float mainOffset, float amplitude, int option){
        // map
        Map<Integer,List<Vector3f>> map = new HashMap<>();

        boolean firstStart = true;
        int index = 0;

        if(option == 0) {
            for (int z = 0; z <= mapHeightOffset * 2; z++) {
                List<Vector3f> positions = new ArrayList<>();
                for (int x = 0; x <= mapWidthOffset * 2; x++) {
                    if (firstStart) {
                        float y = (float) (-height + Math.random() * (height * 2));
                        positions.add(new Vector3f(x, y, z));
                        firstStart = false;
                    } else {
                        if (map.size() != 0) {
                            List<Vector3f> gridInfo = map.get(index - 1);
                            for (Vector3f grid : gridInfo) {
                                if (grid.getX() == x) {
                                    float y = grid.getY();
                                    if (positions.size() != 0) {
                                        float py = positions.get(positions.size() - 1).getY();
                                        float num = Math.abs(py - y) / 2;
                                        float offset = ((float) (-num + Math.random() * (num * 2.0f))) * amplitude;
                                        if(offset > 1.0f){
                                            offset = 1.0f;
                                        }
                                        float newY = (y < py ? y + num + offset : py + num + offset);
                                        if (newY < -height) {
                                            newY = -height;
                                        } else if (newY > height) {
                                            newY = height;
                                        }
                                        positions.add(new Vector3f(x, newY, z));
                                    } else {
                                        float offset = ((float) (-mainOffset + Math.random() * (mainOffset * 2))) * amplitude;
                                        if(offset > 1.0f){
                                            offset = 1.0f;
                                        }
                                        float newY = y + offset;
                                        if (newY < -height) {
                                            newY = -height;
                                        } else if (newY > height) {
                                            newY = height;
                                        }
                                        positions.add(new Vector3f(x, newY, z));
                                    }
                                }
                            }
                        } else {
                            float y = positions.get(positions.size() - 1).getY();
                            float offset = ((float) (-mainOffset + Math.random() * (mainOffset * 2))) * amplitude;
                            if(offset > 1.0f){
                                offset = 1.0f;
                            }
                            float newY = y + offset;
                            if (newY < -height) {
                                newY = -height;
                            } else if (newY > height) {
                                newY = height;
                            }
                            positions.add(new Vector3f(x, newY, z));
                        }
                    }
                }
                map.put(index, positions);
                index++;
            }
        }else if(option == 1){
            for (int z = 0; z <= mapHeightOffset * 2; z++) {
                List<Vector3f> positions = new ArrayList<>();
                for (int x = mapWidthOffset * 2; x >= 0; x--) {
                    if (firstStart) {
                        float y = (float) (-height + Math.random() * (height * 2));
                        positions.add(new Vector3f(x, y, z));
                        firstStart = false;
                    } else {
                        if (map.size() != 0) {
                            List<Vector3f> gridInfo = map.get(index - 1);
                            for (Vector3f grid : gridInfo) {
                                if (grid.getX() == x) {
                                    float y = grid.getY();
                                    if (positions.size() != 0) {
                                        float py = positions.get(positions.size() - 1).getY();
                                        float num = Math.abs(py - y) / 2;
                                        float offset = ((float) (-num + Math.random() * (num * 2.0f))) * amplitude;
                                        if(offset > 1.0f){
                                            offset = 1.0f;
                                        }
                                        float newY = (y < py ? y + num + offset : py + num + offset) * amplitude;
                                        if (newY < -height) {
                                            newY = -height;
                                        } else if (newY > height) {
                                            newY = height;
                                        }
                                        positions.add(new Vector3f(x, newY, z));
                                    } else {
                                        float offset = ((float) (-mainOffset + Math.random() * (mainOffset * 2))) * amplitude;
                                        if(offset > 1.0f){
                                            offset = 1.0f;
                                        }
                                        float newY = y + offset * amplitude;
                                        if (newY < -height) {
                                            newY = -height;
                                        } else if (newY > height) {
                                            newY = height;
                                        }
                                        positions.add(new Vector3f(x, newY, z));
                                    }
                                }
                            }
                        } else {
                            float y = positions.get(positions.size() - 1).getY();
                            float offset = ((float) (-mainOffset + Math.random() * (mainOffset * 2))) * amplitude;
                            if(offset > 1.0f){
                                offset = 1.0f;
                            }
                            float newY = y + offset * amplitude;
                            if (newY < -height) {
                                newY = -height;
                            } else if (newY > height) {
                                newY = height;
                            }
                            positions.add(new Vector3f(x, newY, z));
                        }
                    }
                }
                map.put(index, positions);
                index++;
            }
        }
        return map;
    }

    private static boolean tryGenerateTilt(Vector3f position, List<Vector3f> positions, Projection projection){
        
        float trans_max = 1.42f;
        float trans_min = 1.001f;

        boolean bevel = false;

        Vector3f leftPos = new Vector3f(position.getX() - 1.0f, position.getY(), position.getZ());
        Vector3f rightPos = new Vector3f(position.getX() + 1.0f, position.getY(), position.getZ());
        Vector3f upPos = new Vector3f(position.getX(),position.getY(), position.getZ() + 1.0f);
        Vector3f downPos = new Vector3f(position.getX(), position.getY(), position.getZ() - 1.0f);
        List<Vector3f>lowPos = new ArrayList<>();
        List<Vector3f>highPos = new ArrayList<>();

        int smooth = 0;
        int low = 0;
        int high = 0;

        for(Vector3f somePos : positions){
            float num = Math.abs(Math.round(somePos.getY()) - somePos.getY());
            if(0.45 <= num && num <= 0.55) {
                somePos.setY((float) Math.floor(somePos.getY()) + 0.5f);
            }else{
                somePos.setY(Math.round(somePos.getY()));
            }

            if(leftPos.equals(somePos)){
                if(somePos.getY() == position.getY() + 0.5f){ // high
                    high++;
                    highPos.add(somePos);
                }
                if(somePos.getY() == position.getY() - 0.5f){ // low
                    low++;
                    lowPos.add(somePos);
                }
                if(somePos.getY() == position.getY()){ // smooth
                    smooth++;
                }
            }
            if(rightPos.equals(somePos)){
                if(somePos.getY() == position.getY() + 0.5f){ // high
                    high++;
                    highPos.add(somePos);
                }
                if(somePos.getY() == position.getY() - 0.5f){ // low
                    low++;
                    lowPos.add(somePos);
                }
                if(somePos.getY() == position.getY()){ // smooth
                    smooth++;
                }
            }
            if(upPos.equals(somePos)){
                if(somePos.getY() == position.getY() + 0.5f){ // high
                    high++;
                    highPos.add(somePos);
                }
                if(somePos.getY() == position.getY() - 0.5f){ // low
                    low++;
                    lowPos.add(somePos);
                }
                if(somePos.getY() == position.getY()){ // smooth
                    smooth++;
                }
            }
            if(downPos.equals(somePos)){
                if(somePos.getY() == position.getY() + 0.5f){ // high
                    high++;
                    highPos.add(somePos);
                }
                if(somePos.getY() == position.getY() - 0.5f){ // low
                    low++;
                    lowPos.add(somePos);
                }
                if(somePos.getY() == position.getY()){ // smooth
                    smooth++;
                }
            }
        }

        if(high == 3 && low == 1){
            Vector3f mainPos = new Vector3f(position.getX(),0.0f,position.getZ());
            Vector3f somePos = new Vector3f(lowPos.get(0).getX(),0.0f,lowPos.get(0).getZ());
            Vector3f direction = new Vector3f(mainPos.sub(somePos));

            if(direction.getX() == -1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                projection.setRotation(90.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            if(direction.getX() == 1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                projection.setRotation(270.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            if(direction.getZ() == -1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                projection.setRotation(0.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            if(direction.getZ() == 1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                projection.setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            bevel = true;
        }

        if((high == 2 && low == 1 && smooth == 1) || (high == 1 && low == 1 && smooth == 2)){
            Vector3f mainPos = new Vector3f(position.getX(),0.0f,position.getZ());
            Vector3f somePos = new Vector3f(lowPos.get(0).getX(),0.0f,lowPos.get(0).getZ());
            Vector3f direction = new Vector3f(mainPos.sub(somePos));

            int check = 0;
            for(Vector3f h : highPos){
                Vector3f test = mainPos.sub(h);
                if((test.getX() == 1 && direction.getX() == -1) || (test.getX() == -1 && direction.getX() == 1)){
                    check++;
                }
                if((test.getZ() == 1 && direction.getZ() == -1) || (test.getZ() == -1 && direction.getZ() == 1)){
                    check++;
                }
            }

            if(check > 0) {
                if (direction.getX() == -1) {
                    projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                    projection.setRotation(90.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                if (direction.getX() == 1) {
                    projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                    projection.setRotation(270.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                if (direction.getZ() == -1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    projection.setRotation(0.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                if (direction.getZ() == 1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    projection.setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
            }
            bevel = true;
        }

        if(high == 2 && low == 2){

            int coin = (int)Math.round(Math.random());

            Vector3f mainPos = new Vector3f(position.getX(),0.0f,position.getZ());
            Vector3f somePos = new Vector3f(lowPos.get(coin).getX(),0.0f,lowPos.get(coin).getZ());
            Vector3f direction = new Vector3f(mainPos.sub(somePos));

            int check = 0;
            for(Vector3f h : highPos){
                Vector3f test = mainPos.sub(h);
                if((test.getX() == 1 && direction.getX() == -1) || (test.getX() == -1 && direction.getX() == 1)){
                    check++;
                }
                if((test.getZ() == 1 && direction.getZ() == -1) || (test.getZ() == -1 && direction.getZ() == 1)){
                    check++;
                }
            }

            if(check > 0) {
                if (direction.getX() == -1) {
                    projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                    projection.setRotation(90.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                if (direction.getX() == 1) {
                    projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                    projection.setRotation(270.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                if (direction.getZ() == -1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    projection.setRotation(0.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                if (direction.getZ() == 1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    projection.setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                }
            }
            bevel = true;
        }

        if(high == 1 && low == 3){
            Vector3f mainPos = new Vector3f(position.getX(),0.0f,position.getZ());
            Vector3f somePos = new Vector3f(highPos.get(0).getX(),0.0f,highPos.get(0).getZ());
            Vector3f direction = new Vector3f(mainPos.sub(somePos));

            if(direction.getX() == 1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                projection.setRotation(90.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            if(direction.getX() == -1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                projection.setRotation(270.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            if(direction.getZ() == 1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                projection.setRotation(0.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            if(direction.getZ() == -1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                projection.setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(45.0f, new Vector3f(1.0f, 0.0f, 0.0f));
            }
            bevel = true;
        }

        return bevel;
    }
}
