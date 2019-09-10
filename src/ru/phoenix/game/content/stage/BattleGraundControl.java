package ru.phoenix.game.content.stage;

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
    private int mapX;
    private int mapZ;

    public BattleGraundControl(){
        blocks = new ArrayList<>();
        directLights = new ArrayList<>();
    }

    protected void setBlocks(List<GridElement> gridElements, List<Block> blocks, List<Object> sprites, int mapX, int mapZ){
        this.gridElements = new ArrayList<>(gridElements);
        this.blocks = new ArrayList<>(blocks);
        this.sprites = new ArrayList<>(sprites);
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
        for(Object object : sprites){
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
                    object.setDistance(distance);
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

    public void drawShadowSprites(Shader shader){
        for(Object object : sprites){
            if(object.isBoard()){
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
}
