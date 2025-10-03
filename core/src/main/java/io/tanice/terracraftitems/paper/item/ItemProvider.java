package io.tanice.terracraftitems.paper.item;

import io.tanice.terracraftitems.api.item.TerraItem;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

public final class ItemProvider {

    private int valid;

    public ItemProvider() {
        valid = 0;
    }

    public Optional<TerraItem> createItem(String name, ConfigurationSection cfg) {
        if (cfg == null) return Optional.empty();
        valid ++;
        return Optional.of(new Item(name, cfg));
    }

    public int getTotal() {
        return this.valid;
    }

    public void reload() {
        valid = 0;
    }
}
