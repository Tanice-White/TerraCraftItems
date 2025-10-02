package io.tanice.terracraftitems.bukkit.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraJukeboxPlayable;
import io.tanice.terracraftitems.bukkit.util.logger.TerraCraftLogger;
import io.tanice.terracraftitems.bukkit.util.namespace.TerraNamespaceKey;
import io.tanice.terracraftitems.bukkit.util.version.MinecraftVersions;
import io.tanice.terracraftitems.bukkit.util.version.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class JukeboxPlayable implements TerraJukeboxPlayable {
    private final TerraNamespaceKey musicId;

    public JukeboxPlayable(String musicId) {
        this.musicId = TerraNamespaceKey.from(musicId);
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21)) {
            NBT.modifyComponents(item, nbt -> {
                if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
                    nbt.setString(MINECRAFT_PREFIX + "jukebox_playable", musicId.get());
                } else {
                    nbt.getOrCreateCompound(MINECRAFT_PREFIX + "jukebox_playable").setString("song", musicId.get());
                }
            });
        } else TerraCraftLogger.warning("Jukebox playable component is only supported in Minecraft 1.21 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "music_disc";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "jukebox_playable");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "jukebox_playable");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "jukebox_playable");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicId);
    }
}
