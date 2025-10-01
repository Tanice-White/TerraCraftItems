package io.tanice.terracraftitems.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class TerraItemUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String terraName;
    private final ItemStack pre;
    private final ItemStack newer;

    public TerraItemUpdateEvent(Player player, String terraName, ItemStack pre, ItemStack newer) {
        this.player = player;
        this.terraName = terraName;
        this.pre = pre;
        this.newer = newer;
    }

    public static HandlerList getHandlerList() {return handlers;}

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getPreBukkitItem() {
        return this.pre;
    }

    public ItemStack getNewerBukkitItem() {
        return this.newer;
    }

    public String getItemTerraName() {
        return this.terraName;
    }
}
