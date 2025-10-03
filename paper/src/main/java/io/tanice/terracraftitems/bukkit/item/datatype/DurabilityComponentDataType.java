package io.tanice.terracraftitems.bukkit.item.datatype;

import io.tanice.terracraftitems.api.item.component.AbstractComponentDataType;
import io.tanice.terracraftitems.api.item.ComponentState;
import io.tanice.terracraftitems.bukkit.item.component.custom.DurabilityComponent;
import io.tanice.terracraftitems.bukkit.util.logger.TerraCraftLogger;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

import static io.tanice.terracraftitems.bukkit.util.constance.PDCKey.*;

public class DurabilityComponentDataType extends AbstractComponentDataType<DurabilityComponent> {

    public static final DurabilityComponentDataType INSTANCE = new DurabilityComponentDataType();

    @Nonnull
    @Override
    public Class<DurabilityComponent> getComplexType() {
        return DurabilityComponent.class;
    }

    @Override
    protected void writeToContainer(PersistentDataContainer container, DurabilityComponent component) {
        container.set(STATE_KEY, PersistentDataType.BYTE, component.getState().toNbtByte());
        container.set(MAX_DAMAGE_KEY, PersistentDataType.INTEGER, component.getMaxDamage());
        if (component.getDamage() != null) container.set(DAMAGE_KEY, PersistentDataType.INTEGER, component.getDamage());
        if (component.isBreakLoss() != null) container.set(BREAK_LOSS_KEY, PersistentDataType.BOOLEAN, component.isBreakLoss());
        if (component.getDamageExpr() != null) container.set(EXPR_KEY, PersistentDataType.STRING, component.getDamageExpr());
    }

    @Override
    @Nonnull
    protected DurabilityComponent readFromContainer(PersistentDataContainer container) {
        Integer maxDamage = container.get(MAX_DAMAGE_KEY, PersistentDataType.INTEGER);
        if (maxDamage == null) {
            TerraCraftLogger.error("Durability component(PDC) has no max damage!");
            throw new IllegalArgumentException("PDC missing required max damage value");
        }
        return new DurabilityComponent(
                container.get(DAMAGE_KEY, PersistentDataType.INTEGER),
                maxDamage,
                container.get(BREAK_LOSS_KEY, PersistentDataType.BOOLEAN),
                container.get(EXPR_KEY, PersistentDataType.STRING),
                new ComponentState(container.get(STATE_KEY, PersistentDataType.BYTE))
        );
    }
}
