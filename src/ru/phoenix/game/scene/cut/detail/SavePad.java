package ru.phoenix.game.scene.cut.detail;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Board;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.property.GameController;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.GROUP_G;

public class SavePad {
    public final float SAVE_PAD_BACK         = 0.3f;
    public final float SAVE_PAD_UP           = 0.31f;

    public final float SAVE_PAD_CLOSE_BUTTON = 0.6f;
    public final float SAVE_PAD_SAVE_BUTTON  = 0.61f;

    private boolean hide;

    private List<HeadsUpDisplay> huds;
    private HeadsUpDisplay savePadBackground;
    private HeadsUpDisplay savePadUpper;

    private HeadsUpDisplay savePadCloseButton;
    private HeadsUpDisplay savePadSaveButton;

    private Vector3f backPos;
    private Vector3f buttonsPos;
    private Vector3f uppPos;

    public SavePad(){
        backPos = new Vector3f();
        buttonsPos = new Vector3f();
        uppPos = new Vector3f();

        hide = true;
        huds = new ArrayList<>();
        setupBoards();
        setupButtons();
    }

    private void setDefault(){
        setHide(true);
        savePadBackground.setPosition(backPos);
        savePadUpper.setPosition(uppPos);

        savePadCloseButton.setPosition(buttonsPos);
        savePadSaveButton.setPosition(buttonsPos);
    }

    private void setupBoards(){
        float originW = CutSceneTexture.getInstance().getMapButton().getWidth();
        float originH = CutSceneTexture.getInstance().getMapButton().getHeight();
        float w = Window.getInstance().getWidth() / 5.0f;
        float h = originW * w / originH;
        float x = Window.getInstance().getWidth() / 2;
        float y = Window.getInstance().getHeight() / 2;
        backPos = new Vector3f(x,y,-0.09f);
        savePadBackground = new Board(CutSceneTexture.getInstance().getSavePadBackground(),w,h,backPos,true,GROUP_G,SAVE_PAD_BACK);
        savePadBackground.update(new Vector3f());
        huds.add(savePadBackground);
        uppPos = new Vector3f(x,y,-0.07f);
        savePadUpper = new Board(CutSceneTexture.getInstance().getSavePadUpper(),w,h,uppPos,true,GROUP_G,SAVE_PAD_UP);
        huds.add(savePadUpper);
    }

    private void setupButtons(){
        float originW = CutSceneTexture.getInstance().getMapButton().getWidth();
        float originH = CutSceneTexture.getInstance().getMapButton().getHeight();
        float w = Window.getInstance().getWidth() / 5.0f;
        float h = originW * w / originH;
        float x = Window.getInstance().getWidth() / 2;
        float y = Window.getInstance().getHeight() / 2;
        buttonsPos = new Vector3f(x,y,-0.08f);
        savePadCloseButton = new Button(CutSceneTexture.getInstance().getSavePadCloseButton(),w,h,buttonsPos,true,SAVE_PAD_CLOSE_BUTTON);
        huds.add(savePadCloseButton);
        savePadSaveButton = new Button(CutSceneTexture.getInstance().getSavePadSaveButton(),w,h,buttonsPos,true,SAVE_PAD_SAVE_BUTTON);
        huds.add(savePadSaveButton);
    }

    public void update(Vector3f pixel){
        Camera.getInstance().setMouseMoveLock(false);
        if(!hide) {
            boolean leftClick = GameController.getInstance().isLeftClick();
            int detecter = 0;
            for (HeadsUpDisplay hud : huds) {
                hud.update(pixel);
                if (hud.isTarget()) {
                    detecter++;
                    if (leftClick) {
                        if (hud.getId() == SAVE_PAD_CLOSE_BUTTON) {
                            setDefault();
                        }else if(hud.getId() == SAVE_PAD_SAVE_BUTTON){
                            hud.selected(0.98f);
                        }
                    }
                }
            }
            if(detecter > 0){
                Camera.getInstance().setMouseMoveLock(true);
            }else{
                Camera.getInstance().setMouseMoveLock(false);
            }
        }
    }

    public void draw(Shader shader){
        if(!isHide()) {
            for (HeadsUpDisplay hud : huds) {
                hud.draw(shader);
            }
        }
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}
