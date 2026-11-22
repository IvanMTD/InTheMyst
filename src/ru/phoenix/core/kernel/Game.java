package ru.phoenix.core.kernel;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.db.Database;
import ru.phoenix.game.datafile.LegacySaveImporter;
import ru.phoenix.game.datafile.Saves;

public class Game {
    public static void main(String[] args) {
        // локальная БД должна быть готова до создания сцен (они читают слоты сохранений)
        Database.getInstance().init();
        LegacySaveImporter.importLegacySaves(Saves.repository());

        CoreEngine coreEngine = new CoreEngine();
        coreEngine.createWindow(WindowConfig.getInstance());
        coreEngine.init();
        coreEngine.start();
    }
}
