package io.tanice.terracraftitems.bukkit;

import io.tanice.terracraftitems.api.item.TerraItemManager;
import io.tanice.terracraftitems.bukkit.Listener.TerraListener;
import io.tanice.terracraftitems.bukkit.command.PluginReloadCommand;
import io.tanice.terracraftitems.bukkit.command.TerraCraftCommand;
import io.tanice.terracraftitems.bukkit.command.item.ItemGetCommand;
import io.tanice.terracraftitems.bukkit.command.item.ItemInfoCommand;
import io.tanice.terracraftitems.core.config.ConfigManager;
import io.tanice.terracraftitems.core.item.ItemManager;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class TerraCraftItems extends JavaPlugin {
    private static TerraCraftItems instance;
    private ItemManager itemManager;
    private TerraListener terraListener;
    private TerraCraftCommand terraCraftCommand;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.load();

        terraListener = new TerraListener();
        itemManager = new ItemManager(this);

        terraCraftCommand = new TerraCraftCommand(this);
        terraCraftCommand.register(new ItemGetCommand());
        terraCraftCommand.register(new ItemInfoCommand());
        terraCraftCommand.register(new PluginReloadCommand());
        terraCraftCommand.enable();
    }

    @Override
    public void onDisable() {
        if (terraCraftCommand != null) terraCraftCommand.unload();
        if (itemManager != null) itemManager.unload();

        TerraCraftLogger.success("TerraCraftItems disabled");
        TerraCraftLogger.success("Thank you for using TerraCraft plugin!");
        TerraCraftLogger.success("If you have any questions or suggestions, feel free to share feedback :)");
    }

    public void reload() {
        ConfigManager.reload();
        itemManager.reload();
    }

    public static TerraCraftItems inst() {
        return instance;
    }

    public TerraItemManager getItemManager() {
        return this.itemManager;
    }

}
