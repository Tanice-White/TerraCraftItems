package io.tanice.terracraftitems.api.item.component.custom;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

import javax.annotation.Nullable;

/**
 * 物品额外NBT组件接口，用于管理物品的额外非本插件的NBT数据
 * 继承自基础组件接口 {@link TerraBaseComponent}
 */
public interface TerraExtraNBTComponent extends TerraBaseComponent {

    /**
     * 根据键获取对应的NBT值
     *
     * @param key NBT键名
     * @return 与键对应的NBT值，若不存在则返回 {@code null}
     */
    @Nullable
    String getNBTValue(String key);

    /**
     * 向物品添加额外的NBT键值对
     *
     * @param key   要添加的NBT键名
     * @param value 要添加的NBT值
     */
    void addNBT(String key, String value);

    /**
     * 从物品中移除指定键的NBT数据
     *
     * @param key 要移除的NBT键名
     */
    void removeNBT(String key);
}