package ru.phoenix.core.kernel;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.math.Vector3f;

public class Game {
    public static void main(String[] args) {
        CoreEngine coreEngine = new CoreEngine();
        coreEngine.createWindow(WindowConfig.getInstance());
        coreEngine.init();
        coreEngine.start();
    }
}
