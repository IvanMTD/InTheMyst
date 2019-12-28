package ru.phoenix.game.logic.battle;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleAI {

    private static Cell[][] grid;
    private static Character self;
    private static List<Character> allies;
    private static List<Character> enemies;

    private static boolean lowHealth;
    private static Character targetCharacter;
    private static Cell closerCell;

    // загрузка данных - начало
    public static void dataLoading(Cell[][]grid, Character self, List<Character> allies, List<Character> enemies){
        setGrid(grid);
        setSelf(self);
        setAllies(allies);
        setEnemies(enemies);
    }
    // загрузка данных - конец

    // алгоритмы принятия решений - начало
    public static void dataAnalyze(){
        introspection();
        studyOpponents();
        whereToGo();
    }

    // методы самодиагностики - начало
    private static void introspection(){
        lowHealth = healthCheck();
    }

    private static boolean healthCheck(){
        boolean check = false;
        int helth = self.getCharacteristic().getHealth();
        int maxHealth = self.getCharacteristic().getTotalHealth();
        float percent = helth * 100.0f / maxHealth;
        if(percent < 25.0f){
            check = true;
        }
        return check;
    }
    // методы самодиагностики - конец

    // методы поиска врагов - начало
    private static void studyOpponents(){
        List<Character>closerEnemies = new ArrayList<>();
        for(Character enemy : enemies){
            Vector3f mainPos = new Vector3f(self.getPosition()); mainPos.setY(0.0f);
            Vector3f enemyPos = new Vector3f(enemy.getPosition()); enemyPos.setY(0.0f);
            float distance = Math.abs(mainPos.sub(enemyPos).length());
            if(distance <= self.getCharacteristic().getVision()){
                if(enemy.isBattle() && !enemy.isDead()) {
                    if(checkAround(enemy) != null) {
                        closerEnemies.add(enemy);
                    }
                }
            }
        }

        if(closerEnemies.size() != 0){
            targetCharacter = null;
            for(Character enemy : closerEnemies){
                if(targetCharacter == null){
                    targetCharacter = enemy;
                }else{
                    if(enemy.getPriority(grid,self) > targetCharacter.getPriority(grid,self)){
                        targetCharacter = enemy;
                    }
                }
            }
        }else{
            targetCharacter = null;
            for(Character enemy : enemies){
                if(enemy.isBattle() && !enemy.isDead()) {
                    if (targetCharacter == null) {
                        if(checkAround(enemy) != null) {
                            targetCharacter = enemy;
                        }
                    } else {
                        if (enemy.getPriority(grid,self) > targetCharacter.getPriority(grid,self)) {
                            if(checkAround(enemy) != null) {
                                targetCharacter = enemy;
                            }
                        }
                    }
                }
            }
        }
    }
    // методы поиска врагов - конец

    // поиск ближайших клеток - начало
    private static void whereToGo(){
        if(targetCharacter != null){
            if(targetCharacter.getPriority(grid,self) > 0){ // Большая вероятность победы!
                closerCell = checkAround(targetCharacter);
            }else{ // Вероятность погибнуть!
                if(isLowHealth()){
                    List<Cell> exit = new ArrayList<>();
                    Vector3f selfPos = new Vector3f(self.getPosition()); selfPos.setY(0.0f);
                    for(int x=0; x<grid.length; x++){
                        for(int z=0;z<grid[0].length;z++){
                            if(grid[x][z].isExitBattleGraund()){
                                Vector3f exitPos = new Vector3f(grid[x][z].getPosition());exitPos.setY(0.0f);
                                float length = Math.abs(selfPos.sub(exitPos).length());
                                grid[x][z].setDistance(length);
                                exit.add(grid[x][z]);
                            }
                        }
                    }
                    exit.sort((o1, o2) -> o1.getDistance() > o2.getDistance() ? 0 : -1);
                    closerCell = exit.get(0);
                }else{
                    // временно!
                    closerCell = checkAround(targetCharacter);
                }
            }
        }else{
            System.out.println("Враг не обнаружен!");
            self.setBattle(false);
            self.resetSettings();
        }
    }
    // поиск ближайших клеток - конец

    // вспомогательные методы - начало
    private static Cell checkAround(Character character){
        Cell result = null;
        int x = (int)character.getPosition().getX();
        int z = (int)character.getPosition().getZ();
        Cell left = null;
        Cell right = null;
        Cell up = null;
        Cell down = null;

        Cell leftUp = null;
        Cell leftDown = null;
        Cell rightUp = null;
        Cell rightDown = null;

        if(0<=x-1){ // LEFT
            left = grid[x-1][z];
            if(0 <= z-1){
                leftDown = grid[x-1][z-1];
            }
            if(z+1 < grid[0].length){
                leftUp = grid[x-1][z+1];
            }
        }
        if(x+1 < grid.length){ // RIGHT
            right = grid[x+1][z];
            if(0 <= z-1){
                rightDown = grid[x+1][z-1];
            }
            if(z+1 < grid[0].length){
                rightUp = grid[x+1][z+1];
            }
        }
        if(z+1 < grid[0].length){ // UP
            up = grid[x][z+1];
        }
        if(0<=z-1){ // DOWN
            down = grid[x][z-1];
        }
        List<Cell>cells = new ArrayList<>(Arrays.asList(left,right,up,down,leftUp,leftDown,rightUp,rightDown));
        float tempDistance = 0;
        for(Cell cell : cells){
            if(cell != null){
                Vector3f mainPos = new Vector3f(self.getPosition()); mainPos.setY(0.0f);
                Vector3f cellPos = new Vector3f(cell.getPosition()); cellPos.setY(0.0f);
                float distance = Math.abs(mainPos.sub(cellPos).length());
                if(result == null){
                    if(!cell.isOccupied() && !cell.isBlocked() && Math.abs(character.getPosition().getY() - cell.getModifiedPosition().getY()) <= 0.5f) {
                        result = cell;
                        tempDistance = distance;
                    }
                }else{
                    if(distance < tempDistance){
                        if(!cell.isOccupied() && !cell.isBlocked() && Math.abs(character.getPosition().getY() - cell.getModifiedPosition().getY()) <= 0.5f) {
                            result = cell;
                            tempDistance = distance;
                        }
                    }
                }
            }
        }
        return result;
    }
    // вспомогательные методы - конец
    // алгоритмы принятия решений - конец

    // сетеры и гетеры - начало
    public static Cell[][] getGrid() {
        return grid;
    }

    public static void setGrid(Cell[][] grid) {
        SimpleAI.grid = grid;
    }

    public static Character getSelf() {
        return self;
    }

    public static void setSelf(Character self) {
        SimpleAI.self = self;
    }

    public static List<Character> getAllies() {
        return allies;
    }

    public static void setAllies(List<Character> allies) {
        SimpleAI.allies = allies;
    }

    public static List<Character> getEnemies() {
        return enemies;
    }

    public static void setEnemies(List<Character> enemies) {
        SimpleAI.enemies = enemies;
    }

    public static boolean isLowHealth() {
        return lowHealth;
    }

    public static Character getTargetCharacter() {
        return targetCharacter;
    }

    public static Cell getCloserCell() {
        return closerCell;
    }
    // сетеры и гетеры - конец
}
