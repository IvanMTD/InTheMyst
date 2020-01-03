package ru.phoenix.game.logic.movement;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.logic.element.grid.Cell;
import ru.phoenix.game.property.Characteristic;

import java.util.ArrayList;
import java.util.List;

public class PathSearchAlgorithm extends Thread {
    // РЕЖИМЫ ПОИСКА ПУТИ
    private final int STUDY_MODE          = 0x99100;
    private final int BATTLE_MODE         = 0x99101;
    private final int PREPARE_AREA        = 0x99102;
    // ТЕКУЩИЙ РЕЖИМ АЛГОРИТМА
    private int event;
    // ПАРАМЕТРЫ ПОИСКА ПУТИ
    private Characteristic characteristic;
    private BattleGround battleGround;
    private Cell[][] grid;
    private Cell[][] studyGrid;
    private List<Cell> wayPoints;
    private Cell start;
    private Cell finish;
    private boolean simpleAI;

    // КОНСТРУКТОР
    public PathSearchAlgorithm(){
        wayPoints = new ArrayList<>();
    }
    // -=-=-=-=-=END=-=-=-=-=-

    // ПРЕДУСТАНОВКА
    public void setup(Characteristic characteristic, BattleGround battleGround, Cell[][] grid, Cell start){
        this.grid = grid;
        this.battleGround = battleGround;
        this.characteristic = characteristic;
        this.start = start;
        event = PREPARE_AREA;
    }

