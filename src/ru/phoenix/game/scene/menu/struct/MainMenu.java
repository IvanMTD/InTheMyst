package ru.phoenix.game.scene.menu.struct;

import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Board;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.scene.menu.storage.MainMenuTextures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenu {
    public static final float NEW_GAME_BUTTON   = 0.11f;
    public static final float LOADING_BUTTON    = 0.12f;
    public static final float SETTINGS_BUTTON   = 0.13f;
    public static final float NO_ACTION         = -1.0f;
    private float action;

    // массивы
    private List<HeadsUpDisplay> huds;
    // неактивные элементы
    private HeadsUpDisplay background;
    private HeadsUpDisplay title;
    private HeadsUpDisplay stick;
    // активные элементы
    private HeadsUpDisplay newGame;
    private HeadsUpDisplay loading;
    private HeadsUpDisplay settings;

    public MainMenu() {
        action = NO_ACTION;
        initHudElements();
    }

    private void initHudElements(){
        huds = new ArrayList<>();
        setupBackground();
        setupTitle();
        setupStick();
        setupButtons();
        huds.addAll(Arrays.asList(background,title,stick,newGame,loading,settings));
    }

    private void setupBackground(){
        float x = Window.getInstance().getWidth() / 2.0f;
        float y = Window.getInstance().getHeight() / 2.0f;
        float z = -1.0f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 320.0f;
        float height = Window.getInstance().getHeight();
        background = new Board(MainMenuTextures.getInstance().getBackground(),width,height,position,false);
    }

    private void setupTitle(){
        float x = Window.getInstance().getWidth() / 2.0f;
        float y = 125.0f;
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 420.0f;
        float proportion = MainMenuTextures.getInstance().getTitle().getWidth() / MainMenuTextures.getInstance().getTitle().getHeight();
        float height = width / proportion;
        title = new Board(MainMenuTextures.getInstance().getTitle(),width,height,position,true);
    }

    private void setupStick(){
        float x = Window.getInstance().getWidth() / 2.0f;
        float diff = Window.getInstance().getHeight() - 480;
        float percent = diff * 100.0f / 600.0f;
        float offset = 100.0f + (50.0f * percent / 100.0f);
        float y = Window.getInstance().getHeight() - offset;
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 300.0f;
        float height = MainMenuTextures.getInstance().getStick().getHeight() * width / MainMenuTextures.getInstance().getStick().getWidth();
        stick = new Board(MainMenuTextures.getInstance().getStick(),width,height,position,true);
    }

    private void setupButtons(){
        float x = Window.getInstance().getWidth() / 2.0f;
        float diff = Window.getInstance().getHeight() - 480;
        float percent = diff * 100.0f / 600.0f;
        float offset = 100.0f + (50.0f * percent / 100.0f);
        float y = Window.getInstance().getHeight() - offset;
        float z = -0.08f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 300.0f;
        float height = MainMenuTextures.getInstance().getStick().getHeight() * width / MainMenuTextures.getInstance().getStick().getWidth();
        newGame = new Button(MainMenuTextures.getInstance().getButtonNewGame(),width,height,position,true,NEW_GAME_BUTTON);
        loading = new Button(MainMenuTextures.getInstance().getButtonLoading(),width,height,position,true,LOADING_BUTTON);
        settings = new Button(MainMenuTextures.getInstance().getButtonSettings(),width,height,position,true,SETTINGS_BUTTON);
    }

    public float getAction() {
        return action;
    }

    public void update(Vector3f pixel, boolean leftClick){
        action = NO_ACTION;
        for(HeadsUpDisplay hud : huds){
            hud.update(pixel);
            if(leftClick && hud.isTarget()){
                hud.selected();
                action = hud.getId();
            }
        }
    }

    public void update(Vector3f offsetVector){
        for(HeadsUpDisplay hud : huds){
            hud.setPosition(hud.getPosition().add(offsetVector));
            hud.update(new Vector3f());
        }
    }

    public void draw(Shader shader){
        for(HeadsUpDisplay hud : huds){
            hud.draw(shader);
        }
    }
}
