package ru.phoenix.game.scene.menu.struct;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.text.SymbolStruct;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Board;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.loop.SceneControl;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.menu.elements.SettingsElement;
import ru.phoenix.game.scene.menu.storage.MainMenuTextures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.TYPING_CENTER;

public class SettingsMenu {
    public static final float OK_BUTTON = 0.14f;
    public static final float BACK_BUTTON = 0.15f;
    public static final float NO_ACTION = -1.0f;

    private float action;
    // мвссивы
    private List<HeadsUpDisplay> huds;
    // статические
    private SymbolStruct title;
    private HeadsUpDisplay background;
    // активные
    private SettingsElement screenMode;
    private int smtIndex;
    private SettingsElement screenSize;
    private int sstIndex;
    private SettingsElement antialiasing;
    private int atIndex;
    private SettingsElement gamma;
    private int gtIndex;
    private SettingsElement contrast;
    private int ctIndex;

    private HeadsUpDisplay okButton;
    private HeadsUpDisplay backButton;

    public SettingsMenu(){
        action = NO_ACTION;
        initHudElements();
    }

    private void initHudElements(){
        huds = new ArrayList<>();
        setupBackground();
        setupTitle();
        setupButtons();
        huds.addAll(Arrays.asList(background,okButton,backButton));
    }

    private void setupBackground(){
        float x = -Window.getInstance().getWidth() / 2.0f;
        float y = Window.getInstance().getHeight() / 2.0f;
        float z = -1.0f;
        Vector3f position = new Vector3f(x,y,z);
        float width = 320.0f;
        float height = Window.getInstance().getHeight();
        background = new Board(MainMenuTextures.getInstance().getBackground(),width,height,position,false);
        background.update(new Vector3f());
    }

