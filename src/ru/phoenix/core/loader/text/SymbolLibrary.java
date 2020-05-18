package ru.phoenix.core.loader.text;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;
import static ru.phoenix.core.config.Constants.*;

public class SymbolLibrary {
    private Map<Character,Symbol> symbolMap;
    private Texture alphabetTexture;

    private float fragmentSize;

    public SymbolLibrary(int language){
        symbolMap = new HashMap<>();
        initAlphabet(language);
    }

    private void initAlphabet(int language){
        String path;
        if(language == ENGLISH){
            path = "./data/content/texture/hud/chars/sprFont.png";//"./data/content/texture/hud/chars/eng-alphabet.png";
        }else{
            path = "./data/content/texture/hud/chars/sprFont.png";//"./data/content/texture/hud/chars/eng-alphabet.png";
        }
        initTexture(path);
        initSymbols(language);
    }

    private void initTexture(String path){
        alphabetTexture = new Texture2D();
        alphabetTexture.setup(null,path,GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
    }

    private void initSymbols(int language){
        char[] chars;
        if(language == ENGLISH){
            chars = new char[]{
                    '!','"','#','$','%','&','\'','(',
                    ')','*','+',',','-','.','/','0',
                    '1','2','3','4','5','6','7','8',
                    '9',':',';','<','=','>','?','@',
                    'A','B','C','D','E','F','G','H',
                    'I','J','K','L','M','N','O','P',
                    'Q','R','S','T','U','V','W','X',
                    'Y','Z','[','\\',']','^','_','`',
                    'a','b','c','d','e','f','g','h',
                    'i','j','k','l','m','n','o','p',
                    'q','r','s','t','u','v','w','x',
                    'y','z','{','|','}','~','῍','¡',
                    '€','→','↓','©','⏰','¿','$',' '
            };
            /*chars = new char[]{
                    'A','B','C','D','E','F','G','H',
                    'I','J','K','L','M','N','O','P',
                    'Q','R','S','T','U','V','W','X',
                    'Y','Z','a','b','c','d','e','f',
                    'g','h','i','j','k','l','m','n',
                    'o','p','q','r','s','t','u','v',
                    'w','x','y','z','1','2','3','4',
                    '5','6','7','8','9','0','!','?'
            };*/
        }else{
            chars = new char[]{
                    '!','"','#','$','%','&','\'','(',
                    ')','*','+',',','-','.','/','0',
                    '1','2','3','4','5','6','7','8',
                    '9',':',';','<','=','>','?','@',
                    'A','B','C','D','E','F','G','H',
                    'I','J','K','L','M','N','O','P',
                    'Q','R','S','T','U','V','W','X',
                    'Y','Z','[','\\',']','^','_','`',
                    'a','b','c','d','e','f','g','h',
                    'i','j','k','l','m','n','o','p',
                    'q','r','s','t','u','v','w','x',
                    'y','z','{','|','}','~','῍','¡',
                    '€','→','↓','©','⏰','¿','$',' '
            };
            /*chars = new char[]{
                    'A','B','C','D','E','F','G','H',
                    'I','J','K','L','M','N','O','P',
                    'Q','R','S','T','U','V','W','X',
                    'Y','Z','a','b','c','d','e','f',
                    'g','h','i','j','k','l','m','n',
                    'o','p','q','r','s','t','u','v',
                    'w','x','y','z','1','2','3','4',
                    '5','6','7','8','9','0','!','?'
            };*/
        }

        setSymbols(chars);
    }

    private void setSymbols(char[] chars){
        float w = 8.0f;
        float h = 13.0f;

        int totalWidth = alphabetTexture.getWidth() * 2;
        int totalHeight = alphabetTexture.getHeight() * 2;

        float fragmentWidth = (float)totalWidth / w;
        float fragmentHeight = (float)totalHeight / h;

        float ix = fragmentWidth / 2.0f;
        float iy = fragmentHeight / 2.0f;
        fragmentSize = fragmentWidth / 1.5f;

        float baseX = fragmentWidth * 1.0f / totalWidth;
        float baseY = fragmentHeight * 1.0f / totalHeight;

        int index = 0;

        for(int row = 1; row <= h; row++){
            for(int column = 1; column <= w; column++){
                char description = chars[index];
                float[]pos = new float[]{
                        -ix,  iy,  0.0f,
                        -ix, -iy,  0.0f,
                         ix, -iy,  0.0f,
                         ix,  iy,  0.0f
                };
                float x = (fragmentWidth * column) * 1.0f / totalWidth;
                float y = (fragmentHeight * row) * 1.0f / totalHeight;
                float[] tex = new float[]{
                        x - baseX,  y,
                        x - baseX,  y - baseY,
                        x,          y - baseY,
                        x,          y
                };
                int[]indices = new int[]{
                        0,1,2,
                        0,2,3
                };
                symbolMap.put(description, new Symbol(pos,tex,indices,description));
                index++;
            }
        }

        float[] p = new float[]{
                -ix,  iy,  0.0f,
                -ix, -iy,  0.0f,
                 ix, -iy,  0.0f,
                 ix,  iy,  0.0f
        };
        float[] t = new float[]{
                0.0f,  0.0f,
                0.0f,  baseY,
                baseX, baseY,
                baseX, 0.0f
        };
        int[] i = new int[]{
                0,1,2,
                0,2,3
        };
        Symbol sym = new Symbol(p,t,i,' ');
        sym.setEmpty(true);
        symbolMap.put(' ',sym);
    }

    public List<Symbol> getSymbols(String text, Vector3f position, float size, int typing){
        List<Symbol> symbols = new ArrayList<>();

        boolean first = true;
        float halfOffset = fragmentSize * size / 2.0f;
        float offset = fragmentSize * size;
        Vector3f tempPos = new Vector3f();

        for(int i=0; i<text.length(); i++){
            char description = text.charAt(i);
            Symbol symbol = new Symbol(symbolMap.get(description));

            if(typing == TYPING_LEFT){
                if(first){
                    tempPos = new Vector3f(position);
                    symbol.setPosition(tempPos);
                    symbol.setSize(size);
                    first = false;
                }else{
                    tempPos = new Vector3f(tempPos.add(new Vector3f(offset,0.0f,0.0f)));
                    symbol.setPosition(tempPos);
                    symbol.setSize(size);
                }
            }else if(typing == TYPING_RIGHT){

            }else if(typing == TYPING_CENTER) {
                if (text.length() % 2 == 0) {
                    if (first) {
                        float multiplier = (text.length() / 2.0f) - 1.0f;
                        float x = position.getX() - (halfOffset + (offset * multiplier));
                        tempPos = new Vector3f(x, position.getY(), position.getZ());
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                        first = false;
                    } else {
                        tempPos = new Vector3f(tempPos.add(new Vector3f(offset, 0.0f, 0.0f)));
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                    }
                } else {
                    if (first) {
                        float multiplier = text.length() / 2.0f - 0.5f;
                        float x = position.getX() - (offset * multiplier);
                        tempPos = new Vector3f(x, position.getY(), position.getZ());
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                        first = false;
                    } else {
                        tempPos = new Vector3f(tempPos.add(new Vector3f(offset, 0.0f, 0.0f)));
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                    }
                }
            }
            symbols.add(symbol);
        }

        return  symbols;
    }

    public void drawText(List<Symbol> symbols, Shader shader){
        shader.useProgram();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, alphabetTexture.getTextureID());
        shader.setUniform("image",0);
        for(Symbol symbol : symbols){
            symbol.draw(shader);
        }
    }

