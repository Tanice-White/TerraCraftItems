package io.tanice.terracraftitems.paper.item;

import io.tanice.terracraftitems.api.event.TerraItemSpawnEvent;
import io.tanice.terracraftitems.api.item.TerraItem;
import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.api.item.component.custom.TerraUpdateCodeComponent;
import io.tanice.terracraftitems.paper.item.component.custom.InnerNameComponent;
import io.tanice.terracraftitems.paper.item.component.custom.UpdateCodeComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static io.tanice.terracraftitems.paper.util.EnumUtil.safeValueOf;
import static io.tanice.terracraftitems.core.util.color.CommandColor.*;

public class Item implements TerraItem {
    private final String name; // inner name component

    protected final Material material;
    protected final int amount;
    protected final List<TerraBaseComponent> components;
    protected final ItemStack bukkitItem;

    private final InnerNameComponent innerNameComponent;
    private final TerraUpdateCodeComponent updateCodeComponent;

    /**
     * 依据内部名称和对应的config文件创建mc基础物品
     */
    public Item(String id, ConfigurationSection cfg) {
        this.name = id;

        this.material = safeValueOf(Material.class, cfg.getString("id"), Material.STONE);
        this.amount = Math.max(cfg.getInt("amount"), 1);
        this.components = new ArrayList<>();
        this.bukkitItem = new ItemStack(material, amount);
        ComponentFactory.inst().processComponents(cfg, components, bukkitItem);

        this.innerNameComponent = new InnerNameComponent(id);
        this.innerNameComponent.cover(bukkitItem);
        this.updateCodeComponent = new UpdateCodeComponent(this.getHashCode());
        this.updateCodeComponent.cover(bukkitItem);
    }

    @Override
    public ItemStack getBukkitItem() {
        ItemStack item = bukkitItem.clone();
        TerraItemSpawnEvent event = new TerraItemSpawnEvent(this, item);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return new ItemStack(Material.AIR);
        return event.getBukkitItem();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean updateOld(ItemStack old) {
        UpdateCodeComponent cc = UpdateCodeComponent.from(old);
        if (cc == null) return false;
        if (cc.getCode() == this.updateCodeComponent.getCode()) return false;

        for (TerraBaseComponent component : components) {
            if (!component.canUpdate()) continue;
            component.updatePartial().cover(old);
        }
        return true;
    }

    @Override
    public Set<TerraBaseComponent> getComponents() {
        return Set.copyOf(components);
    }

    @Override
    public int getHashCode() {
        return Objects.hash(name, material, amount, components);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BOLD).append(GREEN).append("Terra Components in ").append(GREEN).append(name).append(RESET).append("\n");
        sb.append(updateCodeComponent).append("\n");

        for (TerraBaseComponent component : components) {
            sb.append(component).append("\n");
        }
        sb.append(RESET);
        return sb.toString();
    }
}
