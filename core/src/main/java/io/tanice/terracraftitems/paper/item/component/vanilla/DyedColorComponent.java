package io.tanice.terracraftitems.paper.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraDyedColor;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftcore.api.version.MinecraftVersions;
import io.tanice.terracraftcore.api.version.ServerVersion;
import io.tanice.terracraftitems.paper.util.ColorUtil;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;

public class DyedColorComponent implements TerraDyedColor {

    @Nullable
    private final Integer color;

    public DyedColorComponent(@Nullable String color) {
        this.color = ColorUtil.stringToRgb(color);
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                if (color != null) nbt.setInteger(MINECRAFT_PREFIX + "dyed_color", color);
            });
        } else TerraCraftItems.inst().logger().warning("Dye color component is only supported in Minecraft 1.20.5 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "dyed_color";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "dyed_color");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "dyed_color");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "dyed_color");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
