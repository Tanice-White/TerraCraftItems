package io.tanice.terracraftitems.paper.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTList;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraRepairComponent;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.core.util.namespace.TerraNamespaceKey;
import io.tanice.terracraftitems.paper.util.version.MinecraftVersions;
import io.tanice.terracraftitems.paper.util.version.ServerVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Repairable + RepairCost
 */
public class RepairComponent implements TerraRepairComponent {
    @Nullable
    private final Integer cost;
    private final List<TerraNamespaceKey> items;

    public RepairComponent(@Nullable Integer cost, List<TerraNamespaceKey> items) {
        this.cost = cost;
        this.items = items;
    }

    public RepairComponent(ConfigurationSection cfg) {
        this(
                cfg.isSet("cost") ? cfg.getInt("cost") : null,
                cfg.getStringList("items").stream().map(TerraNamespaceKey::from).filter(Objects::nonNull).toList()
        );
    }

    @Override
    public void cover(ItemStack item) {
        if (cost != null) {
            if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
                NBT.modifyComponents(item, nbt -> {
                    nbt.setInteger(MINECRAFT_PREFIX + "repair_cost", cost);
                });
            } else {
                NBT.modify(item, nbt -> {
                    nbt.setInteger("RepairCost", cost);
                });
            }
        }

        if (item != null && !items.isEmpty()) {
            if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
                NBT.modifyComponents(item, nbt -> {
                    ReadWriteNBTList<String> list = nbt.getOrCreateCompound(MINECRAFT_PREFIX + "repairable").getStringList("items");
                    /* 覆盖 */
                    list.clear();
                    list.addAll(items.stream().map(TerraNamespaceKey::get).toList());
                });
            } else TerraLogger.warning("Repairable component is only supported in Minecraft 1.21.2 or newer versions");
        }
    }

    @Override
    public String getComponentName() {
        return "repair";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "repair_cost");
            });
        } else {
            NBT.modify(item, nbt -> {
                nbt.removeKey("RepairCost");
            });
        }
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "repairable");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "repair_cost");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "repair_cost");
            });
        } else {
            NBT.modify(item, nbt -> {
                nbt.removeKey("RepairCost");
            });
        }
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "repairable");
                /* 不能禁止武器合武器式修复 */
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "repairable");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, items);
    }
}
