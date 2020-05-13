package ru.phoenix.core.kernel;

import ru.phoenix.core.config.WindowConfig;

public class Game {
    public static void main(String[] args) {
        CoreEngine coreEngine = new CoreEngine();
        coreEngine.createWindow(WindowConfig.getInstance());
        coreEngine.init();
        coreEngine.start();
    }
}
