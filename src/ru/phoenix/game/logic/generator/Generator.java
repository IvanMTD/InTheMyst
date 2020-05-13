package ru.phoenix.game.logic.generator;

import ru.phoenix.core.loader.model.Mesh;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.stage.random.PreparedArena;
import ru.phoenix.game.content.stage.random.RandomArena;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.components.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.MOUNTAIN_AREA;
import static ru.phoenix.core.config.Constants.PLAIN_AREA;

public class Generator {
    // модули компоненты
    private static GraundTexture graundTexture = null;
    // переменные класса
    private static int totalMapWidth;
    private static int totalMapHeight;
    // карта высот
    private static Texture heightMap;

    public static RandomArena getRandomArea(float currentHeight, int w, int h){

        // проверяем состояние модулей компонентов - начало
        checkElements();
        // проверяем состояние модулей компонентов - конец

        // определяем алгоритм генерации - начало
        Mesh mesh;
        Cell[][] grid = null;

        setTotalMapWidth(w);
        setTotalMapHeight(h);

        System.out.println("Создана карта размером " + (getTotalMapWidth() + 1) + "x" + (getTotalMapHeight() + 1));
        grid = HeightMap.get((long)(1 + Math.random() * 10000000000L),getTotalMapWidth(),getTotalMapHeight(),currentHeight, true);
        heightMap = new Texture2D();
        heightMap.setup(HeightMap.getHeiMap(),GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        mesh = ModelCreater.start((int)currentHeight - 6, grid, getTotalMapWidth(), getTotalMapHeight(), graundTexture);
        Reservoir waterReservoir = null;
        waterReservoir = new Reservoir("./data/content/texture/water/waterSet.png", 10, 1);
        waterReservoir.init(grid,currentHeight);
        List<Object> trees = PlantingTrees.start(grid,getTotalMapWidth(),getTotalMapHeight());
        List<Object> grasses = PlantGrass.start(grid,getTotalMapWidth(),getTotalMapHeight(),currentHeight);
        List<Block> blocks = GameElement.setup(grid,getTotalMapWidth(),getTotalMapHeight(),currentHeight);
        List<Object> things = ResourcesAndThings.scatter(grid,getTotalMapWidth(),getTotalMapHeight(),currentHeight);
        GraundModel model = new GraundModel(mesh, graundTexture);
        List<Object> sprites = new ArrayList<>();
        sprites.addAll(trees);
        sprites.addAll(grasses);
        sprites.addAll(things);
        return new RandomArena(grid, model, waterReservoir, blocks, sprites, getTotalMapWidth(), getTotalMapHeight());
        // определяем алгоритм генерации - конец
    }

    public static PreparedArena getPreparedArea(float currentHeight, int w, int h){

        // проверяем состояние модулей компонентов - начало
        checkElements();
        // проверяем состояние модулей компонентов - конец

        // определяем алгоритм генерации - начало
        Mesh mesh;
        Cell[][] grid = null;

        setTotalMapWidth(w);
        setTotalMapHeight(h);

        System.out.println("Создана карта размером " + (getTotalMapWidth() + 1) + "x" + (getTotalMapHeight() + 1));
        grid = HeightMap.get((long)(1 + Math.random() * 10000000000L),getTotalMapWidth(),getTotalMapHeight(),currentHeight, true);
        heightMap = new Texture2D();
        heightMap.setup(HeightMap.getHeiMap(),GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        mesh = ModelCreater.start((int)currentHeight - 6, grid, getTotalMapWidth(), getTotalMapHeight(), graundTexture);
        Reservoir waterReservoir = null;
        waterReservoir = new Reservoir("./data/content/texture/water/waterSet.png", 10, 1);
        waterReservoir.init(grid,currentHeight);
        List<Object> trees = PlantingTrees.start(grid,getTotalMapWidth(),getTotalMapHeight());
        List<Object> grasses = PlantGrass.start(grid,getTotalMapWidth(),getTotalMapHeight(),currentHeight);
        List<Block> blocks = GameElement.setup(grid,getTotalMapWidth(),getTotalMapHeight(),currentHeight);
        List<Object> things = ResourcesAndThings.scatter(grid,getTotalMapWidth(),getTotalMapHeight(),currentHeight);
        GraundModel model = new GraundModel(mesh, graundTexture);
        List<Object> sprites = new ArrayList<>();
        sprites.addAll(trees);
        sprites.addAll(grasses);
        sprites.addAll(things);
        return new PreparedArena(grid, model, waterReservoir, blocks, sprites, getTotalMapWidth(), getTotalMapHeight());
        // определяем алгоритм генерации - конец
    }

    // методы получения свободного поля - начало
    public static Vector3f getRandomPos(Cell[][] grid, boolean occupied){
        int x = (int)Math.round(Math.random() * (float)getTotalMapWidth());
        int z = (int)Math.round(Math.random() * (float)getTotalMapHeight());

        if(!grid[x][z].isBlocked() && !grid[x][z].isOccupied() && !grid[x][z].isBevel() && !grid[x][z].isWater() && !grid[x][z].isRoad()){
            grid[x][z].setOccupied(occupied);
            return new Vector3f(grid[x][z].getModifiedPosition());
        }else{
            return getRandomPos(grid,occupied);
        }
    }

    public static Vector3f getRandomPos(Cell[][]grid, Vector3f point, float radius, boolean occupied){
        float x_min = (point.getX() - radius) < 0.0f ? 0.0f : (point.getX() - radius);
        float x_max = radius * 2;
        float x_random = (float)Math.random();
        int x = (int) Math.floor((x_min + x_random * x_max) > getTotalMapWidth() ? getTotalMapWidth() : (x_min + x_random * x_max));

        float z_min = (point.getZ() - radius) < 0.0f ? 0.0f : (point.getZ() - radius);
        float z_max = radius * 2;
        float z_random = (float)Math.random();
        int z = (int) Math.floor((z_min + z_random * z_max) > getTotalMapHeight() ? getTotalMapHeight() : (z_min + z_random * z_max));

        if(!grid[x][z].isBlocked() && !grid[x][z].isOccupied() && !grid[x][z].isBevel() && !grid[x][z].isWater()){
            grid[x][z].setOccupied(occupied);
            return new Vector3f(grid[x][z].getModifiedPosition());
        }else{
            return getRandomPos(grid,point,radius,occupied);
        }
    }

    // методы получения свободного поля - конец

    // методы проверки - начало
    private static void checkElements(){
        // проверка модуля с текстурами - начало
        if(graundTexture == null){
            graundTexture = new GraundTexture();
            graundTexture.init();
        }
        // проверка модуля с текстурами - конец
    }
    // методы проверки - конец

    // методы гетеры и сетеры - начало

    public static int getTotalMapWidth() {
        return totalMapWidth;
    }

    private static void setTotalMapWidth(int totalMapWidth) {
        Generator.totalMapWidth = totalMapWidth;
    }

    public static int getTotalMapHeight() {
        return totalMapHeight;
    }

    private static void setTotalMapHeight(int totalMapHeight) {
        Generator.totalMapHeight = totalMapHeight;
    }

    public static Texture getHeightMap() {
        return heightMap;
    }
    // методы гетеры и сетеры - конец
}
