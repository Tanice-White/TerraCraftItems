package io.tanice.terracraftitems.paper.item.component.vanilla;

import de.tr7zw.nbtapi.NBT;
import io.tanice.terracraftitems.api.item.component.vanilla.TerraEnchantmentGlintOverrideComponent;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.paper.util.version.MinecraftVersions;
import io.tanice.terracraftitems.paper.util.version.ServerVersion;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Objects;

public class EnchantmentGlintOverrideComponent implements TerraEnchantmentGlintOverrideComponent {
    @Nullable
    private final Boolean glint;

    public EnchantmentGlintOverrideComponent(@Nullable Boolean glint) {
        this.glint = glint;
    }

    @Override
    public void cover(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                if(glint != null) nbt.setBoolean(MINECRAFT_PREFIX + "enchantment_glint_override", glint);
            });
        } else TerraLogger.warning("Enchantment glint override component is only supported in Minecraft 1.20.5 or newer versions");
    }

    @Override
    public String getComponentName() {
        return "glint";
    }

    public static void clear(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "enchantment_glint_override");
            });
        }
    }

    public static void remove(ItemStack item) {
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            NBT.modifyComponents(item, nbt -> {
                nbt.removeKey(MINECRAFT_PREFIX + "enchantment_glint_override");
                nbt.getOrCreateCompound("!" + MINECRAFT_PREFIX + "enchantment_glint_override");
            });
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(glint);
    }
}
