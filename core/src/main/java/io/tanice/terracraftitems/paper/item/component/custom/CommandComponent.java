package io.tanice.terracraftitems.paper.item.component.custom;

import io.tanice.terracraftitems.api.item.component.AbstractCustomComponent;
import io.tanice.terracraftitems.api.item.component.custom.TerraCommandComponent;
import io.tanice.terracraftitems.api.item.ComponentState;
import io.tanice.terracraftitems.paper.item.datatype.CommandComponentDataType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Objects;

import static io.tanice.terracraftitems.paper.util.constance.PDCKey.COMMAND_KEY;
import static io.tanice.terracraftitems.paper.util.constance.PDCKey.TERRA_COMPONENT_KEY;
import static io.tanice.terracraftitems.paper.util.pdc.PDCUtil.getTerraContainer;
import static io.tanice.terracraftitems.paper.util.pdc.PDCUtil.removeSubTerraContainer;

/**
 * 消耗物品的额外拓展
 */
public class CommandComponent extends AbstractCustomComponent implements TerraCommandComponent {

    @Nullable
    private String[] commands;

    public CommandComponent(ConfigurationSection cfg) {
        this(cfg.getStringList("content").toArray(new String[0]), cfg.getBoolean("updatable", true));
    }

    public CommandComponent(@Nullable String[] commands, ComponentState state) {
        super(state);
        this.commands = commands;
    }

    public CommandComponent(@Nullable String[] commands, boolean updatable) {
        super(updatable);
        this.commands = commands;
    }

    @Override
    protected void doCover(ItemStack item) {
        item.editMeta(meta -> {
            PersistentDataContainer root = meta.getPersistentDataContainer();
            PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
            if (container == null) container = root.getAdapterContext().newPersistentDataContainer();
            // 如果先remove，则更新的收就无法继承值了
            container.set(COMMAND_KEY, CommandComponentDataType.INSTANCE, this);
            root.set(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER, container);
        });
    }

    @Nullable
    public static CommandComponent from(ItemStack item) {
        PersistentDataContainer container = getTerraContainer(item);
        if (container == null) return null;
        return container.get(COMMAND_KEY, CommandComponentDataType.INSTANCE);
    }

    public static void clear(ItemStack item) {
        removeSubTerraContainer(item, COMMAND_KEY);
    }

    public static void remove(ItemStack item) {
        clear(item);
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object) commands);
    }

    @Override
    @Nullable
    public String[] getCommands() {
        return this.commands;
    }

    @Override
    public void setCommands(@Nullable String[] commands) {
        this.commands = commands;
    }

    @Override
    public String getComponentName() {
        return "command";
    }
}
