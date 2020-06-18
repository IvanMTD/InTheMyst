package ru.phoenix.game.scene.tactic;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.math.Vector4f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyArcher;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyBandit;
import ru.phoenix.game.content.characters.humans.anarchy.grade.first.AnarchyThief;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.hud.assembled.GraundAim;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.logic.movement.MousePicker;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.property.GameController;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static ru.phoenix.core.config.Constants.*;

public class TacticalScene implements Scene {

    private List<Scene> scenes;
    private List<Character> allies;
    private float currentHeight;

    private Skybox skybox;

    private Shader shader3D;
    private Shader shaderSprite;

    private boolean active;
    private boolean init;

    private StudyArea studyArea;

    private int index;

    private boolean switchControl;
    private float count;

    private Cursor cursorHud;
    private GraundAim graundAim;
    private int aimDrawConfig;

    private List<Cell> first;
    private List<Cell> second;
    private List<Cell> exception;

    private MousePicker mousePicker;

    // элементы контроля повторов
    private Cell lastElement;

    private Vector3f lastCameraPos;
    private Vector3f lastCameraFront;
    private boolean cameraUpdate;

    private boolean start;
    private boolean finish;
    private float currentGamma;
    private int start_counter;

    public TacticalScene(){
        active = false;
        init = false;
        index = 0;
        switchControl = false;
        count = 0.0f;
        cameraUpdate = false;
        aimDrawConfig = 0;
    }

    public void init(){
        Camera.getInstance().preset(20.0f,46.0f,-30.0f);
        start = true;
        finish = false;
        currentGamma = WindowConfig.getInstance().getGamma();
        start_counter = 0;
        if(init){
            generate(currentHeight);
        }else {

            shader3D = new Shader();
            shaderSprite = new Shader();

            cursorHud = new Cursor();
            graundAim = new GraundAim("./data/content/texture/zona/cursor.png");

            skybox = new Skybox();
            mousePicker = new MousePicker();

            shader3D.createVertexShader("VS_game_object.glsl");
            shader3D.createGeometryShader("GS_game_object.glsl");
            shader3D.createFragmentShader("FS_game_object.glsl");
            shader3D.createProgram();

            shaderSprite.createVertexShader("VS_sprite.glsl");
            shaderSprite.createGeometryShader("GS_sprite.glsl");
            shaderSprite.createFragmentShader("FS_sprite.glsl");
            shaderSprite.createProgram();

            cursorHud.init();
            graundAim.init();

            generate(currentHeight);
            skybox.init();

            init = true;
        }
    }

