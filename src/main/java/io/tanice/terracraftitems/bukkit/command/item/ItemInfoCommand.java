package io.tanice.terracraftitems.bukkit.command.item;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.bukkit.TerraCraftItems;
import io.tanice.terracraftitems.bukkit.command.CommandRunner;
import io.tanice.terracraftitems.bukkit.item.ComponentFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static io.tanice.terracraftitems.bukkit.util.color.CommandColor.*;

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
        return "check terraMetas of the item in mainhand or a terra item in config file";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(RED + "Invalid number of arguments");
            return true;
        }
        /* 配置物品信息查询 */
        if (args.length == 1) {
            TerraCraftItems.inst().getItemManager().getItem(args[0])
                    .ifPresentOrElse(terraItem -> sender.sendMessage(terraItem.toString()), () -> sender.sendMessage(RED + "No terra named " + args[0]));
            return true;
        }

        /* 主手物品信息查询 */
        if (!(sender instanceof Player player)) {
            sender.sendMessage(RED + "This command can only be executed by players");
            return true;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(YELLOW + "You must hold an item in your main hand");
            return true;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(BOLD).append(GREEN).append("Terra Components in the item").append(RESET).append("\n");
        List<TerraBaseComponent> components = ComponentFactory.inst().getCustomComponentsFrom(item);
        for (int i = 0; i < components.size(); i++) {
            sb.append(components.get(i));
            if (i != components.size() - 1) sb.append("\n");
        }
        sb.append(RESET);
        sender.sendMessage(sb.toString());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return TerraCraftItems.inst().getItemManager().filterItems(args[0]).stream().sorted().toList();
        return Collections.emptyList();
    }
}
