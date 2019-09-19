package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;
import ru.phoenix.game.logic.element.GridNode;
import ru.phoenix.game.logic.generator.GraundGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class GridMaster extends Thread {

    private Vector3f startPosition;
    private Vector3f finishPosition;
    private Characteristic characteristic;

    private int event;

    public GridMaster(){
        startPosition = new Vector3f();
        characteristic = new Characteristic();
        event = 0;
    }

    public void createMoveGrid(Vector3f start, Characteristic characteristic){
        setStartPosition(start);
        setCharacteristic(characteristic);
        event = 1;
    }

    public void createPath(Vector3f start, Vector3f finish, Characteristic characteristic){
        setStartPosition(start);
        setFinishPosition(finish);
        setCharacteristic(characteristic);
        event = 2;
    }

    public void run(){
        if(event == 1){
            float time1 = (float)glfwGetTime();
            GridNode root = new GridNode(getStartPosition(),0);
            root.setParent(null);
            root.initGrid(getCharacteristic(),1);
            List<Vector3f> positions = new ArrayList<>();
            root.getAllPosition(positions);
            System.out.println("Всего найдено позиций: " + positions.size());
            for(int i=0; i<positions.size() - 1; i++){
                for(int j=i+1; j<positions.size(); j++){
                    if(positions.get(i).equals(positions.get(j))){
                        positions.remove(j);
                        j--;
                    }
                }
            }
            System.out.println("Всего позиций после чистки: " + positions.size());
            System.out.println("Всего потраченно времени: " + ((float)glfwGetTime() - time1));

            for(GridElement element : GraundGenerator.getGridElements()){
                element.setVisible(false);
                element.setWayPoint(false);
                for(Vector3f position : positions){
                    if(element.getPosition().equals(position)){
                        element.setVisible(true);
                    }
                }
            }

            GraundGenerator.setRoot(root);
        }else if(event == 2){

            for(GridElement element : GraundGenerator.getGridElements()){
                element.setWayPoint(false);
            }

            GridNode root = GraundGenerator.getRoot();
            List<GridNode> nodes = new ArrayList<>();
            root.findNodes(getFinishPosition(),nodes);

            int minStep = characteristic.getTotalActionPoint();
            for(GridNode node : nodes){
                if(minStep > node.getStep()){
                    minStep = node.getStep();
                }
            }

            List<GridNode> finalNodes = new ArrayList<>();
            for(GridNode node : nodes){
                if(node.getStep() == minStep){
                    finalNodes.add(node);
                }
            }

            if(finalNodes.size() == 1){
                GridNode node = finalNodes.get(0);
                List<Vector3f> reversePoint = new ArrayList<>();
                while(node.getParent() != null){
                    reversePoint.add(node.getPosition());
                    node = node.getParent();
                }

                List<Vector3f> points = new ArrayList<>();
                for(int i=reversePoint.size() - 1; i >= 0; i--){
                    points.add(reversePoint.get(i));
                }

                GraundGenerator.setWayPoints(points);
            }else{
                GridNode node = finalNodes.get(finalNodes.size() / 4);

                List<Vector3f> reversePoint = new ArrayList<>();
                while(node.getParent() != null){
                    reversePoint.add(node.getPosition());
                    node = node.getParent();
                }

                List<Vector3f> points = new ArrayList<>();
                for(int i=reversePoint.size() - 1; i >= 0; i--){
                    points.add(reversePoint.get(i));
                }

                GraundGenerator.setWayPoints(points);
            }

            for (GridElement element : GraundGenerator.getGridElements()) {
                for (Vector3f point : GraundGenerator.getWayPoints()) {
                    if (element.getPosition().equals(point)) {
                        element.setWayPoint(true);
                    }
                }
            }
        }
    }

    public Vector3f getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Vector3f startPosition) {
        this.startPosition = startPosition;
    }

    public Vector3f getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(Vector3f finishPosition) {
        this.finishPosition = finishPosition;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }
}
