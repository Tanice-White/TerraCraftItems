package io.tanice.terracraftitems.api.item.component.custom;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;

import javax.annotation.Nullable;

/**
 * 物品耐久度组件接口，用于管理物品的耐久度相关属性和操作
 * 继承自基础组件接口 {@link TerraBaseComponent}
 */
public interface TerraDurabilityComponent extends TerraBaseComponent {

    /**
     * 获取物品当前的损伤值（耐久消耗值）
     *
     * @return 当前损伤值，若未设置则返回 {@code null}，代表没有消耗
     */
    @Nullable
    Integer getDamage();

    /**
     * 设置物品当前的损伤值（耐久消耗值）
     *
     * @param damage 要设置的损伤值，可为 {@code null}
     */
    void setDamage(@Nullable Integer damage);

    /**
     * 获取物品的最大耐久值
     *
     * @return 最大耐久值
     */
    int getMaxDamage();

    /**
     * 设置物品的最大耐久值
     *
     * @param maxDamage 要设置的最大耐久值
     */
    void setMaxDamage(int maxDamage);

    /**
     * 获取物品是否在损坏时消失的标志
     *
     * @return 若损坏时消失则返回 {@code true}，否则返回 {@code false}，未设置则返回 {@code null}
     */
    @Nullable
    Boolean isBreakLoss();

    /**
     * 设置物品是否在损坏时消失的标志
     *
     * @param breakLoss 损坏时是否消失的标志，可为 {@code null}
     */
    void setBreakLoss(@Nullable Boolean breakLoss);

    /**
     * 获取用于计算耐久消耗的表达式
     *
     * @return 耐久消耗表达式字符串，若未设置则返回 {@code null}
     */
    @Nullable
    String getDamageExpr();

    /**
     * 设置用于计算耐久消耗的表达式
     *
     * @param damageExpr 耐久消耗表达式字符串，可为 {@code null}
     */
    void setDamageExpr(@Nullable String damageExpr);

    /**
     * 获取本次使用应该减少的耐久值
     *
     * @param damage 造成/承受的伤害，工具、弓、弩的使用默认为 {@code 0}
     * @return 需要减少的耐久值
     */
    int getDamageForUse(double damage);

    /**
     * 判断物品是否已损坏（耐久耗尽）
     *
     * @return 若物品已损坏则返回 {@code true}，否则返回 {@code false}
     */
    boolean broken();
}