package ru.phoenix.game.datafile;

/**
 * Точка доступа к хранилищу сохранений.
 */
public final class Saves {

    private static final SaveRepository REPOSITORY = new H2SaveRepository();

    private Saves() {
    }

    public static SaveRepository repository() {
        return REPOSITORY;
    }
}
