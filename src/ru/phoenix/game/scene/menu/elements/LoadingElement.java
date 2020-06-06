package ru.phoenix.game.scene.menu.elements;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.text.SymbolStruct;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.menu.storage.MainMenuTextures;

import java.io.File;

import static ru.phoenix.core.config.Constants.TYPING_CENTER;

public class LoadingElement {
    private float id;
    private int num;
    private HeadsUpDisplay background;
    private SymbolStruct information;

    private boolean selected;

    public LoadingElement(Vector3f position, float width, float height, int num, float id){
        this.num = num;
        this.id = id;
        File fileDirect = new File("./data/save/data");
        File saveFile = new File(fileDirect,"SaveData" + num + ".ser");
        boolean empty = !saveFile.exists();
        background = new Button(MainMenuTextures.getInstance().getLoadingBackground(),width,height,position,true,id);
        if(empty){
            information = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols("Empty", new Vector3f(position).add(new Vector3f(0.0f,0.0f,0.01f)), 1.0f, TYPING_CENTER));
            information.setTextColor(new Vector3f(0.0f,0.0f,0.0f));
        }else{
            information = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols("NoEmpty", new Vector3f(position).add(new Vector3f(0.0f,0.0f,0.01f)), 1.0f, TYPING_CENTER));
            information.setTextColor(new Vector3f(0.0f,0.0f,0.0f));
        }
    }

    public void update(Vector3f pixel, boolean leftClick){
        background.update(pixel);
        if(background.isTarget() && leftClick){
            background.selected();
            selected = true;
        }
        if(selected){
            background.update(new Vector3f(id,0.0f,0.0f));
        }
    }

    public void update(Vector3f offsetVector){
        information.updatePosition(offsetVector);
        background.setPosition(background.getPosition().add(offsetVector));
        background.update(new Vector3f());
    }

    public void drawHud(Shader shader){
        background.draw(shader);
    }

    public void drawText(Shader shader){
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(information.getSymbols(),shader);
    }

    public int getSaveNum() {
        return num;
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
}
