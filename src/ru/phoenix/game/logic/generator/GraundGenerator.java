package ru.phoenix.game.logic.generator;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
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
import ru.phoenix.game.content.object.water.WaterFlower;
import ru.phoenix.game.content.object.water.WaterLine;
import ru.phoenix.game.content.stage.BattleGraund;
import ru.phoenix.game.content.stage.random.RandomArena;
import ru.phoenix.game.logic.element.GridElement;

import java.util.*;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class GraundGenerator {

    private static List<GridElement> gridElements = new ArrayList<>();

    private static Texture grayZona             = null;
    private static Texture redZona              = null;
    private static Texture greenZona            = null;
    private static Texture goldZona             = null;
    private static Texture blueZona             = null;

    private static Block dirt_main              = null;
    private static Block grass_main             = null;
    private static Block snow_main              = null;
    private static Block mountain_dirt_main     = null;
    private static Block snow_rock_main         = null;
    private static Block rock_main              = null;
    private static Block grass_flower_main      = null;
    private static Block grass_bevel_main       = null;

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
    private static Object waterFlower_main      = null;

    private static Object water_line_main       = null;

    private static int mapWidthOffset;
    private static int mapHeightOffset;

    private static float angleRotation = 0.0f;
    private static int index = 0;

    public static BattleGraund useMapGenerator(int seed){

        float gridId = 0.1f;

        gridElements.clear();

        initTexture();
        initBlocks();
        initSprite();
        generateMapSize();

        List<Block> blocks = new ArrayList<>();
        List<Object> sprites = new ArrayList<>();
        List<Object> water;

        int height = 0;

        Block dirt = new Dirt((Dirt)dirt_main);
        Block grass = new Grass((Grass)grass_main);
        Block grassFlower = new GrassFlower((GrassFlower)grass_flower_main);
        Block coldDirt = new MountainDirt((MountainDirt)mountain_dirt_main);
        Block snow = new Snow((Snow)snow_main);
        Block rock = new Rock((Rock)rock_main);
        Block rockSnow = new SnowRock((SnowRock)snow_rock_main);
        Block bevelGrass = new GrassBevel((GrassBevel)grass_bevel_main);

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
            height = 2;
        }else if(seed == MOUNTAIN_MAP){
            height = 5;
        }

        long mapSeed = (long)(1 + Math.random() * 10000000000L);
        List<Vector3f> finalPos = new ArrayList<>(createMap(mapSeed,height));

        List<Matrix4f>dirtInstanceList = new ArrayList<>();
        List<Matrix4f>grassInstanceList = new ArrayList<>();
        List<Matrix4f>grassFlowerInstanceList = new ArrayList<>();
        List<Matrix4f>coldDirtInstanceList = new ArrayList<>();
        List<Matrix4f>snowInstanceList = new ArrayList<>();
        List<Matrix4f>rockInstanceList = new ArrayList<>();
        List<Matrix4f>rockSnowInstanceList = new ArrayList<>();
        List<Matrix4f>bevelGrassInstanceList = new ArrayList<>();

        List<Matrix4f>smallStoneInstanceList = new ArrayList<>();
        List<Matrix4f>smallStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>smallStoneSnowInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>mediumStoneSnowInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneDirtInstanceList = new ArrayList<>();
        List<Matrix4f>bigStoneSnowInstanceList = new ArrayList<>();

        List<Matrix4f> weedInstanceList1 = new ArrayList<>();
        List<Matrix4f> weedInstanceList2 = new ArrayList<>();
        List<Matrix4f> weedInstanceList3 = new ArrayList<>();
        List<Matrix4f> weedInstanceList4 = new ArrayList<>();
        List<Matrix4f> weedInstanceList5 = new ArrayList<>();
        List<Matrix4f> weedInstanceList6 = new ArrayList<>();
        List<Matrix4f> treeInstanceList = new ArrayList<>();

        List<Matrix4f> waterLineInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineLeftInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineRightInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineUpInstanceList = new ArrayList<>();
        List<Matrix4f> waterLineDownInstanceList = new ArrayList<>();

        for(Vector3f position : finalPos){
            gridId += 0.0001f;
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
                boolean weed = false;
                if (position.getY() < 0) {
                    if(check){
                        bevel = tryGenerateTilt(position,finalPos,projection);
                    }
                    if((0 > position.getY() && position.getY() > -1) && bevel){
                        bevelGrassInstanceList.add(projection.getModelMatrix());
                    }else{
                        dirtInstanceList.add(projection.getModelMatrix());
                    }

                    gridElements.add(new GridElement(gridId,position, dirt, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));

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
                        switch (random) {
                            case 0:
                                smallStoneDirtInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                    }
                                }
                                break;
                            case 1:
                                mediumStoneDirtInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                    }
                                }
                                break;
                            case 2:
                                bigStoneDirtInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                    }
                                }
                                break;
                        }
                    }else if(!bevel && (Math.random() * 100.0f <= 10.0f) && (position.getY() <= -1.0f)){
                        Projection weedProjection = new Projection();
                        weedProjection.setTranslation(position);
                        weedInstanceList1.add(weedProjection.getModelMatrix());
                        weed = true;
                    }

                    if(position.getY() < 0.0f) {
                        Vector3f newPos = new Vector3f(position.getX(), -0.75f, position.getZ());
                        Projection newProjection = new Projection();
                        newProjection.setTranslation(newPos);
                        waterLineInstanceList.add(newProjection.getModelMatrix());
                        for(GridElement grid : gridElements){
                            if(grid.getPosition().equals(position)){
                                if(position.getY() < -0.5f) {
                                    grid.setWater(true);
                                }
                                break;
                            }
                        }

                        if(position.getX() == 0.0f){ // left
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f(position.getX() - 0.49f, someY, position.getZ());
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(-90.0f, new Vector3f(0.0f, 0.0f, 1.0f));
                                waterLineLeftInstanceList.add(rp.getModelMatrix());
                            }
                        }
                        if(position.getX() == mapWidthOffset * 2){ // right
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f(position.getX() + 0.49f, someY, position.getZ());
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(90.0f, new Vector3f(0.0f, 0.0f, 1.0f));
                                waterLineRightInstanceList.add(rp.getModelMatrix());
                            }
                        }
                        if(position.getZ() == 0.0f){ // down
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f(position.getX(), someY, position.getZ() - 0.49f);
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(-90.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                                waterLineDownInstanceList.add(rp.getModelMatrix());
                            }
                        }
                        if(position.getZ() == mapHeightOffset * 2){ // up
                            for(float someY = -1.25f; someY >= -4.25f; someY -= 1.0f) {
                                Vector3f p = new Vector3f(position.getX(), someY, position.getZ() + 0.49f);
                                Projection rp = new Projection();
                                rp.setTranslation(p);
                                rp.setRotation(90.0f, new Vector3f(1.0f, 0.0f, 0.0f));
                                waterLineUpInstanceList.add(rp.getModelMatrix());
                            }
                        }

                        Vector3f leftPos = new Vector3f(position.getX() - 1.0f, 0.0f, position.getZ());
                        Vector3f rightPos = new Vector3f(position.getX() + 1.0f, 0.0f, position.getZ());
                        Vector3f upPos = new Vector3f(position.getX(), 0.0f, position.getZ() + 1);
                        Vector3f downPos = new Vector3f(position.getX(), 0.0f, position.getZ() - 1);

                        int checkInfo = 0;

                        for(GridElement grid : gridElements){
                            if(grid.getPosition().equals(leftPos)){
                                if(grid.getCurrentHeight() >= 0){
                                    checkInfo++;
                                }
                            }else if(grid.getPosition().equals(rightPos)){
                                if(grid.getCurrentHeight() >= 0){
                                    checkInfo++;
                                }
                            }else if(grid.getPosition().equals(upPos)){
                                if(grid.getCurrentHeight() >= 0){
                                    checkInfo++;
                                }
                            }else if(grid.getPosition().equals(downPos)){
                                if(grid.getCurrentHeight() >= 0){
                                    checkInfo++;
                                }
                            }
                        }

                        if(!weed && !bevel && (checkInfo > 0) && position.getY() != -0.5f){
                            if(Math.random() * 100 <= 30.0f) {
                                Object waterFlower = new WaterFlower((WaterFlower) waterFlower_main);
                                waterFlower.init(null);
                                waterFlower.setPosition(new Vector3f(position.getX(), -0.7499f, position.getZ()));
                                sprites.add(waterFlower);
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
                        gridElements.add(new GridElement(gridId,position, grassFlower, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
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
                            switch (random) {
                                case 0:
                                    smallStoneInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                        }
                                    }
                                    break;
                                case 1:
                                    mediumStoneInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                        }
                                    }
                                    break;
                                case 2:
                                    bigStoneInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                        }
                                    }
                                    break;
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
                                if(index > 4){
                                    index = 0;
                                }
                                /*Object weed = new Weed((Weed) weed_main,seed);
                                weed.init(null);
                                weed.setPosition(position);
                                sprites.add(weed);*/
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
                        gridElements.add(new GridElement(gridId,position, grass, bevel,angleRotation, tree,grayZona,redZona,greenZona,goldZona,blueZona));
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
                        gridElements.add(new GridElement(gridId,position, coldDirt, false,angleRotation,tree,grayZona,redZona,greenZona,goldZona,blueZona));
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
                        gridElements.add(new GridElement(gridId,position, coldDirt, false,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
                    }else if(Math.random() * 100.0f <= 1.0f){
                        coldDirtInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(gridId,position, coldDirt, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
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
                            switch (random) {
                                case 0:
                                    smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                        }
                                    }
                                    break;
                                case 1:
                                    mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                        }
                                    }
                                    break;
                                case 2:
                                    bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                        }
                                    }
                                    break;
                            }
                        }
                    }else{
                        coldDirtInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(gridId,position, coldDirt, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
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
                        gridElements.add(new GridElement(gridId,position, snow, false,angleRotation,tree,grayZona,redZona,greenZona,goldZona,blueZona));
                    }else if(Math.random() * 100.0f <= 1.0f){
                        snowInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(gridId,position, snow, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
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
                            switch (random) {
                                case 0:
                                    smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                        }
                                    }
                                    break;
                                case 1:
                                    mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                        }
                                    }
                                    break;
                                case 2:
                                    bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                    for (GridElement grid : gridElements) {
                                        if (grid.getPosition().equals(position)) {
                                            grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                        }
                                    }
                                    break;
                            }
                        }
                    }else{
                        snowInstanceList.add(projection.getModelMatrix());
                        gridElements.add(new GridElement(gridId,position, snow, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
                    }
                } else if(position.getY() > 0.0f){
                    rockSnowInstanceList.add(projection.getModelMatrix());
                    gridElements.add(new GridElement(gridId,position, rockSnow, bevel,angleRotation,false,grayZona,redZona,greenZona,goldZona,blueZona));
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
                        switch (random) {
                            case 0:
                                smallStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 0.5f);
                                    }
                                }
                                break;
                            case 1:
                                mediumStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.0f);
                                    }
                                }
                                break;
                            case 2:
                                bigStoneSnowInstanceList.add(projection.getModelMatrix());
                                for (GridElement grid : gridElements) {
                                    if (grid.getPosition().equals(position)) {
                                        grid.setCurrentHeight(grid.getCurrentHeight() + 1.5f);
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }

        for(int i=0; i<gridElements.size(); i++){
            if(gridElements.get(i).isBlocked()){
                for (GridElement gridElement : gridElements) {
                    if (gridElement.isBevel()) {
                        float distance = gridElements.get(i).getPosition().sub(gridElement.getPosition()).length();
                        if (distance <= 2.0f) {
                            for (int k = 0; k < sprites.size(); k++) {
                                if (sprites.get(k).getPosition().equals(gridElements.get(i).getPosition())) {
                                    gridElements.get(i).setBlocked(false);
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
            for (GridElement gridElement : gridElements) {
                if (gridElements.get(i).getPosition().getY() > gridElement.getPosition().getY()) {
                    if (gridElement.isBlocked() && (gridElements.get(i).getPosition().sub(gridElement.getPosition()).length() <= 3.0f)) {
                        for (int k = 0; k < sprites.size(); k++) {
                            if (gridElement.getPosition().equals(sprites.get(k).getPosition())) {
                                gridElement.setBlocked(false);
                                sprites.remove(k);
                                break;
                            }
                        }
                    }
                }
            }
        }

        for(GridElement grid : gridElements){
            float h = checkBlockBorder(grid);
            for(float y = (int)Math.ceil(grid.getPosition().getY()) - 1.0f; y >= -4.0f; y--){
                if(checkMapBorder(grid.getPosition()) || h != 0) {
                    h--;
                    Projection projection = new Projection();
                    Vector3f position = new Vector3f(grid.getPosition().getX(), y, grid.getPosition().getZ());
                    projection.setTranslation(position);
                    switch (grid.getBlock().getType()) {
                        case BLOCK_DIRT_GRASS:
                        case BLOCK_DIRT:
                        case BLOCK_GRASS_FLOWER:
                            dirtInstanceList.add(projection.getModelMatrix());
                            break;
                        case BLOCK_COLD_DIRT:
                        case BLOCK_DIRT_SNOW:
                            coldDirtInstanceList.add(projection.getModelMatrix());
                            break;
                        case BLOCK_ROCK_SNOW:
                            rockInstanceList.add(projection.getModelMatrix());
                            break;
                    }
                }
            }
        }

        if(dirtInstanceList.size() != 0) {
            dirt.setInstanceMatrix(getInstanceMatrix(dirtInstanceList));
            blocks.add(dirt);
        }
        if(grassInstanceList.size() != 0) {
            grass.setInstanceMatrix(getInstanceMatrix(grassInstanceList));
            blocks.add(grass);
        }
        if(grassFlowerInstanceList.size() != 0) {
            grassFlower.setInstanceMatrix(getInstanceMatrix(grassFlowerInstanceList));
            blocks.add(grassFlower);
        }
        if(coldDirtInstanceList.size() != 0) {
            coldDirt.setInstanceMatrix(getInstanceMatrix(coldDirtInstanceList));
            blocks.add(coldDirt);
        }
        if(snowInstanceList.size() != 0) {
            snow.setInstanceMatrix(getInstanceMatrix(snowInstanceList));
            blocks.add(snow);
        }
        if(rockInstanceList.size() != 0) {
            rock.setInstanceMatrix(getInstanceMatrix(rockInstanceList));
            blocks.add(rock);
        }
        if(rockSnowInstanceList.size() != 0) {
            rockSnow.setInstanceMatrix(getInstanceMatrix(rockSnowInstanceList));
            blocks.add(rockSnow);
        }
        if(bevelGrassInstanceList.size() != 0) {
            bevelGrass.setInstanceMatrix(getInstanceMatrix(bevelGrassInstanceList));
            blocks.add(bevelGrass);
        }

        if(smallStoneInstanceList.size() != 0) {
            smallStone.setInstanceMatrix(getInstanceMatrix(smallStoneInstanceList));
            blocks.add(smallStone);
        }
        if(smallStoneDirtInstanceList.size() != 0) {
            smallStoneDirt.setInstanceMatrix(getInstanceMatrix(smallStoneDirtInstanceList));
            blocks.add(smallStoneDirt);
        }
        if(smallStoneSnowInstanceList.size() != 0) {
            smallStoneSnow.setInstanceMatrix(getInstanceMatrix(smallStoneSnowInstanceList));
            blocks.add(smallStoneSnow);
        }
        if(mediumStoneInstanceList.size() != 0) {
            mediumStone.setInstanceMatrix(getInstanceMatrix(mediumStoneInstanceList));
            blocks.add(mediumStone);
        }
        if(mediumStoneDirtInstanceList.size() != 0) {
            mediumStoneDirt.setInstanceMatrix(getInstanceMatrix(mediumStoneDirtInstanceList));
            blocks.add(mediumStoneDirt);
        }
        if(mediumStoneSnowInstanceList.size() != 0) {
            mediumStoneSnow.setInstanceMatrix(getInstanceMatrix(mediumStoneSnowInstanceList));
            blocks.add(mediumStoneSnow);
        }
        if(bigStoneInstanceList.size() != 0) {
            bigStone.setInstanceMatrix(getInstanceMatrix(bigStoneInstanceList));
            blocks.add(bigStone);
        }
        if(bigStoneDirtInstanceList.size() != 0) {
            bigStoneDirt.setInstanceMatrix(getInstanceMatrix(bigStoneDirtInstanceList));
            blocks.add(bigStoneDirt);
        }
        if(bigStoneSnowInstanceList.size() != 0) {
            bigStoneSnow.setInstanceMatrix(getInstanceMatrix(bigStoneSnowInstanceList));
            blocks.add(bigStoneSnow);
        }

        Object weed1 = new Weed((Weed)weed_main,-1.0f);
        weed1.init(getInstanceMatrix(weedInstanceList1));
        sprites.add(weed1);

        Object weed2 = new Weed((Weed)weed_main,seed);
        weed2.init(getInstanceMatrix(weedInstanceList2));
        sprites.add(weed2);

        Object weed3 = new Weed((Weed)weed_main,seed);
        weed3.init(getInstanceMatrix(weedInstanceList3));
        sprites.add(weed3);

        Object weed4 = new Weed((Weed)weed_main,seed);
        weed4.init(getInstanceMatrix(weedInstanceList4));
        sprites.add(weed4);

        Object weed5 = new Weed((Weed)weed_main,seed);
        weed5.init(getInstanceMatrix(weedInstanceList5));
        sprites.add(weed5);

        Object weed6 = new Weed((Weed)weed_main,seed);
        weed6.init(getInstanceMatrix(weedInstanceList6));
        sprites.add(weed6);

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

        int mapX = mapWidthOffset * 2;
        int mapZ = mapHeightOffset * 2;
        return new RandomArena(gridElements, blocks, sprites, water, mapX, mapZ);
    }

    private static void initTexture(){
        if(grayZona == null){
            grayZona = new Texture2D();
            grayZona.setup(null,"./data/content/texture/zona/grid_element_gray.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        }

        if(redZona == null){
            redZona = new Texture2D();
            redZona.setup(null,"./data/content/texture/zona/grid_element_red.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        }

        if(greenZona == null){
            greenZona = new Texture2D();
            greenZona.setup(null,"./data/content/texture/zona/grid_element_green.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        }

        if(goldZona == null){
            goldZona = new Texture2D();
            goldZona.setup(null,"./data/content/texture/zona/grid_element_gold.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        }

        if(blueZona == null){
            blueZona = new Texture2D();
            blueZona.setup(null,"./data/content/texture/zona/grid_element_blue.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        }
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
        if(grass_bevel_main == null){
            grass_bevel_main = new GrassBevel();
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

        if(water_line_main == null){
            water_line_main = new WaterLine();
        }
        if(waterFlower_main == null){
            waterFlower_main = new WaterFlower();
        }
    }

    private static void generateMapSize(){
        mapWidthOffset = (int)(10.0f + Math.random() * 20.0f);
        mapHeightOffset = (int)(10.0f + Math.random() * 20.0f);
    }

    private static List<Vector3f> createMap(long seed, int height){
        List<Vector3f> map = new ArrayList<>();
        Perlin2D perlin = new Perlin2D(seed);

        float num = 20.0f + (float)Math.random() * 80.0f;

        for(int x = 0; x <= mapWidthOffset * 2; x++) {
            for(int z = 0; z <= mapHeightOffset * 2; z++) {
                float value = perlin.getNoise(x/num,z/num,8,0.5f);
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

    private static boolean tryGenerateTilt(Vector3f position, List<Vector3f> positions, Projection projection){

        float trans_max = 1.42f;
        float trans_min = 0.999f;

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

        float angle = 45.0f;

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
                angleRotation = 90.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
            if(direction.getX() == 1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                angleRotation = 270.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
            if(direction.getZ() == -1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                angleRotation = 0.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
            if(direction.getZ() == 1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                angleRotation = 180.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
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
                    angleRotation = 90.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
                if (direction.getX() == 1) {
                    projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                    angleRotation = 270.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
                if (direction.getZ() == -1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    angleRotation = 0.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
                if (direction.getZ() == 1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    angleRotation = 180.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
            }
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
                    angleRotation = 90.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
                if (direction.getX() == 1) {
                    projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                    angleRotation = 270.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
                if (direction.getZ() == -1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    angleRotation = 0.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
                if (direction.getZ() == 1) {
                    projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                    angleRotation = 180.0f;
                    projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                    projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                    bevel = true;
                }
            }
        }

        if(high == 1 && low == 3){
            Vector3f mainPos = new Vector3f(position.getX(),0.0f,position.getZ());
            Vector3f somePos = new Vector3f(highPos.get(0).getX(),0.0f,highPos.get(0).getZ());
            Vector3f direction = new Vector3f(mainPos.sub(somePos));

            if(direction.getX() == 1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                angleRotation = 90.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
            if(direction.getX() == -1){
                projection.setScaling(new Vector3f(trans_max, trans_max, trans_min));
                angleRotation = 270.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
            if(direction.getZ() == 1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                angleRotation = 0.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
            if(direction.getZ() == -1){
                projection.setScaling(new Vector3f(trans_min, trans_max, trans_max));
                angleRotation = 180.0f;
                projection.setRotation(angleRotation, new Vector3f(0.0f, 1.0f, 0.0f));
                projection.setRotation(angle, new Vector3f(1.0f, 0.0f, 0.0f));
                bevel = true;
            }
        }

        return bevel;
    }

    public static Vector3f getRandomPos(){
        int width = mapWidthOffset * 2;
        int height = mapHeightOffset * 2;
        float x = (float) Math.floor(Math.random() * width);
        float z = (float) Math.floor(Math.random() * height);
        Vector3f randomPos = new Vector3f(x, 0.0f, z);

        for(GridElement grid : gridElements){
            if(grid.getPosition().equals(randomPos)){
                if(!grid.isWater() && !grid.isBlocked()) {
                    grid.setBlocked(true);
                    return new Vector3f(grid.getPosition().getX(),grid.getCurrentHeight(),grid.getPosition().getZ());
                }else{
                    return getRandomPos();
                }
            }
        }

        return new Vector3f();
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

    private static int checkBlockBorder(GridElement block){
        float mainCurrentHeight = block.getPosition().getY() - 1.0f;
        Vector3f left = new Vector3f(block.getPosition().getX() - 1.0f,0.0f,block.getPosition().getZ());
        Vector3f right = new Vector3f(block.getPosition().getX() + 1.0f,0.0f,block.getPosition().getZ());
        Vector3f up = new Vector3f(block.getPosition().getX(),0.0f,block.getPosition().getZ() + 1.0f);
        Vector3f down = new Vector3f(block.getPosition().getX(),0.0f,block.getPosition().getZ() - 1.0f);

        List<Float>heights = new ArrayList<>();

        for(GridElement element : gridElements){
            if(element.getPosition().equals(left)){
                if(element.getPosition().getY() < mainCurrentHeight){
                    heights.add(Math.abs(mainCurrentHeight - element.getCurrentHeight()));
                }
            }
            if(element.getPosition().equals(right)){
                if(element.getPosition().getY() < mainCurrentHeight){
                    heights.add(Math.abs(mainCurrentHeight - element.getCurrentHeight()));
                }
            }
            if (element.getPosition().equals(up)) {
                if(element.getPosition().getY() < mainCurrentHeight){
                    heights.add(Math.abs(mainCurrentHeight - element.getCurrentHeight()));
                }
            }
            if(element.getPosition().equals(down)){
                if(element.getPosition().getY() < mainCurrentHeight){
                    heights.add(Math.abs(mainCurrentHeight - element.getCurrentHeight()));
                }
            }
        }

        int height  = 0;

        if(heights.size() != 0) {
            heights.sort((o1, o2) -> o1 > o2 ? 0 : -1);
            height = (int)Math.ceil(heights.get(0));
        }

        return height;
    }

    private static Matrix4f[] getInstanceMatrix(List<Matrix4f> matrixList){
        Matrix4f[] matrix = new Matrix4f[matrixList.size()];
        for(int i=0; i<matrix.length; i++){
            matrix[i] = matrixList.get(i);
        }
        return matrix;
    }
}