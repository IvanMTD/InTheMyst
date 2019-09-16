package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.generator.GraundGenerator;

import java.util.ArrayList;
import java.util.List;

public class GridMaster extends Thread {

    private int action;
    private List<Vector3f> gridPosition;
    private Vector3f mainPosition;
    private Characteristic characteristic;

    public GridMaster(){
        action = 0;
        gridPosition = new ArrayList<>();
        mainPosition = new Vector3f();
        characteristic = new Characteristic();
    }

    public void preperMove(Vector3f mainPosition, Characteristic characteristic){
        this.mainPosition = new Vector3f(mainPosition);
        this.characteristic = new Characteristic(characteristic);
        action = 1;
    }

    public void run(){
        if(action == 1) {
            gridPosition.clear();
            int step = 0;
            List<Vector3f> startPos = new ArrayList<>();
            startPos.add(mainPosition);
            while (step < characteristic.getCurentActionPoint()) {
                step++;
                List<Vector3f> tempPos = new ArrayList<>();
                for (Vector3f start : startPos) {
                    Vector3f left = new Vector3f(start.getX() - 1.0f, start.getY(), start.getZ());
                    if (checkPosition(left, characteristic)) {
                        gridPosition.add(left);
                        tempPos.add(left);
                    }
                    Vector3f right = new Vector3f(start.getX() + 1.0f, start.getY(), start.getZ());
                    if (checkPosition(right, characteristic)) {
                        gridPosition.add(right);
                        tempPos.add(right);
                    }
                    Vector3f up = new Vector3f(start.getX(), start.getY(), start.getZ() + 1.0f);
                    if (checkPosition(up, characteristic)) {
                        gridPosition.add(up);
                        tempPos.add(up);
                    }
                    Vector3f down = new Vector3f(start.getX(), start.getY(), start.getZ() - 1.0f);
                    if (checkPosition(down, characteristic)) {
                        gridPosition.add(down);
                        tempPos.add(down);
                    }
                }
                startPos.clear();
                startPos.addAll(tempPos);
            }

            for (int i = 0; i < gridPosition.size() - 1; i++) {
                for (int j = i + 1; j < gridPosition.size(); j++) {
                    if (gridPosition.get(i).equals(gridPosition.get(j))) {
                        gridPosition.remove(j);
                        j--;
                    }
                }
            }

            for (GridElement element : GraundGenerator.getGridElements()) {
                for (Vector3f position : gridPosition) {
                    if (element.getPosition().equals(position)) {
                        element.setVisible(true);
                    }
                }
            }
        }
    }

    private boolean checkPosition(Vector3f somePosition, Characteristic characteristic){
        boolean check = false;
        float min = somePosition.getY() - characteristic.getJump();
        float max = somePosition.getY() + characteristic.getJump();

        for(GridElement element : GraundGenerator.getGridElements()){
            if(element.getPosition().equals(somePosition)){
                if(!element.isBlocked()){
                    if(min <= element.getCurrentHeight() && element.getCurrentHeight() <= max){
                        somePosition.setY(element.getCurrentHeight());
                        check = true;
                    }
                }
            }
        }

        return check;
    }
}
