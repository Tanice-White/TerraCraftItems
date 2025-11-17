package io.tanice.terracraftitems.paper.util.message;

import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.util.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class MessageManager {

    private static ConfigurationSection messages;

    public static void load() {
        File messageFile = new File(new File(TerraCraftItems.inst().getDataFolder(), "message"), ConfigManager.getLanguage() + ".yml");
        if (!messageFile.exists()) {
            TerraCraftItems.inst().logger().error("Could not find " + messageFile.getAbsolutePath());
            return;
        }
        messages = YamlConfiguration.loadConfiguration(messageFile);
    }

    public static void reload() {
        load();
    }

    public static void unload() {
        messages = null;
    }

    public static String getMessage(String key) {
        return messages.getString(key) == null ? "§cNo message for key: " + key + "!" : messages.getString(key);
    }
}
