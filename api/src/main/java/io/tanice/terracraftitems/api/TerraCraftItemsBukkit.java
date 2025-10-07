package io.tanice.terracraftitems.api;

import io.tanice.terracraftitems.api.item.TerraComponentFactory;

import javax.annotation.Nonnull;

public final class TerraCraftItemsBukkit {

    private static TerraCraftItemsServer server;

    private TerraCraftItemsBukkit() {

    }

    /**
     * 在 onLoad() 完成组件注册
     * @return
     */
    @Nonnull
    public static TerraComponentFactory getComponentFactory() {
        return server.getComponentFactory();
    }

    public static void setServer(@Nonnull TerraCraftItemsServer server) {
        if (TerraCraftItemsBukkit.server != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");

        } else TerraCraftItemsBukkit.server = server;
    }
}
