package ru.phoenix.core.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindowConfigTest {

    @TempDir
    Path directory;

    @Test
    void createsFileWithDefaultsWhenMissing() {
        WindowConfig config = new WindowConfig(directory);

        assertEquals(1280, config.getWidth());
        assertEquals(720, config.getHeight());
        assertEquals(4, config.getSamples());
        assertEquals(2.2f, config.getGamma());
        assertFalse(config.isFullScreen());
        assertEquals(Constants.ENGLISH, config.getLangueage());
        assertTrue(Files.exists(directory.resolve("window_config.txt")));
    }

    @Test
    void settersPersistBetweenInstances() {
        WindowConfig config = new WindowConfig(directory);
        config.setWidth(1920);
        config.setHeight(1080);
        config.setFullScreen(true);
        config.setSamples(8);
        config.setGamma(2.4f);
        config.setContrast(0.2f);
        config.setLangueage(Constants.RUSSIAN);

        WindowConfig reloaded = new WindowConfig(directory);

        assertEquals(1920, reloaded.getWidth());
        assertEquals(1080, reloaded.getHeight());
        assertTrue(reloaded.isFullScreen());
        assertEquals(8, reloaded.getSamples());
        assertEquals(2.4f, reloaded.getGamma());
        assertEquals(0.2f, reloaded.getContrast());
        assertEquals(Constants.RUSSIAN, reloaded.getLangueage());
    }

    @Test
    void invalidNumbersFallBackToDefaults() throws IOException {
        Files.writeString(directory.resolve("window_config.txt"),
                "window_width abc\n"
                        + "window_height 900\n"
                        + "gamma not-a-number\n");

        WindowConfig config = new WindowConfig(directory);

        assertEquals(1280, config.getWidth());
        assertEquals(900, config.getHeight());
        assertEquals(2.2f, config.getGamma());
    }

    @Test
    void languageIsComparedByValue() throws IOException {
        Files.writeString(directory.resolve("window_config.txt"), "langueage RUSSIAN\n");

        WindowConfig config = new WindowConfig(directory);

        assertEquals(Constants.RUSSIAN, config.getLangueage());
    }

    @Test
    void unknownLanguageFallsBackToEnglish() throws IOException {
        Files.writeString(directory.resolve("window_config.txt"), "langueage GERMAN\n");

        WindowConfig config = new WindowConfig(directory);

        assertEquals(Constants.ENGLISH, config.getLangueage());
    }

    @Test
    void writeIsAtomicAndLeavesNoTempFile() throws IOException {
        WindowConfig config = new WindowConfig(directory);
        config.setWidth(1600);

        try (var files = Files.list(directory)) {
            List<Path> entries = files.toList();
            assertTrue(entries.stream().noneMatch(path -> path.getFileName().toString().endsWith(".tmp")));
        }
    }
}
