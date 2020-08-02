package ru.phoenix.game.scene.cut;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.block.Block;
import ru.phoenix.game.content.block.type.model.CampFire;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisPartisan;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;
import ru.phoenix.game.content.object.Object;
import ru.phoenix.game.content.object.fire.SimpleFire;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.datafile.PersonStruct;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.datafile.TimeElement;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.property.GameController;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.Scene;
import ru.phoenix.game.scene.cut.detail.CampInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static ru.phoenix.core.config.Constants.*;

public class CutScene implements Scene {

    public static final int FIRST_INNER_SCENE = 0x15111;
    public static final int CAMP_SCENE        = 0x15112;

    // блок сохранения игры
    private SaveData saveData;

    private int currentInnerScene;
    private int nextScene;

    private int w;
    private int h;
    private int r;
    private int x;
    private int z;

    private float lastTime;
    private float time;

    private boolean switchControl;
    private float count;

    private List<Scene> scenes;
    private List<Character> allies;

    private Shader shader3D;
    private Shader shaderSprite;

    private Skybox skybox;
    private StudyArea studyArea;
    private Cursor cursor;

    private float currentHeight;
    private boolean active;
    private boolean init;

    private float tempGamma;

    // stage controlls
    private int stage_index;
    private int special_index;
    private int special_counter;
    private boolean stage_first_start;

    private Vector3f camera_point;
    private Vector3f gehard_point;
    private Vector3f swordman_point;
    private Vector3f archer_point;

    private Character gehard;
    private Character swordman;
    private Character archer;

    private CampInterface campInterface;

    public CutScene(){

        time = 0.0f;

        w = 20;
        h = 20;
        r = 9;

        switchControl = false;
        count = 0.0f;

        active = false;
        init = false;
        scenes = new ArrayList<>();
        allies = new ArrayList<>();

        // stage controlls
        stage_index = 0;
        stage_first_start = true;
        special_index = 0;
        special_counter = 0;
    }

    public void init(){
        Camera.getInstance().preset(20.0f,46.0f,-30.0f);
        if(init){
            tempGamma = WindowConfig.getInstance().getGamma();
            generate();
            studyArea.getAllies().addAll(allies);
        }else{
            // Atention
            shader3D = new Shader();
            shader3D.createVertexShader("VS_game_object.glsl");
            shader3D.createGeometryShader("GS_game_object.glsl");
            shader3D.createFragmentShader("FS_game_object.glsl");
            shader3D.createProgram();

            shaderSprite = new Shader();
            shaderSprite.createVertexShader("VS_sprite.glsl");
            shaderSprite.createGeometryShader("GS_sprite.glsl");
            shaderSprite.createFragmentShader("FS_sprite.glsl");
            shaderSprite.createProgram();

            skybox = new Skybox();
            skybox.init();

            cursor = new Cursor();
            cursor.init();

            campInterface = new CampInterface();

            tempGamma = WindowConfig.getInstance().getGamma();
            if(Default.getCurrentData() != null){
                currentInnerScene = CAMP_SCENE;
                currentHeight = Default.getCurrentData().getCurrentBiom();
                load();
                loadAllies();
            }else {
                currentInnerScene = FIRST_INNER_SCENE;
                currentHeight = 25.0f;
                generate();
                createAllies();
            }
            init = true;
        }
    }

    private void createAllies() {
        Character mainCharacter = new Gehard();
        mainCharacter.setRecognition(ALLY);
        mainCharacter.preset();
        mainCharacter.setDefaultCharacteristic();
        allies.add(mainCharacter);


        Character character = new CommunisPartisan();
        character.setRecognition(ALLY);
        character.preset();
        character.setDefaultCharacteristic();
        allies.add(character);

        character = new CommunisArcher();
        character.setRecognition(ALLY);
        character.preset();
        character.setDefaultCharacteristic();
        allies.add(character);

        float id = 0.12f;
        Vector3f position = new Vector3f();
        Vector3f lagerPoint = studyArea.getGrid()[studyArea.getGrid().length / 2][studyArea.getGrid()[0].length / 2].getModifiedPosition();
        for (Character c : allies) {
            position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
            c.setId(id);
            c.setLagerPoint(lagerPoint);
            c.setPosition(position);
            id += 0.01f;
            studyArea.getAllies().add(c);
        }
    }

