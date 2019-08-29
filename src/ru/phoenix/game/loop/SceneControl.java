package ru.phoenix.game.loop;

import ru.phoenix.game.scene.Scene;

import java.util.List;

public class SceneControl {
    // variables
    private static Scene lastScene;

    private static boolean reinit;
    private static boolean loading;

    // methods
    public static Scene getCurrentScene(List<Scene> scenes){
        for(Scene scene : scenes){

            if(!scene.isInit()){
                scene.init();
                scene.start();
            }

            if(scene.isActive()){
                return scene;
            }
        }
        return lastScene;
    }

    public static void setLastScene(Scene scene){
        lastScene = scene;
    }

    public static boolean isReinit() {
        return reinit;
    }

    public static void setReinit(boolean reinit) {
        SceneControl.reinit = reinit;
    }

    public static boolean isLoading() {
        return loading;
    }

    public static void setLoading(boolean loading) {
        SceneControl.loading = loading;
    }
}
