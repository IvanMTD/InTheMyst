package ru.phoenix.game.scene.menu.storage;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class MainMenuTextures {
    private static MainMenuTextures instance;

    private Texture background;
    private Texture loadingBackground;
    private Texture title;
    private Texture stick;

    private Texture buttonPowerOff;
    private Texture buttonNewGame;
    private Texture buttonLoading;
    private Texture buttonSettings;
    private Texture okButton;
    private Texture backButton;
    private Texture dellButton;
    private Texture loadButton;
    private Texture minus;
    private Texture plus;

    private MainMenuTextures() {
        background = getTexture("./data/content/texture/hud/mainmenu/background/mmb_hud.png");
        loadingBackground = getTexture("./data/content/texture/hud/mainmenu/elements/rust.png");
        title = getTexture("./data/content/texture/hud/mainmenu/background/title.png");
        stick = getTexture("./data/content/texture/hud/mainmenu/background/Stick.png");

        buttonPowerOff = getTexture("./data/content/texture/hud/mainmenu/elements/powerOff.png");
        buttonNewGame = getTexture("./data/content/texture/hud/mainmenu/elements/NewGame.png");
        buttonLoading = getTexture("./data/content/texture/hud/mainmenu/elements/Loading.png");
        buttonSettings = getTexture("./data/content/texture/hud/mainmenu/elements/Settings.png");

        okButton = getTexture("./data/content/texture/hud/mainmenu/elements/ok_button.png");
        backButton = getTexture("./data/content/texture/hud/mainmenu/elements/back_button.png");
        dellButton = getTexture("./data/content/texture/hud/mainmenu/elements/dell_button.png");
        loadButton = getTexture("./data/content/texture/hud/mainmenu/elements/load_button.png");

        minus = getTexture("./data/content/texture/hud/mainmenu/elements/minus.png");
        plus = getTexture("./data/content/texture/hud/mainmenu/elements/plus.png");
    }

    private Texture getTexture(String path){
        Texture texture = new Texture2D();
        texture.setup(null,path,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        return texture;
    }

    public Texture getBackground() {
        return background;
    }

    public Texture getLoadingBackground() {
        return loadingBackground;
    }

    public Texture getTitle() {
        return title;
    }

    public Texture getStick() {
        return stick;
    }

    public Texture getButtonPowerOff() {
        return buttonPowerOff;
    }

    public Texture getButtonNewGame() {
        return buttonNewGame;
    }

    public Texture getButtonLoading() {
        return buttonLoading;
    }

    public Texture getButtonSettings() {
        return buttonSettings;
    }

    public Texture getOkButton() {
        return okButton;
    }

    public Texture getBackButton() {
        return backButton;
    }

    public Texture getDellButton() {
        return dellButton;
    }

    public Texture getLoadButton() {
        return loadButton;
    }

    public Texture getMinus() {
        return minus;
    }

    public Texture getPlus() {
        return plus;
    }

    public static MainMenuTextures getInstance() {
        if(instance == null){
            instance = new MainMenuTextures();
        }
        return instance;
    }
}
