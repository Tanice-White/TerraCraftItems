package io.tanice.terracraftitems.bukkit.command.item;

import io.tanice.terracraftitems.api.item.TerraItem;
import io.tanice.terracraftitems.bukkit.TerraCraftItems;
import io.tanice.terracraftitems.bukkit.command.CommandRunner;
import io.tanice.terracraftitems.bukkit.util.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ItemGetCommand extends CommandRunner {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return MessageManager.getMessage("get.description");
    }

    @Override
    public String getUsage() {
        return """
                get <item> [amount]
                get <item> [player] [amount]
                """;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.getMessage("player_only"));
            return true;
        }
        if (args.length < 1 || args.length > 3) {
            sender.sendMessage(MessageManager.getMessage("invalid_arguments"));
            return true;
        }

        String itemName = args[0];
        Player targetPlayer = player;
        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Math.max(1, Integer.parseInt(args[1]));
            } catch (NumberFormatException ignored1) {
                targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    sender.sendMessage(String.format(MessageManager.getMessage("player_not_found"), args[1]));
                    return true;
                }
                if (args.length == 3) {
                    try {
                        amount = Math.max(1, Integer.parseInt(args[2]));
                    } catch (NumberFormatException ignored2) {
                    }
                }
            }
        }
        Optional<TerraItem> item = TerraCraftItems.inst().getItemManager().getItem(itemName);
        if (item.isEmpty()) {
            sender.sendMessage(String.format(MessageManager.getMessage("item_not_found"), itemName));
        } else {
            ItemStack giveItem = item.get().getBukkitItem();
            giveItem.setAmount(amount);
            targetPlayer.getInventory().addItem(giveItem.clone());
            sender.sendMessage(String.format(MessageManager.getMessage("get.success"),
                    targetPlayer.getName(),
                    amount,
                    itemName
            ));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return TerraCraftItems.inst().getItemManager().filterItems(args[0]).stream().sorted().toList();
        if (args.length == 2) return playerList(args[1]);
        return Collections.emptyList();
    }
}