package io.tanice.terracraftitems.bukkit.command.item;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.bukkit.TerraCraftItems;
import io.tanice.terracraftitems.bukkit.command.CommandRunner;
import io.tanice.terracraftitems.bukkit.item.ComponentFactory;
import io.tanice.terracraftitems.core.message.MessageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class ItemInfoCommand extends CommandRunner {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getUsage() {
        return """
                info
                info <item_name>
                """;
    }

    @Override
    public String getDescription() {
        return MessageManager.getMessage("info.description");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(MessageManager.getMessage("invalid_arguments"));
            return true;
        }

        if (args.length == 1) {
            TerraCraftItems.inst().getItemManager().getItem(args[0])
                    .ifPresentOrElse(
                            terraItem -> sender.sendMessage(terraItem.toString()),
                            () -> sender.sendMessage(String.format(MessageManager.getMessage("item_not_found"), args[0]))
                    );
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.getMessage("player_only"));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(MessageManager.getMessage("no_hold_item"));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(MessageManager.getMessage("info.component_header")).append("\n");
        List<TerraBaseComponent> components = ComponentFactory.inst().getCustomComponentsFrom(item);
        for (int i = 0; i < components.size(); i++) {
            sb.append(components.get(i));
            if (i != components.size() - 1) sb.append("\n");
        }
        sender.sendMessage(sb.toString());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return TerraCraftItems.inst().getItemManager().filterItems(args[0]).stream().sorted().toList();
        return Collections.emptyList();
    }
}