package ru.phoenix.game.content.stage;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.components.GraundModel;
import ru.phoenix.game.logic.generator.components.Reservoir;
import ru.phoenix.game.logic.lighting.DirectLight;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.property.GameController;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class StudyAreaControl {
    private Cell[][] grid;
    private List<Light> directLights;
    private GraundModel graundModel;
    private Reservoir waterReservoir;
    private List<Block> blocks;
    private List<Object> sprites;

    // БОЕВЫЕ ЗОНЫ
    private BattleGround battleGround;
    private boolean prepareBattlefield;

    private List<Character> allies;
    private List<Character> alliesInBattle;
    private List<Character> enemies;
    private List<Character> enemiesInBattle;

    private int mapX;
    private int mapZ;

    public StudyAreaControl(){
        blocks = new ArrayList<>();
        directLights = new ArrayList<>();
        allies = new ArrayList<>();
        alliesInBattle = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesInBattle = new ArrayList<>();
        battleGround = new BattleGround();
        prepareBattlefield = true;
    }

    protected void setup(Cell[][] grid, GraundModel graundModel, Reservoir waterReservoir, List<Block> blocks, List<Object>sprites, int mapX, int mapZ){
        this.blocks = new ArrayList<>(blocks);
        this.grid = grid;
        this.graundModel = graundModel;
        this.waterReservoir = waterReservoir;
        this.sprites = new ArrayList<>(sprites);
        this.mapX = mapX;
        this.mapZ = mapZ;
    }

    protected void initLight(){
        float hyp = (float)Math.sqrt(mapX * mapX + mapZ * mapZ);
        float x = (float)mapX / 2.0f + ((float)Math.sin(Math.toRadians(-45.0f)) * (float)mapX / 2.0f);
        float y = hyp;
        float z = (float)mapZ / 2.0f + ((float)Math.cos(Math.toRadians(-45.0f)) * (float)mapZ / 2.0f);
        Light directLight = new DirectLight(
                new Vector3f(x,y,z), // position
                new Vector3f(0.2f,0.2f,0.2f), // ambient
                new Vector3f(1.0f,1.0f,1.0f), // diffuse
                new Vector3f(1.0f,1.0f,1.0f), // specular
                mapX > mapZ ? mapX : mapZ,
                mapX,
                mapZ
        );
        directLights.add(directLight);
    }

    public void update(Cell targetElement, Vector3f pixel){
        // ГЛОБАЛЬНЫЕ ОБНОВЛЕНИЯ ЗОНЫ - НАЧАЛО
        // Обновление воды
        if(waterReservoir != null) {
            waterReservoir.update();
        }
        // Обновление деревьев, травы и т.д.
        for (Object object : sprites) {
            object.update(grid,pixel,targetElement);
        }
        // ГЛОБАЛЬНЫЕ ОБНОВЛЕНИЯ ЗОНЫ - КОНЕЦ

        if(battleGround.isActive()){
            if(prepareBattlefield){
                int minX = Math.round(battleGround.getLocalPoint().getX() - battleGround.getRADIUS()); if(minX < 0) minX = 0; battleGround.setMinW(minX);
                int maxX = Math.round(battleGround.getLocalPoint().getX() + battleGround.getRADIUS()); if(maxX > getMapX()) maxX = getMapX(); battleGround.setMaxW(maxX);
                int minZ = Math.round(battleGround.getLocalPoint().getZ() - battleGround.getRADIUS()); if(minZ < 0) minZ = 0; battleGround.setMinH(minZ);
                int maxZ = Math.round(battleGround.getLocalPoint().getZ() + battleGround.getRADIUS()); if(maxZ > getMapZ()) maxZ = getMapZ(); battleGround.setMaxH(maxZ);
                for(int x=0; x<getMapX(); x++){
                    for(int z=0; z<getMapZ(); z++){
                        if((minX <= x && x <= maxX) && (minZ <= z && z <= maxZ)){
                            if(x == minX || x == maxX || z == minZ || z == maxZ){
                                grid[x][z].setGrayZona();
                                grid[x][z].setVisible(false);
                                grid[x][z].setWayPoint(false);
                                grid[x][z].setParent(null);
                                grid[x][z].setExitBattleGraund(true);
                            }else{
                                grid[x][z].setGrayZona();
                                grid[x][z].setVisible(false);
                                grid[x][z].setWayPoint(false);
                                grid[x][z].setParent(null);
                                grid[x][z].setBattleGraund(true);
                            }
                        }
                    }
                }
                alliesInBattle.clear();
                for(Character ally : allies){
                    int x = Math.round(ally.getPosition().getX());
                    int z = Math.round(ally.getPosition().getZ());
                    if(grid[x][z].isBattleGraund() || grid[x][z].isExitBattleGraund()){
                        ally.setBattle(true);
                        alliesInBattle.add(ally);
                    }
                }
                enemiesInBattle.clear();
                for(Character enemy : enemies){
                    int x = Math.round(enemy.getPosition().getX());
                    int z = Math.round(enemy.getPosition().getZ());
                    if(grid[x][z].isBattleGraund() || grid[x][z].isExitBattleGraund()){
                        enemy.setBattle(true);
                        enemiesInBattle.add(enemy);
                    }
                }
                prepareBattlefield = false;
                Default.setWait(false);
            }else{
                int totalAllies = alliesInBattle.size();
                for(Character ally : alliesInBattle){
                    ally.interaction(grid,null,pixel,enemiesInBattle,alliesInBattle,battleGround);
                    ally.update();
                    if(ally.isDead()){
                        totalAllies--;
                    }
                }

                int totalEnemies = enemiesInBattle.size();
                for(Character enemy : enemiesInBattle){
                    enemy.interaction(grid,null,pixel,alliesInBattle,enemiesInBattle,battleGround);
                    enemy.update();
                    if(enemy.isDead()){
                        totalEnemies--;
                    }
                }

                if(totalAllies == 0 || totalEnemies == 0 || enemiesInBattle.isEmpty() || alliesInBattle.isEmpty()){
                    battleGround.setActive(false);
                    prepareBattlefield = true;
                    Default.setWait(false);
                    for(Character ally : alliesInBattle){
                        ally.setBattle(false);
                        ally.resetSettings();
                    }
                    for(Character enemy : enemiesInBattle){
                        enemy.setBattle(false);
                        enemy.resetSettings();
                    }
                    for(int x=0; x<getMapX(); x++){
                        for(int z=0; z<getMapZ(); z++){
                            grid[x][z].setGrayZona();
                            grid[x][z].setVisible(false);
                            grid[x][z].setWayPoint(false);
                            grid[x][z].setParent(null);
                            grid[x][z].setBattleGraund(false);
                            grid[x][z].setExitBattleGraund(false);
                        }
                    }
                }
            }
        }else{
            int decision = 0;

            if(GameController.getInstance().isLeftClick()){
                decision = 1;
                if(Input.getInstance().isPressed(GLFW_KEY_LEFT_SHIFT)){
                    decision = 2;
                }else if(GameController.getInstance().isRightHold()){
                    decision = 3;
                }
            }

            if(decision == 1) {
                for (Character currentAlly : allies) {
                    if (currentAlly.isTarget()) {
                        currentAlly.setSelected(true);
                        targetElement = null;
                        for (Character ally : allies) {
                            if (ally.getId() != currentAlly.getId()) {
                                ally.setSelected(false);
                            }
                        }
                    }
                }
            }else if(decision == 2){
                for (Character ally : allies) {
                    if (ally.isTarget() && !ally.isBattle()) {
                        ally.setSelected(true);
                        targetElement = null;
                    }
                }
            }else if(decision == 3){
                for (Character ally : allies) {
                    if (!ally.isBattle()) {
                        ally.setSelected(true);
                    }
                }
            }

            for(Character enemy : enemies){
                if(enemy.getId() == pixel.getX()){
                    targetElement = null;
                }
            }

            for(Character ally : allies){
                ally.interaction(grid, targetElement, pixel, enemies, allies, battleGround);
                ally.update();
            }

            for(Character enemy : enemies){
                enemy.interaction(grid, targetElement, pixel, allies, enemies, battleGround);
                enemy.update();
            }
        }
    }

    public void draw(Shader shader){
        // контролеры
        shader.setUniform("animated",0);
        shader.setUniform("instance", 0);
        shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
        // доп данные
        shader.setUniform("localPoint",battleGround.getLocalPoint());
        shader.setUniform("radius",battleGround.getRADIUS());
        shader.setUniform("shininess",64.0f);
        shader.setUniform("group",GROUP_A);
        shader.setUniform("id",0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("board",0);
        // end
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        graundModel.draw(shader);
        glFrontFace(GL_CCW);
        for (Block block : blocks) {
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRADIUS());
            block.draw(shader);
        }
        glDisable(GL_CULL_FACE);
    }

    public void drawSprites(Shader shader){
        for(Object object : sprites){
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRADIUS());
            object.draw(shader,false);
        }
    }

    public void drawPersons(Shader shader){
        /*for(Object object : persons){
            object.draw(shader,false);
        }*/

        for(Character ally : allies){
            ally.draw(shader,false);
        }

        for(Character enemy : enemies){
            enemy.draw(shader,false);
        }
    }

    public void drawWater(Shader shader){
        if(waterReservoir != null) {
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRADIUS());
            waterReservoir.draw(shader);
        }
    }

    public void drawShadowSprites(Shader shader){
        for(Object object : sprites){
            if(object.isShadow()){
                object.draw(shader,true);
            }
        }
    }

    public void drawShadowPersons(Shader shader, boolean shadow){
        for(Character ally : allies){
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRADIUS());
            ally.draw(shader,shadow);
        }

        for(Character enemy : enemies){
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRADIUS());
            enemy.draw(shader,shadow);
        }
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapZ() {
        return mapZ;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public List<Light> getDirectLight() {
        return directLights;
    }

    public List<Object> getSprites(){
        return sprites;
    }

    public List<Character> getAllies() {
        return allies;
    }

    public List<Character> getEnemies() {
        return enemies;
    }

    public boolean isWater(){
        return waterReservoir != null;
    }

    public BattleGround getBattleGround() {
        return battleGround;
    }
}
