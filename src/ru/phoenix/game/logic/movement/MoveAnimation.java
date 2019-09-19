package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;

import java.util.ArrayList;
import java.util.List;

public class MoveAnimation {
    private float goal;
    private int index;

    private Vector3f tempPos;
    private Vector3f tempPos2;
    private List<Vector3f> wayPoints;

    public MoveAnimation(){
        goal = 0.0f;
        index = 0;

        tempPos = new Vector3f();
        tempPos2 = new Vector3f();
        wayPoints = new ArrayList<>();
    }

    public void setup(Vector3f start, List<Vector3f> wayPoints){
        tempPos = new Vector3f(start);
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public boolean move(Vector3f position, Characteristic characteristic){
        boolean end = false;
        goal += characteristic.getSpeed(); // фактор движения в приделах от 0-1
        Vector3f start = new Vector3f(this.tempPos); // точка старта
        Vector3f finish = new Vector3f(wayPoints.get(index)); // точка назначения
        Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
        Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
        position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

        if (goal >= 1.0f) {
            goal = 0.0f;
            this.tempPos = new Vector3f(wayPoints.get(index));
            index++;
            if (characteristic.getCurentActionPoint() <= 0) {
                index = 0;
                goal = 0;
                wayPoints.clear();
                tempPos = new Vector3f();
                end = true;
            }
            if (index <= wayPoints.size()) {
                characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 1);
            }
            if (index >= wayPoints.size()) {
                position.setVector(wayPoints.get(index - 1));
                index = 0;
                goal = 0;
                wayPoints.clear();
                tempPos = new Vector3f();
                end = true;
            }
        }
        return end;
    }
}
