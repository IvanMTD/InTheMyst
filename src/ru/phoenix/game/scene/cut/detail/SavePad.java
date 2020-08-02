package ru.phoenix.game.scene.cut.detail;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.config.Time;
import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.datafile.PersonStruct;
import ru.phoenix.game.datafile.SaveData;
import ru.phoenix.game.datafile.TimeElement;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Board;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.logic.generator.Generator;
import ru.phoenix.game.property.GameController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.GROUP_G;

public class SavePad {
    public final float SAVE_PAD_BACK         = 0.3f;
    public final float SAVE_PAD_UP           = 0.31f;

    public final float SAVE_PAD_CLOSE_BUTTON = 0.6f;
    public final float SAVE_PAD_SAVE_BUTTON  = 0.61f;

    public final float SAVE_BOX_1            = 0.41f;
    public final float SAVE_BOX_2            = 0.42f;
    public final float SAVE_BOX_3            = 0.43f;
    public final float SAVE_BOX_4            = 0.44f;
    public final float SAVE_BOX_5            = 0.45f;

    private boolean hide;

    private List<HeadsUpDisplay> huds;
    private HeadsUpDisplay savePadBackground;
    private HeadsUpDisplay savePadUpper;

    private HeadsUpDisplay savePadCloseButton;
    private HeadsUpDisplay savePadSaveButton;

    private Vector3f box1Position;
    private Vector3f box2Position;
    private Vector3f box3Position;
    private Vector3f box4Position;
    private Vector3f box5Position;

    private SaveBox save_box_1;
    private SaveBox save_box_2;
    private SaveBox save_box_3;
    private SaveBox save_box_4;
    private SaveBox save_box_5;
    private List<SaveBox> saveBoxes;

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
        for(SaveBox saveBox : saveBoxes){
            saveBox.setSelected(false);
        }
    }

    private void setupBoards(){
        float originW = CutSceneTexture.getInstance().getSavePadBackground().getWidth();
        float originH = CutSceneTexture.getInstance().getSavePadBackground().getHeight();
        float w = originW * 2;//Window.getInstance().getWidth() / 5.0f;
        float h = originH * 2;//originW * w / originH;
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
        float originW = CutSceneTexture.getInstance().getSavePadBackground().getWidth();
        float originH = CutSceneTexture.getInstance().getSavePadBackground().getHeight();
        float w = originW * 2;//Window.getInstance().getWidth() / 5.0f;
        float h = originH * 2;//originW * w / originH;
        float x = Window.getInstance().getWidth() / 2;
        float y = Window.getInstance().getHeight() / 2;
        buttonsPos = new Vector3f(x,y,-0.08f);
        savePadCloseButton = new Button(CutSceneTexture.getInstance().getSavePadCloseButton(),w,h,buttonsPos,true,SAVE_PAD_CLOSE_BUTTON);
        huds.add(savePadCloseButton);
        savePadSaveButton = new Button(CutSceneTexture.getInstance().getSavePadSaveButton(),w,h,buttonsPos,true,SAVE_PAD_SAVE_BUTTON);
        huds.add(savePadSaveButton);
        setupSaveButtons(w,h,buttonsPos);
    }

    private void setupSaveButtons(float w, float h, Vector3f position){
        try {
            saveBoxes = new ArrayList<>();
            box1Position = new Vector3f(position).add(new Vector3f(-17.0f,-75.0f,0.0f));
            save_box_1 = new SaveBox(position,box1Position,w,h,1,SAVE_BOX_1);
            box2Position = new Vector3f(position).add(new Vector3f(-17.0f,-33.0f,0.0f));
            save_box_2 = new SaveBox(position,box2Position,w,h,2,SAVE_BOX_2);
            box3Position = new Vector3f(position).add(new Vector3f(-17.0f,9.0f,0.0f));
            save_box_3 = new SaveBox(position,box3Position,w,h,3,SAVE_BOX_3);
            box4Position = new Vector3f(position).add(new Vector3f(-17.0f,51.0f,0.0f));
            save_box_4 = new SaveBox(position,box4Position,w,h,4,SAVE_BOX_4);
            box5Position = new Vector3f(position).add(new Vector3f(-17.0f,93.0f,0.0f));
            save_box_5 = new SaveBox(position,box5Position,w,h,5,SAVE_BOX_5);
            saveBoxes.addAll(Arrays.asList(save_box_1,save_box_2,save_box_3,save_box_4,save_box_5));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update(Vector3f pixel, SaveData saveData, List<Character> allies){
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
                        }else if(hud.getId() == SAVE_PAD_SAVE_BUTTON) {
                            hud.selected(0.98f);

                            int boxNumber = 0;
                            for(SaveBox saveBox : saveBoxes){
                                if(saveBox.isSelected()){
                                    boxNumber = saveBox.getBoxNumber();
                                }
                            }

                            if(boxNumber != 0) {
                                // записываем время
                                int d = Time.getDay();
                                int h = Time.getHour();
                                int m = Time.getMinut();
                                float s = Time.getSecond();
                                TimeElement time = new TimeElement(d, h, m, s);
                                Default.getCurrentData().setTime(time);
                                // Записываем лагерь и персонажей
                                Default.getCurrentData().setCampLocation(saveData);
                                List<PersonStruct> personStructList = new ArrayList<>();
                                for (Character character : allies) {
                                    PersonStruct personStruct = new PersonStruct();
                                    personStruct.setType(character.getType());
                                    personStruct.setCharacteristic(character.getCharacteristic());
                                    personStructList.add(personStruct);
                                }
                                Default.getCurrentData().setPersonStructs(personStructList);
                                // запимываем текстуры ландшафта
                                Texture texture = Generator.getHeightMap();
                                texture.saveImage("cutSceneMap" + boxNumber +".png");

                                File fileDirect = new File("./data/save/data");
                                File saveFile = new File(fileDirect, "saveGame" + boxNumber + ".ser");
                                if (saveFile.exists()) {
                                    if (saveFile.delete()) {
                                        FileOutputStream fileOutputStream = null;
                                        try {
                                            fileOutputStream = new FileOutputStream("./data/save/data/saveGame" + boxNumber + ".ser");
                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                            objectOutputStream.writeObject(Default.getCurrentData());
                                            objectOutputStream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    FileOutputStream fileOutputStream = null;
                                    try {
                                        fileOutputStream = new FileOutputStream("./data/save/data/saveGame" + boxNumber + ".ser");
                                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                                        objectOutputStream.writeObject(Default.getCurrentData());
                                        objectOutputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                for(SaveBox saveBox : saveBoxes){
                                    if(saveBox.getBoxNumber() == boxNumber){
                                        saveBox.updateInformation("Day " + d + " " + h + ":" + m);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for(SaveBox saveBox : saveBoxes){
                saveBox.update(pixel,leftClick);
                if(saveBox.isTarget()){
                    detecter++;
                    if(saveBox.isSelected()){
                        for(SaveBox sb : saveBoxes){
                            if(saveBox.getId() != sb.getId()){
                                sb.setSelected(false);
                            }
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
            for(SaveBox saveBox : saveBoxes){
                saveBox.drawHud(shader);
            }
        }
    }

    public void drawText(Shader shader){
        if(!isHide()) {
            for (SaveBox saveBox : saveBoxes) {
                saveBox.drawText(shader);
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
