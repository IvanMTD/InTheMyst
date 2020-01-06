package ru.phoenix.game.logic.movement;

import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.characters.Character;
import ru.phoenix.game.logic.battle.BattleGround;
import ru.phoenix.game.property.Characteristic;
import ru.phoenix.game.logic.element.grid.Cell;

import java.util.ArrayList;
import java.util.List;

import static ru.phoenix.core.config.Constants.*;

public class MotionAnimation {
    private float goal;
    private int index;
    private int walkControl;

    private Vector3f startPosition;
    private Vector3f tempPos;
    private Vector3f tempPos2;
    private Vector3f tempPos3;
    private List<Cell> wayPoints;

    private ImageAnimation animation;
    private ImageAnimation walkAnimation;

    private boolean walkStage;
    private int timer;
    private boolean firstStart;
    private boolean endFrame;

    public MotionAnimation(){
        startPosition = new Vector3f();
        tempPos = new Vector3f();
        tempPos2 = new Vector3f();
        tempPos3 = new Vector3f();
        wayPoints = new ArrayList<>();
    }

    public void setup(Vector3f start, List<Cell> wayPoints){
        timer = 0;
        firstStart = true;
        endFrame = true;
        walkStage = false;
        walkControl = 0;
        goal = 0;
        index = 1;
        if(wayPoints.size() == 1){
            index = 0;
        }
        startPosition = new Vector3f(start);
        tempPos = new Vector3f(start);
        tempPos3 = new Vector3f(start);
        Vector3f s = new Vector3f(this.tempPos);
        Vector3f f = new Vector3f(wayPoints.get(index).getModifiedPosition());
        Vector3f d = new Vector3f(f.sub(s));
        this.tempPos2 = new Vector3f(s.add(d.div(2.0f)));
        this.wayPoints.clear();
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public int motion(Character character, BattleGround battleGround, Vector3f position, Vector3f currentPos, Characteristic characteristic, ImageAnimation jumpAnim, ImageAnimation climbingAnim, ImageAnimation walkAnim){
        boolean action = true;
        int motion = 0;
        boolean isJump = false;
        boolean isClimbing = false;

        turnControl(character);

        if(!battleGround.isActive()) {
            if (index < wayPoints.size()) {
                if (currentPos.equals(tempPos)) {
                    if (wayPoints.get(index).isOccupied()) {
                        motion = 4;
                        if (wayPoints.get(index).getModifiedPosition().equals(wayPoints.get(wayPoints.size() - 1).getModifiedPosition())) {
                            motion = 0;
                        }
                        action = false;
                    } else {
                        wayPoints.get(index - 1).setOccupied(false);
                        wayPoints.get(index).setOccupied(true);
                    }
                }

                if (characteristic.getStamina() - wayPoints.get(index).getTravelCost() < 0) {
                    action = false;
                }
            }

            if (index < wayPoints.size()) {
                wayPoints.get(index).setOccupied(true);
            } else {
                System.out.println("Current index is " + index);
                System.out.println("Current size is " + wayPoints.size() + "\n");
                motion = 0;
                action = false;
            }
        }

        if(action && (index < wayPoints.size())) {
            this.animation = walkAnim;
            if (index < wayPoints.size()) {
                if (wayPoints.get(index).getCurrentHeight() < this.tempPos.getY() - 0.6f || this.tempPos.getY() + 0.6f < wayPoints.get(index).getCurrentHeight()) {
                    isJump = true;
                    this.animation = jumpAnim;
                }

                if(wayPoints.get(index).getModifiedPosition().sub(tempPos).length() > 1.5f){
                    isJump = true;
                    this.animation = jumpAnim;
                }
            }

            if (!isJump) {
                if (wayPoints.get(index).getCurrentHeight() < this.tempPos.getY() - 0.4f || this.tempPos.getY() + 0.4f < wayPoints.get(index).getCurrentHeight()) {
                    isClimbing = true;
                    this.animation = climbingAnim;
                    this.walkAnimation = walkAnim;
                }
            }

            if (isJump) {
                if (jump(position, characteristic)) {
                    motion = 0;
                } else {
                    motion = 2;
                }
            } else if (isClimbing) {
                if (climbing(position, characteristic)) {
                    motion = 0;
                } else {
                    if (walkStage) {
                        motion = 1;
                    } else {
                        motion = 3;
                    }
                }
            } else {
                if (move(position, characteristic)) {
                    motion = 0;
                } else {
                    motion = 1;
                }
            }
        }

        return motion;
    }

    private void turnControl(Character character){
        if(index < wayPoints.size()){
            Vector3f nextPosition = new Vector3f(wayPoints.get(index).getPosition()); nextPosition.setY(0.0f);
            Vector3f currentPos = new Vector3f(character.getPosition()); currentPos.setY(0.0f);
            Vector3f direction = new Vector3f(nextPosition.sub(currentPos)).normalize();
            if(direction.getZ() == 1.0f){ // NORTH
                character.setLook(NORTH);
            }else if(direction.getX() == 1.0f){ // WEST
                character.setLook(WEST);
            }else if(direction.getZ() == -1.0f){ // SOUTH
                character.setLook(SOUTH);
            }else if(direction.getX() == -1.0f){ // EAST
                character.setLook(EAST);
            }
        }
    }

    private boolean move(Vector3f position, Characteristic characteristic){
        boolean end = false;
        goal += characteristic.getMovementSpeed(); // фактор движения в приделах от 0-1
        walkControl++;
        Vector3f start = new Vector3f(this.tempPos); // точка старта
        Vector3f finish = new Vector3f(wayPoints.get(index).getModifiedPosition()); // точка назначения
        finish.setY(wayPoints.get(index).getCurrentHeight());
        Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
        Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
        position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

        if (walkControl == 27) {
            animation.nextFrame();
        } else if (walkControl == 54) {
            animation.nextFrame();
        }

        if (goal >= 1.0f) {
            animation.nextFrame();
            goal = 0.0f;
            walkControl = 0;
            Cell temp = wayPoints.get(index);
            this.tempPos = new Vector3f(wayPoints.get(index).getModifiedPosition());
            this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
            this.tempPos3 = new Vector3f(wayPoints.get(index).getModifiedPosition());
            this.tempPos3.setY(wayPoints.get(index).getCurrentHeight());
            this.startPosition = new Vector3f(wayPoints.get(index).getModifiedPosition());
            this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
            position.setVector(this.tempPos);
            index++;
            if (index < wayPoints.size()) {
                start = new Vector3f(this.tempPos);
                finish = new Vector3f(wayPoints.get(index).getModifiedPosition());
                finish.setY(wayPoints.get(index).getCurrentHeight());
                delta = new Vector3f(finish.sub(start));
                this.tempPos2 = new Vector3f(start.add(delta.div(2.0f)));
                firstStart = true;
                timer = 0;
            }
            if (characteristic.getStamina() <= 0) {
                end = true;
            }
            if (index <= wayPoints.size()) {
                characteristic.setStamina(characteristic.getStamina() - temp.getTravelCost());
            }
            if (index >= wayPoints.size()) {
                position.setVector(wayPoints.get(index - 1).getModifiedPosition());
                position.setY(wayPoints.get(index - 1).getCurrentHeight());
                end = true;
            }
        }
        return end;
    }

    private boolean climbing(Vector3f position, Characteristic characteristic){
        float speedFactor = 1.2f;
        boolean end = false;

        Vector3f offset;
        Vector3f s = new Vector3f(startPosition.getX(),wayPoints.get(index).getCurrentHeight(),startPosition.getZ());
        Vector3f direction = new Vector3f(wayPoints.get(index).getModifiedPosition().sub(s).normalize().mul(0.3f));
        boolean isStepUp = false;

        if(wayPoints.get(index).getCurrentHeight() > tempPos.getY()){
            offset = new Vector3f(0.0f,0.5f,0.0f).add(direction);
            isStepUp = true;
        }else{
            offset = new Vector3f(0.0f,-0.5f,0.0f).add(direction);
        }

        if(isStepUp) {
            if (firstStart) {
                walkStage = false;
                goal += characteristic.getMovementSpeed() * speedFactor; // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos3); // точка старта
                Vector3f finish = new Vector3f(this.tempPos3.add(offset)); // точка назначения
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if(goal < 0.25f){
                    animation.setFrames(1);
                }else if(goal < 0.5f){
                    animation.setFrames(2);
                }else if(goal < 0.75f){
                    animation.setFrames(3);
                }else{
                    animation.setFrames(4);
                }

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    this.tempPos3 = new Vector3f(this.tempPos3.add(offset));
                    position.setVector(this.tempPos3);
                    firstStart = false;
                }
            } else {
                walkControl++;
                walkStage = true;
                goal += characteristic.getMovementSpeed(); // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos3); // точка старта
                Vector3f finish = new Vector3f(wayPoints.get(index).getModifiedPosition()); // точка назначения
                finish.setY(wayPoints.get(index).getCurrentHeight());
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if(walkControl == 27){
                    walkAnimation.nextFrame();
                }else if(walkControl == 54){
                    walkAnimation.nextFrame();
                }

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    walkControl = 0;
                    walkAnimation.nextFrame();
                    Cell temp = wayPoints.get(index);
                    this.tempPos = new Vector3f(wayPoints.get(index).getModifiedPosition());
                    this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
                    this.tempPos3 = new Vector3f(wayPoints.get(index).getModifiedPosition());
                    this.tempPos3.setY(wayPoints.get(index).getCurrentHeight());
                    this.startPosition = new Vector3f(wayPoints.get(index).getModifiedPosition());
                    this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
                    position.setVector(this.tempPos3);
                    index++;
                    if (index < wayPoints.size()) {
                        start = new Vector3f(this.tempPos3);
                        finish = new Vector3f(wayPoints.get(index).getModifiedPosition());
                        finish.setY(wayPoints.get(index).getCurrentHeight());
                        delta = new Vector3f(finish.sub(start));
                        this.tempPos2 = new Vector3f(start.add(delta.div(2.0f)));
                        firstStart = true;
                        timer = 0;
                    }
                    if (characteristic.getStamina() <= 0) {
                        end = true;
                    }
                    if (index <= wayPoints.size()) {
                        characteristic.setStamina(characteristic.getStamina() - temp.getTravelCost());
                    }
                    if (index >= wayPoints.size()) {
                        position.setVector(wayPoints.get(index - 1).getModifiedPosition());
                        position.setY(wayPoints.get(index - 1).getCurrentHeight());
                        end = true;
                    }
                }
            }
        }else{
            if (firstStart) {
                walkControl++;
                walkStage = true;
                goal += characteristic.getMovementSpeed(); // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos3); // точка старта
                Vector3f finish = new Vector3f(wayPoints.get(index).getModifiedPosition().add(offset.mul(-1.0f))); // точка назначения
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if(walkControl == 27){
                    walkAnimation.nextFrame();
                }else if(walkControl == 54){
                    walkAnimation.nextFrame();
                }

                if (goal >= 1.0f) {
                    walkAnimation.nextFrame();
                    walkControl = 0;
                    goal = 0.0f;
                    this.tempPos3 = new Vector3f(wayPoints.get(index).getModifiedPosition().add(offset.mul(-1.0f)));
                    position.setVector(this.tempPos3);
                    firstStart = false;
                }
            } else {
                walkStage = false;
                goal += characteristic.getMovementSpeed() * speedFactor; // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos3); // точка старта
                Vector3f finish = new Vector3f(this.tempPos3.add(offset)); // точка назначения
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if(goal < 0.25f){
                    animation.setFrames(1);
                }else if(goal < 0.5f){
                    animation.setFrames(2);
                }else if(goal < 0.75f){
                    animation.setFrames(3);
                }else{
                    animation.setFrames(4);
                }

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    Cell temp = wayPoints.get(index);
                    this.tempPos = new Vector3f(wayPoints.get(index).getModifiedPosition());
                    this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
                    this.tempPos3 = new Vector3f(wayPoints.get(index).getModifiedPosition());
                    this.tempPos3.setY(wayPoints.get(index).getCurrentHeight());
                    this.startPosition = new Vector3f(wayPoints.get(index).getModifiedPosition());
                    this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
                    position.setVector(this.tempPos3);
                    index++;
                    if (index < wayPoints.size()) {
                        start = new Vector3f(this.tempPos3);
                        finish = new Vector3f(wayPoints.get(index).getModifiedPosition());
                        finish.setY(wayPoints.get(index).getCurrentHeight());
                        delta = new Vector3f(finish.sub(start));
                        this.tempPos2 = new Vector3f(start.add(delta.div(2.0f)));
                        firstStart = true;
                        timer = 0;
                    }
                    if (characteristic.getStamina() <= 0) {
                        end = true;
                    }
                    if (index <= wayPoints.size()) {
                        characteristic.setStamina(characteristic.getStamina() - temp.getTravelCost());
                    }
                    if (index >= wayPoints.size()) {
                        position.setVector(wayPoints.get(index - 1).getModifiedPosition());
                        position.setY(wayPoints.get(index - 1).getCurrentHeight());
                        end = true;
                    }
                }
            }
        }

        return end;
    }

    private boolean jump(Vector3f position, Characteristic characteristic) {
        boolean end = false;

        if(firstStart){
            timer++;
            position.setVector(tempPos);
            if(timer < 20) {
                animation.setFrames(1);
            }else{
                animation.setFrames(2);
            }

            if(timer > 50) {
                firstStart = false;
                endFrame = true;
                timer = 0;
            }
        }else {
            goal += 1.5f;
            Vector3f start = new Vector3f(this.tempPos);
            Vector3f finish = new Vector3f(wayPoints.get(index).getModifiedPosition());
            finish.setY(wayPoints.get(index).getCurrentHeight());
            Vector3f centerS = new Vector3f(this.tempPos2);
            centerS.setY(start.getY());
            Vector3f centerF = new Vector3f(this.tempPos2);
            centerF.setY(finish.getY());
            if (start.getY() < finish.getY()) { // запрыгивание
                if (start.getX() != finish.getX() && start.getZ() == finish.getZ()) {
                    if (start.getX() < finish.getX()) { // 1
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getX() > finish.getX()) { // 2
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    }
                } else if (start.getX() == finish.getX() && start.getZ() != finish.getZ()) {
                    if (start.getZ() < finish.getZ()) { // 3
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getZ() > finish.getZ()) { // 4
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    }
                }
            } else if (start.getY() > finish.getY()) { // спрыгивание
                if (start.getX() != finish.getX() && start.getZ() == finish.getZ()) {
                    if (start.getX() < finish.getX()) { // 5
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(start.getY() - finish.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getX() > finish.getX()) { // 6
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(start.getY() - finish.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    }
                } else if (start.getX() == finish.getX() && start.getZ() != finish.getZ()) {
                    if (start.getZ() < finish.getZ()) { // 7
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(start.getY() - finish.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getZ() > finish.getZ()) { // 8
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(start.getY() - finish.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    }
                }
            }else{
                if (start.getX() != finish.getX() && start.getZ() == finish.getZ()) {
                    if (start.getX() < finish.getX()) { // 5
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getX() > finish.getX()) { // 6
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    }
                } else if (start.getX() == finish.getX() && start.getZ() != finish.getZ()) {
                    if (start.getZ() < finish.getZ()) { // 7
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getZ() > finish.getZ()) { // 8
                        if (goal < 90.0f) {
                            animation.setFrames(3);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(4);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    }
                }
            }
        }

        return end;
    }

    /*private boolean isBevel(List<GridElement> elements){
        boolean bevel = false;
        boolean mainPosBevel = false;
        boolean nextPosBevel = false;

        for(GridElement element : elements){
            if(element.getPosition().equals(tempPos)){
                mainPosBevel = element.isBevel();
            }
            if(element.getPosition().equals(wayPoints.get(index).getPosition())){
                nextPosBevel = element.isBevel();
            }
        }

        if(mainPosBevel || nextPosBevel){
            bevel = true;
        }

        return !bevel;
    }*/

    private boolean setStep(Characteristic characteristic, Vector3f position){
        boolean end = false;
        goal = 0.0f;
        Cell temp = wayPoints.get(index);
        this.tempPos = new Vector3f(wayPoints.get(index).getModifiedPosition());
        this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
        this.tempPos3 = new Vector3f(wayPoints.get(index).getModifiedPosition());
        this.tempPos3.setY(wayPoints.get(index).getCurrentHeight());
        this.startPosition = new Vector3f(wayPoints.get(index).getModifiedPosition());
        this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
        index++;

        if(index < wayPoints.size()) {
            Vector3f start = new Vector3f(this.tempPos);
            Vector3f finish = new Vector3f(wayPoints.get(index).getModifiedPosition());
            finish.setY(wayPoints.get(index).getCurrentHeight());
            Vector3f delta = new Vector3f(finish.sub(start));
            this.tempPos2 = new Vector3f(start.add(delta.div(2.0f)));
            firstStart = true;
            timer = 0;
        }
        if (characteristic.getStamina() <= 0) {
            end = true;
        }
        if (index <= wayPoints.size()) {
            characteristic.setStamina(characteristic.getStamina() - temp.getTravelCost());
        }
        if (index >= wayPoints.size()) {
            position.setVector(wayPoints.get(index - 1).getModifiedPosition());
            position.setY(wayPoints.get(index - 1).getCurrentHeight());
            end = true;
        }
        return end;
    }

    private boolean endFrame(Characteristic characteristic, Vector3f position){
        boolean end = false;
        if(endFrame){
            position.setVector(wayPoints.get(index).getModifiedPosition());
            position.setY(wayPoints.get(index).getCurrentHeight());
            timer++;
            if(timer < 20){
                animation.setFrames(5);
            }else if(timer < 40){
                animation.setFrames(6);
            }else{
                animation.setFrames(7);
            }
            if(timer > 50){
                endFrame = false;
                timer = 0;
            }
        }else {
            end = setStep(characteristic, position);
        }
        return end;
    }

    public void setWayPoints(List<Cell> wayPoints) {
        this.wayPoints = wayPoints;
    }
}
