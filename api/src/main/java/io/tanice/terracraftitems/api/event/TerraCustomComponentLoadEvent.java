package io.tanice.terracraftitems.api.event;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class TerraCustomComponentLoadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    /**
     * 如果在配置文件中，{@code key -> ConfigurationSection} 则参数为此 {@code key} 下的 {@code ConfigurationSection}
     * 否则参数为整个物品配置节点下的 {@code ConfigurationSection}
     */
    private final String itemInnerName;
    private final String componentKey;
    private final ConfigurationSection cfg;

    public TerraCustomComponentLoadEvent(@Nonnull String itemInnerName, @Nonnull String componentKey, @Nonnull ConfigurationSection cfg) {
        this.itemInnerName = itemInnerName;
        this.componentKey = componentKey;
        this.cfg = cfg;
    }

    public static HandlerList getHandlerList() {return handlers;}

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public ConfigurationSection getConfigurationSection() {
        return cfg;
    }

    @Nonnull
    public String getItemInnerName() {
        return itemInnerName;
    }

    @Nonnull
    public String getComponentKey() {
        return componentKey;
    }
}
