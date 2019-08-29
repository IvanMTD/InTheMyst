package ru.phoenix.game.logic.element;

import ru.phoenix.core.math.Vector3f;

public class Pixel {
    private static Vector3f pixel;

    public static void setPixel(Vector3f p){
        pixel = new Vector3f(p);
    }

    public static Vector3f getPixel(){
        if(pixel != null) {
            return pixel;
        }else{
            return new Vector3f();
        }
    }
}
