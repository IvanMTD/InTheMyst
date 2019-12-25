package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.property.Characteristic;

import java.util.ArrayList;
import java.util.List;

public class PathSearchAlgorithm {
    /*// РЕЖИМЫ ПОИСКА ПУТИ
    private final int STUDY_MODE          = 0x99100;
    private final int BATTLE_MODE         = 0x99101;
    private final int PREPARE_AREA        = 0x99102;
    // ТЕКУЩИЙ РЕЖИМ АЛГОРИТМА
    private int event;
    // ПАРАМЕТРЫ ПОИСКА ПУТИ
    private Characteristic characteristic;
    private BattleGround battleGround;
    private Cell[][] grid;
    private List<Cell> wayPoints;
    private Cell start;
    private Cell finish;

    // КОНСТРУКТОР
    public PathSearchAlgorithm(){
        wayPoints = new ArrayList<>();
    }
    // -=-=-=-=-=END=-=-=-=-=-

    // ПРЕДУСТАНОВКА
    public void setup(Characteristic characteristic, Cell[][] grid, Cell start){
        this.grid = grid;
        this.characteristic = characteristic;
        this.start = start;
        event = PREPARE_AREA;
    }

    public void setup(Characteristic characteristic, BattleGround battleGround, Cell[][] grid, List<Cell> wayPoints, Cell start, Cell finish){
        this.characteristic = characteristic;
        this.battleGround = battleGround;
        this.grid = grid;
        this.wayPoints = wayPoints;
        this.start = start;
        this.finish = finish;
        if(this.battleGround.isActive()){
            event = BATTLE_MODE;
        }else{
            event = STUDY_MODE;
        }
    }
    // -=-=-=-=-=END=-=-=-=-=-

    // ЗАПУСК ПОТОКА
    public void run(){
        switch (event){
            case PREPARE_AREA:
                prepareArea();
                break;
            case CREATE_PATH:
                createPathAStar();
                break;
            case TOTAL_PATH:
                createPathAStarTotal();
                break;
        }
    }
    // -=-=-=-=-=END=-=-=-=-=-

    // Методы поиска путей
    // Методы поиска в ширину - начало
    private void prepareArea(){
        frontier.clear();
        costSoFar.clear();
        cameFrom.clear();
        Cell startGraph = graphs[(int)start.getX()][(int)start.getZ()];
        startGraph.setStep(0);
        startGraph.setCost(0);
        frontier.add(startGraph);
        costSoFar.add(startGraph);
        cameFrom.add(startGraph);

        int index = 0;

        while(!frontier.isEmpty()) {
            List<Cell> currentFrontier = new ArrayList<>(frontier);
            if(index == 0){
                for (Cell front : currentFrontier) {
                    if (front.getStep() < (characteristic.getMove() * characteristic.getCurentActionPoint())) {
                        // ВВЕРХ
                        Cell upElement = findGraph(front.getModifiedPosition(), front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                        if(upElement != null){
                            if(checkStudyPosition(front,upElement)){
                                setStep(front,upElement);
                            }
                        }
                        // ЛЕВО
                        Cell leftElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                        if(leftElement != null){
                            if(checkStudyPosition(front,leftElement)){
                                setStep(front,leftElement);
                            }
                        }
                        // ВНИЗ
                        Cell downElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                        if(downElement != null){
                            if(checkStudyPosition(front,downElement)){
                                setStep(front,downElement);
                            }
                        }
                        // ПРАВО
                        Cell rightElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                        if(rightElement != null){
                            if(checkStudyPosition(front,rightElement)){
                                setStep(front,rightElement);
                            }
                        }
                    }
                    frontier.remove(front);
                }
            }else if(index == 1){
                for (Cell front : currentFrontier) {
                    if (front.getStep() < (characteristic.getMove() * characteristic.getCurentActionPoint())) {
                        // ПРАВО
                        Cell rightElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                        if(rightElement != null){
                            if(checkStudyPosition(front,rightElement)){
                                setStep(front,rightElement);
                            }
                        }
                        // ВНИЗ
                        Cell downElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                        if(downElement != null){
                            if(checkStudyPosition(front,downElement)){
                                setStep(front,downElement);
                            }
                        }
                        // ЛЕВО
                        Cell leftElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                        if(leftElement != null){
                            if(checkStudyPosition(front,leftElement)){
                                setStep(front,leftElement);
                            }
                        }
                        // ВВЕРХ
                        Cell upElement = findGraph(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                        if(upElement != null){
                            if(checkStudyPosition(front,upElement)){
                                setStep(front,upElement);
                            }
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

        for(Cell came : cameFrom){
            if(!came.isBlocked() && !came.isOccupied()){
                came.setVisible(true);
            }
        }
    }
    // Методы поиска в ширину - конец

    // Метод поиска пути А* - начало
    private void createPathAStarTotal(){
        Cell resultFinish = null;
        if(start != null && finish != null){
            resultFinish = createPath(false,false);
        }
        if(resultFinish != null) {
            List<Cell> reverse = new ArrayList<>();
            while (resultFinish.getParent() != null) {
                reverse.add(resultFinish);
                resultFinish = resultFinish.getParent();
            }
            wayPoints.clear();
            for (int i = reverse.size() - 1; i >= 0; i--) {
                wayPoints.add(reverse.get(i));
            }
        }else{
            wayPoints.clear();
        }
    }

    private void createPathAStar(){
        if(finish != null){
            graphs[(int)finish.getX()][(int)finish.getZ()].setTarget(false);
            for(int x=0; x<=width; x++){
                for(int z=0; z<=height; z++){
                    if(graphs[x][z].isVisible()){
                        graphs[x][z].setWayPoint(false);
                    }
                }
            }
            Cell resultFinish = createPath(true,true);
            if(resultFinish == null) {
                resultFinish = createPath(false,true);
            }

            List<Cell> reverse = new ArrayList<>();
            if(resultFinish != null) { // Проверка!
                while (resultFinish.getParent() != null) {
                    reverse.add(resultFinish);
                    resultFinish = resultFinish.getParent();
                }


                List<Cell> wayPoints = new ArrayList<>();
                for (int i = reverse.size() - 1; i >= 0; i--) {
                    wayPoints.add(reverse.get(i));
                }

                int currentStamina = characteristic.getStamina();

                for (Cell point : wayPoints) {
                    point.setVisible(true);
                    point.setWayPoint(true);
                    currentStamina -= point.getTravelCost();
                    if (currentStamina >= 0) {
                        point.setGreenZona();
                    } else {
                        point.setRedZona();
                    }
                }
            }else{
                wayPoints.clear();
            }
        }
    }

    private Cell createPath(boolean restriction, boolean isZones){
        Cell resultFinish = null;
        Cell Start = findGraph(start);
        Cell Finish = findGraph(finish);
        if(Start != null && Finish != null) {
            List<Cell> closedSet = new ArrayList<>();
            List<Cell> openSet = new ArrayList<>();
            Start.setParent(null);
            Start.setStep(0);
            Start.setCost(getHeuristicPathLength(Start, Finish));
            openSet.add(Start);
            while (!openSet.isEmpty()) {
                Cell currentElement = minFullPathLength(openSet);
                if (currentElement.getModifiedPosition().equals(finish)) {
                    resultFinish = currentElement;
                    break;
                }
                openSet.remove(currentElement);
                closedSet.add(currentElement);

                for (Cell neighbour : getNeighbours(currentElement, Finish, restriction, isZones)) {
                    if(!neighbour.isBlocked() && !neighbour.isOccupied()) {
                        if (checkClosedSet(neighbour, closedSet)) {
                            continue;
                        }

                        Cell open = getOpenSetElement(neighbour, openSet);
                        if (open == null) {
                            int result = 0;
                            float h = Math.abs(currentElement.getCurrentHeight() - neighbour.getCurrentHeight());
                            if (h > 0) {
                                if (Math.round(h) - h == 0.5f) {
                                    result = 1;
                                } else {
                                    result = 3;
                                }
                            }

                            neighbour.setParent(currentElement);
                            neighbour.setStep(currentElement.getStep() + neighbour.getTravelCost() + result);
                            neighbour.setCost(getHeuristicPathLength(neighbour, Finish));
                            openSet.add(neighbour);
                        } else {
                            if (open.getStep() > neighbour.getStep()) {
                                open.setParent(currentElement);
                                open.setStep(neighbour.getStep());
                            }
                        }
                    }
                }
            }
        }
        return resultFinish;
    }
    // Метод поиска пути А* - конец

    // Методы установок - начало
    private void setStep(Cell front, Cell studyGraph){
        if (checkVisit(studyGraph) && (front.getStep() + 1 <= characteristic.getMove() * characteristic.getCurentActionPoint())) {
            studyGraph.setParent(front);
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

    private Cell tryAddGraph(Cell element, Vector3f direction, boolean isBlueZona, boolean restriction){
        Cell finalResult = null;
        int skipInfo = 1 + characteristic.getJump() / 2;
        if(skipInfo > 1){
            List<Cell> studyGraphs = new ArrayList<>();
            for(int i=1; i<=skipInfo; i++){
                Cell studyGraph = findGraph(element.getModifiedPosition(),element.getModifiedPosition().add(direction.mul(i)));
                if(studyGraph != null) {
                    if (isBlueZona) {
                        if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                            studyGraphs.add(studyGraph);
                        } else {
                            break;
                        }
                    } else {
                        if (studyGraph.isVisible()) {
                            studyGraphs.add(studyGraph);
                        } else {
                            break;
                        }
                    }
                }else{
                    break;
                }
            }

            if(!studyGraphs.isEmpty()) {
                for (Cell studyGraph : studyGraphs) {
                    studyGraph.setSkip(false);
                    if (studyGraph.getCurrentHeight() <= element.getCurrentHeight() - 1) {
                        if (!studyGraph.getModifiedPosition().equals(finish)) {
                            studyGraph.setSkip(true);
                        }
                    }
                }

                for (Cell studyGraph : studyGraphs) {
                    if (!studyGraph.isSkip()) {
                        if (studyGraph.getCurrentHeight() == element.getCurrentHeight()) {
                            finalResult = studyGraph;
                            break;
                        } else {
                            if(restriction) {
                                if (element.getCurrentHeight() - 0.5f <= studyGraphs.get(0).getCurrentHeight() && studyGraphs.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                    finalResult = studyGraphs.get(0);
                                }
                            }else{
                                finalResult = studyGraphs.get(0);
                            }
                            break;
                        }
                    }
                }

                if(finalResult == null){
                    if(isBlueZona){
                        if(restriction){
                            if (element.getCurrentHeight() - 0.5f <= studyGraphs.get(0).getCurrentHeight() && studyGraphs.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                finalResult = studyGraphs.get(0);
                            }
                        }else{
                            finalResult = studyGraphs.get(0);
                        }
                    }else{
                        if(restriction){
                            if (element.getCurrentHeight() - 0.5f <= studyGraphs.get(0).getCurrentHeight() && studyGraphs.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                finalResult = studyGraphs.get(0);
                            }
                        }else{
                            finalResult = studyGraphs.get(0);
                        }
                    }
                }
            }else{
                Cell studyGraph = findGraph(element.getModifiedPosition(),element.getModifiedPosition().add(direction));
                if(studyGraph != null) {
                    if(checkStudyPosition(element,studyGraph)) {
                        if (isBlueZona) {
                            if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                                if (restriction) {
                                    if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                        finalResult = studyGraph;
                                    }
                                } else {
                                    finalResult = studyGraph;
                                }
                            }
                        } else {
                            if (studyGraph.isVisible()) {
                                if (restriction) {
                                    if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                        finalResult = studyGraph;
                                    }
                                } else {
                                    finalResult = studyGraph;
                                }
                            }
                        }
                    }
                }
            }
        }else {
            Cell studyGraph = findGraph(element.getModifiedPosition(), element.getModifiedPosition().add(direction));
            if(studyGraph != null) {
                if(checkStudyPosition(element,studyGraph)) {
                    if (isBlueZona) {
                        if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                            if (restriction) {
                                if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                    finalResult = studyGraph;
                                }
                            } else {
                                finalResult = studyGraph;
                            }
                        }
                    } else {
                        if (studyGraph.isVisible()) {
                            if (restriction) {
                                if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                    finalResult = studyGraph;
                                }
                            } else {
                                finalResult = studyGraph;
                            }
                        }
                    }
                }
            }
        }
        return finalResult;
    }

    private Cell tryAddGraph(Cell element, Vector3f direction, boolean restriction){
        Cell finalResult = null;
        int skipInfo = 1 + characteristic.getJump() / 2;
        if(skipInfo > 1){
            List<Cell> studyGraphs = new ArrayList<>();
            for(int i=1; i<=skipInfo; i++){
                Cell studyGraph = findGraph(element.getModifiedPosition(), element.getModifiedPosition().add(direction.mul(i)));
                if(studyGraph != null) {
                    studyGraphs.add(studyGraph);
                }else{
                    break;
                }
            }

            if(!studyGraphs.isEmpty()) {
                for (Cell studyGraph : studyGraphs) {
                    studyGraph.setSkip(false);
                    if (studyGraph.getCurrentHeight() <= element.getCurrentHeight() - 1) {
                        if (!studyGraph.getModifiedPosition().equals(finish)) {
                            studyGraph.setSkip(true);
                        }
                    }
                }

                for (Cell studyGraph : studyGraphs) {
                    if (!studyGraph.isSkip()) {
                        if (studyGraph.getCurrentHeight() == element.getCurrentHeight()) {
                            finalResult = studyGraph;
                            break;
                        } else {
                            if(restriction) {
                                if (element.getCurrentHeight() - 0.5f <= studyGraphs.get(0).getCurrentHeight() && studyGraphs.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                    finalResult = studyGraphs.get(0);
                                }
                            }else{
                                finalResult = studyGraphs.get(0);
                            }
                            break;
                        }
                    }
                }

                if(finalResult == null){
                    if(restriction){
                        if (element.getCurrentHeight() - 0.5f <= studyGraphs.get(0).getCurrentHeight() && studyGraphs.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                            finalResult = studyGraphs.get(0);
                        }
                    }else{
                        finalResult = studyGraphs.get(0);
                    }
                }
            }else{
                Cell studyGraph = findGraph(element.getModifiedPosition(), element.getModifiedPosition().add(direction));
                if(restriction) {
                    if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                        finalResult = studyGraph;
                    }
                }else{
                    finalResult = studyGraph;
                }
            }
        }else {
            Cell studyGraph = findGraph(element.getModifiedPosition(), element.getModifiedPosition().add(direction));
            if(restriction) {
                if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                    finalResult = studyGraph;
                }
            }else{
                finalResult = studyGraph;
            }
        }

        return finalResult;
    }
    // методы установок - конец

    // методы проверок - начало
    private boolean checkStudyPosition(Cell parentElement, Cell studyCell){
        boolean check = false;
        float min = parentElement.getCurrentHeight() - (float)characteristic.getJump();
        float max = parentElement.getCurrentHeight() + (float)characteristic.getJump();

        if(!occupied){
            if(finish != null) {
                if (studyCell.getModifiedPosition().equals(finish)) {
                    if (!studyCell.isBlocked()) {
                        if (min <= studyCell.getCurrentHeight() && studyCell.getCurrentHeight() <= max) {
                            check = true;
                        }
                    }
                } else {
                    if (!studyCell.isBlocked() && !studyCell.isOccupied()) {
                        if (min <= studyCell.getCurrentHeight() && studyCell.getCurrentHeight() <= max) {
                            check = true;
                        }
                    }
                }
            }
        }else {
            if (!studyCell.isBlocked() && !studyCell.isOccupied()) {
                if (min <= studyCell.getCurrentHeight() && studyCell.getCurrentHeight() <= max) {
                    check = true;
                }
            }
        }

        return check;
    }

    private boolean checkClosedSet(Cell study, List<Cell> closedSet){
        boolean visit = false;

        for(Cell closed : closedSet){
            if(closed.getId() == study.getId()){
                visit = true;
                break;
            }
        }

        return visit;
    }

    private boolean checkVisit(Cell studyGraph){
        boolean visit = false;

        for(Cell came : cameFrom){
            if(came.getId() == studyGraph.getId()){
                visit = true;
                break;
            }
        }

        return !visit;
    }
    // методы проверок - конец

    // методы гетеры - начало
    private Cell minFullPathLength(List<Cell> elements){

        for (Cell element : elements) {
            float lenght = element.getModifiedPosition().sub(finish).length();
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
                    Cell temp = elements.get(i);
                    elements.set(i,elements.get(j));
                    elements.set(j,temp);
                }
            }
        }

        return elements.get(0);
    }

    private List<Cell> getNeighbours(Cell element, Cell finish, boolean restriction,boolean isZones) {
        List<Cell> result = new ArrayList<>();
        // Вверх
        Cell upElement = null;
        if(isZones) {
            upElement = tryAddGraph(element, new Vector3f(0.0f, 0.0f, 1.0f), finish.isBlueZona(), restriction);
        }else{
            upElement = tryAddGraph(element, new Vector3f(0.0f, 0.0f, 1.0f), restriction);
        }
        if(upElement != null){
            result.add(upElement);
        }
        // Влево
        Cell leftElement = null;
        if(isZones) {
            leftElement = tryAddGraph(element, new Vector3f(-1.0f, 0.0f, 0.0f), finish.isBlueZona(), restriction);
        }else{
            leftElement = tryAddGraph(element, new Vector3f(-1.0f, 0.0f, 0.0f), restriction);
        }
        if(leftElement != null){
            result.add(leftElement);
        }
        // Вниз
        Cell downElement = null;
        if(isZones) {
            downElement = tryAddGraph(element, new Vector3f(0.0f, 0.0f, -1.0f), finish.isBlueZona(), restriction);
        }else{
            downElement = tryAddGraph(element, new Vector3f(0.0f, 0.0f, -1.0f), restriction);
        }
        if(downElement != null){
            result.add(downElement);
        }
        // Вправо
        Cell rightElement = null;
        if(isZones) {
            rightElement = tryAddGraph(element, new Vector3f(1.0f, 0.0f, 0.0f), finish.isBlueZona(), restriction);
        }else{
            rightElement = tryAddGraph(element, new Vector3f(1.0f, 0.0f, 0.0f), restriction);
        }
        if(rightElement != null){
            result.add(rightElement);
        }

        return result;
    }

    private Cell getOpenSetElement(Cell study, List<Cell> openSet){
        Cell open = null;

        for(Cell element : openSet){
            if(element.getId() == study.getId()){
                open = element;
                break;
            }
        }

        return open;
    }

    private Cell findGraph(Vector3f position){
        Cell graph = null;

        if((0 <= position.getX() && position.getX() <= width) && (0 <= position.getZ() && position.getZ() <= height)){
            graph = graphs[(int)position.getX()][(int)position.getZ()];
        }

        return graph;
    }

    private Cell findGraph(Vector3f parentPos, Vector3f position){
        Cell graph = null;
        Cell testGraphParent = null;
        Cell testGraphStudy = null;

        if((0 <= parentPos.getX() && parentPos.getX() <= width) && (0 <= parentPos.getZ() && parentPos.getZ() <= height)){
            testGraphParent = graphs[(int)parentPos.getX()][(int)parentPos.getZ()];
        }

        if((0 <= position.getX() && position.getX() <= width) && (0 <= position.getZ() && position.getZ() <= height)){
            testGraphStudy = graphs[(int)position.getX()][(int)position.getZ()];
        }

        if(testGraphParent != null && testGraphStudy != null){
            if(Math.abs(testGraphParent.getCurrentHeight() - testGraphStudy.getCurrentHeight()) <= characteristic.getJump()){
                graph = graphs[(int)position.getX()][(int)position.getZ()];
            }
        }

        return graph;
    }

    private static int getHeuristicPathLength(Cell from, Cell to) {
        return (int)(Math.abs(from.getModifiedPosition().getX() - to.getModifiedPosition().getX()) + Math.abs(from.getModifiedPosition().getZ() - to.getModifiedPosition().getZ()));
    }
    // методы гетеры - конец*/
}
