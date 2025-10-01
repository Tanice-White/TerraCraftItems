package io.tanice.terracraftitems.bukkit.item;

import io.tanice.terracraftitems.api.item.TerraItem;
import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.api.item.component.custom.TerraUpdateCodeComponent;
import io.tanice.terracraftitems.api.event.TerraItemSpawnEvent;
import io.tanice.terracraftitems.bukkit.item.component.custom.InnerNameComponent;
import io.tanice.terracraftitems.bukkit.item.component.custom.UpdateCodeComponent;
import io.tanice.terracraftitems.core.item.AbstractItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static io.tanice.terracraftitems.bukkit.util.color.CommandColor.*;

public class Item extends AbstractItem implements TerraItem {
    private final String name; // inner name component
    private final InnerNameComponent innerNameComponent;
    private final TerraUpdateCodeComponent updateCodeComponent;

    /**
     * 依据内部名称和对应的config文件创建mc基础物品
     */
    public Item(String id, ConfigurationSection cfg) {
        super(cfg);
        this.name = id;
        this.innerNameComponent = new InnerNameComponent(id);
        this.innerNameComponent.cover(bukkitItem);
        this.updateCodeComponent = new UpdateCodeComponent(this.hashCode());
        this.updateCodeComponent.cover(bukkitItem);
    }

    @Override
    public ItemStack getBukkitItem() {
        ItemStack item = super.getBukkitItem();
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
        return super.updateOld(old);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
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
