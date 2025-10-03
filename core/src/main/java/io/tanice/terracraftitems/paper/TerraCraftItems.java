package io.tanice.terracraftitems.paper;

import io.tanice.terracraftitems.api.item.TerraItemManager;
import io.tanice.terracraftitems.paper.Listener.TerraListener;
import io.tanice.terracraftitems.paper.command.PluginReloadCommand;
import io.tanice.terracraftitems.paper.command.TerraCraftItemsCommand;
import io.tanice.terracraftitems.paper.command.item.ItemDurabilityCommand;
import io.tanice.terracraftitems.paper.command.item.ItemGetCommand;
import io.tanice.terracraftitems.paper.command.item.ItemInfoCommand;
import io.tanice.terracraftitems.paper.util.config.ConfigManager;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.paper.util.message.MessageManager;
import io.tanice.terracraftitems.core.item.ItemManager;
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
        itemManager = new ItemManager();

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

        TerraLogger.success("TerraCraftItems disabled");
        TerraLogger.success("Thank you for using TerraCraft plugin!");
        TerraLogger.success("If you have any questions or suggestions, feel free to share feedback :)");
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
