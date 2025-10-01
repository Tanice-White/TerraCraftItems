package io.tanice.terracraftitems.api.item.component.vanilla;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

public interface TerraRarityComponent extends TerraBaseComponent {
    /**
     * 默认稀有度
     */
    enum Rarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
    }
}
