package io.tanice.terracraftitems.api.event;

import io.tanice.terracraftitems.api.item.TerraItem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class TerraItemSpawnEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final TerraItem terraItem;
    private final ItemStack item;

    public TerraItemSpawnEvent(TerraItem terraItem, ItemStack item) {
        this.terraItem = terraItem;
        this.item = item;
    }

    public static HandlerList getHandlerList() {return handlers;}

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public TerraItem getTerraItem() {
        return this.terraItem;
    }

    public ItemStack getBukkitItem() {
        return this.item;
    }
}