    public void setup(Characteristic characteristic, BattleGround battleGround, Cell[][] grid, List<Cell> wayPoints, Cell start, Cell finish, boolean simpleAI){
        this.characteristic = characteristic;
        this.battleGround = battleGround;
        this.grid = grid;
        this.wayPoints = wayPoints;
        this.start = start;
        this.finish = finish;
        this.simpleAI = simpleAI;
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
            case STUDY_MODE:
                studyMode();
                break;
            case BATTLE_MODE:
                battleMode();
                break;
        }
    }
    // -=-=-=-=-=END=-=-=-=-=-

    // ПОИСК В ШИРИНУ - НАЧАЛО
    private void prepareArea(){
        prepareGrid();
        showRadius();
        List<Cell> frontier = new ArrayList<>();
        List<Cell> cameFrom = new ArrayList<>();
        start.setStep(0);
        start.setCost(0);
        frontier.add(start);

        int index = 0;

        while(!frontier.isEmpty()) {
            List<Cell> currentFrontier = new ArrayList<>(frontier);
            if(index == 0){
                for (Cell front : currentFrontier) {
                    if (front.getStep() < (characteristic.getMove() * characteristic.getCurentActionPoint())) {
                        // ВВЕРХ
                        Cell upElement = findAndCheckCell(front.getModifiedPosition(), front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                        if(upElement != null){
                            setStep(frontier,cameFrom,front,upElement);
                        }
                        // ЛЕВО
                        Cell leftElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                        if(leftElement != null){
                            setStep(frontier,cameFrom,front,leftElement);
                        }
                        // ВНИЗ
                        Cell downElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                        if(downElement != null){
                            setStep(frontier,cameFrom,front,downElement);
                        }
                        // ПРАВО
                        Cell rightElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                        if(rightElement != null){
                            setStep(frontier,cameFrom,front,rightElement);
                        }
                    }
                    frontier.remove(front);
                }
            }else if(index == 1){
                for (Cell front : currentFrontier) {
                    if (front.getStep() < (characteristic.getMove() * characteristic.getCurentActionPoint())) {
                        // ПРАВО
                        Cell rightElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(1.0f, 0.0f, 0.0f)));
                        if(rightElement != null){
                            setStep(frontier,cameFrom,front,rightElement);
                        }
                        // ВНИЗ
                        Cell downElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, -1.0f)));
                        if(downElement != null){
                            setStep(frontier,cameFrom,front,downElement);
                        }
                        // ЛЕВО
                        Cell leftElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(-1.0f, 0.0f, 0.0f)));
                        if(leftElement != null){
                            setStep(frontier,cameFrom,front,leftElement);
                        }
                        // ВВЕРХ
                        Cell upElement = findAndCheckCell(front.getModifiedPosition(),front.getModifiedPosition().add(new Vector3f(0.0f, 0.0f, 1.0f)));
                        if(upElement != null){
                            setStep(frontier,cameFrom,front,upElement);
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
            came.setVisible(true);
        }
    }
    // ПОИСК В ШИРИНУ - КОНЕЦ

    // ПОИСК A* - НАЧАЛО
    private void studyMode(){
        prepareStudyGrid();
        Cell resultFinish = createStudyPath();
        if(resultFinish != null) {
            wayPoints.clear();
            List<Cell> reverse = new ArrayList<>();
            while (resultFinish.getParent() != null) {
                reverse.add(resultFinish);
                resultFinish = resultFinish.getParent();
            }
            for (int i = reverse.size() - 1; i >= 0; i--) {
                Cell cell = grid[(int)reverse.get(i).getPosition().getX()][(int)reverse.get(i).getPosition().getZ()];
                wayPoints.add(cell);
            }
        }else{
            System.out.println("Путь не найден!");
            wayPoints.clear();
        }
    }

    private void battleMode(){
        clearWayPoints();
        Cell resultFinish = createPath(true,true);
        if(resultFinish == null) {
            resultFinish = createPath(false,true);
        }
        if(resultFinish != null) {
            wayPoints.clear();
            List<Cell> reverse = new ArrayList<>();
            while (resultFinish.getParent() != null) {
                reverse.add(resultFinish);
                resultFinish = resultFinish.getParent();
            }
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
            System.out.println("Путь не найден!");
            wayPoints.clear();
        }
    }

    private Cell createStudyPath(){
        Cell resultFinish = null;
        List<Cell> closedSet = new ArrayList<>();
        List<Cell> openSet = new ArrayList<>();
        start.setParent(null);
        start.setStep(0);
        start.setCost(getHeuristicPathLength(start, finish));
        openSet.add(start);
        while (!openSet.isEmpty()) {
            Cell currentElement = minFullPathLength(openSet);
            if (currentElement.getId() == finish.getId()) {
                resultFinish = currentElement;
                break;
            }

            openSet.remove(currentElement);
            closedSet.add(currentElement);

            for (Cell neighbour : getNeighbours(currentElement, finish, false, false)) {
                if (checkClosedSet(neighbour, closedSet)) {
                    continue;
                }

                Cell open = getOpenSetElement(neighbour, openSet);
                if (open == null) {
                    int result = 0;
                    if(!neighbour.isBevel()) {
                        float h = Math.abs(currentElement.getCurrentHeight() - neighbour.getCurrentHeight());
                        if (h > 0) {
                            if (Math.round(h) - h == 0.5f) {
                                result = 1;
                            } else {
                                result = 3;
                            }
                        }
                    }

                    neighbour.setParent(currentElement);
                    neighbour.setStep(currentElement.getStep() + neighbour.getTravelCost() + result);
                    neighbour.setCost(getHeuristicPathLength(neighbour, finish));
                    openSet.add(neighbour);
                } else {
                    if (open.getStep() > neighbour.getStep()) {
                        open.setParent(currentElement);
                        open.setStep(neighbour.getStep());
                    }
                }
            }
        }
        return resultFinish;
    }

    private Cell createPath(boolean restriction, boolean isZones){
        Cell resultFinish = null;
        List<Cell> closedSet = new ArrayList<>();
        List<Cell> openSet = new ArrayList<>();
        start.setParent(null);
        start.setStep(0);
        start.setCost(getHeuristicPathLength(start, finish));
        openSet.add(start);
        while (!openSet.isEmpty()) {
            Cell currentElement = minFullPathLength(openSet);

            if (currentElement.getId() == finish.getId()) {
                resultFinish = currentElement;
                break;
            }

            openSet.remove(currentElement);
            closedSet.add(currentElement);

            for (Cell neighbour : getNeighbours(currentElement, finish, restriction, isZones)) {
                if (checkClosedSet(neighbour, closedSet)) {
                    continue;
                }

                Cell open = getOpenSetElement(neighbour, openSet);
                if (open == null) {
                    int result = 0;
                    if(!neighbour.isBevel()) {
                        float h = Math.abs(currentElement.getCurrentHeight() - neighbour.getCurrentHeight());
                        if (h > 0) {
                            if (Math.round(h) - h == 0.5f) {
                                result = 1;
                            } else {
                                result = 3;
                            }
                        }
                    }

                    neighbour.setParent(currentElement);
                    neighbour.setStep(currentElement.getStep() + neighbour.getTravelCost() + result);
                    neighbour.setCost(getHeuristicPathLength(neighbour, finish));
                    openSet.add(neighbour);
                } else {
                    if (open.getStep() > neighbour.getStep()) {
                        open.setParent(currentElement);
                        open.setStep(neighbour.getStep());
                    }
                }
            }
        }
        return resultFinish;
    }
    // ПОИСК А* - КОНЕЦ

    // ПОИСК, КОНТРОЛЬ И ПРОВЕРКА КЛЕТОК - НАЧАЛО
    private Cell tryAddGraph(Cell element, Vector3f direction, boolean isBlueZona, boolean restriction){
        Cell finalResult = null;
        int skipInfo = 1 + characteristic.getJump() / 2;
        if(skipInfo > 1){
            List<Cell> studyGrids = new ArrayList<>();
            for(int i=1; i<=skipInfo; i++){
                Cell studyGraph = findAndCheckCell(element.getModifiedPosition(),element.getModifiedPosition().add(direction.mul(i)));
                if(studyGraph != null) {
                    if (isBlueZona) {
                        if (studyGraph.isVisible() && studyGraph.isBlueZona()) {
                            studyGrids.add(studyGraph);
                        } else {
                            break;
                        }
                    } else {
                        if(simpleAI){
                            studyGrids.add(studyGraph);
                        }else {
                            if (studyGraph.isVisible()) {
                                studyGrids.add(studyGraph);
                            } else {
                                break;
                            }
                        }
                    }
                }else{
                    break;
                }
            }

            if(!studyGrids.isEmpty()) {
                for (Cell studyGrid : studyGrids) {
                    studyGrid.setSkip(false);
                    if (studyGrid.getCurrentHeight() <= element.getCurrentHeight() - 1) {
                        if (studyGrid.getId() != finish.getId()) {
                            studyGrid.setSkip(true);
                        }
                    }
                }

                for (Cell studyGrid : studyGrids) {
                    if (!studyGrid.isSkip()) {
                        if (studyGrid.getCurrentHeight() == element.getCurrentHeight()) {
                            finalResult = studyGrid;
                            break;
                        } else {
                            if(restriction) {
                                if (element.getCurrentHeight() - 0.5f <= studyGrids.get(0).getCurrentHeight() && studyGrids.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                    finalResult = studyGrids.get(0);
                                }
                            }else{
                                finalResult = studyGrids.get(0);
                            }
                            break;
                        }
                    }
                }

                if(finalResult == null){
                    if(isBlueZona){
                        if(restriction){
                            if (element.getCurrentHeight() - 0.5f <= studyGrids.get(0).getCurrentHeight() && studyGrids.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                finalResult = studyGrids.get(0);
                            }
                        }else{
                            finalResult = studyGrids.get(0);
                        }
                    }else{
                        if(restriction){
                            if (element.getCurrentHeight() - 0.5f <= studyGrids.get(0).getCurrentHeight() && studyGrids.get(0).getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                finalResult = studyGrids.get(0);
                            }
                        }else{
                            finalResult = studyGrids.get(0);
                        }
                    }
                }
            }else{
                Cell studyGraph = findAndCheckCell(element.getModifiedPosition(),element.getModifiedPosition().add(direction));
                if(studyGraph != null) {
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
                        if(simpleAI){
                            if (restriction) {
                                if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                    finalResult = studyGraph;
                                }
                            } else {
                                finalResult = studyGraph;
                            }
                        }else {
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
            Cell studyGraph = findAndCheckCell(element.getModifiedPosition(), element.getModifiedPosition().add(direction));
            if(studyGraph != null) {
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
                    if(simpleAI){
                        if (restriction) {
                            if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                                finalResult = studyGraph;
                            }
                        } else {
                            finalResult = studyGraph;
                        }
                    }else {
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

        if(finalResult != null && !simpleAI && battleGround.isActive()){
            if(!finalResult.isBlueZona() && !finalResult.isGoldZona()){
                finalResult = null;
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
                Cell studyGraph = findAndCheckCell(element.getModifiedPosition(), element.getModifiedPosition().add(direction.mul(i)));
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
                        if (!studyGraph.getModifiedPosition().equals(finish.getModifiedPosition())) {
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
                Cell studyGraph = findAndCheckCell(element.getModifiedPosition(), element.getModifiedPosition().add(direction));
                if(restriction) {
                    if (element.getCurrentHeight() - 0.5f <= studyGraph.getCurrentHeight() && studyGraph.getCurrentHeight() <= element.getCurrentHeight() + 0.5f) {
                        finalResult = studyGraph;
                    }
                }else{
                    finalResult = studyGraph;
                }
            }
        }else {
            Cell studyGraph = findAndCheckCell(element.getModifiedPosition(), element.getModifiedPosition().add(direction));
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

    private Cell minFullPathLength(List<Cell> openSet){
        openSet.sort(((o1, o2) -> o1.getEstimateFullPathLength() > o2.getEstimateFullPathLength() ? 0 : -1));
        return openSet.get(0);
    }

    private List<Cell> getNeighbours(Cell element, Cell finish, boolean restriction, boolean isZones) {
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

    private Cell findAndCheckCell(Vector3f parentPos, Vector3f studyPos){
        Cell cell = null;
        Cell testParent = null;
        Cell testStudy = null;

        if((0 <= parentPos.getX() && parentPos.getX() < grid.length) && (0 <= parentPos.getZ() && parentPos.getZ() < grid[0].length)){
            if(battleGround.isActive()) {
                testParent = grid[(int) parentPos.getX()][(int) parentPos.getZ()];
            }else{
                if(studyGrid != null) {
                    testParent = studyGrid[(int) parentPos.getX()][(int) parentPos.getZ()];
                }
            }
        }

        if((0 <= studyPos.getX() && studyPos.getX() < grid.length) && (0 <= studyPos.getZ() && studyPos.getZ() < grid[0].length)){
            if(battleGround.isActive()) {
                testStudy = grid[(int) studyPos.getX()][(int) studyPos.getZ()];
            }else{
                if(studyGrid != null) {
                    testStudy = studyGrid[(int) studyPos.getX()][(int) studyPos.getZ()];
                }
            }
        }

        if(testParent != null && testStudy != null){
            // ПРОВЕРКА ВЫСОТЫ
            if(Math.abs(testParent.getCurrentHeight() - testStudy.getCurrentHeight()) <= characteristic.getJump()){
                // ПРОВЕРКА ДОСТУПНОСТИ
                if(!testStudy.isBlocked()) {
                    if(battleGround.isActive()){
                        if(event == PREPARE_AREA){
                            if (!testStudy.isOccupied() && (testStudy.isBattleGraund() || testStudy.isExitBattleGraund())) {
                                cell = grid[(int) studyPos.getX()][(int) studyPos.getZ()];
                            }
                        }else {
                            if (testStudy.getId() == finish.getId()) {
                                if (!testStudy.isOccupied() && (testStudy.isBattleGraund() || testStudy.isExitBattleGraund())) {
                                    cell = grid[(int) studyPos.getX()][(int) studyPos.getZ()];
                                }
                            } else {
                                if (!testStudy.isOccupied() && (testStudy.isBattleGraund() || !testStudy.isExitBattleGraund())) {
                                    cell = grid[(int) studyPos.getX()][(int) studyPos.getZ()];
                                }
                            }
                        }
                    }else{
                        if(!testStudy.isOccupied()) { // НАДО ПОДУМАТЬ!!!!!!!
                            cell = studyGrid[(int) studyPos.getX()][(int) studyPos.getZ()];
                        }
                    }
                }
            }
        }

        return cell;
    }

    private void setStep(List<Cell> frontier, List<Cell> cameFrom, Cell front, Cell studyCell){
        if (checkVisit(cameFrom,studyCell) && (front.getStep() + 1 <= characteristic.getMove() * characteristic.getCurentActionPoint())) {
            studyCell.setParent(front);
            studyCell.setStep(front.getStep() + 1);
            if(studyCell.getStep() <= characteristic.getMove()){
                studyCell.setBlueZona();
            }else{
                studyCell.setGoldZona();
            }
            frontier.add(studyCell);
            cameFrom.add(studyCell);
        }
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

    private boolean checkVisit(List<Cell> cameFrom, Cell studyGraph){
        boolean noVisit = true;

        for(Cell came : cameFrom){
            if(came.getId() == studyGraph.getId()){
                noVisit = false;
                break;
            }
        }

        return noVisit;
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

    private int getHeuristicPathLength(Cell from, Cell to) {
        return (int)(Math.abs(from.getModifiedPosition().getX() - to.getModifiedPosition().getX()) + Math.abs(from.getModifiedPosition().getZ() - to.getModifiedPosition().getZ()));
    }
    // ПОИСК, КОНТРОЛЬ И ПРОВЕРКА КЛЕТОК - КОНЕЦ

    // МЕТОДЫ ПРЕСЕТЫ - НАЧАЛО
    private void showRadius(){
        int radius = characteristic.getVision();
        Vector3f position = new Vector3f(start.getModifiedPosition()); position.setY(0.0f);
        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                Vector3f cellPos = new Vector3f(grid[x][z].getPosition()); cellPos.setY(0.0f);
                if(Math.abs(cellPos.sub(position).length()) <= radius){
                    if(!grid[x][z].isBlocked() && grid[x][z].isBattleGraund()) {
                        grid[x][z].setVisible(true);
                    }
                }
            }
        }
    }

    private void prepareStudyGrid(){
        studyGrid = new Cell[grid.length][grid[0].length];
        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                grid[x][z].setGrayZona();
                grid[x][z].setWayPoint(false);
                grid[x][z].setVisible(false);
                studyGrid[x][z] = new Cell(grid[x][z]);
            }
        }
    }

    private void prepareGrid(){
        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                grid[x][z].setGrayZona();
                grid[x][z].setWayPoint(false);
                grid[x][z].setVisible(false);
            }
        }
    }

    private void clearWayPoints(){
        for(int x=0; x<grid.length; x++){
            for(int z=0; z<grid[0].length; z++){
                grid[x][z].setWayPoint(false);
            }
        }
    }
    // МЕТОДЫ ПРЕСЕТЫ - КОНЕЦ
}
