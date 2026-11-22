package ru.phoenix.game.datafile;

/**
 * Хранилище игровых сохранений. Слоты 1..5, null из {@link #load(int)} означает пустой слот.
 */
public interface SaveRepository {

    SaveGame load(int slot);

    void save(int slot, SaveGame saveGame, byte[] heightmapPng);

    byte[] loadHeightmap(int slot);

    boolean exists(int slot);

    void delete(int slot);
}
