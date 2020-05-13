package ru.phoenix.game.hud.elements.strucet;

import ru.phoenix.core.loader.texture.Texture;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.core.shader.Shader;
import ru.phoenix.core.util.Collector;
import ru.phoenix.game.hud.elements.HeadsUpDisplay;
import ru.phoenix.game.hud.elements.LayoutDisplay;

import static ru.phoenix.core.config.Constants.GROUP_R;

public class Button extends LayoutDisplay implements HeadsUpDisplay{
    public Button(Texture texture, float width, float height, Vector3f position, boolean discard, float id){
        super();
        init(texture,width,height,position,discard,id);
    }

    private void init(Texture texture,float width, float height, Vector3f position, boolean discard, float id){
        setTexture(texture);
        setPosition(position);

        setGroup(GROUP_R);
        setId(id);

        setDiscard(discard);

        setVbo(Collector.getStruct(width,height));
    }

    @Override
    public void selected(){
        setTarget(false);
        setSize(0.96f);
    }

    @Override
    public void update(Vector3f pixel){
        updateProjection();
        float id = pixel.getX();
        if(id == getId()){
            setTarget(true);
        }else{
            setTarget(false);
        }
    }

    @Override
    public void draw(Shader shader){
        super.draw(shader);
    }
}
