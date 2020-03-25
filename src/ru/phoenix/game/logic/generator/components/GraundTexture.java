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

    private Texture desert_low;
    private Texture desert_side;
    private Texture desert_up;

    private Texture steppe_low;
    private Texture steppe_side;
    private Texture steppe_up;

    private Texture plain_low;
    private Texture plain_side;
    private Texture plain_up;

    private Texture forest_low;
    private Texture forest_side;
    private Texture forest_up;

    private Texture mountains_low;
    private Texture mountains_side;
    private Texture mountains_up;

    private Texture road;

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

        desert_low = new Texture2D();
        desert_side = new Texture2D();
        desert_up = new Texture2D();

        steppe_low = new Texture2D();
        steppe_side = new Texture2D();
        steppe_up = new Texture2D();

        plain_low = new Texture2D();
        plain_side = new Texture2D();
        plain_up = new Texture2D();

        forest_low = new Texture2D();
        forest_side = new Texture2D();
        forest_up = new Texture2D();

        mountains_low = new Texture2D();
        mountains_side = new Texture2D();
        mountains_up = new Texture2D();

        road = new Texture2D();

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

        desert_low.setup(null,"./data/content/texture/atlas/graund/desert_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        desert_side.setup(null,"./data/content/texture/atlas/graund/desert_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        desert_up.setup(null,"./data/content/texture/atlas/graund/desert_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        steppe_low.setup(null,"./data/content/texture/atlas/graund/steppe_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        steppe_side.setup(null,"./data/content/texture/atlas/graund/steppe_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        steppe_up.setup(null,"./data/content/texture/atlas/graund/steppe_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        plain_low.setup(null,"./data/content/texture/atlas/graund/plain_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        plain_side.setup(null,"./data/content/texture/atlas/graund/plain_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        plain_up.setup(null,"./data/content/texture/atlas/graund/plain_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        forest_low.setup(null,"./data/content/texture/atlas/graund/forest_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        forest_side.setup(null,"./data/content/texture/atlas/graund/forest_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        forest_up.setup(null,"./data/content/texture/atlas/graund/forest_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        mountains_low.setup(null,"./data/content/texture/atlas/graund/mountains_low.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        mountains_side.setup(null,"./data/content/texture/atlas/graund/mountains_side.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);
        mountains_up.setup(null,"./data/content/texture/atlas/graund/mountains_up.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

        road.setup(null,"./data/content/texture/atlas/graund/road.png",GL_SRGB_ALPHA,GL_CLAMP_TO_EDGE);

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

    public Texture getDesert_low() {
        return desert_low;
    }

    public Texture getDesert_side() {
        return desert_side;
    }

    public Texture getDesert_up() {
        return desert_up;
    }

    public Texture getSteppe_low() {
        return steppe_low;
    }

    public Texture getSteppe_side() {
        return steppe_side;
    }

    public Texture getSteppe_up() {
        return steppe_up;
    }

    public Texture getPlain_low() {
        return plain_low;
    }

    public Texture getPlain_side() {
        return plain_side;
    }

    public Texture getPlain_up() {
        return plain_up;
    }

    public Texture getForest_low() {
        return forest_low;
    }

    public Texture getForest_side() {
        return forest_side;
    }

    public Texture getForest_up() {
        return forest_up;
    }

    public Texture getMountains_low() {
        return mountains_low;
    }

    public Texture getMountains_side() {
        return mountains_side;
    }

    public Texture getMountains_up() {
        return mountains_up;
    }

    public Texture getRoad() {
        return road;
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
