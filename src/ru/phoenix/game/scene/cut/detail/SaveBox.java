package ru.phoenix.game.scene.cut.detail;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.text.SymbolStruct;
import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.datafile.SaveGame;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.logic.element.Pixel;
import ru.phoenix.game.property.TextDisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.TYPING_CENTER;

public class SaveBox {
    private float id;
    private int number;
    private float size;

    private HeadsUpDisplay button;
    private SymbolStruct information;

    private boolean selected;
    private SaveGame saveGame;

    public SaveBox(Vector3f butPos, Vector3f infPos, float width, float height, int number, float id) throws IOException, ClassNotFoundException {
        size = 0.7f;
        saveGame = new SaveGame();
        File fileDirect = new File("./data/save/data");
        File saveFile = new File(fileDirect,"saveGame" + number + ".ser");
        boolean empty = !saveFile.exists();
        if(saveFile.exists()){
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream("./data/save/data/saveGame" + number + ".ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            saveGame = (SaveGame) objectInputStream.readObject();
            objectInputStream.close();
        }else{
            saveGame = null;
        }

        this.number = number;
        this.id = id;
        Texture texture = new Texture2D();
        texture.setup(null,"./data/content/texture/hud/camp/savepad/save_pad_slot_" + this.number + ".png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        button = new Button(texture,width,height,butPos,true,id);
        if(empty){
            information = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols("Empty", new Vector3f(infPos).add(new Vector3f(0.0f,0.0f,0.01f)), size, TYPING_CENTER));
            information.setTextColor(new Vector3f(0.0f,0.0f,0.0f));
        }else{
            int d = saveGame.getTime().getDay();
            int h = saveGame.getTime().getHour();
            int m = saveGame.getTime().getMinut();
            int s = (int)saveGame.getTime().getSecond();
            String info = "Day " + d + " " + h + ":" + m;
            information = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols(info, new Vector3f(infPos).add(new Vector3f(0.0f,0.0f,0.01f)), size, TYPING_CENTER));
            information.setTextColor(new Vector3f(0.0f,0.0f,0.0f));
        }
        information.setPosition(new Vector3f(infPos).add(new Vector3f(0.0f,0.0f,0.01f)));
    }

    public void update(Vector3f pixel, boolean leftClick){
        button.update(pixel);
        if(button.isTarget() && leftClick){
            button.selected();
            selected = true;
        }
        if(selected){
            button.update(new Vector3f(id,0.0f,0.0f));
        }
    }

    public void update(Vector3f offsetVector){
        information.updatePosition(offsetVector);
        information.setPosition(new Vector3f(information.getPosition()).add(offsetVector));
        button.setPosition(button.getPosition().add(offsetVector));
        button.update(new Vector3f());
    }

    public void drawHud(Shader shader){
        button.draw(shader);
    }

    public void drawText(Shader shader){
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(information.getSymbols(),shader);
    }

    public int getBoxNumber() {
        return number;
    }

    public float getId() {
        return id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SaveGame getSaveGame() {
        return saveGame;
    }

    public void updateInformation(){
        Vector3f pos = new Vector3f(information.getPosition());
        information = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols("Empty", pos, size, TYPING_CENTER));
        information.setPosition(pos);
        information.setTextColor(new Vector3f(0.0f,0.0f,0.0f));
    }

    public void updateInformation(String text){
        Vector3f pos = new Vector3f(information.getPosition());
        information = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols(text, pos, size, TYPING_CENTER));
        information.setPosition(pos);
        information.setTextColor(new Vector3f(0.0f,0.0f,0.0f));
    }

    public boolean isTarget(){
        boolean target = button.isTarget();
        if(isSelected()){
            float id = Pixel.getPixel().getX();
            if(id != button.getId()){
                target = false;
            }
        }
        return target;
    }
}
