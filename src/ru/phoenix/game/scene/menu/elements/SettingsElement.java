package ru.phoenix.game.scene.menu.elements;

import ru.phoenix.core.config.Default;
import ru.phoenix.core.loader.text.SymbolStruct;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.strucet.Button;
import ru.phoenix.game.property.TextDisplay;
import ru.phoenix.game.scene.menu.storage.MainMenuTextures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.phoenix.core.config.Constants.TYPING_CENTER;
import static ru.phoenix.core.config.Constants.TYPING_LEFT;

public class SettingsElement {
    private SymbolStruct titre;
    private List<SymbolStruct> symbolStructs;
    private List<HeadsUpDisplay> huds;

    private HeadsUpDisplay minus;
    private HeadsUpDisplay plus;

    private float minusId;
    private float plusId;
    private int index;

    public SettingsElement(Vector3f position, float width, float height, String titre, List<String> info, float minusId, float plusId){
        symbolStructs = new ArrayList<>();
        huds = new ArrayList<>();
        settupTitre(position, width, height, titre);
        settupButtons(position, width, height,minusId,plusId);
        settupInfo(position, width, height, info);
        huds.addAll(Arrays.asList(minus,plus));
        this.minusId = minusId;
        this.plusId = plusId;
        index = 0;
    }

    private void settupTitre(Vector3f position, float width, float height, String titre){
        float xOffset = width / 2.0f;
        Vector3f pos = new Vector3f(position.getX() - xOffset, position.getY(),position.getZ());
        this.titre = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols(titre,pos,0.6f,TYPING_LEFT));
        this.titre.setTextColor(new Vector3f(1.0f,1.0f,1.0f));
    }

    private void settupButtons(Vector3f position, float width, float height,float minusId, float plusId){
        float halfWidth = width / 2.0f;
        float offset = halfWidth / 12.0f;
        Vector3f pos1 = new Vector3f(position.getX() + offset,position.getY(),position.getZ());
        Vector3f pos2 = new Vector3f(position.getX() + (halfWidth - offset),position.getY(), position.getZ());
        minus = new Button(MainMenuTextures.getInstance().getMinus(),20.0f,20.0f,pos1,true,minusId);
        plus = new Button(MainMenuTextures.getInstance().getPlus(),20.0f,20.0f,pos2,true,plusId);
    }

    private void settupInfo(Vector3f position, float width, float height, List<String> info){
        float xOffset = width / 4.0f;
        Vector3f pos = new Vector3f(position.getX() + xOffset, position.getY(), position.getZ());
        for(int i=0; i<info.size(); i++) {
            SymbolStruct s = new SymbolStruct(TextDisplay.getInstance().getText(Default.getLangueage()).getSymbols(info.get(i), pos, 0.58f, TYPING_CENTER));
            s.setTextColor(new Vector3f(1.0f,1.0f,1.0f));
            symbolStructs.add(s);
        }
    }

    public void update(Vector3f pixel, boolean leftClick){
        for(HeadsUpDisplay hud : huds){
            hud.update(pixel);
            if(leftClick && hud.isTarget()){
                hud.selected();
                if(hud.getId() == minusId){
                    index--;
                }else if(hud.getId() == plusId){
                    index++;
                }
                if(index<0){
                    index = symbolStructs.size() - 1;
                }
                if(index>symbolStructs.size() - 1){
                    index = 0;
                }
            }
        }
    }

    public void update(float offset){
        titre.updatePosition(offset);
        for(SymbolStruct symbolStruct : symbolStructs){
            symbolStruct.updatePosition(offset);
        }
        for(HeadsUpDisplay hud : huds){
            hud.setPosition(hud.getPosition().add(new Vector3f(offset,0.0f,0.0f)));
            hud.update(new Vector3f());
        }
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void drawText(Shader shader){
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(titre.getSymbols(),shader);
        TextDisplay.getInstance().getText(Default.getLangueage()).drawText(symbolStructs.get(index).getSymbols(),shader);
    }

    public void drawHud(Shader shader){
        for(HeadsUpDisplay hud : huds){
            hud.draw(shader);
        }
    }
}
