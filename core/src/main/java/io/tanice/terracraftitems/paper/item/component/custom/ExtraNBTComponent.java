package io.tanice.terracraftitems.paper.item.component.custom;

import io.tanice.terracraftitems.api.item.component.custom.TerraExtraNBTComponent;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static io.tanice.terracraftitems.paper.util.constance.PDCKey.PLUGIN_NAME;

public class ExtraNBTComponent implements TerraExtraNBTComponent {

    private final Map<String, String> nbtMap;

    public ExtraNBTComponent() {
        nbtMap = new HashMap<>();
    }

    public ExtraNBTComponent(ConfigurationSection cfg) {
        nbtMap = new HashMap<>();
        for (String key : cfg.getKeys(false)) nbtMap.put(key, cfg.getString(key));
    }

    @Nullable
    public String getNBTValue(String key) {
        return nbtMap.get(key);
    }

    public void addNBT(String key, String value) {
        nbtMap.put(key, value);
    }

    public void removeNBT(String key) {
        nbtMap.remove(key);
    }

    @Nullable
    public static ExtraNBTComponent from(ItemStack item) {
        ExtraNBTComponent component = new ExtraNBTComponent();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for (NamespacedKey key : container.getKeys()) {
            if (key.getNamespace().startsWith(PLUGIN_NAME)) continue;
            component.addNBT(key.toString(), container.get(key, PersistentDataType.STRING));
        }
        return component;
    }

    @Override
    public void cover(ItemStack item) {
        /* 移除额外的NBT */
        clear(item);
        /* 写入 */
        item.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey k;
            for (Map.Entry<String, String> entry : nbtMap.entrySet()) {
                k = NamespacedKey.fromString(entry.getKey());
                if (k == null) {
                    TerraLogger.warning("Invalid key: " + entry.getKey() + " in " + getComponentName() + " section");
                    continue;
                }
                container.set(k, PersistentDataType.STRING, entry.getValue());
            }
        });
    }

    public static void clear(ItemStack item) {
        item.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            /* 移除额外的NBT */
            for (NamespacedKey key : container.getKeys()) {
                if (key.getNamespace().startsWith(PLUGIN_NAME)) continue;
                container.remove(key);
            }
        });
    }

    public static void remove(ItemStack item) {
        clear(item);
    }

    @Override
    public String getComponentName() {
        return "nbt";
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
