package io.tanice.terracraftitems.api.item.component;

/**
 * 组件状态管理接口，定义了组件的原始性、可修改性和可更新性等状态属性的操作规范。
 * <p>
 * 该接口提供了对组件状态的检查、修改以及NBT序列化功能，
 * 支持将状态信息压缩为字节形式进行存储或传输。
 */
public interface TerraComponentState {

    /**
     * 判断当前组件是否为初始自带的原始状态。
     *
     * @return 如果是初始状态则返回 {@code true}，否则返回 {@code false}
     *         当状态未设置时，默认返回 {@code false}
     */
    boolean isOriginal();

    /**
     * 判断当前组件是否被修改过。
     *
     * @return 如果已被修改则返回 {@code true}，否则返回{@code false}。
     *         当状态未设置时，默认返回{@code false}
     */
    boolean isModified();

    /**
     * 判断当前组件是否允许更新。
     *
     * @return 如果允许更新则返回 {@code true}，否则返回 {@code false}。
     *         当状态未设置时，默认返回 {@code true}
     */
    boolean isUpdatable();

    /**
     * 设置组件是否为初始自带的原始状态。
     *
     * @param original {@code true}表示标记为初始状态，{@code false}表示标记为非初始状态
     */
    void setOriginal(boolean original);

    /**
     * 设置组件是否被修改过。
     *
     * @param modified {@code true}表示标记为已修改，{@code false}表示标记为未修改
     */
    void setModified(boolean modified);

    /**
     * 设置组件是否允许更新。
     *
     * @param updatable {@code true}表示允许更新，{@code false}表示禁止更新
     */
    void setUpdatable(boolean updatable);

    /**
     * 将当前组件的所有状态信息转换为NBT存储格式的字节。
     * <p>
     * 字节的低三位分别表示：
     * 0b001 - 原始状态(original)
     * 0b010 - 修改状态(modified)
     * 0b100 - 更新权限(updatable)
     *
     * @return 包含状态信息的字节，不会返回 {@code null}
     */
    Byte toNbtByte();
}
