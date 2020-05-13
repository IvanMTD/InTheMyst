package ru.phoenix.game.property;

import ru.phoenix.core.loader.text.SymbolLibrary;
import ru.phoenix.core.shader.Shader;

import static ru.phoenix.core.config.Constants.ENGLISH;

public class TextDisplay {
    private static TextDisplay instance;

    private Shader shader;

    private SymbolLibrary english;

    private TextDisplay(){
        english = new SymbolLibrary(ENGLISH);
        initShader();
    }

    private void initShader(){
        shader = new Shader();
        shader.createVertexShader("VS_symbol.glsl");
        shader.createFragmentShader("FS_symbol.glsl");
        shader.createProgram();
    }

    public SymbolLibrary getText(int langueage) {
        SymbolLibrary result;
        if(langueage == ENGLISH){
            result = english;
        }else{
            result = english;
        }
        return result;
    }

    public Shader getShader() {
        return shader;
    }

    public static TextDisplay getInstance() {
        if(instance == null){
            instance = new TextDisplay();
        }
        return instance;
    }
}
