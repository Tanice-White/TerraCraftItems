package io.tanice.terracraftitems.bukkit.command.item;

import io.tanice.terracraftitems.bukkit.command.CommandRunner;
import io.tanice.terracraftitems.bukkit.item.component.custom.DurabilityComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static io.tanice.terracraftitems.bukkit.util.color.CommandColor.*;

public class ItemDurabilityCommand extends CommandRunner {

    private static final List<String> OPERATIONS = List.of("add", "reduce", "set");

    @Override
    public String getName() {
        return "durability";
    }

    @Override
    public String getUsage() {
        return """
                durability <add> <number>
                durability <reduce> <number>
                durability <set> <number>
                """;
    }

    @Override
    public String getDescription() {
        return "Manages terra_durability or ori_durability for TerraCraft items";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(RED + "This command can only be executed by players");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(RED + "Invalid number of arguments");
            return true;
        }

        int value;
        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(RED + "Invalid number format");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            sender.sendMessage(YELLOW + "You must hold an item in your main hand");
            return true;
        }

        String action = args[0].toLowerCase();
        DurabilityComponent component = DurabilityComponent.from(item);
        if (component == null) {
            sender.sendMessage(YELLOW + "Item has no terra_durability");
            return true;
        }
        Integer d = component.getDamage();
        switch (action) {
            case "add" -> component.setDamage((d == null ? 0 : d) + value);
            case "reduce" -> component.setDamage((d == null ? 0 : d) - value);
            case "set" -> component.setDamage(value);
        }
        component.cover(item);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return OPERATIONS.stream().filter(op -> op.startsWith(args[0])).toList();
        if (args.length == 2) return Collections.singletonList("<number>");
        return Collections.emptyList();
    }
}
