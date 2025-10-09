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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.tanice.terracraftitems.paper.util.EnumUtil.safeValueOf;

public class Item implements TerraItem {
    /**
     * lore的每行只识别第一个占位符
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]*)}");

    private final String name; // inner name component
    private final Map<String, Integer> loreComponentPlaceholderMap;

    private final Material material;
    private final int amount;
    private final List<TerraBaseComponent> components;
    private final ItemStack bukkitItem;

    private final InnerNameComponent innerNameComponent;
    private final TerraUpdateCodeComponent updateCodeComponent;

    /**
     * 依据内部名称和对应的config文件创建mc基础物品
     */
    public Item(String id, ConfigurationSection cfg) {
        this.name = id;

        this.material = safeValueOf(Material.class, cfg.getString("id"), Material.STONE);
        this.amount = Math.max(cfg.getInt("amount"), 1);

        /* {}占位符读取 */
        this.loreComponentPlaceholderMap = new HashMap<>();
        this.initLorePlaceholder(cfg);

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
        if (cc.getCode() == updateCodeComponent.getCode()) return false;

        for (TerraBaseComponent component : components) {
            if (!component.canUpdate()) continue;
            component.updatePartial(old).cover(old);
        }
        /* 更新 code */
        cc.setCode(updateCodeComponent.getCode());
        cc.cover(old);
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

    public int getLorePlaceholderIdx(String componentName) {
        Integer idx = loreComponentPlaceholderMap.get(componentName);
        return idx == null ? -1 : idx;
    }

    private void initLorePlaceholder(ConfigurationSection cfg) {
        List<String> lore = cfg.getStringList("lore");
        Matcher matcher;
        String placeholder;
        for (int i = 0; i < lore.size(); i++) {
            matcher = PLACEHOLDER_PATTERN.matcher(lore.get(i));
            if (matcher.find()) {
                placeholder = matcher.group(1);
                if (placeholder != null && !placeholder.isBlank()) loreComponentPlaceholderMap.put(placeholder, i);
            }
        }
    }
}
