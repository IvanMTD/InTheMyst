package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathfindingAlgorithm extends Thread {
    private int event;

    private final int PREPARE_AREA          = 0x99100;
    private final int CREATE_PATH           = 0x99101;

    private List<GridElement> graphs;
    private Characteristic characteristic;
    private Vector3f start;
    private Vector3f finish;

    private List<GridElement> frontier;
    private List<GridElement> costSoFar;
    private List<GridElement> cameFrom;

    public PathfindingAlgorithm(){
        frontier = new ArrayList<>();
        costSoFar = new ArrayList<>();
        cameFrom = new ArrayList<>();
    }

    public void setup(List<GridElement> elements, Characteristic characteristic,Vector3f startPosition){
        this.graphs = new ArrayList<>(elements);
        this.characteristic = characteristic;
        start = new Vector3f(startPosition);
        event = PREPARE_AREA;
    }

    public void setup(List<GridElement> elements, Characteristic characteristic,Vector3f startPosition, Vector3f finishPosition){
        this.graphs = new ArrayList<>(elements);
        this.characteristic = characteristic;
        start = new Vector3f(startPosition);
        finish = finishPosition;
        event = CREATE_PATH;
    }

    public void run(){
        switch (event){
            case PREPARE_AREA:
                configureGraphs();
                prepareArea();
                break;
            case CREATE_PATH:
                createPath();
                break;
        }
    }

    private void configureGraphs(){
        Map<Integer,Vector3f> direction = new HashMap<>();
        direction.put(0,new Vector3f(-1.0f,0.0f,0.0f)); // left
        direction.put(1,new Vector3f(0.0f,0.0f,1.0f)); // up
        direction.put(2,new Vector3f(1.0f,0.0f,0.0f)); // right
        direction.put(3,new Vector3f(0.0f,0.0f,-1.0f)); // down
        for(GridElement graph : graphs){
            if(graph.getPosition().sub(start).length() <= characteristic.getCurentActionPoint() * 2) {
                graph.setVisible(false);
                graph.setWayPoint(false);
                graph.clearDirection();
                for (int key = 0; key < direction.size(); key++) {
                    Vector3f studyPosition = new Vector3f(graph.getPosition().add(direction.get(key)));
                    if (checkStudyPosition(graph, studyPosition)) {
                        switch (key) {
                            case 0:
                                graph.setLeft(true);
                                break;
                            case 1:
                                graph.setUp(true);
                                break;
                            case 2:
                                graph.setRight(true);
                                break;
                            case 3:
                                graph.setDown(true);
                                break;
                        }
                    }
                }
            }
        }
    }

    private void prepareArea(){
        frontier.clear();
        costSoFar.clear();
        cameFrom.clear();
        GridElement startGraph = findGraph(start);
        startGraph.setStep(0);
        frontier.add(startGraph);
        costSoFar.add(startGraph);
        cameFrom.add(startGraph);

        int index = 0;

        while(!frontier.isEmpty()) {
            List<GridElement> currentFrontier = new ArrayList<>(frontier);
            if(index == 0){
                for (GridElement front : currentFrontier) {
                    if (front.getStep() < characteristic.getCurentActionPoint()) {
                        if (front.isUp()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                            setCostStep(front,studyGraph);
                        }
                        if (front.isLeft()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                            setCostStep(front,studyGraph);
                        }
                        if (front.isDown()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                            setCostStep(front,studyGraph);
                        }
                        if (front.isRight()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                            setCostStep(front,studyGraph);
                        }
                    }
                    frontier.remove(front);
                }
            }else if(index == 1){
                for (GridElement front : currentFrontier) {
                    if (front.getStep() < characteristic.getCurentActionPoint()) {
                        if (front.isRight()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                            setCostStep(front,studyGraph);
                        }
                        if (front.isDown()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                            setCostStep(front,studyGraph);
                        }
                        if (front.isLeft()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                            setCostStep(front,studyGraph);
                        }
                        if (front.isUp()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                            setCostStep(front,studyGraph);
                        }
                    }
                    frontier.remove(front);
                }
            }
            index++;
            if(index >= 2){
                index = 0;
            }
        }

        for(GridElement element : graphs){
            for(GridElement came : cameFrom){
                if(element.getPosition().equals(came.getPosition()) && !element.isBlocked()){
                    element.setVisible(true);
                }
            }
        }
    }

    private void createPath(){
        for (GridElement graph : graphs) {
            if(finish != null) {
                if (!graph.getPosition().equals(finish)) {
                    graph.setTarget(false);
                }
            }
            graph.setWayPoint(false);
        }
        if(finish != null) {
            GridElement element = findGraph(finish);
            List<Vector3f> wayPoints = new ArrayList<>();
            while (element.cameFrom() != null) {
                Vector3f position = new Vector3f(element.getPosition());
                position.setY(element.getCurrentHeight());
                wayPoints.add(position);
                element = element.cameFrom();
            }
            for (GridElement graph : graphs) {
                for (Vector3f wayPoint : wayPoints) {
                    if (graph.getPosition().equals(wayPoint)) {
                        graph.setWayPoint(true);
                    }
                }
            }
        }else{
            for (GridElement graph : graphs) {
                if (graph.isVisible()) {
                    graph.setWayPoint(false);
                }
            }
        }
    }

    private boolean checkStudyPosition(GridElement parentElement, Vector3f studyPosition){
        boolean check = false;
        float min = parentElement.getCurrentHeight() - characteristic.getJump();
        float max = parentElement.getCurrentHeight() + characteristic.getJump();

        for(GridElement graph : graphs){
            if(graph.getPosition().equals(studyPosition) && !graph.isBlocked()){
                if(min <= graph.getCurrentHeight() && graph.getCurrentHeight() <= max){
                    check = true;
                }
            }
        }

        return check;
    }

    private GridElement findGraph(Vector3f position){
        GridElement graph = null;

        for(GridElement element : graphs){
            if(element.getPosition().equals(position)){
                graph = element;
            }
        }

        return graph;
    }

    private boolean checkVisit(GridElement studyGraph){
        boolean check = false;

        for(GridElement came : cameFrom){
            if(came.getPosition().equals(studyGraph.getPosition())){
                check = true;
                break;
            }
        }

        return !check;
    }

    private void setStep(GridElement front, GridElement studyGraph){
        if (checkVisit(studyGraph) && (front.getStep() + studyGraph.getTravelCost() <= characteristic.getCurentActionPoint())) {
            studyGraph.setCameFromElement(front);
            studyGraph.setStep(front.getStep() + studyGraph.getTravelCost());
            frontier.add(studyGraph);
            cameFrom.add(studyGraph);
        }
    }

    private void setCostStep(GridElement front, GridElement studyGraph){
        int cost = front.getStep() + studyGraph.getTravelCost();
        if(checkCostVisit(studyGraph,cost) &&(front.getStep() + studyGraph.getTravelCost() <= characteristic.getCurentActionPoint())){
            studyGraph.setCameFromElement(front);
            studyGraph.setStep(front.getStep() + studyGraph.getTravelCost());
            frontier.add(studyGraph);
            costSoFar.add(studyGraph);
            cameFrom.add(studyGraph);
        }
    }

    private boolean checkCostVisit(GridElement studyGraph, int cost){
        boolean check = true;

        for(int i=0; i<costSoFar.size(); i++){
            if(costSoFar.get(i).getPosition().equals(studyGraph.getPosition())){
                check = false;
                if(cost < costSoFar.get(i).getStep()) {
                    costSoFar.remove(costSoFar.get(i));
                    check = true;
                }
            }
        }

        return check;
    }
}
