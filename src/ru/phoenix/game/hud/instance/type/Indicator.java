package ru.phoenix.game.hud.instance.type;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.hud.instance.Board;
import ru.phoenix.game.hud.instance.BoardControl;

public class Indicator extends BoardControl implements Board {
    // Конструкторы класса
    public Indicator(String texturePath, float width, Vector3f offsetPosition){
        super();
        init(texturePath,width,offsetPosition);
    }

    @Override
    public void update(Vector3f currentPos){
        super.update(currentPos);
    }
}
