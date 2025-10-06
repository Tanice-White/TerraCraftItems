package io.tanice.terracraftitems.api.item;

import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 组件工厂接口，负责物品组件的注册和实例创建
 */
public interface TerraComponentFactory {

    /**
     * 注册组件创建器和移除器
     * <br>
     * 配置节点的传递规则：
     * <br>
     * 1. 当YAML配置中组件对应的 {@code key} 值是一个子节点（如: {@code component: {key: value}}），则creator的参数是该子节点下对应的 {@code ConfigurationSection} 实例
     * <br>
     * 2. 当YAML配置中组件对应的 {@code key} 值是基础数据类型（如: {@code component: "value"}），则cfg会传入整个物品的根配置节点，组件需在creator中自行通过key获取对应值（如 {@code cfg.getString("component")}）
     * <br>
     *
     * @param <T>           组件类型
     * @param componentName 组件名称
     * @param creator       组件创建器函数
     * @param from          组件从物品初始化函数
     * @param remover       组件移除器函数
     */
    <T extends TerraBaseComponent> void register(
            @Nonnull String componentName,
            @Nonnull Function<ConfigurationSection, T> creator,
            @Nonnull Function<ItemStack, T> from,
            @Nonnull Consumer<ItemStack> remover
    );

    /**
     * 根据组件名称和配置节点创建组件实例
     * @param componentName 组件名称
     * @param cfg           一个物品的配置文件
     * @return 成功创建的组件实例，若组件未注册则返回null
     */
    @Nullable
    <T extends TerraBaseComponent> T create(@Nonnull String componentName, @Nullable ConfigurationSection cfg);

    /**
     * 根据组件名称从物品中读取并初始化 custom 组件（原版组件无法读取）
     * @param componentName 组件名称
     * @param item          目标物品
     * @return 成功创建的组件实例，若组件未注册则返回null
     */
    @Nullable
    <T extends TerraBaseComponent> T from(@Nonnull String componentName, @Nonnull ItemStack item);

    /**
     * 移除物品上的组件
     * @param componentName 组件名称
     * @param item 物品栈
     */
    void remove(@Nonnull String componentName, @Nonnull ItemStack item);

    /**
     * 检查组件是否已注册
     * @param componentName 组件名称
     * @return 如果已注册则返回 true
     */
    boolean isRegistered(@Nonnull String componentName);

    /**
     * 注销组件名称对应的组件
     * @param componentName 组件的识别名称
     */
    void unRegister(@Nonnull String componentName);

    /**
     * 处理配置 并将所有相关组件添加到组件列表
     * @param cfg 配置节点
     * @param components 组件记录列表
     * @param bukkitItem 物品栈
     */
    void processComponents(
            @Nonnull ConfigurationSection cfg,
            @Nonnull List<TerraBaseComponent> components,
            @Nonnull ItemStack bukkitItem
    );
}
