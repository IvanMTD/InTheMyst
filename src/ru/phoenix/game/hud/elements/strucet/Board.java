package ru.phoenix.game.hud.elements.strucet;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.core.util.Collector;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.LayoutDisplay;

public class Board extends LayoutDisplay implements HeadsUpDisplay {
    public Board(Texture texture,float width, float height, Vector3f position, boolean discard){
        super();
        init(texture,width,height,position,discard);
    }

    private void init(Texture texture,float width, float height, Vector3f position, boolean discard){
        setTexture(texture);
        setPosition(position);

        setDiscard(discard);

        setVbo(Collector.getStruct(width,height));
    }

    @Override
    public void selected(){

    }

    @Override
    public void update(Vector3f pixel){
        updateProjection();
    }

    @Override
    public void draw(Shader shader){
        super.draw(shader);
    }
}
