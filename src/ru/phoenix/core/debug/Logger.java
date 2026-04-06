package ru.phoenix.core.debug;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Легковесный асинхронный логгер для игрового движка.
 * Заменяет System.out.println для устранения блокировок потока.
 */
public class Logger {
    
    public enum Level {
        INFO,
        WARN,
        ERROR,
        DEBUG
    }
    
    private static final BlockingQueue<LogMessage> logQueue = new LinkedBlockingQueue<>();
    private static volatile boolean running = true;
    private static Thread loggerThread;
    private static PrintWriter writer;
    
    static {
        init();
    }
    
    private static void init() {
        try {
            // Пытаемся создать файл лога, если не получится - пишем только в консоль
            File logFile = new File("game.log");
            writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)), true);
        } catch (IOException e) {
            writer = null;
        }
        
        loggerThread = new Thread(() -> {
            while (running || !logQueue.isEmpty()) {
                try {
                    LogMessage msg = logQueue.take();
                    processMessage(msg);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Logger-Thread");
        
        loggerThread.setDaemon(true);
        loggerThread.start();
    }
    
    private static void processMessage(LogMessage msg) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String formattedMsg = String.format("[%s] [%s] %s", timestamp, msg.level, msg.message);
        
        // Пишем в консоль (быстро, без синхронизации)
        if (msg.level == Level.ERROR) {
            System.err.println(formattedMsg);
        } else {
            System.out.println(formattedMsg);
        }
        
        // Пишем в файл
        if (writer != null) {
            writer.println(formattedMsg);
        }
    }
    
    public static void info(String message) {
        logQueue.offer(new LogMessage(Level.INFO, message));
    }
    
    public static void warn(String message) {
        logQueue.offer(new LogMessage(Level.WARN, message));
    }
    
    public static void error(String message) {
        logQueue.offer(new LogMessage(Level.ERROR, message));
    }
    
    public static void error(String message, Throwable t) {
        logQueue.offer(new LogMessage(Level.ERROR, message + ": " + t.getMessage()));
        if (writer != null) {
            t.printStackTrace(writer);
        }
    }
    
    public static void debug(String message) {
        logQueue.offer(new LogMessage(Level.DEBUG, message));
    }
    
    public static void shutdown() {
        running = false;
        try {
            loggerThread.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (writer != null) {
            writer.close();
        }
    }
    
    private static class LogMessage {
        final Level level;
        final String message;
        
        LogMessage(Level level, String message) {
            this.level = level;
            this.message = message;
        }
    }
}
