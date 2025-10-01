package io.tanice.terracraftitems.bukkit.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraToolComponent;
import io.tanice.terracraftitems.bukkit.util.version.MinecraftVersions;
import io.tanice.terracraftitems.bukkit.util.version.ServerVersion;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import io.tanice.terracraftitems.core.util.namespace.TerraNamespaceKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ToolComponent implements TerraToolComponent {
    @Nullable
    private final Boolean canDestroyInCreative;
    @Nullable
    private final Integer damagePerBlock;
    @Nullable
    private final Float defaultMiningSpeed;

    private final List<DigConfig> rules;

    public ToolComponent(@Nullable Boolean canDestroyInCreative, @Nullable Integer damagePerBlock, @Nullable Float defaultMiningSpeed) {
        this.canDestroyInCreative = canDestroyInCreative;
        this.damagePerBlock = damagePerBlock;
        this.defaultMiningSpeed = defaultMiningSpeed;
        this.rules = new ArrayList<>();
    }

    public ToolComponent(ConfigurationSection cfg) {
        this(
                cfg.isSet("creative_destroy") ? cfg.getBoolean("creative_destroy") : null,
                cfg.isSet("damage_per_block") ? cfg.getInt("damage_per_block") : null,
                cfg.isSet("default_speed") ? (float) cfg.getDouble("default_speed") : null
        );
        ConfigurationSection sub = cfg.getConfigurationSection("rules");
        if (sub == null)  return;
        ConfigurationSection tmp;
        for (String key : sub.getKeys(false)) {
            tmp = sub.getConfigurationSection(key);
            if (tmp == null) continue;
            this.rules.add(new DigConfig(tmp));
        }
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                ReadWriteNBT component = nbt.getOrCreateCompound(MINECRAFT_PREFIX + "tool");
                if (canDestroyInCreative != null) {
                    if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) component.setBoolean("can_destroy_blocks_in_creative", canDestroyInCreative);
                    else TerraCraftLogger.warning("can_destroy_blocks_in_creative in Tool component is only supported in Minecraft 1.21.5 or newer versions");
                }
                if (damagePerBlock != null) component.setInteger("damage_per_block", damagePerBlock);
                if (defaultMiningSpeed != null) component.setFloat("default_mining_speed", defaultMiningSpeed);
                ReadWriteNBTCompoundList compoundList = component.getCompoundList("rules");
                /* 覆盖 */
                compoundList.clear();
                for (DigConfig rule : rules) rule.addToCompound(compoundList.addCompound());
            });
        } else TerraCraftLogger.warning("Tool component is only supported in Minecraft 1.20.5 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "tool";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "tool");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "tool");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "tool");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(canDestroyInCreative, damagePerBlock, defaultMiningSpeed, rules);
    }

    private static class DigConfig {
        private final List<TerraNamespaceKey> items;
        @Nullable
        private final Boolean correctForDrops;
        @Nullable
        private final Float speed;

        public DigConfig(List<TerraNamespaceKey> items, @Nullable Boolean correctForDrops, @Nullable Float speed) {
            this.items = items;
            this.correctForDrops = correctForDrops;
            this.speed = speed;
        }

        public DigConfig(ConfigurationSection cfg) {
            this(
                    cfg.getStringList("items").stream().map(TerraNamespaceKey::from).filter(Objects::nonNull).toList(),
                    cfg.isSet("drop") ? cfg.getBoolean("drop") : null,
                    cfg.isSet("speed") ? (float) cfg.getDouble("speed") : null
            );
        }

        public void addToCompound(ReadWriteNBT compound) {
            compound.getStringList("blocks").addAll(items.stream().map(TerraNamespaceKey::get).toList());
            if (speed != null) compound.setFloat("speed", speed);
            if (correctForDrops != null) compound.setBoolean("correct_for_drops", correctForDrops);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items, correctForDrops, speed);
        }
    }
}
