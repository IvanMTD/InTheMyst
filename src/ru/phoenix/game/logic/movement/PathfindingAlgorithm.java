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
                //createPath();
                createPathAStar();
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
            if(graph.getPosition().sub(start).length() <= ((characteristic.getMove() * characteristic.getCurentActionPoint()) + 2)) {
                graph.setVisible(false);
                graph.setWayPoint(false);
                graph.setGrayZona();
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
        startGraph.setCost(0);
        frontier.add(startGraph);
        costSoFar.add(startGraph);
        cameFrom.add(startGraph);

        int index = 0;

        while(!frontier.isEmpty()) {
            List<GridElement> currentFrontier = new ArrayList<>(frontier);
            if(index == 0){
                for (GridElement front : currentFrontier) {
                    if (front.getStep() < (characteristic.getMove() * characteristic.getCurentActionPoint())) {
                        if (front.isUp()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                            setStep(front,studyGraph);
                        }
                        if (front.isLeft()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                            setStep(front,studyGraph);
                        }
                        if (front.isDown()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                            setStep(front,studyGraph);
                        }
                        if (front.isRight()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                            setStep(front,studyGraph);
                        }
                    }
                    frontier.remove(front);
                }
            }else if(index == 1){
                for (GridElement front : currentFrontier) {
                    if (front.getStep() < (characteristic.getMove() * characteristic.getCurentActionPoint())) {
                        if (front.isRight()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                            setStep(front,studyGraph);
                        }
                        if (front.isDown()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                            setStep(front,studyGraph);
                        }
                        if (front.isLeft()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                            setStep(front,studyGraph);
                        }
                        if (front.isUp()) {
                            GridElement studyGraph = findGraph(front.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                            setStep(front,studyGraph);
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

    private void createPathAStar(){
        if(finish != null){
            for (GridElement graph : graphs) {
                if (!graph.getPosition().equals(finish)) {
                    graph.setTarget(false);
                }
                graph.setWayPoint(false);
            }
            GridElement resultFinish = createPath(true);
            List<GridElement> reverse = new ArrayList<>();

            if(resultFinish == null) {
                resultFinish = createPath(false);
            }

            assert resultFinish != null;
            while (resultFinish.cameFrom() != null) {
                reverse.add(resultFinish);
                resultFinish = resultFinish.cameFrom();
            }


            List<GridElement> wayPoints = new ArrayList<>();
            for(int i=reverse.size() - 1; i >= 0; i--){
                wayPoints.add(reverse.get(i));
            }

            int currentStamina = characteristic.getStamina();

            for(GridElement point : wayPoints){
                point.setWayPoint(true);
                currentStamina -= point.getTravelCost();
                if(currentStamina >= 0){
                    point.setGreenZona();
                }else{
                    point.setRedZona();
                }
            }
        }
    }

    private GridElement createPath(boolean restriction){
        GridElement resultFinish = null;
        GridElement Start = findGraph(start);
        GridElement Finish = findGraph(finish);
        List<GridElement> closedSet = new ArrayList<>();
        List<GridElement> openSet = new ArrayList<>();
        Start.setCameFromElement(null);
        Start.setStep(0);
        Start.setCost(getHeuristicPathLength(Start,Finish));
        openSet.add(Start);
        while (!openSet.isEmpty()){
            GridElement currentElement = minFullPathLength(openSet);
            if(currentElement.getPosition().equals(finish)){
                resultFinish = currentElement;
                break;
            }
            openSet.remove(currentElement);
            closedSet.add(currentElement);

            for(GridElement neighbour : getNeighbours(currentElement,Finish,restriction)){
                if(checkClosedSet(neighbour,closedSet)){
                    continue;
                }

                GridElement open = getOpenSetElement(neighbour,openSet);
                if(open == null){
                    int result = 0;
                    float h = Math.abs(currentElement.getCurrentHeight() - neighbour.getCurrentHeight());
                    if(h > 0){
                        if(Math.round(h) - h == 0.5f){
                            result = 1;
                        }else{
                            result = 3;
                        }
                    }

                    neighbour.setCameFromElement(currentElement);
                    neighbour.setStep(currentElement.getStep() + neighbour.getTravelCost() + result);
                    neighbour.setCost(getHeuristicPathLength(neighbour,Finish));
                    openSet.add(neighbour);
                }else{
                    if(open.getStep() > neighbour.getStep()){
                        open.setCameFromElement(currentElement);
                        open.setStep(neighbour.getStep());
                    }
                }
            }
        }
        return resultFinish;
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

    private boolean checkClosedSet(GridElement study, List<GridElement> closedSet){
        boolean visit = false;

        for(GridElement closed : closedSet){
            if(closed.getPosition().equals(study.getPosition())){
                visit = true;
                break;
            }
        }

        return visit;
    }

    private boolean checkVisit(GridElement studyGraph){
        boolean visit = false;

        for(GridElement came : cameFrom){
            if(came.getPosition().equals(studyGraph.getPosition())){
                visit = true;
                break;
            }
        }

        return !visit;
    }

    private void setStep(GridElement front, GridElement studyGraph){
        if (checkVisit(studyGraph) && (front.getStep() + 1 <= characteristic.getMove() * characteristic.getCurentActionPoint())) {
            studyGraph.setCameFromElement(front);
            studyGraph.setStep(front.getStep() + 1);
            if(studyGraph.getStep() <= characteristic.getMove()){
                studyGraph.setBlueZona();
            }else{
                studyGraph.setGoldZona();
            }
            frontier.add(studyGraph);
            cameFrom.add(studyGraph);
        }
    }

    private void setCostStep(GridElement front, GridElement studyGraph){

        int cost = front.getCost() + studyGraph.getTravelCost();

        if(checkCostVisit(studyGraph,cost) &&(front.getStep() + 1 <= (characteristic.getMove() * characteristic.getCurentActionPoint()))){
            studyGraph.setCameFromElement(front);
            studyGraph.setStep(front.getStep() + 1);
            studyGraph.setCost(front.getCost() + cost);
            if(studyGraph.getStep() <= characteristic.getMove()){
                studyGraph.setBlueZona();
            }else{
                studyGraph.setGoldZona();
            }
            frontier.add(studyGraph);
            costSoFar.add(studyGraph);
            cameFrom.add(studyGraph);
        }
    }

    private boolean checkCostVisit(GridElement studyGraph, float cost){
        boolean check = true;

        for(int i=0; i<costSoFar.size(); i++){
            if(costSoFar.get(i).getPosition().equals(studyGraph.getPosition())){
                check = false;
                if(cost < costSoFar.get(i).getCost()) {
                    costSoFar.remove(costSoFar.get(i));
                    check = true;
                }
            }
        }

        return check;
    }

    private static int getHeuristicPathLength(GridElement from, GridElement to) {
        return (int)(Math.abs(from.getPosition().getX() - to.getPosition().getX()) + Math.abs(from.getPosition().getZ() - to.getPosition().getZ()));
    }

    private GridElement minFullPathLength(List<GridElement> elements){

        for (GridElement element : elements) {
            float lenght = element.getPosition().sub(finish).length();
            element.setDistance(lenght);
        }

        elements.sort(((o1, o2) -> o1.getEstimateFullPathLength() > o2.getEstimateFullPathLength() ? 0 : -1));

        int num = 1;

        for(int i=1; i<elements.size(); i++){
            if(start.getY() < finish.getY()){
                if(elements.get(0).getEstimateFullPathLength() + 1 >= elements.get(i).getEstimateFullPathLength()){
                    if(start.getY() <= elements.get(i).getCurrentHeight()){
                        num++;
                    }
                }
            }else if(start.getY() > finish.getY()){
                if(elements.get(0).getEstimateFullPathLength() + 1 >= elements.get(i).getEstimateFullPathLength()){
                    if(start.getY() >= elements.get(i).getCurrentHeight()){
                        num++;
                    }
                }
            }else{
                if(elements.get(0).getEstimateFullPathLength() == elements.get(i).getEstimateFullPathLength()){
                    num++;
                }
            }
        }

        for(int i=0; i<num-1;i++){
            for(int j=i+1; j<num; j++){
                if(elements.get(i).getDistance() > elements.get(j).getDistance()){
                    GridElement temp = elements.get(i);
                    elements.set(i,elements.get(j));
                    elements.set(j,temp);
                }
            }
        }

        return elements.get(0);
    }

    private List<GridElement> getNeighbours(GridElement element, GridElement finish, boolean restriction) {
        List<GridElement> result = new ArrayList<>();
        if(finish.isBlueZona()){
            if(restriction){
                if (element.isUp()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        if(element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f){
                            result.add(studyGraph);
                        }
                    }
                }
                if (element.isLeft()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        if(element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f){
                            result.add(studyGraph);
                        }
                    }
                }
                if (element.isDown()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        if(element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f){
                            result.add(studyGraph);
                        }
                    }
                }
                if (element.isRight()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        if(element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f){
                            result.add(studyGraph);
                        }
                    }
                }
            }else {
                if (element.isUp()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        result.add(studyGraph);
                    }
                }
                if (element.isLeft()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        result.add(studyGraph);
                    }
                }
                if (element.isDown()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        result.add(studyGraph);
                    }
                }
                if (element.isRight()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                        result.add(studyGraph);
                    }
                }
            }
        }else if(finish.isGoldZona()){
            if(restriction) {
                if (element.isUp()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                    if (studyGraph.isVisible()) {
                        if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                            result.add(studyGraph);
                        }
                    }
                }
                if (element.isLeft()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible()) {
                        if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                            result.add(studyGraph);
                        }
                    }
                }
                if (element.isDown()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                    if (studyGraph.isVisible()) {
                        if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                            result.add(studyGraph);
                        }
                    }
                }
                if (element.isRight()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible()) {
                        if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                            result.add(studyGraph);
                        }
                    }
                }
            }else{
                if (element.isUp()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                    if (studyGraph.isVisible()) {
                        result.add(studyGraph);
                    }
                }
                if (element.isLeft()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible()) {
                        result.add(studyGraph);
                    }
                }
                if (element.isDown()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                    if (studyGraph.isVisible()) {
                        result.add(studyGraph);
                    }
                }
                if (element.isRight()) {
                    GridElement studyGraph = findGraph(element.getPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                    if (studyGraph.isVisible()) {
                        result.add(studyGraph);
                    }
                }
            }
        }

        return result;
    }

    private GridElement getOpenSetElement(GridElement study, List<GridElement> openSet){
        GridElement open = null;

        for(GridElement element : openSet){
            if(element.getPosition().equals(study.getPosition())){
                open = element;
                break;
            }
        }

        return open;
    }
}
