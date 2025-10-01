package io.tanice.terracraftitems.api.item.component.vanilla;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

import javax.annotation.Nullable;

/**
 * 物品属性修饰符组件接口，用于管理物品对实体属性的修饰效果
 * 继承自基础组件接口 {@link TerraBaseComponent}
 */
public interface TerraAttributeModifiersComponent extends TerraBaseComponent {

    /**
     * 为物品添加一个属性修饰符
     *
     * @param id           所更改属性的命名空间值
     * @param attribute    要修饰的属性名称
     * @param amount       修饰的数值大小
     * @param op           修饰操作类型，对应 {@link Operation} 枚举值的字符串表示
     * @param slot         适用的装备槽位，可为null表示适用于所有槽位
     * @param displayType  修饰符的显示类型，对应 {@link DisplayType} 枚举值的字符串表示，可为 {@code null}
     * @param extraValue   额外的修饰值信息，可为 {@code null}
     */
    void addAttributeModifier(String id, String attribute, double amount, String op, @Nullable String slot, @Nullable String displayType, @Nullable String extraValue);

    /**
     * 属性修饰符的操作类型枚举，表示修饰符如何影响属性值
     */
    enum Operation {
        /** 直接添加固定值到属性 */
        ADD_VALUE,
        /** 基于属性基础值的乘法加成 */
        ADD_MULTIPLY_BASE,
        /** 基于属性总值的乘法加成 */
        ADD_MULTIPLY_TOTAL;
    }

    /**
     * 属性修饰符的显示类型枚举，表示修饰符在界面中的展示方式
     */
    enum DisplayType {
        /** 默认显示方式，遵循常规规则展示 */
        DEFAULT,
        /** 隐藏修饰符，不在界面中显示 */
        HIDDEN,
        /** 覆盖显示方式，使用自定义规则展示 */
        OVERRIDE,
    }
}