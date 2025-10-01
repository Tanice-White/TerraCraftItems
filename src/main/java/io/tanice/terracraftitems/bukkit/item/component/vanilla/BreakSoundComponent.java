package io.tanice.terracraftitems.bukkit.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraBreakSoundComponent;
import io.tanice.terracraftitems.bukkit.util.nbtapi.vanilla.NBTSound;
import io.tanice.terracraftitems.bukkit.util.version.MinecraftVersions;
import io.tanice.terracraftitems.bukkit.util.version.ServerVersion;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import io.tanice.terracraftitems.core.util.namespace.TerraNamespaceKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;

public class BreakSoundComponent implements TerraBreakSoundComponent {
    @Nullable
    private final NBTSound sound;

    public BreakSoundComponent(@Nullable NBTSound sound) {
        this.sound = sound;
    }

    public BreakSoundComponent(ConfigurationSection cfg) {
        this(new NBTSound(cfg.isSet("range") ? (float) cfg.getDouble("range") : null, TerraNamespaceKey.from(cfg.getString("id"))));
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
            NBT.modifyComponents(item, nbt -> {
                if (sound != null) sound.addToCompound(nbt.getOrCreateCompound(MINECRAFT_PREFIX + "break_sound"));
            });
        } else TerraCraftLogger.warning("break sound component is only supported in Minecraft 1.21.5 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "break_sound";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "break_sound");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "break_sound");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "break_sound");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(sound);
    }
}
