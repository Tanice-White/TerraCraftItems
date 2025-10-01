package io.tanice.terracraftitems.api.item.component.custom;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

import javax.annotation.Nullable;

/**
 * 物品指令组件接口，用于定义物品消耗后执行的指令相关操作
 * 继承自基础组件接口 {@link TerraBaseComponent}
 */
public interface TerraCommandComponent extends TerraBaseComponent {

    /**
     * 返回物品消耗后执行的指令列表的引用
     *
     * @return 指令字符串数组，若没有指令则返回 {@code null}
     */
    @Nullable
    String[] getCommands();

    /**
     * 设置物品消耗后执行的指令列表
     *
     * @param commands 要设置的指令字符串数组，可为 {@code null}
     */
    void setCommands(@Nullable String[] commands);
}