package ru.phoenix.game.datafile;

import ru.phoenix.core.debug.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Одноразовый перенос старых сейвов (data/save/data/saveGameN.ser + cutSceneMapN.png)
 * в H2. После успешного импорта файлы убираются в data/save/legacy,
 * чтобы не оставалось двух источников правды.
 */
public final class LegacySaveImporter {

    private static final Pattern SAVE_FILE = Pattern.compile("saveGame(\\d+)\\.ser");

    private LegacySaveImporter() {
    }

    public static void importLegacySaves(SaveRepository repository) {
        File directory = new File("./data/save/data");
        File[] files = directory.listFiles((dir, name) -> SAVE_FILE.matcher(name).matches());
        if (files == null || files.length == 0) {
            return;
        }

        Path legacyDirectory = Paths.get("./data/save/legacy");
        int imported = 0;
        for (File file : files) {
            Matcher matcher = SAVE_FILE.matcher(file.getName());
            if (!matcher.matches()) {
                continue;
            }
            int slot = Integer.parseInt(matcher.group(1));
            try {
                if (repository.exists(slot)) {
                    continue;
                }
                SaveGame saveGame = read(file);
                repository.save(slot, saveGame, readHeightmap(slot));

                Files.createDirectories(legacyDirectory);
                Files.move(file.toPath(), legacyDirectory.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                moveHeightmap(slot, legacyDirectory);

                imported++;
                Logger.info("Imported legacy save slot " + slot + " into H2");
            } catch (Exception e) {
                Logger.error("Failed to import legacy save " + file.getName(), e);
            }
        }
        if (imported > 0) {
            Logger.info("Legacy saves imported: " + imported + " (originals moved to " + legacyDirectory + ")");
        }
    }

    private static SaveGame read(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SaveGame) in.readObject();
        }
    }

    private static byte[] readHeightmap(int slot) throws IOException {
        Path path = heightmapPath(slot);
        return Files.exists(path) ? Files.readAllBytes(path) : null;
    }

    private static void moveHeightmap(int slot, Path legacyDirectory) throws IOException {
        Path path = heightmapPath(slot);
        if (Files.exists(path)) {
            Files.move(path, legacyDirectory.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Path heightmapPath(int slot) {
        return Paths.get("./data/save/image", "cutSceneMap" + slot + ".png");
    }
}
