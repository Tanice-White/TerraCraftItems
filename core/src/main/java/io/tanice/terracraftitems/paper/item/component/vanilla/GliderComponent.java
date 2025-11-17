package io.tanice.terracraftitems.paper.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraGliderComponent;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftcore.api.version.MinecraftVersions;
import io.tanice.terracraftcore.api.version.ServerVersion;
import org.bukkit.inventory.ItemStack;

public class GliderComponent implements TerraGliderComponent {
    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.getOrCreateCompound(MINECRAFT_PREFIX + "glider");
            });
        } else TerraCraftItems.inst().logger().warning("Glider component is only supported in Minecraft 1.21.2 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "glider";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "glider");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "glider");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "glider");
            });
        }
    }
}