    private void loadAllies(){
        for(PersonStruct personStruct : Default.getCurrentData().getPersonStructs()){
            if(personStruct.getType() == Constants.C_GEHARD){
                Character character = new Gehard();
                character.setRecognition(ALLY);
                character.preset();
                character.setCharacteristic(personStruct);
                allies.add(character);
            }else if(personStruct.getType() == Constants.C_1T_PARTISAN){
                Character character = new CommunisPartisan();
                character.setRecognition(ALLY);
                character.preset();
                character.setCharacteristic(personStruct);
                allies.add(character);
            }else if(personStruct.getType() == Constants.C_1T_ARCHER){
                Character character = new CommunisArcher();
                character.setRecognition(ALLY);
                character.preset();
                character.setCharacteristic(personStruct);
                allies.add(character);
            }
        }

        float id = 0.12f;
        Vector3f position = new Vector3f();
        Vector3f lagerPoint = studyArea.getGrid()[studyArea.getGrid().length / 2][studyArea.getGrid()[0].length / 2].getModifiedPosition();
        for (Character c : allies) {
            position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
            c.setId(id);
            c.setLagerPoint(lagerPoint);
            c.setPosition(position);
            id += 0.01f;
            studyArea.getAllies().add(c);
        }
    }

    private void generate(){
        saveData = new SaveData();
        Default.setMapFrameStart(false);
        studyArea = Generator.getCutArea(currentHeight,w,h,r,saveData);
        if(currentInnerScene == CAMP_SCENE){
            Block campFire = new CampFire(0.1f);
            x = w / 2;
            z = h / 2;
            Vector3f point = findFirePlace(x,z);
            studyArea.getGrid()[(int)point.getX()][(int)point.getZ()].setBlocked(true);
            campFire.setPosition(point);
            campFire.setMeshSize(1.5f);
            campFire.updateProjection();
            studyArea.getBlocks().add(campFire);
            Object fire = new SimpleFire();
            fire.init(null);
            fire.setPosition(new Vector3f(point.add(new Vector3f(0.0f,0.2f,0.0f))));
            studyArea.getSprites().add(fire);
        }
    }

    private void load(){
        Default.setMapFrameStart(false);
        studyArea = Generator.loadCutArea(Default.getCurrentData().getCampLocation(),Default.getSlot());
        if(currentInnerScene == CAMP_SCENE){
            Block campFire = new CampFire(0.1f);
            x = w / 2;
            z = h / 2;
            Vector3f point = findFirePlace(x,z);
            studyArea.getGrid()[(int)point.getX()][(int)point.getZ()].setBlocked(true);
            campFire.setPosition(point);
            campFire.setMeshSize(1.5f);
            campFire.updateProjection();
            studyArea.getBlocks().add(campFire);
            Object fire = new SimpleFire();
            fire.init(null);
            fire.setPosition(new Vector3f(point.add(new Vector3f(0.0f,0.2f,0.0f))));
            studyArea.getSprites().add(fire);
        }
    }

    private Vector3f findFirePlace(int x, int z){
        if(studyArea.getGrid()[x][z].isGrass()){
            int newX = Math.round((float)(this.x-3) + ((float)Math.random() * 6.0f));
            int newZ = Math.round((float)(this.z-3) + ((float)Math.random() * 6.0f));
            return findFirePlace(newX,newZ);
        }else{
            return new Vector3f(studyArea.getGrid()[x][z].getModifiedPosition());
        }
    }


