package io.tanice.terracraftitems.api.item.component;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

/**
 * 组件数据类型抽象类，实现了PersistentDataType接口，用于处理物品组件与NBT数据的转换。
 * <p>
 * 提供了组件与PersistentDataContainer之间的基础转换框架，
 * 子类需实现具体的读写逻辑以完成组件数据的序列化和反序列化。
 *
 * @param <T> 组件类型，必须继承自AbstractItemComponent
 */
public abstract class AbstractComponentDataType<T extends AbstractCustomComponent> implements PersistentDataType<PersistentDataContainer, T> {

    /**
     * 获取原始数据类型（PersistentDataContainer）的Class对象
     *
     * @return PersistentDataContainer的Class对象，非空
     */
    @Nonnull
    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    /**
     * 将组件对象转换为PersistentDataContainer（NBT容器）
     * <p>
     * 会创建新的容器并调用writeToContainer方法写入组件数据
     *
     * @param component 要转换的组件对象，非空
     * @param context   数据适配器上下文，非空
     * @return 包含组件数据的PersistentDataContainer，非空
     */
    @Nonnull
    @Override
    public PersistentDataContainer toPrimitive(@Nonnull T component, @Nonnull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        writeToContainer(container, component);
        return container;
    }

    /**
     * 将PersistentDataContainer（NBT容器）转换为组件对象
     * <p>
     * 调用readFromContainer方法从容器中读取数据并构建组件
     *
     * @param container 包含组件数据的容器，非空
     * @param context   数据适配器上下文，非空
     * @return 从容器中读取的组件对象，非空
     */
    @Nonnull
    @Override
    public T fromPrimitive(@Nonnull PersistentDataContainer container, @Nonnull PersistentDataAdapterContext context) {
        return readFromContainer(container);
    }

    /**
     * 获取复杂数据类型（组件类型）的Class对象
     *
     * @return 组件类型的Class对象，非空
     */
    @Nonnull
    @Override
    public abstract Class<T> getComplexType();

    /**
     * 将组件数据写入PersistentDataContainer
     * <p>
     * 子类需实现此方法以完成具体属性的序列化
     *
     * @param container 目标数据容器，非空
     * @param component 要写入的组件，非空
     */
    protected abstract void writeToContainer(PersistentDataContainer container, T component);

    /**
     * 从PersistentDataContainer读取数据并构建组件
     * <p>
     * 子类需实现此方法以完成具体属性的反序列化
     *
     * @param container 包含组件数据的容器，非空
     * @return 构建的组件对象，非空
     */
    protected abstract T readFromContainer(PersistentDataContainer container);
}