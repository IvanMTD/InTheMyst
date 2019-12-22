package ru.phoenix.game.logic.element.grid;

import ru.phoenix.core.kernel.Camera;
import ru.phoenix.core.kernel.Input;
import ru.phoenix.core.kernel.Window;
import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Vector2f;
import ru.phoenix.core.math.Vector3f;

public class Cell extends CellDrawing {
    // кострукторы класса
    // начало
    public Cell(){
        super();
    }

    public Cell(Cell cell){
        super(cell);
    }
    // конец

    // методы класса
    // начало
    public void update(Vector3f pixel){
        if(pixel.getY() == getId()){
            setTarget(true);
        }else{
            setTarget(false);
        }
    }
    // конец
}
