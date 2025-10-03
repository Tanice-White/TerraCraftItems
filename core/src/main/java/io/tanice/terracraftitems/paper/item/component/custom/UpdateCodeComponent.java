package io.tanice.terracraftitems.paper.item.component.custom;

import io.tanice.terracraftitems.api.item.component.custom.TerraUpdateCodeComponent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;

import static io.tanice.terracraftitems.core.util.color.CommandColor.*;
import static io.tanice.terracraftitems.paper.util.constance.PDCKey.TERRA_COMPONENT_KEY;
import static io.tanice.terracraftitems.paper.util.constance.PDCKey.UPDATE_CODE_KEY;

public class UpdateCodeComponent implements TerraUpdateCodeComponent {

    private int code;

    public UpdateCodeComponent(int code) {
        this.code = code;
    }

    @Nullable
    public static UpdateCodeComponent from(ItemStack item) {
        if (item == null || item.getType().isAir()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer().get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null) return null;
        Integer code = container.get(UPDATE_CODE_KEY, PersistentDataType.INTEGER);
        if (code == null) return null;
        return new UpdateCodeComponent(code);
    }

    @Override
    public void cover(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer root = meta.getPersistentDataContainer();
        PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null) container = root.getAdapterContext().newPersistentDataContainer();
        container.set(UPDATE_CODE_KEY, PersistentDataType.INTEGER, code);
        root.set(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER, container);
        item.setItemMeta(meta);
    }

    @Override
    public String getComponentName() {
        return "code";
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public String toString() {
        return BOLD + YELLOW + "update_code:" + code + RESET;
    }
}
