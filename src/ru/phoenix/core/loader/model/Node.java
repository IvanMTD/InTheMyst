package ru.phoenix.core.loader.model;

import ru.phoenix.core.math.Matrix4f;
import ru.phoenix.core.math.Quaternion;
import ru.phoenix.core.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private Node parentNode;
    private List<Node> childNode;
    private List<Matrix4f> transformations;
    private List<Double> times;

    private List<Vector3f> position;
    private List<Vector3f> scaling;
    private List<Quaternion> rotation;

    public Node(String name, Node parentNode) {
        this.name = name;
        this.parentNode = parentNode;
        childNode = new ArrayList<>();
        transformations = new ArrayList<>();
        times = new ArrayList<>();

        position = new ArrayList<>();
        scaling = new ArrayList<>();
        rotation = new ArrayList<>();
    }

    public void addTransformation(Matrix4f transMatrix, Vector3f position, Vector3f scaling, Quaternion rotation){
        transformations.add(transMatrix);
        this.position.add(position);
        this.scaling.add(scaling);
        this.rotation.add(rotation);
    }

    public List<Matrix4f> getTransformations() {
        return transformations;
    }

    public void addTime(double time){
        times.add(time);
    }

    public List<Double> getTimes(){
        return times;
    }

    public int getAnimationFrames() {
        int numFrames = this.transformations.size();
        for (Node child : childNode) {
            int childFrame = child.getAnimationFrames();
            numFrames = Math.max(numFrames, childFrame);
        }
        return numFrames;
    }

    public static Matrix4f getParentTransforms(Node node, int framePos) {
        if (node == null) {
            return new Matrix4f().identity();
        } else {
            Matrix4f parentTransform = new Matrix4f(getParentTransforms(node.getParentNode(), framePos));
            List<Matrix4f> transformations = node.getTransformations();
            Matrix4f nodeTransform;
            int transfSize = transformations.size();
            if (framePos < transfSize) {
                nodeTransform = transformations.get(framePos);
            } else if (transfSize > 0) {
                nodeTransform = transformations.get(transfSize - 1);
            } else {
                nodeTransform = new Matrix4f().identity();
            }
            return parentTransform.mul(nodeTransform);
        }
    }

    public Node findByName(String name){
        Node node = null;
        if(this.name.equals(name)){
            node = this;
        }else{
            for (Node child : childNode) {
                node = child.findByName(name);
                if (node != null) {
                    break;
                }
            }
        }
        return node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void addChild(Node childNode){
        this.childNode.add(childNode);
    }

    public List<Node> getChildNode(){
        return childNode;
    }

    public Vector3f getPosition(int framePos){
        return position.get(framePos);
    }

    public Vector3f getScaling(int framePos){
        return scaling.get(framePos);
    }

    public Quaternion getRotation(int framePos){
        return rotation.get(framePos);
    }
}
