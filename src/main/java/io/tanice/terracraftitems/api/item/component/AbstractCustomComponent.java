package io.tanice.terracraftitems.api.item.component;

import io.tanice.terracraftitems.bukkit.item.component.ComponentState;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Objects;

import static io.tanice.terracraftitems.bukkit.util.constance.PDCKey.TERRA_COMPONENT_KEY;

/**
 * 物品组件抽象基类，实现了TerraBaseComponent接口，提供组件的基础功能实现。
 * <p>
 * 包含组件状态管理、物品数据容器操作等通用功能，
 * 子类需实现具体的组件逻辑（如属性覆盖、数据展示等）。
 * <p>
 * 1. {@code public static XXX from(ItemStack item)} -从 {@code itemStack}的nbt中获取本组件实例
 * <p>
 * 2. {@code public static void remove(ItemStack item)} -从 {@code itemStack}中移除组件的nbt
 */
public abstract class AbstractCustomComponent implements TerraBaseComponent {

    /**
     * 组件状态对象，管理原始性、可修改性和可更新性
     */
    protected ComponentState state;

    /**
     * 构造方法，初始化组件状态
     *
     * @param updatable 是否允许更新
     */
    public AbstractCustomComponent(boolean updatable) {
        state = new ComponentState(true, false, updatable);
    }

    /**
     * 构造方法，使用指定的状态对象初始化
     *
     * @param state 组件状态对象，非空
     */
    public AbstractCustomComponent(ComponentState state) {
        this.state = state;
    }

    /**
     * 获取组件状态对象
     *
     * @return 组件状态对象，非空
     */
    public ComponentState getState() {
        return this.state;
    }

    /**
     * 设置组件状态对象
     * <p>
     * 如果传入 {@code null}，会创建默认状态对象
     *
     * @param state 新的组件状态对象，可为 {@code null}
     */
    public void setState(@Nullable ComponentState state) {
        if (state == null) this.state = new ComponentState(null, null, null);
        else this.state = state;
    }

    /**
     * 判断组件是否可更新
     * <p>
     * 仅当组件是原始状态且允许更新时返回true
     *
     * @return 如果组件可更新则返回true，否则返回false
     */
    @Override
    public boolean canUpdate() {
        return state.isUpdatable() && state.isOriginal();
    }

    /**
     * 将组件覆盖到物品上
     * <p>
     * 先执行具体覆盖逻辑，再更新物品 lore 信息
     *
     * @param item 目标物品栈，非空
     */
    @Override
    public void cover(ItemStack item) {
        doCover(item);
        updateLore(item);
    }

    /**
     * 更新物品的 lore 信息
     * <p>
     * 默认实现为空，子类可重写以添加自定义 lore 逻辑
     */
    protected void updateLore(ItemStack item) {

    }

    /**
     * 获取物品的TerraCraft组件数据容器
     *
     * @param item 目标物品栈，可为 {@code null}
     * @return 物品的组件数据容器，若物品为空、是空气或无元数据则返回 {@code null}
     */
    @Nullable
    protected static PersistentDataContainer getTerraContainer(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer root = meta.getPersistentDataContainer();
        return root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
    }

    /**
     * 移除物品上本插件nbt下的最外层的一个key
     * @param item 目标物品
     * @param target 目标 key
     * @throws NullPointerException 如果 {@code item} 为 {@code null}
     */
    protected static void removeSubTerraContainer(ItemStack item, NamespacedKey target) {
        Objects.requireNonNull(item, "item should not be null");
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer root = meta.getPersistentDataContainer();
        PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null) return;
        container.remove(target);
        item.setItemMeta(meta);
    }

    /**
     * 获取支持游戏内指令页面显示的数据展示字符串
     *
     * @return 组件的字符串表示，非空
     */
    public abstract String toString();

    /**
     * 将组件属性（包括状态和额外属性）写入物品
     * <p>
     * 子类需实现此方法以完成具体的属性覆盖逻辑
     *
     * @param item 目标物品栈，非空
     */
    protected abstract void doCover(ItemStack item);
}