package ru.phoenix.core.kernel;

import ru.phoenix.core.config.WindowConfig;
import ru.phoenix.core.db.Database;
import ru.phoenix.game.datafile.LegacySaveImporter;
import ru.phoenix.game.datafile.Saves;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Game {
    public static void main(String[] args) {
        ensureRuntimeDirectories();

        // локальная БД должна быть готова до создания сцен (они читают слоты сохранений)
        Database.getInstance().init();
        if (Boolean.getBoolean("myst.importLegacy")) {
            LegacySaveImporter.importLegacySaves(Saves.repository());
        }

        CoreEngine coreEngine = new CoreEngine();
        coreEngine.createWindow(WindowConfig.getInstance());
        coreEngine.init();
        coreEngine.start();
    }

    /**
     * Каталоги, которых нет в поставке: они появляются при первом запуске.
     * Нужны до первых сохранений и генерации фона меню.
     */
    private static void ensureRuntimeDirectories() {
        try {
            Files.createDirectories(Paths.get("data", "save", "data"));
            Files.createDirectories(Paths.get("data", "save", "image"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create runtime directories", e);
        }
    }
}
