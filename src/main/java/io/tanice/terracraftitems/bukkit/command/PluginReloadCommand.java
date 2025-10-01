package io.tanice.terracraftitems.bukkit.command;

import io.tanice.terracraftitems.bukkit.TerraCraftItems;
import org.bukkit.command.CommandSender;

public class PluginReloadCommand extends CommandRunner {
    @Override
    public String getName() {
        return "reload";
    }

    public String getDescription() {
        return "reload terracraftitems plugin";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        TerraCraftItems.inst().reload();
        return true;
    }
}
