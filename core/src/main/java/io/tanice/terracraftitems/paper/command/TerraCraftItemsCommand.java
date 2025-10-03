package io.tanice.terracraftitems.paper.command;

import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.paper.util.message.MessageManager;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.List;

public class TerraCraftItemsCommand extends CommandGroup implements CommandExecutor, TabCompleter {

    public TerraCraftItemsCommand(JavaPlugin plugin) {
        super(plugin);
    }

    public void enable() {
        PluginCommand command = plugin.getCommand("terracraftitems");
        if (command == null) {
            TerraLogger.error("Main command(terracraftitems) not found");
            return;
        }
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, String[] args) {
        return this.execute(sender, args);
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, String[] args) {
        return this.tabComplete(sender, args);
    }

    @Override
    protected void sendHelp(CommandSender sender) {
        sender.sendMessage( MessageManager.getMessage("help.header"));
        sender.sendMessage(MessageManager.getMessage("help.main_command"));
        super.sendHelp(sender);
    }

    @Override
    public String getName() {
        return "command";
    }

    @Override
    public String getPermission() {
        return "terracraftitems.command";
    }
}