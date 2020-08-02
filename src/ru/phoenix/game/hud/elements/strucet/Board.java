package ru.phoenix.game.hud.elements.strucet;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.core.util.Collector;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.LayoutDisplay;

import static ru.phoenix.core.config.Constants.*;

public class Board extends LayoutDisplay implements HeadsUpDisplay {
    public Board(Texture texture,float width, float height, Vector3f position, boolean discard){
        super();
        init(texture,width,height,position,discard);
        setFakeTarget(true);
    }

    public Board(Texture texture,float width, float height, Vector3f position, boolean discard, int group, float id){
        super();
        init(texture,width,height,position,discard);
        setGroup(group);
        setId(id);
        setFakeTarget(true);
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
    public void selected(float size) {

    }

    @Override
    public void update(Vector3f pixel){
        updateProjection();
        if(getGroup() != GROUP_A){
            float id = 0.0f;
            if(getGroup() == GROUP_R){
                id = pixel.getR();
            }else if(getGroup() == GROUP_G){
                id = pixel.getG();
            }else if(getGroup() == GROUP_B){
                id = pixel.getB();
            }

            if(id == getId()){
                setTarget(true);
            }else{
                setTarget(false);
            }
        }
    }

    @Override
    public void draw(Shader shader){
        super.draw(shader);
    }
}
