package io.tanice.terracraftitems.bukkit.command;

import io.tanice.terracraftitems.bukkit.command.item.ItemGetCommand;
import io.tanice.terracraftitems.bukkit.command.item.ItemInfoCommand;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.List;

import static io.tanice.terracraftitems.bukkit.util.color.CommandColor.*;

public class TerraCraftCommand extends CommandGroup implements CommandExecutor, TabCompleter {

    public TerraCraftCommand(JavaPlugin plugin) {
        super(plugin);
    }

    public void enable() {
        PluginCommand command = plugin.getCommand("terracraftitems");
        if (command == null) {
            TerraCraftLogger.error("Main command(terracraftitems) not found");
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
        sender.sendMessage(GOLD + "=== TerraCraft Help ===");
        sender.sendMessage(AQUA + "main command: " + WHITE + "/terracraftitems");
        sender.sendMessage(AQUA + "sub commands:");
        subCommands.values().forEach(cmd ->
                sender.sendMessage(String.format(WHITE + "\t%s " + GRAY + "\t- %s", cmd.getName(), cmd.getDescription()))
        );
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