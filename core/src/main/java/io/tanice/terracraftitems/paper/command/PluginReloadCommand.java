package io.tanice.terracraftitems.paper.command;

import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.util.message.MessageManager;
import org.bukkit.command.CommandSender;

public class PluginReloadCommand extends CommandRunner {
    @Override
    public String getName() {
        return "reload";
    }

    public String getDescription() {
        return MessageManager.getMessage("reload.description");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        TerraCraftItems.inst().reload();
        sender.sendMessage(MessageManager.getMessage("reload.success"));
        return true;
    }
}