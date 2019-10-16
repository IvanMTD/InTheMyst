package ru.phoenix.game.content.stage;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.lighting.DirectLight;
import ru.phoenix.game.logic.lighting.Light;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.GROUP_A;

public abstract class BattleGraundControl {
    private List<GridElement> gridElements;
    private List<Light> directLights;
    private List<Block> blocks;
    private List<Object> sprites;
    private List<Object> water;
    private int mapX;
    private int mapZ;

    public BattleGraundControl(){
        blocks = new ArrayList<>();
        directLights = new ArrayList<>();
    }

    protected void setBlocks(List<GridElement> gridElements, List<Block> blocks, List<Object> water, List<Object> sprites, int mapX, int mapZ){
        this.gridElements = new ArrayList<>(gridElements);
        this.blocks = new ArrayList<>(blocks);
        this.sprites = new ArrayList<>(sprites);
        if(water == null){
            this.water = new ArrayList<>();
        }else {
            this.water = new ArrayList<>(water);
        }
        this.mapX = mapX;
        this.mapZ = mapZ;
    }

    protected void initLight(){
        float x = (float)(Math.random() * mapX / 2);
        float z = (float)(Math.random() * mapZ / 2);
        float y = (mapX + mapZ) / 2.0f;
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

    public void update(){
        for(Object object : sprites){
            object.update(getGridElements());
        }

        for(Object object : water){
            object.update(getGridElements());
        }
    }

    public void draw(Shader shader){
        // setUniforms
        shader.useProgram();
        // глобальный юниформ
        shader.setUniformBlock("matrices",0);
        // контролеры
        shader.setUniform("animated",0);
        shader.setUniform("board",0);
        // доп данные
        shader.setUniform("shininess",64.0f);
        shader.setUniform("group",GROUP_A);
        shader.setUniform("id",0.0f);
        shader.setUniform("onTarget", 0);
        // end
        for(Block block : blocks){
            block.draw(shader);
        }
    }

    public void drawSprites(Shader shader){
        if(sprites.size() != 0) {
            for (Object object : sprites) {
                float distance = Math.abs(Camera.getInstance().getPos().sub(object.getPosition()).length());
                object.setDistance(distance);
            }

            sprites.sort((o1, o2) -> o1.getDistance() < o2.getDistance() ? 0 : -1);
            sprites.sort(((o1, o2) -> (o1.isActive() ? 1 : 0) < (o2.isActive() ? 1 : 0) ? 0 : -1));
        }

        for(Object object : sprites){
            object.draw(shader,false);
        }
    }

    public void drawWater(Shader shader){
        for(Object object : water){
            object.draw(shader,false);
        }
    }

    public void drawGrid(Shader shader){
        for(GridElement element : gridElements){
            element.draw(shader);
        }
    }

    public void drawShadowSprites(Shader shader){
        for(Object object : sprites){
            if(object.isShadow()){
                object.draw(shader,true);
            }
        }
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapZ() {
        return mapZ;
    }

    public List<GridElement> getGridElements() {
        return gridElements;
    }

    public List<Light> getDirectLight() {
        return directLights;
    }

    public List<Object> getSprites(){
        return sprites;
    }
}
