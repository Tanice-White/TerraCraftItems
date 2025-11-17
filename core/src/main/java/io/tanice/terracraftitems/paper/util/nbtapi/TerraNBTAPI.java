package io.tanice.terracraftitems.paper.util.nbtapi;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import io.tanice.terracraftcore.api.version.MinecraftVersions;
import io.tanice.terracraftcore.api.version.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

import static io.tanice.terracraftitems.api.item.component.TerraBaseComponent.MINECRAFT_PREFIX;

public final class TerraNBTAPI {

    /**
     * 判断物品是否有盾牌属性
     */
    public static boolean isShield(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        /* 检测组件 */
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
            return NBT.getComponents(item, nbt -> {
                /* 有组件 */
                if (nbt.resolveCompound(MINECRAFT_PREFIX + "blocks_attacks") != null) return true;
                return nbt.resolveCompound("!" + MINECRAFT_PREFIX + "blocks_attacks") == null && item.getType() == Material.SHIELD;
            });
        }
        /* 看数据类型 */
        return item.getType() == Material.SHIELD;
    }

    /**
     * 获取原版tool组件中的 damage_per_block
     */
    public static int getOriDamagePerBlockInToolComponent(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) return 0;
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_20_5)) {
            return NBT.getComponents(item, nbt -> {
                ReadableNBT compound = nbt.resolveCompound(MINECRAFT_PREFIX + "tool");
                if (compound == null) return 1;
                if (compound.hasTag("damage_per_block")) return compound.getInteger("damage_per_block");
                return 1;
            });
        }
        return 1;
    }

    /**
     * 获取原版weapon组件中的 damage_per_attack
     */
    public static int getOriDamagePerAttackInWeaponComponent(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) return 0;
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
            return NBT.getComponents(item, nbt -> {
                ReadableNBT component = nbt.resolveCompound(MINECRAFT_PREFIX + "weapon");
                if (component == null) return 1;
                if (component.hasTag("item_damage_per_attack")) return component.getInteger("item_damage_per_attack");
                return 1;
            });
        }
        return 1;
    }

    /**
     * 获取原版的break_sound
     */
    public static String getOriBreakSound(ItemStack item) {
        String res = "minecraft:entity.item.break";
        if (ServerVersion.isAfterOrEq(MinecraftVersions.v1_21_5)) {
            return NBT.getComponents(item, nbt -> {
                ReadableNBT compound = nbt.getCompound(MINECRAFT_PREFIX + "break_sound");
                if (compound != null) return compound.getString("sound_id");
                return res;
            });
        }
        return res;
    }
}
