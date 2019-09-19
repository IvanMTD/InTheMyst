package ru.phoenix.game.logic.element;

import ru.phoenix.core.math.Vector3f;
import ru.phoenix.game.content.object.active.property.Characteristic;
import ru.phoenix.game.logic.generator.GraundGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridNode {
    // описание
    private GridNode parent;
    private List<GridNode> childs;
    private int step;
    private Vector3f position;
    private Map<Integer,Vector3f> direction;

    // конструктор
    public GridNode(Vector3f position, int step){
        childs = new ArrayList<>();
        setPosition(position);
        setStep(step);

        direction = new HashMap<>();
        Vector3f l = new Vector3f(-1.0f,0.0f,0.0f);
        Vector3f r = new Vector3f(1.0f,0.0f,0.0f);
        Vector3f u = new Vector3f(0.0f,0.0f,1.0f);
        Vector3f d = new Vector3f(0.0f,0.0f,-1.0f);
        direction.put(0,l);
        direction.put(1,r);
        direction.put(2,u);
        direction.put(3,d);
    }

    // логические методы
    public void initGrid(Characteristic characteristic, int step){
        if(step <= characteristic.getCurentActionPoint()) {
            for (int key = 0; key < 4; key++) {
                Vector3f somePos = new Vector3f(getPosition()).add(direction.get(key));
                if (checkPosition(somePos, characteristic)) {
                    GridNode node = new GridNode(somePos,step);
                    addChild(node);
                    node.setParent(this);
                    node.initGrid(characteristic,step + 1);
                }
            }
        }
    }

    private boolean checkPosition(Vector3f somePosition, Characteristic characteristic){
        boolean check = false;
        float min = somePosition.getY() - characteristic.getJump();
        float max = somePosition.getY() + characteristic.getJump();

        GridElement studyElement = null;
        for(GridElement element : GraundGenerator.getGridElements()){
            if(!element.isBlocked() && element.getPosition().equals(somePosition)){
                if(min <= element.getCurrentHeight() && element.getCurrentHeight() <= max){
                    studyElement = element;
                }
            }
        }

        if(studyElement != null){
            GridNode node = this;
            if(node.getParent() != null){
                somePosition.setY(studyElement.getCurrentHeight());
                check = true;
                while(node.getParent() != null){
                    if(somePosition.equals(node.getParent().getPosition())){
                        check = false;
                    }
                    node = node.getParent();
                }
            }else{
                somePosition.setY(studyElement.getCurrentHeight());
                check = true;
            }
        }

        return check;
    }

    // методы гетеры и сетеры


    public GridNode getParent() {
        return parent;
    }

    public void setParent(GridNode parent) {
        this.parent = parent;
    }

    public int getStep() {
        return step;
    }

    public List<GridNode> getChilds(){
        return childs;
    }

    public void addChild(GridNode child){
        childs.add(child);
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void getAllPosition(List<Vector3f> positions){
        positions.add(getPosition());
        for (GridNode child : childs) {
            child.getAllPosition(positions);
        }
    }

    public void findNodes(Vector3f position, List<GridNode> nodes){
        if(getPosition().equals(position)){
            nodes.add(this);
        }
        for(GridNode child : childs){
            child.findNodes(position,nodes);
        }
    }
}
