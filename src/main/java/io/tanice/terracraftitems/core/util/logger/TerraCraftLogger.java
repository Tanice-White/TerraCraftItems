package io.tanice.terracraftitems.core.util.logger;

import io.tanice.terracraftitems.bukkit.TerraCraftItems;
import io.tanice.terracraftitems.bukkit.util.color.ConsoleColor;

import java.util.logging.Level;

public final class TerraCraftLogger {
    private TerraCraftLogger() {
        throw new UnsupportedOperationException("TerraCraftLogger class cannot be instantiated");
    }

    public static void success(String message) {
        TerraCraftItems.inst().getLogger().log(Level.INFO, ConsoleColor.GREEN + message + ConsoleColor.RESET);
    }

    public static void warning(String message) {
        TerraCraftLogger.info(Level.WARNING, message + ConsoleColor.RESET);
    }

    public static void error(String message) {
        TerraCraftLogger.info(Level.WARNING, ConsoleColor.RED + message + ConsoleColor.RESET);
    }

    public static void debug(String message) {
        TerraCraftLogger.info(Level.INFO, ConsoleColor.CYAN + "[" + ConsoleColor.YELLOW + "DEBUG" + ConsoleColor.CYAN + "] " + message + ConsoleColor.RESET);
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
