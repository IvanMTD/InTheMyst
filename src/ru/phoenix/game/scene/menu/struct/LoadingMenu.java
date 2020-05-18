package ru.phoenix.game.scene.menu.struct;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.text.SymbolStruct;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Board;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.menu.storage.MainMenuTextures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.TYPING_CENTER;

public class LoadingMenu {
    // константы
    public static final float BACK_BUTTON = 0.16f;
    public static final float NO_ACTION = -1.0f;
    // массивы
    private List<HeadsUpDisplay> huds;
    // статические элементы
    private HeadsUpDisplay background;
    private SymbolStruct title;
    // активные элементы
    private HeadsUpDisplay backButton;
    // контроль
    private float action;

    public LoadingMenu(){
        action = NO_ACTION;
        huds = new ArrayList<>();
        initHudElements();
        huds.addAll(Arrays.asList(background,backButton));
    }

    private void initHudElements(){
        setupBackground();
        setupTitle();
        setupButtons();
    }

    private void setupBackground(){
        float x = Window.getInstance().getWidth() / 2.0f;
        float y = -Window.getInstance().getHeight() / 2.0f;
        float z = -1.0f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 320.0f;
        float height = Window.getInstance().getHeight();
        background = new Board(MainMenuTextures.getInstance().getBackground(),width,height,position,false);
        background.update(new Vector3f());
    }

    private void setupTitle(){
        float x = Window.getInstance().getWidth() / 2.0f;
        float y = -(Window.getInstance().getHeight() - Window.getInstance().getHeight() / 12.0f);
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        title = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols("Loading",position,2.0f,TYPING_CENTER));
        title.setTextColor(new Vector3f(1.0f,1.0f,1.0f));
    }

    private void setupButtons(){
        float x = (Window.getInstance().getWidth() / 2.0f) + 80.0f;
        float y = -30.0f;
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 60.0f;
        float height = MainMenuTextures.getInstance().getBackButton().getHeight() * width / MainMenuTextures.getInstance().getBackButton().getWidth();
        backButton = new Button(MainMenuTextures.getInstance().getBackButton(),width,height,position,true,BACK_BUTTON);
        backButton.update(new Vector3f());
    }

    public void update(Vector3f pixel, boolean leftClick){
        action = NO_ACTION;
        for(HeadsUpDisplay hud : huds){
            hud.update(pixel);
            if(leftClick && hud.isTarget()){
                action = BACK_BUTTON;
            }
        }
    }

    public void update(Vector3f offsetVector){
        for(HeadsUpDisplay hud : huds){
            hud.setPosition(hud.getPosition().add(offsetVector));
            hud.update(new Vector3f());
        }
        title.updatePosition(offsetVector);
    }

    public float getAction() {
        return action;
    }

    public void draw(Shader shader){
        for(HeadsUpDisplay hud : huds){
            hud.draw(shader);
        }

        TextDisplay.getInstance().getShader().useProgram();
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(title.getSymbols(),TextDisplay.getInstance().getShader());
    }
}
