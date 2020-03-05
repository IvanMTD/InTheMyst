package ru.phoenix.game.logic.generator.components;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.loader.texture.Texture2D;

import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL21.GL_SRGB_ALPHA;

public class GraundTexture {
    // атлас поверхностей
    private Texture graundAtlas;
    private Texture graundAtlasNormalMap;

    private Texture grass;
    private Texture dirt;
    private Texture snow;
    private Texture soil;

    // элементы сетки
    private Texture cursor;
    private Texture grayZona;
    private Texture redZona;
    private Texture greenZona;
    private Texture goldZona;
    private Texture blueZona;

    // конструкторы - начало
    public GraundTexture(){
        // атлас поверхностей
        graundAtlas = new Texture2D();
        graundAtlasNormalMap = new Texture2D();
        grass = new Texture2D();
        dirt = new Texture2D();
        snow = new Texture2D();
        soil = new Texture2D();
        // элементы сетки
        cursor = new Texture2D();
        grayZona = new Texture2D();
        redZona = new Texture2D();
        greenZona = new Texture2D();
        goldZona = new Texture2D();
        blueZona = new Texture2D();

    }
    // конструкторы - конец

    // методы инициализции - начало
    public void init(){
        // атлас поверхностей
        graundAtlas.setup(null,"./data/content/texture/atlas/graund/graund_atlas.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        graundAtlasNormalMap.setup(null,"./data/content/texture/atlas/graund/graund_atlas_normalMap.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        grass.setup(null,"./data/content/texture/atlas/graund/grass.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        dirt.setup(null,"./data/content/texture/atlas/graund/dirt.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        snow.setup(null,"./data/content/texture/atlas/graund/snow.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        soil.setup(null,"./data/content/texture/atlas/graund/soil.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        // элементы сетки
        cursor.setup(null,"data/content/texture/zona/cursor.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        grayZona.setup(null,"./data/content/texture/zona/grid_element_gray.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        redZona.setup(null,"./data/content/texture/zona/grid_element_red.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        greenZona.setup(null,"./data/content/texture/zona/grid_element_green.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        goldZona.setup(null,"./data/content/texture/zona/grid_element_gold.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
        blueZona.setup(null,"./data/content/texture/zona/grid_element_blue.png",GL_SRGB_ALPHA,GL_CLAMP_TO_BORDER);
    }
    // методы инициализации - конец

    // методы гетеры - начало
    public Texture getGraundAtlas() {
        return graundAtlas;
    }

    public Texture getGraundAtlasNormalMap() {
        return graundAtlasNormalMap;
    }

    public Texture getGrass() {
        return grass;
    }

    public Texture getDirt() {
        return dirt;
    }

    public Texture getSnow() {
        return snow;
    }

    public Texture getSoil() {
        return soil;
    }

    public Texture getCursor() {
        return cursor;
    }

    public Texture getGrayZona() {
        return grayZona;
    }

    public Texture getRedZona() {
        return redZona;
    }

    public Texture getGreenZona() {
        return greenZona;
    }

    public Texture getGoldZona() {
        return goldZona;
    }

    public Texture getBlueZona() {
        return blueZona;
    }

    // методы гетеры - конец
}
