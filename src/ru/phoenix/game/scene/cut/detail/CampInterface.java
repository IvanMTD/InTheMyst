package ru.phoenix.game.scene.cut.detail;

import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Button;

import java.util.ArrayList;
import java.util.List;

public class CampInterface {

    public final float NO_BUTTON = -1.0f;
    public final float NEXT_SCENE = 0.5f;

    private SavePad savePad;

    private float currentButton;

    private Shader shader;

    private List<HeadsUpDisplay> huds;

    public CampInterface(){
        currentButton = NO_BUTTON;
        setupShader();
        setupHuds();
    }

    private void setupShader(){
        shader = new Shader();
        shader.createVertexShader("VS_hud.glsl");
        shader.createFragmentShader("FS_hud.glsl");
        shader.createProgram();
    }

    private void setupHuds(){
        huds = new ArrayList<>();
        savePad = new SavePad();
        setupButtons();
    }

    private void setupButtons(){
        float originW = CutSceneTexture.getInstance().getMapButton().getWidth();
        float originH = CutSceneTexture.getInstance().getMapButton().getHeight();
        float w = Window.getInstance().getWidth() / 5.0f;
        float h = originH * w / originW;
        float x = Window.getInstance().getWidth() - (w / 2) - 10;
        float y = Window.getInstance().getHeight() - (h / 2) - 10;
        HeadsUpDisplay mapButton = new Button(CutSceneTexture.getInstance().getMapButton(),w,h, new Vector3f(x,y,-0.05f),true,NEXT_SCENE);
        mapButton.update(new Vector3f());
        huds.add(mapButton);
    }

    public void update(Vector3f pixel, boolean leftClick, SaveData saveData, List<Character> allies){
        currentButton = NO_BUTTON;
        for(HeadsUpDisplay hud : huds){
            hud.update(pixel);
            if(hud.isTarget() && leftClick){
                if(hud.getId() == NEXT_SCENE){
                    hud.selected();
                    currentButton = NEXT_SCENE;
                }
            }
        }
        savePad.update(pixel,saveData,allies);
    }

    public void draw(){
        shader.useProgram();
        for(HeadsUpDisplay hud : huds){
            hud.draw(shader);
        }
        savePad.draw(shader);
    }

    public void drawText(Shader shader){
        savePad.drawText(shader);
    }

    public void setSavePadHide(boolean hide){
        savePad.setHide(hide);
    }

    public float getCurrentButton() {
        return currentButton;
    }
}
