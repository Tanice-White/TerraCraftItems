package io.tanice.terracraftitems.api;

import io.tanice.terracraftitems.api.item.TerraComponentFactory;

import javax.annotation.Nonnull;
import java.io.File;

public final class TerraCraftItemsBukkit {

    private static TerraCraftItemsServer server;

    private TerraCraftItemsBukkit() {

    }

    public static void setServer(@Nonnull TerraCraftItemsServer server) {
        if (TerraCraftItemsBukkit.server != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");

        } else TerraCraftItemsBukkit.server = server;
    }

    /**
     * 在 onLoad() 完成组件注册
     */
    @Nonnull
    public static TerraComponentFactory getComponentFactory() {
        return server.getComponentFactory();
    }

    @Nonnull
    public static File getDataFolder() {
        return server.getDataFolder();
    }
}
