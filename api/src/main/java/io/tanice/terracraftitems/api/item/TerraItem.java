package io.tanice.terracraftitems.api.item;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * 物品的基础接口，定义了物品的核心属性和行为
 */
public interface TerraItem {

    /**
     * 获取当前物品所包含的所有组件集合
     *
     * @return 物品组件的集合，包含该物品所有功能相关的组件
     */
    Set<TerraBaseComponent> getComponents();

    /**
     * 获取对应的Bukkit物品栈实例
     *
     * @return 该物品对应的Bukkit {@code ItemStack} 对象
     */
    ItemStack getBukkitItem();

    /**
     * 获取物品的名称（标识符）
     *
     * @return 物品的唯一名称字符串
     */
    String getName();

    /**
     * 更新旧的物品栈为当前物品的状态
     *
     * @param old 旧的物品栈实例
     * @return 如果更新成功返回 {@code true}，否则返回 {@code false}
     */
    boolean updateOld(ItemStack old);

    /**
     * 获取物品的哈希码，用于物品的更新标记
     *
     * @return 物品的哈希码值
     */
    int getHashCode();
}