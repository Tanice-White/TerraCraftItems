package io.tanice.terracraftitems.paper.util.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Objects;

import static io.tanice.terracraftitems.paper.util.constance.PDCKey.TERRA_COMPONENT_KEY;

public final class PDCUtil {
    /**
     * 获取物品的TerraCraft组件数据容器
     *
     * @param item 目标物品栈，可为 {@code null}
     * @return 物品的组件数据容器，若物品为空、是空气或无元数据则返回 {@code null}
     */
    @Nullable
    public static PersistentDataContainer getTerraContainer(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer root = meta.getPersistentDataContainer();
        return root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
    }

    /**
     * 移除物品上本插件nbt下的最外层的一个key
     * @param item 目标物品
     * @param target 目标 key
     * @throws NullPointerException 如果 {@code item} 为 {@code null}
     */
    public  static void removeSubTerraContainer(ItemStack item, NamespacedKey target) {
        Objects.requireNonNull(item, "item should not be null");
        item.editMeta(meta -> {
            PersistentDataContainer root = meta.getPersistentDataContainer();
            PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
            if (container == null) return;
            container.remove(target);
        });
    }
}
