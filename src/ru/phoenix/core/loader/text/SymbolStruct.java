package ru.phoenix.core.loader.text;

import ru.phoenix.core.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SymbolStruct {
    private List<Symbol> symbols;
    private String text;
    private Vector3f position;
    private float size;
    private int typing;

    public SymbolStruct(String text, Vector3f position, float size, int typing) {
        symbols = new ArrayList<>();
        this.text = text;
        this.position = position;
        this.size = size;
        this.typing = typing;
    }

    public SymbolStruct(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public void updatePosition(Vector3f offsetVector){
        for(Symbol symbol : symbols){
            symbol.setPosition(symbol.getPosition().add(offsetVector));
        }
    }

    public void setTextColor(Vector3f color){
        for(Symbol symbol : symbols){
            symbol.setColor(color);
        }
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public String getText() {
        return text;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getSize() {
        return size;
    }

    public int getTyping() {
        return typing;
    }
}
