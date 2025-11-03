package io.tanice.terracraftitems.paper.util.logger;

import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.core.util.color.ConsoleColor;

import java.util.logging.Level;

public final class TerraLogger {
    private TerraLogger() {
        throw new UnsupportedOperationException("TerraLogger class cannot be instantiated");
    }

    public static void success(String message) {
        TerraCraftItems.inst().getLogger().log(Level.INFO, ConsoleColor.GREEN + message + ConsoleColor.RESET);
    }

    public static void warning(String message) {
        TerraLogger.info(Level.WARNING, message + ConsoleColor.RESET);
    }

    public static void error(String message) {
        TerraLogger.info(Level.SEVERE, ConsoleColor.RED + message + ConsoleColor.RESET);
    }

    public static void error(String message, Throwable throwable) {
        TerraCraftItems.inst().getLogger().log(Level.SEVERE, ConsoleColor.RED + message + ConsoleColor.RESET, throwable);
    }

    public static void debug(String message) {
        TerraLogger.info(Level.INFO, ConsoleColor.CYAN + "[" + ConsoleColor.YELLOW + "DEBUG" + ConsoleColor.CYAN + "] " + message + ConsoleColor.RESET);
    }

    public static void info(String message) {
        TerraCraftItems.inst().getLogger().log(Level.INFO, message + ConsoleColor.RESET);
    }

    public static void info(String message, Object ... params) {
        TerraCraftItems.inst().getLogger().log(Level.INFO, message + ConsoleColor.RESET, params);
    }

    public static void info(Level level, String message) {
        TerraCraftItems.inst().getLogger().log(level, message + ConsoleColor.RESET);
    }

    public static void info(Level level, String message, Object ... params) {
        TerraCraftItems.inst().getLogger().log(level, message + ConsoleColor.RESET, params);
    }
}
