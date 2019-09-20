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

    private final int CONFIGURE_GRAPHS      = 0x99100;
    private final int PREPARE_AREA          = 0x99101;

    private List<GridElement> graphs;
    private Characteristic characteristic;
    private Vector3f start;

    private List<GridElement> frontier;
    private List<GridElement> visited;

    public PathfindingAlgorithm(){
        frontier = new ArrayList<>();
        visited = new ArrayList<>();
    }

    public void setup(List<GridElement> elements, Characteristic characteristic){
        this.graphs = elements;
        this.characteristic = characteristic;
        event = CONFIGURE_GRAPHS;
    }

    public void setup(Vector3f startPosition){
        start = new Vector3f(startPosition);
        event = PREPARE_AREA;
    }

    public void run(){
        switch (event){
            case CONFIGURE_GRAPHS:
                configureGraphs();
                break;
            case PREPARE_AREA:
                prepareArea();
                break;
        }
    }

    private void configureGraphs(){
        for(GridElement graph : graphs){
            graph.setVisible(false);
            graph.setWayPoint(false);
        }
        Map<Integer,Vector3f> direction = new HashMap<>();
        direction.put(0,new Vector3f(-1.0f,0.0f,0.0f)); // left
        direction.put(1,new Vector3f(0.0f,0.0f,1.0f)); // up
        direction.put(2,new Vector3f(1.0f,0.0f,0.0f)); // right
        direction.put(3,new Vector3f(0.0f,0.0f,-1.0f)); // down
        for(GridElement graph : graphs){
            graph.clearDirection();
            for(int key = 0; key < direction.size(); key++) {
                Vector3f studyPosition = new Vector3f(graph.getPosition().add(direction.get(key)));
                if(checkStudyPosition(graph, studyPosition)){
                    if(key == 0){
                        graph.setLeft(true);
                    }else if(key == 1){
                        graph.setUp(true);
                    }else if(key == 2){
                        graph.setRight(true);
                    }else if(key == 3){
                        graph.setDown(true);
                    }
                }
            }
        }
    }

    private void prepareArea(){
        frontier.clear();
        GridElement startGraph = findGraph(start);
        startGraph.setStep(0);
        frontier.add(startGraph);
        visited.add(startGraph);

        while(!frontier.isEmpty()){
            List<GridElement>currentFrontier = new ArrayList<>(frontier);
            for(GridElement front : currentFrontier){
                if(front.getStep() < characteristic.getCurentActionPoint()) {
                    if (front.isUp()) {
                        GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                        if (!isVisited(studyGraph)) {
                            studyGraph.setCameFromElement(front);
                            studyGraph.setStep(front.getStep() + 1);
                            frontier.add(studyGraph);
                            visited.add(studyGraph);
                        }
                    }
                    if (front.isRight()) {
                        GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                        if (!isVisited(studyGraph)) {
                            studyGraph.setCameFromElement(front);
                            studyGraph.setStep(front.getStep() + 1);
                            frontier.add(studyGraph);
                            visited.add(studyGraph);
                        }
                    }
                    if (front.isDown()) {
                        GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                        if (!isVisited(studyGraph)) {
                            studyGraph.setCameFromElement(front);
                            studyGraph.setStep(front.getStep() + 1);
                            frontier.add(studyGraph);
                            visited.add(studyGraph);
                        }
                    }
                    if (front.isLeft()) {
                        GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                        if (!isVisited(studyGraph)) {
                            studyGraph.setCameFromElement(front);
                            studyGraph.setStep(front.getStep() + 1);
                            frontier.add(studyGraph);
                            visited.add(studyGraph);
                        }
                    }
                }
                frontier.remove(front);
            }
        }

        System.out.println(visited.size());

        for(GridElement element : graphs){
            for(GridElement visit : visited){
                if(element.getPosition().equals(visit.getPosition())){
                    element.setVisible(true);
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

    private boolean isVisited(GridElement studyGraph){
        boolean check = false;

        for(GridElement visit : visited){
            if(visit.getPosition().equals(studyGraph.getPosition())){
                check = true;
                break;
            }
        }

        return check;
    }
}
