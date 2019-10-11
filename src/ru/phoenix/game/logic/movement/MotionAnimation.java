package ru.phoenix.game.logic.movement;

import ru.phoenix.core.loader.sprite.ImageAnimation;
import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.element.GridElement;

import java.util.ArrayList;
import java.util.List;

public class MotionAnimation {
    private float goal;
    private int index;
    private int walkControl;

    private Vector3f startPosition;
    private Vector3f tempPos;
    private Vector3f tempPos2;
    private List<GridElement> wayPoints;

    private ImageAnimation animation;

    private boolean walkStage;
    private int timer;
    private boolean firstStart;
    private boolean endFrame;

    public MotionAnimation(){
        startPosition = new Vector3f();
        tempPos = new Vector3f();
        tempPos2 = new Vector3f();
        wayPoints = new ArrayList<>();
    }

    public void setup(Vector3f start, List<GridElement> wayPoints){
        timer = 0;
        firstStart = true;
        endFrame = true;
        walkStage = false;
        walkControl = 0;
        goal = 0;
        index = 0;
        startPosition = new Vector3f(start);
        tempPos = new Vector3f(start);
        Vector3f s = new Vector3f(this.tempPos);
        Vector3f f = new Vector3f(wayPoints.get(index).getPosition());
        Vector3f d = new Vector3f(f.sub(s));
        this.tempPos2 = new Vector3f(s.add(d.div(2.0f)));
        this.wayPoints.clear();
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public int motion(List<GridElement> elements, Vector3f position, Characteristic characteristic,ImageAnimation jumpAnim, ImageAnimation climbingAnim, ImageAnimation walkAnim){
        boolean action = true;
        int motion = 0;
        boolean isJump = false;
        boolean isClimbing = false;

        if(index < wayPoints.size()){
            if(characteristic.getStamina() - wayPoints.get(index).getTravelCost() < 0){
                action = false;
            }
        }

        if(action) {
            this.animation = walkAnim;
            if (index < wayPoints.size()) {
                if (wayPoints.get(index).getCurrentHeight() < this.tempPos.getY() - 0.6f || this.tempPos.getY() + 0.6f < wayPoints.get(index).getCurrentHeight()) {
                    isJump = true;
                    this.animation = jumpAnim;
                }

                if(wayPoints.get(index).getPosition().sub(tempPos).length() > 1.5f){
                    isJump = true;
                    this.animation = jumpAnim;
                }
            }

            if (!isJump) {
                if (wayPoints.get(index).getCurrentHeight() < this.tempPos.getY() - 0.4f || this.tempPos.getY() + 0.4f < wayPoints.get(index).getCurrentHeight()) {
                    isClimbing = true;
                    this.animation = climbingAnim;
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

    private boolean move(Vector3f position, Characteristic characteristic){
        boolean end = false;
        goal += characteristic.getMovementSpeed(); // фактор движения в приделах от 0-1
        walkControl++;
        Vector3f start = new Vector3f(this.tempPos); // точка старта
        Vector3f finish = new Vector3f(wayPoints.get(index).getPosition()); // точка назначения
        finish.setY(wayPoints.get(index).getCurrentHeight());
        Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
        Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
        position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

        if(walkControl == 27){
            animation.nextFrame();
        }else if(walkControl == 54){
            animation.nextFrame();
        }

        if (goal >= 1.0f) {
            animation.nextFrame();
            goal = 0.0f;
            walkControl = 0;
            GridElement temp = wayPoints.get(index);
            this.tempPos = new Vector3f(wayPoints.get(index).getPosition());
            this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
            this.startPosition = new Vector3f(wayPoints.get(index).getPosition());
            this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
            position.setVector(this.tempPos);
            index++;
            if(index < wayPoints.size()) {
                start = new Vector3f(this.tempPos);
                finish = new Vector3f(wayPoints.get(index).getPosition());
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
                position.setVector(wayPoints.get(index - 1).getPosition());
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
        Vector3f direction = new Vector3f(wayPoints.get(index).getPosition().sub(s).normalize().mul(0.3f));
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
                Vector3f start = new Vector3f(this.tempPos); // точка старта
                Vector3f finish = new Vector3f(this.tempPos.add(offset)); // точка назначения
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if(goal < 0.33f){
                    animation.setFrames(4);
                }else if(goal < 0.88f){
                    animation.setFrames(5);
                }else if(goal < 0.99f){
                    animation.setFrames(1);
                }

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    this.tempPos = new Vector3f(this.tempPos.add(offset));
                    position.setVector(this.tempPos);
                    firstStart = false;
                }
            } else {
                walkStage = true;
                goal += characteristic.getMovementSpeed(); // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos); // точка старта
                Vector3f finish = new Vector3f(wayPoints.get(index).getPosition()); // точка назначения
                finish.setY(wayPoints.get(index).getCurrentHeight());
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    GridElement temp = wayPoints.get(index);
                    this.tempPos = new Vector3f(wayPoints.get(index).getPosition());
                    this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
                    this.startPosition = new Vector3f(wayPoints.get(index).getPosition());
                    this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
                    position.setVector(this.tempPos);
                    index++;
                    if (index < wayPoints.size()) {
                        start = new Vector3f(this.tempPos);
                        finish = new Vector3f(wayPoints.get(index).getPosition());
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
                        position.setVector(wayPoints.get(index - 1).getPosition());
                        position.setY(wayPoints.get(index - 1).getCurrentHeight());
                        end = true;
                    }
                }
            }
        }else{
            if (firstStart) {
                walkStage = true;
                goal += characteristic.getMovementSpeed(); // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos); // точка старта
                Vector3f finish = new Vector3f(wayPoints.get(index).getPosition().add(offset.mul(-1.0f))); // точка назначения
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    this.tempPos = new Vector3f(wayPoints.get(index).getPosition().add(offset.mul(-1.0f)));
                    position.setVector(this.tempPos);
                    firstStart = false;
                }
            } else {
                walkStage = false;
                goal += characteristic.getMovementSpeed() * speedFactor; // фактор движения в приделах от 0-1
                Vector3f start = new Vector3f(this.tempPos); // точка старта
                Vector3f finish = new Vector3f(this.tempPos.add(offset)); // точка назначения
                Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
                Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
                position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

                if(goal < 0.33f){
                    animation.setFrames(4);
                }else if(goal < 0.88f){
                    animation.setFrames(5);
                }else if(goal < 0.99f){
                    animation.setFrames(6);
                }

                if (goal >= 1.0f) {
                    goal = 0.0f;
                    GridElement temp = wayPoints.get(index);
                    this.tempPos = new Vector3f(wayPoints.get(index).getPosition());
                    this.tempPos.setY(wayPoints.get(index).getCurrentHeight());
                    this.startPosition = new Vector3f(wayPoints.get(index).getPosition());
                    this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
                    position.setVector(this.tempPos);
                    index++;
                    if (index < wayPoints.size()) {
                        start = new Vector3f(this.tempPos);
                        finish = new Vector3f(wayPoints.get(index).getPosition());
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
                        position.setVector(wayPoints.get(index - 1).getPosition());
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
            animation.setFrames(4);
            /*if(50 <= timer && timer < 100){
                animation.setFrames(3);
            }

            if(100 <= timer & timer <= 150){
                animation.setFrames(4);
            }*/

            if(timer > 50) {
                firstStart = false;
                endFrame = true;
                timer = 0;
            }
        }else {
            goal += 1.5f;
            Vector3f start = new Vector3f(this.tempPos);
            Vector3f finish = new Vector3f(wayPoints.get(index).getPosition());
            finish.setY(wayPoints.get(index).getCurrentHeight());
            Vector3f centerS = new Vector3f(this.tempPos2);
            centerS.setY(start.getY());
            Vector3f centerF = new Vector3f(this.tempPos2);
            centerF.setY(finish.getY());
            if (start.getY() < finish.getY()) { // запрыгивание
                if (start.getX() != finish.getX() && start.getZ() == finish.getZ()) {
                    if (start.getX() < finish.getX()) { // 1
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getX() > finish.getX()) { // 2
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
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
                            animation.setFrames(5);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getZ() > finish.getZ()) { // 4
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(finish.getY() - start.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
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
                            animation.setFrames(5);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(start.getY() - finish.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getX() > finish.getX()) { // 6
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
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
                            animation.setFrames(5);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * (Math.abs(start.getY() - finish.getY()) + 0.5f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getZ() > finish.getZ()) { // 8
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f) * 0.5f;
                            float y = (float) Math.sin(goal * Math.PI / 180.0f) * 0.5f;
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
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
                            animation.setFrames(5);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
                            float x = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getX() > finish.getX()) { // 6
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float x = (float) Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(x, y, 0.0f));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
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
                            animation.setFrames(5);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
                            float z = (float) -Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerF).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else {
                            end = endFrame(characteristic,position);
                        }
                    } else if (start.getZ() > finish.getZ()) { // 8
                        if (goal < 90.0f) {
                            animation.setFrames(5);
                            float z = (float) Math.cos(goal * Math.PI / 180.0f);
                            float y = (float) Math.sin(goal * Math.PI / 180.0f);
                            Vector3f currentPos = new Vector3f(centerS).add(new Vector3f(0.0f, y, z));
                            position.setVector(currentPos);
                        } else if (90.0f <= goal && goal < 180.0f) {
                            animation.setFrames(6);
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

    private boolean isBevel(List<GridElement> elements){
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
    }

    private boolean setStep(Characteristic characteristic, Vector3f position){
        boolean end = false;
        goal = 0.0f;
        GridElement temp = wayPoints.get(index);
        this.tempPos = new Vector3f(wayPoints.get(index).getPosition());
        tempPos.setY(wayPoints.get(index).getCurrentHeight());
        this.startPosition = new Vector3f(wayPoints.get(index).getPosition());
        this.startPosition.setY(wayPoints.get(index).getCurrentHeight());
        index++;

        if(index < wayPoints.size()) {
            Vector3f start = new Vector3f(this.tempPos);
            Vector3f finish = new Vector3f(wayPoints.get(index).getPosition());
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
            position.setVector(wayPoints.get(index - 1).getPosition());
            position.setY(wayPoints.get(index - 1).getCurrentHeight());
            end = true;
        }
        return end;
    }

    private boolean endFrame(Characteristic characteristic, Vector3f position){
        boolean end = false;
        if(endFrame){
            position.setVector(wayPoints.get(index).getPosition());
            position.setY(wayPoints.get(index).getCurrentHeight());
            animation.setFrames(7);
            timer++;
            if(timer > 50){
                endFrame = false;
                timer = 0;
            }
        }else {
            end = setStep(characteristic, position);
        }
        return end;
    }
}