    @Override
    public void start(List<Scene> scenes){
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void reInit() {

    }

    @Override
    public void over(){
        active = false;
    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {
        this.currentHeight = currentHeight;
        this.allies = allies;
    }

    @Override
    public void update(){
        GameController.getInstance().update();
        Vector3f pixel = Pixel.getPixel();
        cursorHud.update(studyArea.getEnemies());

        if(finish){
            Window.getInstance().setGamma(Window.getInstance().getGamma() - 0.002f);
            if(Window.getInstance().getGamma() <= 0.0f) {
                Window.getInstance().setGamma(0.0f);
                cameraUpdate = false;
                SceneControl.setLastScene(this);
                for (Scene scene : scenes) {
                    if (scene.getSceneId() == Constants.SCENE_STRATEGIC) {
                        scene.preset(currentHeight, allies);
                        scene.start(scenes);
                    }
                }
                over();
                index++;
                if (index > 1) {
                    index = 0;
                }
            }
        }else {
            if (GameController.getInstance().isfClick()) {
                Default.setShowAlpha(!Default.isShowAlpha());
            }

            // Обновляем информацию в сетке
            for (int x = 0; x <= studyArea.getMapX(); x++) {
                for (int z = 0; z <= studyArea.getMapZ(); z++) {
                    if (!studyArea.getBattleGround().isActive()) {
                        studyArea.getGrid()[x][z].setOccupied(false);
                    }
                    if (studyArea.getGrid()[x][z].isVisible()) {
                        studyArea.getGrid()[x][z].update(pixel);
                    }
                }
            }

            if (!studyArea.getBattleGround().isActive()) {
                checkOccupied(studyArea.getAllies());
                checkOccupied(studyArea.getEnemies());
            }

            Cell targetElement = null;

            if (GameController.getInstance().isLeftClick()) {
                mousePicker.update(studyArea.getGrid());
                Vector3f endPos = mousePicker.getCurrentTerrainPoint();
                if (endPos.getX() < 0) endPos.setX(0);
                if (endPos.getX() > studyArea.getMapX()) endPos.setX(studyArea.getMapX());
                if (endPos.getZ() < 0) endPos.setZ(0);
                if (endPos.getZ() > studyArea.getMapZ()) endPos.setZ(studyArea.getMapZ());
                targetElement = studyArea.getGrid()[Math.round(endPos.getX())][Math.round(endPos.getZ())];
                if (targetElement.isBlocked() || targetElement.isOccupied()) {
                    targetElement = null;
                } else {
                    if (pixel.getX() == 0.0f && pixel.getY() == 0.0f && pixel.getZ() == 0.0f) {
                        graundAim.update(targetElement.getModifiedPosition(), targetElement.isBevel(), targetElement.getBevelAngle());
                        graundAim.setVisible(true);
                        aimDrawConfig = getAimDrawConfig();
                    }
                }
            }

            if (lastElement != null && targetElement != null) {
                if (lastElement.getId() == targetElement.getId()) {
                    targetElement = null;
                }
            }

            if (targetElement != null) {
                lastElement = targetElement;
            }

            if (studyArea.isWater()) {
                // обнавляем движение волн
                if (switchControl) {
                    count += 0.0005f;
                    Default.setOffset(count);
                    if (count >= 0.05f) {
                        switchControl = !switchControl;
                    }
                } else {
                    count -= 0.0005f;
                    Default.setOffset(count);
                    if (count <= -0.05f) {
                        switchControl = !switchControl;
                    }
                }
            }
            // обновляем все объекты сцены
            studyArea.update(targetElement, pixel);
            // обновляем камеру
            if (studyArea.getBattleGround().isActive()) { // ЕСЛИ БОЕВОЙ РЕЖИМ АКТИВИРОВАН!
                graundAim.setVisible(false);
                aimDrawConfig = 0;
                if (cameraUpdate) {
                    Camera.getInstance().update(studyArea.getBattleGround().getMinW(), studyArea.getBattleGround().getMaxW(),
                            studyArea.getBattleGround().getMinH(), studyArea.getBattleGround().getMaxH(), studyArea.getGrid());
                    if (Math.abs(lastCameraPos.sub(Camera.getInstance().getPos()).length()) > 2.0f) {
                        Camera.getInstance().setPos(lastCameraPos);
                    }
                } else {
                    if (Camera.getInstance().cameraMove(studyArea.getBattleGround().getLocalPoint(), 0.1f)) {
                        cameraUpdate = true;
                    } else {
                        Camera.getInstance().update(0.0f, studyArea.getMapX(), 0.0f, studyArea.getMapZ(), studyArea.getGrid());
                    }
                }
            } else { // ОБЫЧНЫЙ РЕЖИМ
                cameraUpdate = false;
                Camera.getInstance().update(0.0f, studyArea.getMapX(), 0.0f, studyArea.getMapZ(), studyArea.getGrid());
            }

            lastCameraPos = new Vector3f(Camera.getInstance().getPos());
            lastCameraFront = new Vector3f(Camera.getInstance().getFront());

            if(start){
                start_counter++;
                if(start_counter > 500) {
                    Window.getInstance().setGamma(Window.getInstance().getGamma() + 0.002f);
                    if (Window.getInstance().getGamma() >= currentGamma) {
                        Window.getInstance().setGamma(currentGamma);
                        start = false;
                    }
                }
            }else {
                // TЕСТОВЫЙ ТРИГЕР - НАЧАЛО
                if (GameController.getInstance().isSpaceClick()) {
                    finish = true;
                }
                // ТЕСТОВЫЙ ТРИГЕР - КОНЕЦ
            }
        }
    }

    private void checkOccupied(List<Character> characters){
        for (Character person : characters) {
            if((person.getPosition().equals(new Vector3f(graundAim.getX(),0.0f,graundAim.getZ()))) && person.getRecognition() == ALLY){
                graundAim.setVisible(false);
                aimDrawConfig = 0;
            }
            if (studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().equals(person.getPosition())) {
                studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].setOccupied(true);
            } else {
                float size = 0.5f;
                float personMinX = person.getPosition().getX() - size;
                float personMaxX = person.getPosition().getX() + size;
                float personMinZ = person.getPosition().getZ() - size;
                float personMaxZ = person.getPosition().getZ() + size;

                float elementMinX = studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().getX() - size;
                float elementMaxX = studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().getX() + size;
                float elementMinZ = studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().getZ() - size;
                float elementMaxZ = studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().getZ() + size;

                if (person.getPosition().getX() == studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().getX()) {
                    if (elementMinZ < personMinZ && personMinZ < elementMaxZ) {
                        studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].setOccupied(true);
                        //element.setVisible(true);
                    }
                    if (elementMinZ < personMaxZ && personMaxZ < elementMaxZ) {
                        studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].setOccupied(true);
                        //element.setVisible(true);
                    }
                }
                if (person.getPosition().getZ() == studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].getPosition().getZ()) {
                    if (elementMinX < personMinX && personMinX < elementMaxX) {
                        studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].setOccupied(true);
                        //element.setVisible(true);
                    }
                    if (elementMinX < personMaxX && personMaxX < elementMaxX) {
                        studyArea.getGrid()[Math.round(person.getPosition().getX())][Math.round(person.getPosition().getZ())].setOccupied(true);
                        //element.setVisible(true);
                    }
                }
            }
        }
    }

    @Override
    public void draw(){

        boolean battle = studyArea.getBattleGround().isActive();
        // рисуем поле и сетку
        shader3D.setUniform("currentHeight",currentHeight);
        shader3D.setUniform("useMap",1);
        studyArea.draw(shader3D);

        skybox.draw();

        shaderSprite.useProgram();
        shaderSprite.setUniformBlock("matrices", 0);
        shaderSprite.setUniform("w",studyArea.getMapX());
        shaderSprite.setUniform("h",studyArea.getMapZ());
        shaderSprite.setUniform("useMap",1);
        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, Default.getMapTextureId());
        shaderSprite.setUniform("map", 5);
        for(int i=0; i<6; i++){
            if(i < studyArea.getAllies().size()){
                Vector3f position = new Vector3f(studyArea.getAllies().get(i).getPosition());
                float distance = studyArea.getAllies().get(i).getCharacteristic().getVision();
                Vector4f unit = new Vector4f(position.getX(),position.getY(),position.getZ(),distance);
                shaderSprite.setUniform("unit" + i, unit);
            }else{
                shaderSprite.setUniform("unit" + i, new Vector4f(-1.0f,-1.0f,-1.0f,1.0f));
            }
        }
        // рисуем спрайты и воду
        drawElement(battle,first); // 1
        if(aimDrawConfig == 1){
            graundAim.draw(shaderSprite);
        }
        studyArea.drawPersons(shaderSprite);
        studyArea.drawSprites(shaderSprite);
        if(!exception.isEmpty()){
            drawElement(battle,exception); // 2
        }
        if(aimDrawConfig == 2){
            graundAim.draw(shaderSprite);
        }
        studyArea.drawWater(shaderSprite);
        drawElement(battle,second); // 3
        if(aimDrawConfig == 3){
            graundAim.draw(shaderSprite);
        }

        cursorHud.draw();
    }

    private void drawElement(boolean battle, List<Cell> elements){
        if(battle){
            for (Cell element : elements) {
                /*shaderSprite.setUniform("battlefield",studyArea.getBattleGround().isActive() ? 1 : 0);
                shaderSprite.setUniform("localPoint",studyArea.getBattleGround().getLocalPoint());
                shaderSprite.setUniform("radius",studyArea.getBattleGround().getRADIUS());*/
                element.draw(shaderSprite);
            }
        }
    }

    @Override
    public void draw(Shader shader, boolean isShadow){
        if(isShadow) {
            studyArea.draw(shader);
            studyArea.drawShadowSprites(shader);
            studyArea.drawShadowPersons(shader, true);
        }else{
            studyArea.draw(shader);
        }
    }

    @Override
    public int getSceneId(){
        return Constants.SCENE_TACTICAL;
    }

    @Override
    public boolean isActive(){
        return active;
    }

    @Override
    public List<Light> getLights() {
        return studyArea.getDirectLight();
    }

    @Override
    public Shader getShader() {
        return shader3D;
    }

    @Override
    public void setCurrentInnerScene(int currentInnerScene) {

    }

    private void generate(float currentHeight){
        Default.setMapFrameStart(false);
        aimDrawConfig = 0;
        graundAim.setVisible(false);
        studyArea = Generator.getRandomArea(currentHeight,(int)(49.0f + (float)Math.random() * 49.0f),(int)(49.0f + (float)Math.random() * 49.0f));
        // ALLIES - ВРЕМЕННО!
        Vector3f position;
        Vector3f lagerPoint = studyArea.getGrid()[studyArea.getGrid().length / 2][studyArea.getGrid()[0].length - 2].getModifiedPosition();
        Vector3f cameraLook = new Vector3f(lagerPoint);
        for(Character character : allies){
            character.preset();
            position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
            character.setLagerPoint(lagerPoint);
            character.setPosition(position);
            studyArea.getAllies().add(character);
        }
        // ALLIES - ВРЕМЕННО!

        // ENEMIES - НАЧАЛО
        float id = 0.2f;
        int percent = studyArea.getMapX() + studyArea.getMapZ();
        int amount = Math.round(2.0f + (float)Math.random()) * percent / 100;
        System.out.println("Число групп противников: " + amount);
        for(int i=0; i<amount; i++) {
            lagerPoint = Generator.getRandomPos(studyArea.getGrid(),true);
            int count = Math.round(3.0f + (float)Math.random() * 3.0f);
            for (int j = 0; j < count; j++) {
                int coin = (int) Math.round(Math.random() * 2.0f);
                if (coin == 0) {
                    id += 0.01f;
                    position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
                    Character character = new AnarchyThief(Default.getAnarchyThief(), position, lagerPoint, id, ENEMY);
                    character.setDefaultCharacteristic();
                    studyArea.getEnemies().add(character);
                } else if(coin == 1) {
                    id += 0.01f;
                    position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
                    Character character = new AnarchyBandit(Default.getAnarchyBandit(), position, lagerPoint, id, ENEMY);
                    character.setDefaultCharacteristic();
                    studyArea.getEnemies().add(character);
                } else if(coin == 2){
                    id += 0.01f;
                    position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
                    Character character = new AnarchyArcher(Default.getAnarchyArcher(), position, lagerPoint, id, ENEMY);
                    character.setDefaultCharacteristic();
                    studyArea.getEnemies().add(character);
                }
            }
        }
        // ENEMIES - КОНЕЦ

        first = new ArrayList<>();
        second = new ArrayList<>();
        exception = new ArrayList<>();
        for(int x=0; x<studyArea.getGrid().length; x++){
            for(int z=0; z<studyArea.getGrid()[0].length; z++){
                if(studyArea.getGrid()[x][z].getCurrentHeight() == studyArea.getGrid()[x][z].getPosition().getY()){
                    first.add(studyArea.getGrid()[x][z]);
                }else{
                    if(studyArea.isWater()){
                        if(studyArea.getGrid()[x][z].getCurrentHeight() <= -0.75f){
                            exception.add(studyArea.getGrid()[x][z]);
                        }else{
                            second.add(studyArea.getGrid()[x][z]);
                        }
                    }else {
                        second.add(studyArea.getGrid()[x][z]);
                    }
                }
            }
        }

        Vector3f reverse = new Vector3f(Camera.getInstance().getFront()).mul(-1.0f);
        Camera.getInstance().setPos(cameraLook.add(reverse.mul(Camera.getInstance().getHypotenuse())));

        // КОНЕЦ ГЕНЕРАЦИИ
    }

    private int getAimDrawConfig(){
        int config = 0;

        if(studyArea.getGrid()[graundAim.getX()][graundAim.getZ()].getCurrentHeight() == studyArea.getGrid()[graundAim.getX()][graundAim.getZ()].getPosition().getY()){
            config = 1;
        }else{
            if(studyArea.isWater()){
                if(studyArea.getGrid()[graundAim.getX()][graundAim.getZ()].getCurrentHeight() <= -0.75f){
                    config = 2;
                }else{
                    config = 3;
                }
            }else {
                config = 3;
            }
        }

        return config;
    }
}