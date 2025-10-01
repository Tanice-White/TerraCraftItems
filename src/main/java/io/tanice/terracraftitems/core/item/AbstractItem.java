package io.tanice.terracraftitems.core.item;

import io.tanice.terracraftitems.api.item.TerraItem;
import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.bukkit.item.ComponentFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.tanice.terracraftitems.core.util.EnumUtil.safeValueOf;

public abstract class AbstractItem implements TerraItem {
    protected final Material material;
    protected final int amount;
    protected final List<TerraBaseComponent> components;
    protected final ItemStack bukkitItem;

    public AbstractItem(ConfigurationSection cfg) {
        Objects.requireNonNull(cfg, "Item configurationSection cannot be null");
        this.material = safeValueOf(Material.class, cfg.getString("id"), Material.STONE);
        this.amount = Math.max(cfg.getInt("amount"), 1);
        this.components = new ArrayList<>();
        this.bukkitItem = new ItemStack(material, amount);
        ComponentFactory.inst().processComponents(cfg, components, bukkitItem);
    }

    public Set<TerraBaseComponent> getComponents() {
        return Set.copyOf(components);
    }

    @Override
    public ItemStack getBukkitItem() {
        return this.bukkitItem.clone();
    }

    @Override
    public int getHashCode() {
        return Objects.hash(material, amount, components);
    }

    @Override
    public boolean updateOld(ItemStack old) {
        for (TerraBaseComponent component : components) {
            if (!component.canUpdate()) continue;
            component.updatePartial().cover(old);
        }
        return true;
    }
}