    public void drawText(String text, Vector3f position, float size, int typing, Shader shader){
        shader.useProgram();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, alphabetTexture.getTextureID());
        shader.setUniform("image",0);

        float halfOffset = fragmentSize * size / 2.0f;
        float offset = fragmentSize * size;
        boolean first = true;
        Vector3f tempPos = new Vector3f();

        for(Symbol symbol : readText(text)){
            if(typing == TYPING_LEFT){
                if(first){
                    tempPos = new Vector3f(position);
                    symbol.setPosition(tempPos);
                    symbol.setSize(size);
                    first = false;
                }else{
                    tempPos = new Vector3f(tempPos.add(new Vector3f(offset,0.0f,0.0f)));
                    symbol.setPosition(tempPos);
                    symbol.setSize(size);
                }
            }else if(typing == TYPING_RIGHT){

            }else if(typing == TYPING_CENTER){
                if(text.length() % 2 == 0){
                    if(first){
                        float multiplier = (text.length() / 2.0f) - 1.0f;
                        float x = position.getX() - (halfOffset + (offset * multiplier));
                        tempPos = new Vector3f(x,position.getY(),position.getZ());
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                        first = false;
                    }else{
                        tempPos = new Vector3f(tempPos.add(new Vector3f(offset,0.0f,0.0f)));
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                    }
                }else{
                    if(first){
                        float multiplier = text.length() / 2 + 1;
                        float x = position.getX() - (offset * multiplier);
                        tempPos = new Vector3f(x,position.getY(),position.getZ());
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                        first = false;
                    }else{
                        tempPos = new Vector3f(tempPos.add(new Vector3f(offset,0.0f,0.0f)));
                        symbol.setPosition(tempPos);
                        symbol.setSize(size);
                    }
                }
            }
            symbol.draw(shader);
        }
    }

    private List<Symbol> readText(String text){
        List<Symbol> sym = new ArrayList<>();

        for(int i=0; i<text.length(); i++){
            char description = text.charAt(i);
            sym.add(symbolMap.get(description));
        }

        return  sym;
    }
}
