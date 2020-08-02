package ru.phoenix.game.scene.menu.struct;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.text.SymbolStruct;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Board;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.menu.elements.LoadingElement;
import ru.phoenix.game.scene.menu.storage.MainMenuTextures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.TYPING_CENTER;

public class LoadingMenu {
    // константы
    public static final float BACK_BUTTON   = 0.16f;
    public static final float DELL_BUTTON   = 0.17f;
    public static final float LOAD_BUTTON   = 0.18f;
    public static final float BOX_1         = 0.19f;
    public static final float BOX_2         = 0.20f;
    public static final float BOX_3         = 0.21f;
    public static final float BOX_4         = 0.22f;
    public static final float BOX_5         = 0.23f;
    public static final float NO_ACTION     = -1.0f;
    // массивы
    private List<HeadsUpDisplay> huds;
    // статические элементы
    private HeadsUpDisplay background;
    private SymbolStruct title;
    // активные элементы
    private HeadsUpDisplay backButton;
    private HeadsUpDisplay dellButton;
    private HeadsUpDisplay loadButton;

    private List<LoadingElement> boxes;
    private LoadingElement box1;
    private LoadingElement box2;
    private LoadingElement box3;
    private LoadingElement box4;
    private LoadingElement box5;
    // контроль
    private float action;

    public LoadingMenu(){
        action = NO_ACTION;
        huds = new ArrayList<>();
        boxes = new ArrayList<>();
        initHudElements();
        huds.addAll(Arrays.asList(background,backButton,dellButton,loadButton));
        boxes.addAll(Arrays.asList(box1,box2,box3,box4,box5));
    }

    private void initHudElements(){
        setupBackground();
        setupTitle();
        try {
            setupLoadingBox();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        title.setTextColor(new Vector3f(0.0f,0.0f,1.0f));
    }

    private void setupLoadingBox() throws IOException, ClassNotFoundException {
        float x = Window.getInstance().getWidth() / 2.0f;
        float y = -(Window.getInstance().getHeight() - Window.getInstance().getHeight() / 2.0f);
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        box1 = new LoadingElement(position.add(new Vector3f(0.0f,-120.0f, 0.0f)),250.0f,50.0f,1, BOX_1);
        box2 = new LoadingElement(position.add(new Vector3f(0.0f,-60.0f, 0.0f)),250.0f,50.0f,2, BOX_2);
        box3 = new LoadingElement(position, 250.0f,50.0f,3, BOX_3);
        box4 = new LoadingElement(position.add(new Vector3f(0.0f,60.0f, 0.0f)),250.0f,50.0f,4, BOX_4);
        box5 = new LoadingElement(position.add(new Vector3f(0.0f,120.0f, 0.0f)),250.0f,50.0f,5, BOX_5);
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

        x = (Window.getInstance().getWidth() / 2.0f) - 80.0f;
        y = -30.0f;
        z = -0.09f;
        position = new Vector3f(x,y,z);
        width = 60.0f;
        height = MainMenuTextures.getInstance().getBackButton().getHeight() * width / MainMenuTextures.getInstance().getBackButton().getWidth();
        loadButton = new Button(MainMenuTextures.getInstance().getLoadButton(),width,height,position,true,LOAD_BUTTON);
        loadButton.update(new Vector3f());

        x = Window.getInstance().getWidth() / 2.0f;
        y = -30.0f;
        z = -0.09f;
        position = new Vector3f(x,y,z);
        width = 60.0f;
        height = MainMenuTextures.getInstance().getBackButton().getHeight() * width / MainMenuTextures.getInstance().getBackButton().getWidth();
        dellButton = new Button(MainMenuTextures.getInstance().getDellButton(),width,height,position,true,DELL_BUTTON);
        dellButton.update(new Vector3f());
    }

    public void update(Vector3f pixel, boolean leftClick){
        action = NO_ACTION;
        for(HeadsUpDisplay hud : huds){
            hud.update(pixel);
            if(leftClick && hud.isTarget()){
                action = hud.getId();
                if(action == BACK_BUTTON){
                    for(LoadingElement box : boxes){
                        box.setSelected(false);
                    }
                }
            }
        }
        for(LoadingElement box : boxes){
            box.update(pixel,leftClick);
            if(box.isSelected()){
                for(LoadingElement box2 : boxes){
                    if(box.getId() != box2.getId()){
                        box2.setSelected(false);
                    }
                }
            }
        }
    }

    public void update(Vector3f offsetVector){
        for(HeadsUpDisplay hud : huds){
            hud.setPosition(hud.getPosition().add(offsetVector));
            hud.update(new Vector3f());
        }
        for(LoadingElement box : boxes){
            box.update(offsetVector);
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

        for(LoadingElement box : boxes){
            box.drawHud(shader);
        }

        TextDisplay.getInstance().getShader().useProgram();
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(title.getSymbols(),TextDisplay.getInstance().getShader());
        for(LoadingElement box : boxes){
            box.drawText(TextDisplay.getInstance().getShader());
        }
    }

    public boolean initLoad(){
        boolean load = false;

        for(LoadingElement box : boxes){
            if(box.isSelected()){
                if(box.getSaveGame() != null) {
                    Default.setCurrentData(box.getSaveGame());
                    Default.setSlot(box.getSaveNum());
                    Time.setDay(box.getSaveGame().getTime().getDay());
                    Time.setHour(box.getSaveGame().getTime().getHour());
                    Time.setMinut(box.getSaveGame().getTime().getMinut());
                    Time.setSecond(box.getSaveGame().getTime().getSecond());
                    load = true;
                }else {
                    box.setSelected(false);
                }
            }
        }

        return load;
    }

    public void deleteSaveData(){
        for(LoadingElement box : boxes){
            if(box.isSelected()){
                File fileDirect = new File("./data/save/data");
                File saveData = new File(fileDirect,"saveGame" + box.getSaveNum() + ".ser");
                if(saveData.exists()){
                    if(saveData.delete()){
                        box.updateInformation();
                    }
                }
                box.setSelected(false);
            }
        }
    }
}
