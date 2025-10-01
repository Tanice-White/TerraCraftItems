package io.tanice.terracraftitems.bukkit.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraMaxStackSizeComponent;
import io.tanice.terracraftitems.bukkit.util.version.MinecraftVersions;
import io.tanice.terracraftitems.bukkit.util.version.ServerVersion;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;

public class MaxStackSizeComponent implements TerraMaxStackSizeComponent {
    @Nullable
    private final Integer size;

    public MaxStackSizeComponent(@Nullable Integer size) {
        this.size = size;
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                if (size != null) nbt.setInteger(MINECRAFT_PREFIX + "max_stack_size", size);
            });
        } else TerraCraftLogger.warning("Modifying max stack size is not supported in this Minecraft version. This feature requires 1.20.5 or later.");
    }

    @Override
    public String getComponentName() {
        return "max_stack_size";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "max_stack_size");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "max_stack_size");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "max_stack_size");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(size);
    }
}
