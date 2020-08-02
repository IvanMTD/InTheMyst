package ru.phoenix.game.scene.cut.detail;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class CutSceneTexture {
    private static CutSceneTexture instance = null;

    private Texture mapButton;
    private Texture savePadCloseButton;
    private Texture savePadSaveButton;

    private Texture savePadBackground;
    private Texture savePadUpper;

    private CutSceneTexture(){
        setupBoards();
        setupButtons();
    }

    private void setupBoards(){
        savePadBackground = new Texture2D();
        savePadBackground.setup(null,"./data/content/texture/hud/camp/savepad/save_pad_back.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        savePadUpper = new Texture2D();
        savePadUpper.setup(null,"./data/content/texture/hud/camp/savepad/save_pad_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    private void setupButtons(){
        mapButton = new Texture2D();
        mapButton.setup(null,"./data/content/texture/hud/camp/treasure-map.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        savePadCloseButton = new Texture2D();
        savePadCloseButton.setup(null,"./data/content/texture/hud/camp/savepad/save_pad_close.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        savePadSaveButton = new Texture2D();
        savePadSaveButton.setup(null,"./data/content/texture/hud/camp/savepad/save_pad_button.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    public Texture getMapButton() {
        return mapButton;
    }

    public Texture getSavePadCloseButton() {
        return savePadCloseButton;
    }

    public Texture getSavePadSaveButton() {
        return savePadSaveButton;
    }

    public Texture getSavePadBackground() {
        return savePadBackground;
    }

    public Texture getSavePadUpper() {
        return savePadUpper;
    }

    public static CutSceneTexture getInstance(){
        if(instance == null){
            instance = new CutSceneTexture();
        }
        return instance;
    }
}
