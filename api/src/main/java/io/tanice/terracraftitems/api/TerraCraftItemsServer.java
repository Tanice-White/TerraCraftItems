package io.tanice.terracraftitems.api;

import io.tanice.terracraftitems.api.item.TerraComponentFactory;

import javax.annotation.Nonnull;
import java.io.File;

public interface TerraCraftItemsServer {

    @Nonnull
    TerraComponentFactory getComponentFactory();

    @Nonnull
    File getDataFolder();
}
