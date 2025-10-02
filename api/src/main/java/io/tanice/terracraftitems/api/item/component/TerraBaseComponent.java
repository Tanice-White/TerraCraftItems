package io.tanice.terracraftitems.api.item.component;

import org.bukkit.inventory.ItemStack;

/**
 * 物品组件基础接口，定义了物品组件的核心功能规范。
 * <p>
 * 所有物品组件都需实现此接口，提供组件覆盖、名称获取、更新控制等基础能力。
 * <p>
 * 自定义组件还需要同时实现2个静态方法
 * <p>
 * 1. {@code public static XXX from(ItemStack item)} -从 {@code itemStack}的nbt中获取本组件实例
 * <p>
 * 2. {@code public static void remove(ItemStack item)} -从 {@code itemStack}中移除组件的nbt
 */
public interface TerraBaseComponent {

    /**
     * Minecraft命名空间前缀，用于标识游戏内原生资源
     */
    String MINECRAFT_PREFIX = "minecraft:";

    /**
     * 将当前组件的属性和状态覆盖到目标物品上
     *
     * @param item 要应用组件的物品栈，非空
     */
    void cover(ItemStack item);

    /**
     * 获取组件的内部唯一名称（key），用于标识和区分不同组件
     *
     * @return 组件内部名称，非空字符串
     */
    String getComponentName();

    /**
     * 判断组件是否可更新
     * <p>
     * 默认允许更新
     */
    default boolean canUpdate() {
        return true;
    }

    /**
     * 更新组件时继承部分原有值
     * <p>
     * 默认实现不继承任何值，直接返回当前组件实例
     *
     * @return 更新后的组件实例，非空
     */
    default TerraBaseComponent updatePartial() {
        /* 默认不需要继承任何值 */
        return this;
    }
}