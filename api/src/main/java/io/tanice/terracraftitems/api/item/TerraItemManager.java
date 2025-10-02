package io.tanice.terracraftitems.api.item;

import java.util.Collection;
import java.util.Optional;

/**
 * 物品管理器接口，负责物品的管理和查询
 */
public interface TerraItemManager {

    /**
     * 获取所有已注册物品的名称集合
     *
     * @return 包含所有物品名称的集合
     */
    Collection<String> getItemNames();

    /**
     * 根据物品名称获取对应的物品实例
     *
     * @param name 物品的名称标识符
     * @return 包含 {@link TerraItem} 实例的 {@code Optional} 对象，如果不存在则为空 {@code Optional}
     */
    Optional<TerraItem> getItem(String name);

    /**
     * 判断指定名称的物品是否为泰拉瑞亚风格物品
     *
     * @param name 物品名称
     * @return 如果是泰拉瑞亚物品返回 {@code true}，否则返回 {@code false}
     */
    boolean isTerraItem(String name);

    /**
     * 根据名称筛选物品
     *
     * @param name 筛选关键词
     * @return 符合筛选条件的物品名称集合
     */
    Collection<String> filterItems(String name);

    /**
     * 获取组件工厂实例
     *
     * @return {@link TerraComponentFactory} 实例
     */
    TerraComponentFactory getComponentFactory();
}