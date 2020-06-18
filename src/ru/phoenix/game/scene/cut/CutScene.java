package ru.phoenix.game.scene.cut;

import ru.phoenix.core.config.Constants;
import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Skybox;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisArcher;
import ru.phoenix.game.content.characters.humans.communis.grade.first.CommunisPartisan;
import ru.phoenix.game.content.characters.humans.communis.hero.Gehard;
import ru.phoenix.game.content.stage.StudyArea;
import ru.phoenix.game.hud.assembled.Cursor;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.logic.lighting.Light;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.property.GameController;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.ALLY;
import static ru.phoenix.core.config.Constants.SCENE_CUT;
import static ru.phoenix.core.config.Constants.SCENE_STRATEGIC;

public class CutScene implements Scene {

    public static final int FIRST_INNER_SCENE = 0x15111;

    private int currentInnerScene;
    private int nextScene;

    private int w;
    private int h;
    private int r;

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

    public CutScene(){
        w = 20;
        h = 20;
        r = 9;

        switchControl = false;
        count = 0.0f;

        currentHeight = 25.0f;
        active = false;
        init = false;
        scenes = new ArrayList<>();
        allies = new ArrayList<>();

        tempGamma = WindowConfig.getInstance().getGamma();

        // stage controlls
        stage_index = 0;
        stage_first_start = true;
        special_index = 0;
        special_counter = 0;
    }

    public void init(){
        Camera.getInstance().preset(20.0f,46.0f,-30.0f);
        if(init){
            generate();
        }else{
            currentInnerScene = FIRST_INNER_SCENE;
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

            generate();
            createAllies();

            init = true;
        }
    }

    private void createAllies(){
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
        for(Character c : allies){
            position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
            c.setId(id);
            c.setLagerPoint(lagerPoint);
            c.setPosition(position);
            id += 0.01f;
            studyArea.getAllies().add(c);
        }
    }

    private void generate(){
        Default.setMapFrameStart(false);
        studyArea = Generator.getCutArea(currentHeight,w,h,r);
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
        for(int i=0; i<allies.size(); i++){
            if(allies.get(i).isDead()){
                allies.remove(i);
                i--;
            }
        }
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

        TextDisplay.getInstance().getShader().useProgram();
        studyArea.drawPersonsText(TextDisplay.getInstance().getShader());

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
