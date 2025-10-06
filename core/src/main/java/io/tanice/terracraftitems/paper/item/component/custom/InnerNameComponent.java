package io.tanice.terracraftitems.paper.item.component.custom;

import io.tanice.terracraftitems.api.item.component.custom.TerraInnerNameComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

import static io.tanice.terracraftitems.paper.util.constance.PDCKey.TERRA_COMPONENT_KEY;
import static io.tanice.terracraftitems.paper.util.constance.PDCKey.TERRA_NAME_KEY;

public record InnerNameComponent(String name) implements TerraInnerNameComponent {

    @Nullable
    public static InnerNameComponent from(ItemStack item) {
        if (item == null || item.getType().isAir()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer().get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null) return null;
        String name = container.get(TERRA_NAME_KEY, PersistentDataType.STRING);
        if (name == null) return null;
        return new InnerNameComponent(name);
    }

    @Override
    public void cover(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer root = meta.getPersistentDataContainer();
        PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null) container = root.getAdapterContext().newPersistentDataContainer();
        container.set(TERRA_NAME_KEY, PersistentDataType.STRING, name);
        root.set(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER, container);
        item.setItemMeta(meta);
    }

    @Override
    public String getComponentName() {
        return "terr_name";
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
