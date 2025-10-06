package io.tanice.terracraftitems.paper.Listener.item;

import io.tanice.terracraftitems.api.event.TerraItemUpdateEvent;
import io.tanice.terracraftitems.api.item.component.custom.TerraInnerNameComponent;
import io.tanice.terracraftitems.api.item.component.custom.TerraUpdateCodeComponent;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.item.component.custom.InnerNameComponent;
import io.tanice.terracraftitems.paper.item.component.custom.UpdateCodeComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUpdateListener implements Listener {
    private final List<Player> checkPlayers = new ArrayList<>();

    public ItemUpdateListener() {
        Bukkit.getScheduler().runTaskTimer(TerraCraftItems.inst(), () -> {
            if (checkPlayers.isEmpty()) return;
            List<Player> checkPlayers = new ArrayList<>(this.checkPlayers);
            this.checkPlayers.clear();
            for (Player player : checkPlayers) {
                if (player != null) checkAndUpdateItem(player, player.getInventory().getContents());
            }
        }, 20, 20);
        TerraCraftItems.inst().getServer().getPluginManager().registerEvents(this, TerraCraftItems.inst());
    }

    @EventHandler
    void onPlayerHeld(PlayerItemHeldEvent event) {
        Inventory inv = event.getPlayer().getInventory();
        checkAndUpdateItem(event.getPlayer(), inv.getItem(event.getPreviousSlot()), inv.getItem(event.getNewSlot()));
    }

    @EventHandler
    void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player player && player.equals(event.getInventory().getHolder())) {
            checkPlayers.add(player);
        }
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        checkPlayers.add(event.getPlayer());
    }

    /**
     * 物品更新
     */
    private void checkAndUpdateItem(Player player, ItemStack... itemStacks) {
        for (ItemStack item : itemStacks) {
            if (item == null || item.getType().isAir()) continue;
            TerraUpdateCodeComponent codeComponent = UpdateCodeComponent.from(item);
            TerraInnerNameComponent nameComponent = InnerNameComponent.from(item);
            if (codeComponent == null || nameComponent == null) continue;
            TerraCraftItems.inst().getItemManager().getItem(nameComponent.name()).ifPresent(terraItem -> {
                ItemStack preItem = item.clone();
                if (terraItem.updateOld(item)) {
                    player.updateInventory();
                    Bukkit.getPluginManager().callEvent(new TerraItemUpdateEvent(player, nameComponent.name(), preItem, item));
                }
            });
            return;
        }
    }
}
