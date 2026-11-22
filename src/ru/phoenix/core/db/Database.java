package ru.phoenix.core.db;

import org.flywaydb.core.Flyway;
import ru.phoenix.core.debug.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Встроенная H2-база приложения.
 * Файл по умолчанию: &lt;рабочая директория&gt;/data/db/inthemyst.mv.db
 * Схема создаётся и обновляется Flyway-миграциями из classpath:db/migration.
 */
public final class Database {

    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String DB_FILE = "inthemyst";

    private static Database instance;

    private Connection connection;

    private Database() {
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Открывает файловую БД приложения (data/db/inthemyst.mv.db).
     */
    public synchronized void init() {
        init(defaultUrl());
    }

    /**
     * Открывает БД по указанному JDBC URL. Нужен тестам (in-memory H2)
     * и ручным прогонам без файла.
     */
    public synchronized void init(String url) {
        if (connection != null) {
            return;
        }
        try {
            Flyway.configure()
                    .dataSource(url, USER, PASSWORD)
                    .locations("classpath:db/migration")
                    .load()
                    .migrate();

            connection = DriverManager.getConnection(url, USER, PASSWORD);
            Logger.info("H2 database is ready: " + url);
        } catch (Exception e) {
            throw new DatabaseException("Unable to initialize H2 database", e);
        }
    }

    /**
     * Соединение живёт всё время работы приложения и используется из игрового потока.
     * Закрывать его в вызывающем коде нельзя.
     */
    public synchronized Connection getConnection() {
        if (connection == null) {
            throw new DatabaseException("Database is not initialized", null);
        }
        return connection;
    }

    public synchronized void close() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.error("Failed to close H2 database", e);
        }
        connection = null;
        Logger.info("H2 database closed");
    }

    private static String defaultUrl() {
        try {
            Path directory = Paths.get(System.getProperty("user.dir"), "data", "db");
            Files.createDirectories(directory);
            return "jdbc:h2:file:"
                    + directory.toAbsolutePath().toString().replace('\\', '/')
                    + "/" + DB_FILE;
        } catch (IOException e) {
            throw new DatabaseException("Unable to create database directory", e);
        }
    }
}