    private void setupTitle(){
        float x = -Window.getInstance().getWidth() / 2.0f;
        float y = Window.getInstance().getHeight() / 12.0f;
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        title = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols("Settings",position,1.0f,TYPING_CENTER));
        title.setTextColor(new Vector3f(1.0f,1.0f,1.0f));
    }

    private void setupButtons(){
        setupMenuButtons();
        setupMainButtons();
    }

    private void setupMenuButtons(){
        float x = -Window.getInstance().getWidth() / 2.0f;
        float y = Window.getInstance().getHeight() / 3.0f;
        float z = -0.09f;
        Vector3f position = new Vector3f(x,y,z);
        List<String> info = new ArrayList<>(Arrays.asList("full","window"));
        float id1 = 0.2f;
        float id2 = 0.21f;
        screenMode = new SettingsElement(position,250.0f,50.0f,"Screen mode",info,id1,id2);
        if(WindowConfig.getInstance().isFullScreen()){
            screenMode.setIndex(0);
        }else{
            screenMode.setIndex(1);
        }
        smtIndex = screenMode.getIndex();
        position = position.add(new Vector3f(0.0f,30.0f,0.0f));
        info = new ArrayList<>(Arrays.asList("640x480","800x600","1024x768","1280x720","1440x900","1920x1080"));
        id1+=0.01f;
        id2+=0.01f;
        screenSize = new SettingsElement(position,250.0f,50.0f,"Screen size",info,id1,id2);
        int w = WindowConfig.getInstance().getWidth();
        if(w == 640){
            screenSize.setIndex(0);
        }else if(w == 800){
            screenSize.setIndex(1);
        }else if(w == 1024){
            screenSize.setIndex(2);
        }else if(w == 1280){
            screenSize.setIndex(3);
        }else if(w == 1440){
            screenSize.setIndex(4);
        }else{
            screenSize.setIndex(5);
        }
        sstIndex = screenSize.getIndex();
        position = position.add(new Vector3f(0.0f,30.0f,0.0f));
        info = new ArrayList<>(Arrays.asList("off","x2","x4","x8","x16"));
        id1+=0.01f;
        id2+=0.01f;
        antialiasing = new SettingsElement(position,250.0f,50.0f,"Antialiasing",info,id1,id2);
        int a = WindowConfig.getInstance().getSamples();
        if(a == 1){
            antialiasing.setIndex(0);
        }else if(a == 2){
            antialiasing.setIndex(1);
        }else if(a == 4){
            antialiasing.setIndex(2);
        }else if(a == 8){
            antialiasing.setIndex(3);
        }else{
            antialiasing.setIndex(4);
        }
        atIndex = antialiasing.getIndex();
        position = position.add(new Vector3f(0.0f,30.0f,0.0f));
        info = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
        id1+=0.01f;
        id2+=0.01f;
        gamma = new SettingsElement(position,250.0f,50.0f,"Gamma",info,id1,id2);
        float g = Window.getInstance().getGamma();
        if(g == 1.2f){
            gamma.setIndex(0);
        }else if(g == 1.4f){
            gamma.setIndex(1);
        }else if(g == 1.6f){
            gamma.setIndex(2);
        }else if(g == 1.8f){
            gamma.setIndex(3);
        }else if(g == 2.0f){
            gamma.setIndex(4);
        }else if(g == 2.2f){
            gamma.setIndex(5);
        }else if(g == 2.4f){
            gamma.setIndex(6);
        }else if(g == 2.6f){
            gamma.setIndex(7);
        }else if(g == 2.8f){
            gamma.setIndex(8);
        }else{
            gamma.setIndex(9);
        }
        gtIndex = gamma.getIndex();
        position = position.add(new Vector3f(0.0f,30.0f,0.0f));
        info = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9","10"));
        id1+=0.01f;
        id2+=0.01f;
        contrast = new SettingsElement(position,250.0f,50.0f,"Contrast",info,id1,id2);
        float c = Window.getInstance().getContrast();
        if(c == 0.0f){
            contrast.setIndex(0);
        }else if(c == 0.1f){
            contrast.setIndex(1);
        }else if(c == 0.2f){
            contrast.setIndex(2);
        }else if(c == 0.3f){
            contrast.setIndex(3);
        }else if(c == 0.4f){
            contrast.setIndex(4);
        }else if(c == 0.5f){
            contrast.setIndex(5);
        }else if(c == 0.6f){
            contrast.setIndex(6);
        }else if(c == 0.7f){
            contrast.setIndex(7);
        }else if(c == 0.8f){
            contrast.setIndex(8);
        }else{
            contrast.setIndex(9);
        }
        ctIndex = contrast.getIndex();
    }

    private void setupMainButtons(){
        float x1 = (-Window.getInstance().getWidth() / 2.0f) - 80.0f;
        float x2 = (-Window.getInstance().getWidth() / 2.0f) + 80.0f;
        float y = Window.getInstance().getHeight() - 30.0f;
        float z = -0.09f;
        Vector3f position1 = new Vector3f(x1,y,z);
        Vector3f position2 = new Vector3f(x2,y,z);
        float width = 60.0f;
        float height = MainMenuTextures.getInstance().getOkButton().getHeight() * width / MainMenuTextures.getInstance().getOkButton().getWidth();
        okButton = new Button(MainMenuTextures.getInstance().getOkButton(),width,height,position1,true,OK_BUTTON);
        okButton.update(new Vector3f());
        backButton = new Button(MainMenuTextures.getInstance().getBackButton(),width,height,position2,true,BACK_BUTTON);
        backButton.update(new Vector3f());
    }

    public float getAction() {
        return action;
    }

    private void setDefault(){
        screenMode.setIndex(smtIndex);
        screenSize.setIndex(sstIndex);
        antialiasing.setIndex(atIndex);
        // контроль гаммы
        gamma.setIndex(gtIndex);
        if(gamma.getIndex() == 0){
            Window.getInstance().setGamma(1.2f);
        }else if(gamma.getIndex() == 1){
            Window.getInstance().setGamma(1.4f);
        }else if(gamma.getIndex() == 2){
            Window.getInstance().setGamma(1.6f);
        }else if(gamma.getIndex() == 3){
            Window.getInstance().setGamma(1.8f);
        }else if(gamma.getIndex() == 4){
            Window.getInstance().setGamma(2.0f);
        }else if(gamma.getIndex() == 5){
            Window.getInstance().setGamma(2.2f);
        }else if(gamma.getIndex() == 6){
            Window.getInstance().setGamma(2.4f);
        }else if(gamma.getIndex() == 7){
            Window.getInstance().setGamma(2.6f);
        }else if(gamma.getIndex() == 8){
            Window.getInstance().setGamma(2.8f);
        }else{
            Window.getInstance().setGamma(3.0f);
        }

        contrast.setIndex(ctIndex);
        if(contrast.getIndex() == 0){
            Window.getInstance().setContrast(0.0f);
        }else if(contrast.getIndex() == 1){
            Window.getInstance().setContrast(0.1f);
        }else if(contrast.getIndex() == 2){
            Window.getInstance().setContrast(0.2f);
        }else if(contrast.getIndex() == 3){
            Window.getInstance().setContrast(0.3f);
        }else if(contrast.getIndex() == 4){
            Window.getInstance().setContrast(0.4f);
        }else if(contrast.getIndex() == 5){
            Window.getInstance().setContrast(0.5f);
        }else if(contrast.getIndex() == 6){
            Window.getInstance().setContrast(0.6f);
        }else if(contrast.getIndex() == 7){
            Window.getInstance().setContrast(0.7f);
        }else if(contrast.getIndex() == 8){
            Window.getInstance().setContrast(0.8f);
        }else{
            Window.getInstance().setContrast(0.9f);
        }
    }

    private void saveSettings(){
        saveWindowMode();
        saveWindowSize();
        saveAntialiasing();
        saveGamma();
        saveContrast();
    }

    private void saveWindowMode(){
        smtIndex = screenMode.getIndex();
        boolean fullScreen = false;
        if(smtIndex == 0){
            fullScreen = true;
        }
        Window.getInstance().setScreenMode(fullScreen);
    }

    private void saveWindowSize(){
        if(sstIndex != screenSize.getIndex()) {
            sstIndex = screenSize.getIndex();
            int w = 0;
            int h = 0;
            switch (sstIndex) {
                case 0:
                    w = 640;
                    h = 480;
                    break;
                case 1:
                    w = 800;
                    h = 600;
                    break;
                case 2:
                    w = 1024;
                    h = 768;
                    break;
                case 3:
                    w = 1280;
                    h = 720;
                    break;
                case 4:
                    w = 1440;
                    h = 900;
                    break;
                case 5:
                    w = 1920;
                    h = 1080;
                    break;
            }
            Window.getInstance().setWindowSize(w, h);
            SceneControl.setReinit(true);
        }
    }

    private void saveAntialiasing(){
        if(atIndex != antialiasing.getIndex()){
            atIndex = antialiasing.getIndex();
            int samples = 0;
            switch (atIndex){
                case 0:
                    samples = 1;
                    break;
                case 1:
                    samples = 2;
                    break;
                case 2:
                    samples = 4;
                    break;
                case 3:
                    samples = 8;
                    break;
                case 4:
                    samples = 16;
                    break;
            }
            WindowConfig.getInstance().setSamples(samples);
            SceneControl.setReinit(true);
        }
    }

    private void saveGamma(){
        gtIndex = gamma.getIndex();
        WindowConfig.getInstance().setGamma(Window.getInstance().getGamma());
    }

    private void saveContrast(){
        ctIndex = contrast.getIndex();
        WindowConfig.getInstance().setContrast(Window.getInstance().getContrast());
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

        if(action == BACK_BUTTON){
            setDefault();
        }else if(action == OK_BUTTON){
            saveSettings();
        }

        screenMode.update(pixel,leftClick);
        screenSize.update(pixel,leftClick);
        antialiasing.update(pixel,leftClick);
        // контрль гаммы
        int index = gamma.getIndex();
        gamma.update(pixel,leftClick);
        if(index != gamma.getIndex()){
            index = gamma.getIndex();
            if(index == 0){
                Window.getInstance().setGamma(1.2f);
            }else if(index == 1){
                Window.getInstance().setGamma(1.4f);
            }else if(index == 2){
                Window.getInstance().setGamma(1.6f);
            }else if(index == 3){
                Window.getInstance().setGamma(1.8f);
            }else if(index == 4){
                Window.getInstance().setGamma(2.0f);
            }else if(index == 5){
                Window.getInstance().setGamma(2.2f);
            }else if(index == 6){
                Window.getInstance().setGamma(2.4f);
            }else if(index == 7){
                Window.getInstance().setGamma(2.6f);
            }else if(index == 8){
                Window.getInstance().setGamma(2.8f);
            }else{
                Window.getInstance().setGamma(3.0f);
            }
        }
        // контроль контраста
        index = contrast.getIndex();
        contrast.update(pixel,leftClick);
        if(index != contrast.getIndex()){
            index = contrast.getIndex();
            if(index == 0){
                Window.getInstance().setContrast(0.0f);
            }else if(index == 1){
                Window.getInstance().setContrast(0.1f);
            }else if(index == 2){
                Window.getInstance().setContrast(0.2f);
            }else if(index == 3){
                Window.getInstance().setContrast(0.3f);
            }else if(index == 4){
                Window.getInstance().setContrast(0.4f);
            }else if(index == 5){
                Window.getInstance().setContrast(0.5f);
            }else if(index == 6){
                Window.getInstance().setContrast(0.6f);
            }else if(index == 7){
                Window.getInstance().setContrast(0.7f);
            }else if(index == 8){
                Window.getInstance().setContrast(0.8f);
            }else{
                Window.getInstance().setContrast(0.9f);
            }
        }
    }

    public void update(float offset){
        for(HeadsUpDisplay hud : huds){
            hud.setPosition(hud.getPosition().add(new Vector3f(offset,0.0f,0.0f)));
            hud.update(new Vector3f());
        }
        title.updatePosition(offset);
        screenMode.update(offset);
        screenSize.update(offset);
        antialiasing.update(offset);
        gamma.update(offset);
        contrast.update(offset);
    }

    public void draw(Shader shader){
        for(HeadsUpDisplay hud : huds){
            hud.draw(shader);
        }
        screenMode.drawHud(shader);
        screenSize.drawHud(shader);
        antialiasing.drawHud(shader);
        gamma.drawHud(shader);
        contrast.drawHud(shader);
        TextDisplay.getInstance().getShader().useProgram();
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(title.getSymbols(),TextDisplay.getInstance().getShader());
        screenMode.drawText(TextDisplay.getInstance().getShader());
        screenSize.drawText(TextDisplay.getInstance().getShader());
        antialiasing.drawText(TextDisplay.getInstance().getShader());
        gamma.drawText(TextDisplay.getInstance().getShader());
        contrast.drawText(TextDisplay.getInstance().getShader());
    }
}
