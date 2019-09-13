package ru.phoenix.game.content.stage;

import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.logic.generator.component.GridElement;
import ru.phoenix.game.logic.lighting.DirectLight;
import ru.phoenix.game.logic.lighting.Light;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class BattleGraundControl {
    private List<GridElement> gridElements;
    private List<Light> directLights;
    private List<Block> blocks;
    private List<Object> sprites;
    private List<Object> water;
    private int mapX;
    private int mapZ;

    private int tempSecond;

    public BattleGraundControl(){
        blocks = new ArrayList<>();
        directLights = new ArrayList<>();
        tempSecond = Time.getSecond();
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
        float x = (float)(Math.random() * mapX);
        float z = (float)(Math.random() * mapZ);
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

        if(tempSecond != Time.getSecond()){
            for(Object object : sprites){
                if(object.isActive()){

                }
            }
        }
        tempSecond = Time.getSecond();

        for(Object object : sprites){
            object.update();
        }

        for(Object object : water){
            object.update();
        }
    }

    public void draw(Shader shader){
        for(Block block : blocks){
            block.draw(shader);
        }
    }

    public void drawSprites(Shader shader){
        if(sprites.size() != 0) {
            if (!sprites.get(0).isInstance()) {
                for (Object object : sprites) {
                    float distance = Camera.getInstance().getPos().sub(object.getPosition()).length();
                    if(object.isActive()){
                        object.setDistance(distance + 0.1f);
                    }else {
                        object.setDistance(distance);
                    }
                }

                sprites.sort(new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return o1.getDistance() < o2.getDistance() ? 0 : -1;
                    }
                });
            }
        }

        for(Object object : sprites){
            object.draw(shader);
        }
    }

    public void drawWater(Shader shader){
        for(Object object : water){
            object.draw(shader);
        }
    }

    public void drawShadowSprites(Shader shader){
        for(Object object : sprites){
            if(object.isShadow()){
                object.draw(shader);
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