    @Override
    public void start(List<Scene> scenes) {
        this.scenes = scenes;
        active = true;
        init();
    }

    @Override
    public void reInit() {

    }

    @Override
    public void over() {
        active = false;
    }

    @Override
    public void preset(float currentHeight, List<Character> allies) {
        this.currentHeight = currentHeight;
        this.allies = allies;
    }

    @Override
    public void update() {
        Vector3f pixel = Pixel.getPixel();
        GameController.getInstance().update();
        cursor.update(null);

        if(studyArea.isWater()) {
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

        if(currentInnerScene == FIRST_INNER_SCENE){
            pixel = new Vector3f();
            Camera.getInstance().setCameraControlLock(true);
            Camera.getInstance().update(0.0f,w,0.0f,h,studyArea.getGrid());
            switch (stage_index){
                case 0:
                    if(stage_first_start){
                        Window.getInstance().setGamma(0.0f);
                        stage_first_start = false;
                        gehard = allies.get(0);
                        gehard.setPosition(studyArea.getGrid()[studyArea.getGrid().length - 1][studyArea.getGrid()[0].length - 2].getModifiedPosition());
                        swordman = allies.get(1);
                        swordman.setPosition(studyArea.getGrid()[0][studyArea.getGrid()[0].length - 1].getModifiedPosition());
                        archer = allies.get(2);
                        archer.setPosition(studyArea.getGrid()[0][studyArea.getGrid()[0].length - 3].getModifiedPosition());

                        Vector3f cameraLook = new Vector3f(studyArea.getGrid()[studyArea.getGrid().length / 2][0].getModifiedPosition());
                        Vector3f reverse = new Vector3f(Camera.getInstance().getFront()).mul(-1.0f);
                        Camera.getInstance().setPos(cameraLook.add(reverse.mul(Camera.getInstance().getHypotenuse())));
                        Camera.getInstance().updateViewMatrix();
                        Camera.getInstance().update(0.0f,w,0.0f,h,studyArea.getGrid());

                        camera_point = new Vector3f(studyArea.getGrid()[studyArea.getGrid().length / 2][studyArea.getGrid()[0].length - 2].getModifiedPosition());
                        gehard_point = new Vector3f(studyArea.getGrid()[11][19].getModifiedPosition());
                        swordman_point = new Vector3f(studyArea.getGrid()[9][20].getModifiedPosition());
                        archer_point = new Vector3f(studyArea.getGrid()[9][18].getModifiedPosition());
                    }else {
                        Window.getInstance().setGamma(Window.getInstance().getGamma() + 0.002f);
                        if(Window.getInstance().getGamma() >= tempGamma){
                            Window.getInstance().setGamma(tempGamma);
                            stage_index++;
                            stage_first_start = true;
                        }
                    }
                    break;
                case 1:
                    if(Camera.getInstance().cameraMove(camera_point,0.025f)){
                        gehard.setTargetPoint(studyArea.getGrid()[(int)gehard_point.getX()][(int)gehard_point.getZ()]);
                        swordman.setTargetPoint(studyArea.getGrid()[(int)swordman_point.getX()][(int)swordman_point.getZ()]);
                        archer.setTargetPoint(studyArea.getGrid()[(int)archer_point.getX()][(int)archer_point.getZ()]);
                        stage_index++;
                    }
                    break;
                case 2:
                    if(special_index == 0){
                        special_counter++;
                        if(special_counter > 500){
                            special_counter = 0;
                            special_index++;
                        }
                    }else if(special_index == 1){
                        gehard.setText("It's time! Let's go ...");
                        special_counter++;
                        if(special_counter > 700){
                            special_counter = 0;
                            special_index++;
                            gehard.clearText();
                        }
                    }else if(special_index == 2){
                        swordman.setText("...");
                        archer.setText("Let's do that!");
                        special_counter++;
                        if(special_counter > 500){
                            special_counter = 0;
                            special_index = 0;
                            swordman.clearText();
                            archer.clearText();
                            camera_point = new Vector3f(studyArea.getGrid()[0][19].getModifiedPosition());
                            gehard_point = new Vector3f(studyArea.getGrid()[20][19].getModifiedPosition());
                            swordman_point = new Vector3f(studyArea.getGrid()[20][20].getModifiedPosition());
                            archer_point = new Vector3f(studyArea.getGrid()[20][18].getModifiedPosition());
                            gehard.setTargetPoint(studyArea.getGrid()[(int)gehard_point.getX()][(int)gehard_point.getZ()]);
                            swordman.setTargetPoint(studyArea.getGrid()[(int)swordman_point.getX()][(int)swordman_point.getZ()]);
                            archer.setTargetPoint(studyArea.getGrid()[(int)archer_point.getX()][(int)archer_point.getZ()]);
                            stage_index++;
                        }
                    }
                    break;
                case 3:
                    Window.getInstance().setGamma(Window.getInstance().getGamma() - 0.003f);
                    Camera.getInstance().cameraMove(camera_point,0.015f);
                    if(Window.getInstance().getGamma() < 0.0f){
                        Window.getInstance().setGamma(0.0f);
                        nextScene = SCENE_STRATEGIC;
                        stage_index++;
                    }
                    break;
                case 4:
                    stage_index = 0;
                    nextScene();
                    break;
            }
            Camera.getInstance().setCameraControlLock(false);
        }else if(currentInnerScene == CAMP_SCENE){
            switch (stage_index){
                case 0:
                    lastTime = (float)glfwGetTime();
                    time = 0.0f;
                    if(stage_first_start){
                        Vector3f campPoint = new Vector3f(studyArea.getGrid()[w/2][h/2].getModifiedPosition());
                        for(Character ally : allies){
                            ally.preset();
                            ally.setLagerPoint(campPoint);
                            ally.setPosition(Generator.getRandomPos(studyArea.getGrid(),campPoint,5.0f,true));
                            ally.setRecognition(ENEMY);
                        }

                        Vector3f cameraLook = new Vector3f(campPoint);
                        Vector3f reverse = new Vector3f(Camera.getInstance().getFront()).mul(-1.0f);
                        Camera.getInstance().setPos(cameraLook.add(reverse.mul(Camera.getInstance().getHypotenuse())));
                        Camera.getInstance().updateViewMatrix();
                        Camera.getInstance().update(0.0f,w,0.0f,h,studyArea.getGrid());
                        stage_first_start = false;
                    }else{
                        Window.getInstance().setGamma(Window.getInstance().getGamma() + 0.002f);
                        if(Window.getInstance().getGamma() > tempGamma) {
                            Window.getInstance().setGamma(tempGamma);
                            stage_first_start = true;
                            stage_index++;
                        }
                    }
                    break;
                case 1:
                    Camera.getInstance().update(0.0f,w,0.0f,h,studyArea.getGrid());
                    campInterface.update(pixel,GameController.getInstance().isLeftClick());
                    float currentTime = (float)glfwGetTime();
                    float delta = currentTime - lastTime;
                    lastTime = currentTime;
                    time += delta;
                    if(time > 1.0f){
                        for(Character ally : allies){
                            ally.getCharacteristic().setHealthCharge(10);
                            ally.getCharacteristic().setMannaCharge(10);
                            ally.getCharacteristic().updateIndicators();
                            ally.getCharacteristic().setHealthCharge(0);
                            ally.getCharacteristic().setMannaCharge(0);
                        }
                        time = 0.0f;
                    }

                    if(Default.isCampFireOn()){
                        campInterface.setSavePadHide(false);
                        /*int d = Time.getDay();
                        int h = Time.getHour();
                        int m = Time.getMinut();
                        float s = Time.getSecond();
                        TimeElement time = new TimeElement(d,h,m,s);
                        Default.getCurrentData().setTime(time);
                        Default.getCurrentData().setCampLocation(saveData);
                        List<PersonStruct>personStructList = new ArrayList<>();
                        for(Character character : allies){
                            PersonStruct personStruct = new PersonStruct();
                            personStruct.setType(character.getType());
                            personStruct.setCharacteristic(character.getCharacteristic());
                            personStructList.add(personStruct);
                        }
                        Default.getCurrentData().setPersonStructs(personStructList);
                        Texture texture = Generator.getHeightMap();
                        texture.saveImage("cutSceneMap1.png");

                        File fileDirect = new File("./data/save/data");
                        File saveFile = new File(fileDirect,"saveGame1.ser");
                        if(saveFile.exists()){
                            if(saveFile.delete()){
                                FileOutputStream fileOutputStream = null;
                                try {
                                    fileOutputStream = new FileOutputStream("./data/save/data/saveGame1.ser");
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                    objectOutputStream.writeObject(Default.getCurrentData());
                                    objectOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            FileOutputStream fileOutputStream = null;
                            try {
                                fileOutputStream = new FileOutputStream("./data/save/data/saveGame1.ser");
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                objectOutputStream.writeObject(Default.getCurrentData());
                                objectOutputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }*/

                        Default.setCampFireOn(false);
                    }

                    if(campInterface.getCurrentButton() == campInterface.NEXT_SCENE){
                        stage_index++;
                    }
                    break;
                case 2:
                    campInterface.update(new Vector3f(),false);
                    Window.getInstance().setGamma(Window.getInstance().getGamma() - 0.003f);
                    //Camera.getInstance().cameraMove(camera_point,0.015f);
                    if(Window.getInstance().getGamma() < 0.0f){
                        Window.getInstance().setGamma(0.0f);
                        nextScene = SCENE_TACTICAL;
                        stage_index++;
                    }
                    break;
                case 3:
                    for(Character ally : allies){
                        ally.preset();
                        ally.setRecognition(ALLY);
                    }
                    stage_index = 0;
                    nextScene();
                    break;
            }
        }else{
            Camera.getInstance().update(0.0f,w,0.0f,h,studyArea.getGrid());
        }
        studyArea.update(null,pixel);
    }

    @Override
    public void draw() {
        shader3D.setUniform("currentHeight",currentHeight);
        shader3D.setUniform("useMap",0);
        studyArea.draw(shader3D);

        skybox.draw();

        shaderSprite.useProgram();
        shaderSprite.setUniformBlock("matrices", 0);
        shaderSprite.setUniform("w",studyArea.getMapX());
        shaderSprite.setUniform("h",studyArea.getMapZ());
        shaderSprite.setUniform("useMap",0);
        studyArea.drawPersons(shaderSprite);
        studyArea.drawSprites(shaderSprite);
        studyArea.drawWater(shaderSprite);

        if(currentInnerScene == CAMP_SCENE){
            campInterface.draw();
        }

        TextDisplay.getInstance().getShader().useProgram();
        studyArea.drawPersonsText(TextDisplay.getInstance().getShader());
        if(currentInnerScene == CAMP_SCENE){
            campInterface.drawText(TextDisplay.getInstance().getShader());
        }

        cursor.draw();
    }

    @Override
    public void draw(Shader shader, boolean isShadow) {
        if(isShadow) {
            studyArea.draw(shader);
            studyArea.drawShadowSprites(shader);
            studyArea.drawShadowPersons(shader, true);
        }else{
            studyArea.draw(shader);
        }
    }

    @Override
    public int getSceneId() {
        return SCENE_CUT;
    }

    @Override
    public boolean isActive() {
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
        this.currentInnerScene = currentInnerScene;
    }

    private void nextScene(){
        SceneControl.setLastScene(this);
        for (Scene scene : scenes) {
            if (scene.getSceneId() == nextScene) {
                scene.preset(currentHeight,allies);
                scene.start(scenes);
            }
        }
        over();
    }
}
