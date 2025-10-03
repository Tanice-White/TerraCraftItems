package io.tanice.terracraftitems.paper.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBTCompoundList;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraDeathProtectionComponent;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.paper.util.nbtapi.vanilla.NBTEffect;
import io.tanice.terracraftitems.paper.util.version.MinecraftVersions;
import io.tanice.terracraftitems.paper.util.version.ServerVersion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeathProtectionComponent implements TerraDeathProtectionComponent {

    private final List<NBTEffect> effects;

    public DeathProtectionComponent() {
        this.effects = new ArrayList<>();
    }

    public DeathProtectionComponent(ConfigurationSection cfg) {
        this();
        NBTEffect effect;
        for (String key : cfg.getKeys(false)) {
            effect = NBTEffect.from(key, cfg.getConfigurationSection(key));
            if (effect != null) effects.add(effect);
        }
    }

    @Override
    public void cover(ItemStack item) {
        clear(item);
        if (effects.isEmpty()) return;
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt ->{
                if (effects.isEmpty()) return;
                ReadWriteNBTCompoundList compoundList = nbt.getOrCreateCompound(MINECRAFT_PREFIX + "death_protection").getCompoundList("death_effects");
                /* 覆盖 */
                compoundList.clear();
                ReadWriteNBT component;
                for (NBTEffect effect : effects) {
                    component = compoundList.addCompound();
                    effect.addToCompound(component);
                }
            });
        } else TerraLogger.warning("death protection component is only supported in Minecraft 1.21.2 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "death_protection";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt ->{
                nbt.removeKey(MINECRAFT_PREFIX + "death_protection");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_2)) {
            NBT.modifyComponents(item, nbt ->{
                nbt.removeKey(MINECRAFT_PREFIX + "death_protection");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "death_protection");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(effects);
    }
}
