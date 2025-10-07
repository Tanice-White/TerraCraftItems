package io.tanice.terracraftitems.api;

import io.tanice.terracraftitems.api.item.TerraComponentFactory;

import javax.annotation.Nonnull;

public interface TerraCraftItemsServer {

    @Nonnull
    TerraComponentFactory getComponentFactory();
}
