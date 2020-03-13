package ru.phoenix.game.loop;

import ru.phoenix.core.config.Constants;
import ru.phoenix.game.scene.Scene;

import java.util.List;

public class SceneControl {
    // variables
    private static Scene lastScene;

    private static boolean logo;
    private static boolean mainMenu;
    private static boolean strategyGame;
    private static boolean tacticalGame;

    private static boolean reinit;
    private static boolean loading;

    // methods
    public static Scene getCurrentScene(List<Scene> scenes){
        for(Scene scene : scenes){
            if(logo && scene.getSceneId() == Constants.SCENE_LOGO){
                return scene;
            }else if(mainMenu && scene.getSceneId() == Constants.SCENE_MAIN_MENU){
                if(!scene.isActive()){
                    scene.start();
                }
                return scene;
            }else if(strategyGame && scene.getSceneId() == Constants.SCENE_STRATEGIC){
                if(!scene.isActive()){
                    scene.start();
                }
                return scene;
            }else if(tacticalGame && scene.getSceneId() == Constants.SCENE_TACTICAL){
                if(!scene.isActive()){
                    scene.start();
                }
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

    public static boolean isLogo() {
        return logo;
    }

    public static void setLogo(boolean logo) {
        SceneControl.logo = logo;
    }

    public static boolean isMainMenu() {
        return mainMenu;
    }

    public static void setMainMenu(boolean mainMenu) {
        SceneControl.mainMenu = mainMenu;
    }

    public static boolean isStrategyGame() {
        return strategyGame;
    }

    public static void setStrategyGame(boolean strategyGame) {
        SceneControl.strategyGame = strategyGame;
    }

    public static boolean isTacticalGame() {
        return tacticalGame;
    }

    public static void setTacticalGame(boolean tacticalGame) {
        SceneControl.tacticalGame = tacticalGame;
    }
}
