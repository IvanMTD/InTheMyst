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

    private Vector3f tempPos;
    private Vector3f tempPos2;
    private List<Vector3f> wayPoints;

    private ImageAnimation animation;

    private int timer;
    private boolean firstStart;
    private boolean endFrame;

    public MotionAnimation(){
        tempPos = new Vector3f();
        tempPos2 = new Vector3f();
        wayPoints = new ArrayList<>();
    }

    public void setup(Vector3f start, List<Vector3f> wayPoints){
        timer = 0;
        firstStart = true;
        endFrame = true;
        goal = 0;
        index = 0;
        tempPos = new Vector3f(start);
        Vector3f s = new Vector3f(this.tempPos);
        Vector3f f = new Vector3f(wayPoints.get(index));
        Vector3f d = new Vector3f(f.sub(s));
        this.tempPos2 = new Vector3f(s.add(d.div(2.0f)));
        this.wayPoints.clear();
        this.wayPoints = new ArrayList<>(wayPoints);
    }

    public int motion(List<GridElement> elements, Vector3f position, Characteristic characteristic,ImageAnimation animation){
        int motion;
        boolean isJump = false;
        this.animation = animation;

        if (index < wayPoints.size() && !isBevel(elements)) {
            if (wayPoints.get(index).getY() < this.tempPos.getY() - 0.4f || this.tempPos.getY() + 0.4f < wayPoints.get(index).getY()) {
                isJump = true;
            }

            if (wayPoints.get(index).getX() < this.tempPos.getX() - 1.1f || this.tempPos.getX() + 1.1f < wayPoints.get(index).getX()) {
                isJump = true;
            }

            if (wayPoints.get(index).getZ() < this.tempPos.getZ() - 1.1f || this.tempPos.getZ() + 1.1f < wayPoints.get(index).getZ()) {
                isJump = true;
            }
        }

        if (isJump) {
            if (jump(position, characteristic)) {
                motion = 0;
            } else {
                motion = 2;
            }
        } else {
            if (move(position, characteristic)) {
                motion = 0;
            } else {
                motion = 1;
            }
        }

        return motion;
    }

    private boolean move(Vector3f position, Characteristic characteristic){
        boolean end = false;
        goal += characteristic.getSpeed(); // фактор движения в приделах от 0-1
        Vector3f start = new Vector3f(this.tempPos); // точка старта
        Vector3f finish = new Vector3f(wayPoints.get(index)); // точка назначения
        Vector3f delta = finish.sub(start); // разница векторов - вектор направления движения
        Vector3f currentPos = start.add(delta.mul(goal)); // добавляем к вектору начала, разницу векторов помноженную на фактор движения
        position.setVector(currentPos); // устанавливаем обьекту текущее положение по направлению движения

        if (goal >= 1.0f) {
            goal = 0.0f;
            this.tempPos = new Vector3f(wayPoints.get(index));
            index++;
            if(index < wayPoints.size()) {
                start = new Vector3f(this.tempPos);
                finish = new Vector3f(wayPoints.get(index));
                delta = new Vector3f(finish.sub(start));
                this.tempPos2 = new Vector3f(start.add(delta.div(2.0f)));
                firstStart = true;
                timer = 0;
            }
            if (characteristic.getCurentActionPoint() <= 0) {
                end = true;
            }
            if (index <= wayPoints.size()) {
                characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 1);
            }
            if (index >= wayPoints.size()) {
                position.setVector(wayPoints.get(index - 1));
                end = true;
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
            Vector3f finish = new Vector3f(wayPoints.get(index));
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
            if(element.getPosition().equals(wayPoints.get(index))){
                nextPosBevel = element.isBevel();
            }
        }

        if(mainPosBevel || nextPosBevel){
            bevel = true;
        }

        return bevel;
    }

    private boolean setStep(Characteristic characteristic, Vector3f position){
        boolean end = false;
        goal = 0.0f;
        this.tempPos = new Vector3f(wayPoints.get(index));
        index++;

        if(index < wayPoints.size()) {
            Vector3f start = new Vector3f(this.tempPos);
            Vector3f finish = new Vector3f(wayPoints.get(index));
            Vector3f delta = new Vector3f(finish.sub(start));
            this.tempPos2 = new Vector3f(start.add(delta.div(2.0f)));
            firstStart = true;
            timer = 0;
        }
        if (characteristic.getCurentActionPoint() <= 0) {
            end = true;
        }
        if (index <= wayPoints.size()) {
            characteristic.setCurentActionPoint(characteristic.getCurentActionPoint() - 1);
        }
        if (index >= wayPoints.size()) {
            position.setVector(wayPoints.get(index - 1));
            end = true;
        }
        return end;
    }

    private boolean endFrame(Characteristic characteristic, Vector3f position){
        boolean end = false;
        if(endFrame){
            position.setVector(wayPoints.get(index));
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
