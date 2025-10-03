package io.tanice.terracraftitems.paper.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraDamageResistantComponent;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.core.util.namespace.TerraNamespaceKey;
import io.tanice.terracraftitems.paper.util.version.MinecraftVersions;
import io.tanice.terracraftitems.paper.util.version.ServerVersion;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DamageResistantComponent implements TerraDamageResistantComponent {

    private final TerraNamespaceKey resistantType;

    public DamageResistantComponent(TerraNamespaceKey resistantType) {
        this.resistantType = resistantType;
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt ->{
                nbt.getOrCreateCompound(MINECRAFT_PREFIX + "damage_resistant").setString("types", "#" + resistantType.get());
            });
        } else TerraLogger.warning("damage resistant component is only supported in Minecraft 1.21.2 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "resistant";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt ->{
                nbt.removeKey(MINECRAFT_PREFIX + "damage_resistant");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt ->{
                nbt.removeKey(MINECRAFT_PREFIX + "damage_resistant");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "damage_resistant");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(resistantType);
    }
}
