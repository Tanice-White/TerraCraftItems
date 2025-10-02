package io.tanice.terracraftitems.api.item.component.custom;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

/**
 * 物品更新代码组件接口，用于管理物品的更新代码
 * 继承自基础组件接口 {@link TerraBaseComponent}
 */
public interface TerraUpdateCodeComponent extends TerraBaseComponent {

    /**
     * 获取物品的更新代码
     *
     * @return 物品的更新代码值
     */
    int getCode();

    /**
     * 设置物品的更新代码
     *
     * @param code 要设置的更新代码值
     */
    void setCode(int code);
}