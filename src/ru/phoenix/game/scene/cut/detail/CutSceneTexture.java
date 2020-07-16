package ru.phoenix.game.scene.cut.detail;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class CutSceneTexture {
    private static CutSceneTexture instance = null;

    private Texture mapButton;

    private CutSceneTexture(){
        setupButtons();
    }

    private void setupButtons(){
        mapButton = new Texture2D();
        mapButton.setup(null,"./data/content/texture/hud/camp/treasure-map.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    public Texture getMapButton() {
        return mapButton;
    }

    public static CutSceneTexture getInstance(){
        if(instance == null){
            instance = new CutSceneTexture();
        }
        return instance;
    }
}
