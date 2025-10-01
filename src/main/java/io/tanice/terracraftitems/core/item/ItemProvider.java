package io.tanice.terracraftitems.core.item;

import io.tanice.terracraftitems.api.item.TerraItem;
import io.tanice.terracraftitems.bukkit.item.Item;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

public final class ItemProvider {

    private int valid;

    public ItemProvider() {
        valid = 0;
    }

    Optional<TerraItem> createItem(String name, ConfigurationSection cfg) {
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
