package io.tanice.terracraftitems.paper;

import io.tanice.terracraftcore.api.TerraCraftCoreBukkit;
import io.tanice.terracraftcore.api.event.TerraCraftEventBus;
import io.tanice.terracraftcore.api.logger.TerraCraftLogger;
import io.tanice.terracraftcore.api.scheduler.TerraCraftScheduler;
import io.tanice.terracraftitems.api.TerraCraftItemsBukkit;
import io.tanice.terracraftitems.api.TerraCraftItemsServer;
import io.tanice.terracraftitems.api.item.TerraComponentFactory;
import io.tanice.terracraftitems.api.item.TerraItemManager;
import io.tanice.terracraftitems.paper.Listener.TerraListener;
import io.tanice.terracraftitems.paper.command.PluginReloadCommand;
import io.tanice.terracraftitems.paper.command.TerraCraftItemsCommand;
import io.tanice.terracraftitems.paper.command.item.ItemDurabilityCommand;
import io.tanice.terracraftitems.paper.command.item.ItemGetCommand;
import io.tanice.terracraftitems.paper.item.ComponentFactory;
import io.tanice.terracraftitems.paper.util.config.ConfigManager;
import io.tanice.terracraftitems.paper.util.message.MessageManager;
import io.tanice.terracraftitems.core.item.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public final class TerraCraftItems extends JavaPlugin implements TerraCraftItemsServer {
    private static TerraCraftItems INSTANCE;

    private ItemManager itemManager;

    private TerraListener terraListener;

    /* core */
    private TerraCraftLogger logger;
    private TerraCraftScheduler scheduler;
    private TerraCraftEventBus eventBus;

    private TerraCraftItemsCommand terraCraftItemsCommand;

    @Override
    public void onLoad() {
        ComponentFactory.inst();
        TerraCraftItemsBukkit.setServer(this);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        logger = TerraCraftCoreBukkit.getLogger(this, "#62BDC4", "#D3AF73");
        scheduler = TerraCraftCoreBukkit.scheduler();
        eventBus = TerraCraftCoreBukkit.getEventBus();

        ConfigManager.load();
        MessageManager.load();

        terraListener = new TerraListener();
        itemManager = new ItemManager();

        terraCraftItemsCommand = new TerraCraftItemsCommand(this);
        terraCraftItemsCommand.register(new ItemGetCommand());
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

        TerraCraftCoreBukkit.removeLogger(this);
    }

    public void reload() {
        ConfigManager.reload();
        MessageManager.reload();
        itemManager.reload();
    }

    public static TerraCraftItems inst() {
        return INSTANCE;
    }

    public TerraItemManager getItemManager() {
        return this.itemManager;
    }

    @Override
    @Nonnull
    public TerraComponentFactory getComponentFactory() {
        return ComponentFactory.inst();
    }

    /* core */
    public TerraCraftLogger logger() {
        return logger;
    }

    public TerraCraftScheduler scheduler() {
        return scheduler;
    }

    public TerraCraftEventBus getEventBus() {
        return eventBus;
    }
}
