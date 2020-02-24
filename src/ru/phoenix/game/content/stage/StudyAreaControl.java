package ru.phoenix.game.content.stage;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Projection;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.math.Vector4f;
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
import static ru.phoenix.core.config.Constants.*;

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
    private boolean invert;

    private List<Character> allies;
    private List<Character> alliesInBattle;
    private List<Character> enemies;
    private List<Character> enemiesInBattle;

    private Matrix4f mapCamera;
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
        invert = false;
        mapCamera = new Matrix4f();
    }

    protected void setup(Cell[][] grid, GraundModel graundModel, Reservoir waterReservoir, List<Block> blocks, List<Object>sprites, int mapX, int mapZ){
        this.blocks = new ArrayList<>(blocks);
        this.grid = grid;
        this.graundModel = graundModel;
        this.waterReservoir = waterReservoir;
        this.sprites = new ArrayList<>(sprites);
        this.mapX = mapX;
        this.mapZ = mapZ;
        float x = mapX;
        float y = mapZ;
        Projection projection = new Projection();
        float size = 16.0f;
        projection.setOrtho(-x * size, x * size,-y * size,y * size, WindowConfig.getInstance().getNear(), 100.0f);
        projection.setView(
                new Vector3f(getMapX() / 2.0f, 50.0f, getMapZ() / 2.0f),
                new Vector3f(getMapX() / 2.0f, 0.0f, getMapZ() / 2.0f),
                new Vector3f(0.0f,0.0f,-1.0f)
                );
        mapCamera.setMatrix(projection.getProjection().mul(projection.getViewMatrix()));
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
                Default.setRadiance(1.0f);
                invert = false;
                int minX = Math.round(battleGround.getLocalPoint().getX() - battleGround.getRadius()); if(minX < 0) minX = 0; battleGround.setMinW(minX);
                int maxX = Math.round(battleGround.getLocalPoint().getX() + battleGround.getRadius()); if(maxX > getMapX()) maxX = getMapX(); battleGround.setMaxW(maxX);
                int minZ = Math.round(battleGround.getLocalPoint().getZ() - battleGround.getRadius()); if(minZ < 0) minZ = 0; battleGround.setMinH(minZ);
                int maxZ = Math.round(battleGround.getLocalPoint().getZ() + battleGround.getRadius()); if(maxZ > getMapZ()) maxZ = getMapZ(); battleGround.setMaxH(maxZ);
                for(int x=0; x<=getMapX(); x++){
                    for(int z=0; z<=getMapZ(); z++){
                        if((minX <= x && x <= maxX) && (minZ <= z && z <= maxZ)){
                            if(x == minX || x == maxX || z == minZ || z == maxZ){
                                grid[x][z].setGrayZona();
                                grid[x][z].setVisible(false);
                                grid[x][z].setWayPoint(false);
                                grid[x][z].setParent(null);
                                if(x == 0 || z == 0 || x == getMapX() || z == getMapZ()){
                                    if(x == 0 || x == getMapX()){
                                        if(z == minZ || z == maxZ){
                                            grid[x][z].setExitBattleGraund(true);
                                        }else{
                                            grid[x][z].setBattleGraund(true);
                                        }
                                    }else if(z == 0 || z == getMapZ()){
                                        if(x == minX || x == maxX){
                                            grid[x][z].setExitBattleGraund(true);
                                        }else{
                                            grid[x][z].setBattleGraund(true);
                                        }
                                    }
                                }else {
                                    grid[x][z].setExitBattleGraund(true);
                                }
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
                    int x = (int)ally.getPosition().getX();
                    int z = (int)ally.getPosition().getZ();
                    if(grid[x][z].isBattleGraund()){
                        ally.setBattle(true);
                        alliesInBattle.add(ally);
                    }
                }
                enemiesInBattle.clear();
                for(Character enemy : enemies){
                    int x = (int)enemy.getPosition().getX();
                    int z = (int)enemy.getPosition().getZ();
                    if(grid[x][z].isBattleGraund()){
                        enemy.setBattle(true);
                        enemiesInBattle.add(enemy);
                    }
                }
                prepareBattlefield = false;
                Default.setWait(false);
            }else{
                if(invert){
                    Default.setRadiance(Default.getRadiance() - 0.03f);
                    if(Default.getRadiance() < 1.0f){
                        Default.setRadiance(1.0f);
                        invert = false;
                    }
                }else{
                    Default.setRadiance(Default.getRadiance() + 0.03f);
                    if(Default.getRadiance() > 10.0f){
                        Default.setRadiance(10.0f);
                        invert = true;
                    }
                }

                int totalAllies = alliesInBattle.size();
                Character removeAlly = null;
                for(Character ally : alliesInBattle){
                    ally.interaction(grid,null,pixel,enemiesInBattle,alliesInBattle,battleGround);
                    ally.update();
                    if(ally.isDead()){
                        totalAllies--;
                    }else{
                        Cell cell = grid[Math.round(ally.getPosition().getX())][Math.round(ally.getPosition().getZ())];
                        if(cell.getModifiedPosition().equals(ally.getPosition()) && cell.isExitBattleGraund() && !ally.isJump()){
                            removeAlly = ally;
                        }
                    }
                }

                if(removeAlly != null){
                    removeAlly.setBattle(false);
                    removeAlly.resetSettings();
                    removeAlly.setShowIndicators(false);
                    Default.setWait(false);
                    alliesInBattle.remove(removeAlly);
                }

                int totalEnemies = enemiesInBattle.size();
                Character removeEnemy = null;
                for(Character enemy : enemiesInBattle){
                    enemy.interaction(grid,null,pixel,alliesInBattle,enemiesInBattle,battleGround);
                    enemy.update();
                    if(enemy.isDead()){
                        totalEnemies--;
                    }else{
                        Cell cell = grid[Math.round(enemy.getPosition().getX())][Math.round(enemy.getPosition().getZ())];
                        if(cell.getModifiedPosition().equals(enemy.getPosition()) && cell.isExitBattleGraund() && !enemy.isJump()){
                            removeEnemy = enemy;
                        }
                    }
                }

                if(removeEnemy != null){
                    removeEnemy.setBattle(false);
                    removeEnemy.resetSettings();
                    removeEnemy.setShowIndicators(false);
                    Default.setWait(false);
                    enemiesInBattle.remove(removeEnemy);
                    enemies.remove(removeEnemy);
                }

                if(totalAllies == 0 || totalEnemies == 0 || enemiesInBattle.isEmpty() || alliesInBattle.isEmpty()){
                    battleGround.setActive(false);
                    prepareBattlefield = true;
                    Default.setWait(false);
                    for(Character ally : alliesInBattle){
                        ally.getCharacteristic().setInitiative(0);
                        ally.setBattle(false);
                        ally.setShowIndicators(false);
                        ally.resetSettings();
                    }
                    for(Character enemy : enemiesInBattle){
                        enemy.getCharacteristic().setInitiative(0);
                        enemy.setBattle(false);
                        enemy.setShowIndicators(false);
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
        shader.setUniform("instance", 0);
        shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
        // доп данные
        shader.setUniform("localPoint",battleGround.getLocalPoint());
        shader.setUniform("radius",battleGround.getRadius());
        shader.setUniform("shininess",64.0f);
        shader.setUniform("group",GROUP_A);
        shader.setUniform("id",0.0f);
        shader.setUniform("onTarget", 0);
        shader.setUniform("radiance", Default.getRadiance());
        shader.setUniform("board",0);
        // map
        shader.setUniform("w",getMapX());
        shader.setUniform("h",getMapZ());
        shader.setUniform("mapCam_m",mapCamera);
        for(int i=0; i<6; i++){
            if(i < allies.size()){
                Vector3f p = new Vector3f(getAllies().get(i).getPosition());
                float vision = getAllies().get(i).getCharacteristic().getVision();
                float tempVision = getAllies().get(i).getCharacteristic().getTempVision();
                Vector4f unit;
                if(tempVision != vision){
                    if(tempVision < vision){
                        getAllies().get(i).getCharacteristic().setTempVision(tempVision + 0.01f);
                        float v = getAllies().get(i).getCharacteristic().getTempVision() / 100.0f;
                        float a = (0.1f - v) * -1.0f;
                        float d = 0.1f - a;
                        unit = new Vector4f(p.getX(),p.getY(),p.getZ(),d);
                        if(getAllies().get(i).getCharacteristic().getTempVision() > vision){
                            getAllies().get(i).getCharacteristic().setTempVision(vision);
                        }
                    }else{
                        getAllies().get(i).getCharacteristic().setTempVision(tempVision - 0.01f);
                        float v = getAllies().get(i).getCharacteristic().getTempVision() / 100.0f;
                        float a = (0.1f - v) * -1.0f;
                        float d = 0.1f - a;
                        unit = new Vector4f(p.getX(),p.getY(),p.getZ(),d);
                        if(getAllies().get(i).getCharacteristic().getTempVision() < vision){
                            getAllies().get(i).getCharacteristic().setTempVision(vision);
                        }
                    }
                }else {
                    float v = getAllies().get(i).getCharacteristic().getVision() / 100.0f;
                    float a = (0.1f - v) * -1.0f;
                    float d = 0.1f - a;
                    unit = new Vector4f(p.getX(),p.getY(),p.getZ(),d);
                }
                shader.setUniform("unit" + i, unit);
            }else{
                shader.setUniform("unit" + i, new Vector4f(-1.0f,-1.0f,-1.0f,1.0f));
            }
        }
        // end
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        graundModel.draw(shader);
        glFrontFace(GL_CCW);
        for (Block block : blocks) {
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRadius());
            block.draw(shader);
        }
        glDisable(GL_CULL_FACE);
    }

    public void drawSprites(Shader shader){
        for(Object object : sprites){
            shader.setUniform("battlefield", battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint", battleGround.getLocalPoint());
            shader.setUniform("radius", battleGround.getRadius());
            object.draw(shader, false);
        }
    }

    public void drawPersons(Shader shader){
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
            shader.setUniform("radius",battleGround.getRadius());
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
            shader.setUniform("radius",battleGround.getRadius());
            ally.draw(shader,shadow);
        }

        for(Character enemy : enemies){
            shader.setUniform("battlefield",battleGround.isActive() ? 1 : 0);
            shader.setUniform("localPoint",battleGround.getLocalPoint());
            shader.setUniform("radius",battleGround.getRadius());
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
