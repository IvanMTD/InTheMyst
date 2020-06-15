package ru.phoenix.game.scene.cut;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Camera;
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
import ru.phoenix.game.property.GameController;
import ru.phoenix.game.scene.Scene;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.ENEMY;
import static ru.phoenix.core.config.Constants.SCENE_CUT;

public class CutScene implements Scene {

    public static final int FIRST_INNER_SCENE = 0x15111;

    private int currentInnerScene;

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
    }

    public void init(){
        Camera.getInstance().preset(20.0f,46.0f,-30.0f);
        if(init){
            generate();
        }else{
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
        mainCharacter.setRecognition(ENEMY);
        mainCharacter.preset();
        mainCharacter.setDefaultCharacteristic();
        allies.add(mainCharacter);


        Character character = new CommunisPartisan();
        character.setRecognition(ENEMY);
        character.preset();
        character.setDefaultCharacteristic();
        allies.add(character);

        character = new CommunisArcher();
        character.setRecognition(ENEMY);
        character.preset();
        character.setDefaultCharacteristic();
        allies.add(character);

        float id = 0.12f;
        Vector3f position = new Vector3f();
        Vector3f lagerPoint = studyArea.getGrid()[studyArea.getGrid().length / 2][studyArea.getGrid()[0].length / 2].getModifiedPosition();
        Vector3f cameraLook = new Vector3f(lagerPoint);
        for(Character c : allies){
            position = Generator.getRandomPos(studyArea.getGrid(), lagerPoint, 5.0f, true);
            c.setId(id);
            c.setLagerPoint(lagerPoint);
            c.setPosition(position);
            id += 0.01f;
            studyArea.getAllies().add(c);
        }
        Vector3f reverse = new Vector3f(Camera.getInstance().getFront()).mul(-1.0f);
        Camera.getInstance().setPos(cameraLook.add(reverse.mul(Camera.getInstance().getHypotenuse())));
        Camera.getInstance().updateViewMatrix();
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
        GameController.getInstance().update();
        Camera.getInstance().update(0.0f,w,0.0f,h,studyArea.getGrid());
        cursor.update(null);
        Vector3f pixel = Pixel.getPixel();

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
}
