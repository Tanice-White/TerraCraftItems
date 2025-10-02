package io.tanice.terracraftitems.bukkit;

import io.tanice.terracraftitems.api.item.TerraItemManager;
import io.tanice.terracraftitems.bukkit.Listener.TerraListener;
import io.tanice.terracraftitems.bukkit.command.PluginReloadCommand;
import io.tanice.terracraftitems.bukkit.command.TerraCraftItemsCommand;
import io.tanice.terracraftitems.bukkit.command.item.ItemDurabilityCommand;
import io.tanice.terracraftitems.bukkit.command.item.ItemGetCommand;
import io.tanice.terracraftitems.bukkit.command.item.ItemInfoCommand;
import io.tanice.terracraftitems.core.config.ConfigManager;
import io.tanice.terracraftitems.core.item.ItemManager;
import io.tanice.terracraftitems.core.message.MessageManager;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class TerraCraftItems extends JavaPlugin {
    private static TerraCraftItems instance;
    private ItemManager itemManager;
    private TerraListener terraListener;
    private TerraCraftItemsCommand terraCraftItemsCommand;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.load();
        MessageManager.load();

        terraListener = new TerraListener();
        itemManager = new ItemManager(this);

        terraCraftItemsCommand = new TerraCraftItemsCommand(this);
        terraCraftItemsCommand.register(new ItemGetCommand());
        terraCraftItemsCommand.register(new ItemInfoCommand());
        terraCraftItemsCommand.register(new PluginReloadCommand());
        terraCraftItemsCommand.register(new ItemDurabilityCommand());
        terraCraftItemsCommand.enable();
    }

    @Override
    public void onDisable() {
        if (terraCraftItemsCommand != null) terraCraftItemsCommand.unload();
        if (itemManager != null) itemManager.unload();
        ConfigManager.unload();
        MessageManager.unload();

        TerraCraftLogger.success("TerraCraftItems disabled");
        TerraCraftLogger.success("Thank you for using TerraCraft plugin!");
        TerraCraftLogger.success("If you have any questions or suggestions, feel free to share feedback :)");
    }

    public void reload() {
        ConfigManager.reload();
        MessageManager.reload();
        itemManager.reload();
    }

    public static TerraCraftItems inst() {
        return instance;
    }

    public TerraItemManager getItemManager() {
        return this.itemManager;
    }

}
