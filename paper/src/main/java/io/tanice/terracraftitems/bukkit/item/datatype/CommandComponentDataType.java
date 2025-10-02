package io.tanice.terracraftitems.bukkit.item.datatype;

import io.tanice.terracraftitems.api.item.component.AbstractComponentDataType;
import io.tanice.terracraftitems.api.item.ComponentState;
import io.tanice.terracraftitems.bukkit.item.component.custom.CommandComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

import static io.tanice.terracraftitems.bukkit.util.constance.PDCKey.CONTENT_KEY;
import static io.tanice.terracraftitems.bukkit.util.constance.PDCKey.STATE_KEY;

public class CommandComponentDataType extends AbstractComponentDataType<CommandComponent> {

    public static final CommandComponentDataType INSTANCE = new CommandComponentDataType();

    @Nonnull
    @Override
    public Class<CommandComponent> getComplexType() {
        return CommandComponent.class;
    }

    @Override
    protected void writeToContainer(PersistentDataContainer container, CommandComponent component) {
        container.set(STATE_KEY, PersistentDataType.BYTE, component.getState().toNbtByte());
        container.set(CONTENT_KEY, StringArray.INSTANCE, component.getCommands() == null ? new String[0] : component.getCommands());
    }

    @Override
    protected CommandComponent readFromContainer(PersistentDataContainer container) {
        String[] commands = container.get(CONTENT_KEY, StringArray.INSTANCE);
        ComponentState state = new ComponentState(container.get(STATE_KEY, PersistentDataType.BYTE));
        return new CommandComponent(commands, state);
    }
}
