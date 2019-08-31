package ru.phoenix.game.logic.generator.component;

import ru.phoenix.core.math.Vector3f;

public class TerraElement {
    private Vector3f position;
    private int radius;
    private int height;
    private float distance;

    public TerraElement(){
        position = new Vector3f();
        radius = 1;
        height = 1;
        distance = 0.0f;
    }

    public TerraElement(Vector3f position, int radius, int height){
        this.position = position;
        this.radius = radius;
        this.height = height;
        distance = 0.0f;
    }

    public Vector3f getTerraEffect(Vector3f blockPosition){
        Vector3f newBlockPosition = blockPosition;

        if(blockPosition.equals(position)){
            newBlockPosition = new Vector3f(position.getX(),height,position.getZ());
        }else{
            if(position.sub(blockPosition).length() <= radius){
                newBlockPosition = new Vector3f(blockPosition.getX(), getHeight(blockPosition), blockPosition.getZ());
            }
        }

        return newBlockPosition;
    }

    private float getHeight(Vector3f blockPosition){

        float r = radius;
        float h = Math.abs(height);

        float hyp = (float)Math.sqrt((h * h) + (r * r));
        float cosine = r / hyp;

        float cat = Math.abs(r - (blockPosition.sub(position).length()));
        hyp = cat / cosine;
        float result = (float)Math.sqrt((hyp * hyp) - (cat * cat));

        if(height < 0){
            result *= -1.0f;
        }

        if(0.49f <= Math.abs(result - Math.round(result)) && Math.abs(result - Math.round(result)) <= 0.51f){
            if(height > 0){
                result = (float)Math.ceil(result) - 0.5f;
            }else if(height < 0){
                result = (float)Math.floor(result) + 0.5f;
            }
        }else{
            result = Math.round(result);
        }

        return result;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Vector3f getPosition() {
        return position;
    }
}
