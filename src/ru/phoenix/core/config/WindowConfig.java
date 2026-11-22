package ru.phoenix.core.config;

import ru.phoenix.core.debug.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;

import static ru.phoenix.core.config.Constants.ENGLISH;
import static ru.phoenix.core.config.Constants.RUSSIAN;

/**
 * Настройки окна и графики из data/config/window/window_config.txt.
 * Значения кэшируются в памяти, отсутствующие параметры берутся из дефолтов,
 * запись атомарная (temp-файл + move), чтобы не оставить битый конфиг при сбое.
 */
public class WindowConfig {

    private static final String FILE_NAME = "window_config.txt";
    private static final String LANGUAGE_KEY = "langueage"; // историческое имя ключа

    private static final Map<String, String> DEFAULTS = new LinkedHashMap<>();

    static {
        DEFAULTS.put("full_screen", "false");
        DEFAULTS.put("window_width", "1280");
        DEFAULTS.put("window_height", "720");
        DEFAULTS.put("samples", "4");
        DEFAULTS.put("gamma", "2.2");
        DEFAULTS.put("contrast", "0.0");
        DEFAULTS.put("z_near", "0.01");
        DEFAULTS.put("z_far", "300.0");
        DEFAULTS.put(LANGUAGE_KEY, "ENGLISH");
    }

    private static WindowConfig instance;

    private final Path file;
    private final Map<String, String> values = new LinkedHashMap<>(DEFAULTS);

    public WindowConfig() {
        this(Paths.get("data", "config", "window"));
    }

    WindowConfig(Path directory) {
        this.file = directory.resolve(FILE_NAME);
        load();
    }

    private void load() {
        if (!Files.exists(file)) {
            save();
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+", 2);
                if (parts.length == 2 && !parts[0].isEmpty()) {
                    values.put(parts[0], parts[1].trim());
                }
            }
        } catch (IOException e) {
            Logger.error("Failed to read window config, using defaults: " + e.getMessage());
        }
    }

    private void save() {
        try {
            Path directory = file.getParent();
            if (directory != null) {
                Files.createDirectories(directory);
            }
            Path temp = file.resolveSibling(file.getFileName() + ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                for (String key : DEFAULTS.keySet()) {
                    writer.write(key + " " + values.getOrDefault(key, DEFAULTS.get(key)));
                    writer.newLine();
                }
            }
            try {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            Logger.error("Failed to write window config: " + e.getMessage());
        }
    }

    public void setFullScreen(boolean fullScreen) {
        put("full_screen", Boolean.toString(fullScreen));
    }

    public boolean isFullScreen() {
        return Boolean.parseBoolean(get("full_screen"));
    }

    public void setWidth(int width) {
        put("window_width", Integer.toString(width));
    }

    public int getWidth() {
        return getInt("window_width");
    }

    public void setHeight(int height) {
        put("window_height", Integer.toString(height));
    }

    public int getHeight() {
        return getInt("window_height");
    }

    public void setSamples(int samples) {
        put("samples", Integer.toString(samples));
    }

    public int getSamples() {
        return getInt("samples");
    }

    public void setGamma(float gamma) {
        put("gamma", Float.toString(gamma));
    }

    public float getGamma() {
        return getFloat("gamma");
    }

    public void setContrast(float contrast) {
        put("contrast", Float.toString(contrast));
    }

    public float getContrast() {
        return getFloat("contrast");
    }

    public void setNear(float zNear) {
        put("z_near", Float.toString(zNear));
    }

    public float getNear() {
        return getFloat("z_near");
    }

    public void setFar(float zFar) {
        put("z_far", Float.toString(zFar));
    }

    public float getFar() {
        return getFloat("z_far");
    }

    public int getLangueage() {
        return "RUSSIAN".equalsIgnoreCase(get(LANGUAGE_KEY)) ? RUSSIAN : ENGLISH;
    }

    public void setLangueage(int languageDescription) {
        put(LANGUAGE_KEY, languageDescription == RUSSIAN ? "RUSSIAN" : "ENGLISH");
    }

    private String get(String key) {
        return values.getOrDefault(key, DEFAULTS.get(key));
    }

    private int getInt(String key) {
        try {
            return Integer.parseInt(get(key).trim());
        } catch (NumberFormatException e) {
            Logger.error("Invalid integer for '" + key + "', default is used");
            return Integer.parseInt(DEFAULTS.get(key));
        }
    }

    private float getFloat(String key) {
        try {
            return Float.parseFloat(get(key).trim());
        } catch (NumberFormatException e) {
            Logger.error("Invalid number for '" + key + "', default is used");
            return Float.parseFloat(DEFAULTS.get(key));
        }
    }

    private void put(String key, String value) {
        values.put(key, value);
        save();
    }

    public static WindowConfig getInstance() {
        if (instance == null) {
            instance = new WindowConfig();
        }
        return instance;
    }
}
