package io.tanice.terracraftitems.api.item.component.custom;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

/**
 * 物品内部名称组件接口，用于获取物品的内部名称
 * 继承自基础组件接口 {@link TerraBaseComponent}
 */
public interface TerraInnerNameComponent extends TerraBaseComponent {

    /**
     * 获取物品的内部名称
     *
     * @return 物品的内部名称字符串
     */
    String getName();
}