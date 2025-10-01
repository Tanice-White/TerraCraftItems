package io.tanice.terracraftitems.bukkit.item.component.custom;

import io.tanice.terracraftitems.api.item.component.AbstractCustomComponent;
import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.api.item.component.custom.TerraDurabilityComponent;
import io.tanice.terracraftitems.bukkit.item.component.ComponentState;
import io.tanice.terracraftitems.bukkit.item.datatype.DurabilityComponentDataType;
import io.tanice.terracraftitems.core.config.ConfigManager;
import io.tanice.terracraftitems.core.util.expression.TerraExpression;
import io.tanice.terracraftitems.core.util.logger.TerraCraftLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Objects;

import static io.tanice.terracraftitems.bukkit.util.color.CommandColor.*;
import static io.tanice.terracraftitems.bukkit.util.constance.PDCKey.*;

public class DurabilityComponent extends AbstractCustomComponent implements TerraDurabilityComponent {

    @Nullable
    private Integer damage;
    private int maxDamage;
    @Nullable
    private Boolean breakLoss;
    @Nullable
    private String damageExpr; /* 每次使用消耗的耐久 */

    public DurabilityComponent(ConfigurationSection cfg) {
        this(
                cfg.isSet("damage") ? cfg.getInt("damage") : null,
                cfg.getInt("max_damage", 1),
                cfg.isSet("break_loss") ? cfg.getBoolean("break_loss") : null,
                cfg.getString("damage_per_use_expr"),
                cfg.getBoolean("updatable", true)
        );
    }

    public DurabilityComponent(@Nullable Integer damage, int maxDamage, @Nullable Boolean breakLoss, @Nullable String damageExpr, ComponentState state) {
        super(state);
        this.damage = damage;
        this.maxDamage = maxDamage;
        this.breakLoss = breakLoss;
        this.damageExpr = damageExpr;
        registerExpression();
    }

    public DurabilityComponent(@Nullable Integer damage, int maxDamage, @Nullable Boolean breakLoss, @Nullable String damageExpr, boolean updatable) {
        super(updatable);
        this.damage = damage;
        this.maxDamage = maxDamage;
        this.breakLoss = breakLoss;
        this.damageExpr = damageExpr;
        registerExpression();
    }

    @Nullable
    public static DurabilityComponent from(ItemStack item) {
        PersistentDataContainer container = getTerraContainer(item);
        if (container == null) return null;
        return container.get(DURABILITY_KEY, DurabilityComponentDataType.INSTANCE);
    }

    @Override
    protected void doCover(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer root = meta.getPersistentDataContainer();
        PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
        if (container == null) container = root.getAdapterContext().newPersistentDataContainer();
        container.remove(DURABILITY_KEY);
        container.set(DURABILITY_KEY, DurabilityComponentDataType.INSTANCE, this);
        root.set(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER, container);
        /* 默认原版不可破坏 */
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }

    public static void clear(ItemStack item) {
        removeSubTerraContainer(item, DURABILITY_KEY);
    }

    public static void remove(ItemStack item) {
        clear(item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(damage, maxDamage, breakLoss);
    }

    @Override
    public String getComponentName() {
        return "terra_durability";
    }

    @Override
    public TerraBaseComponent updatePartial() {
        /* 组件的初始值不能变，返回一个新的值，实现继承原本的数值 */
        return new DurabilityComponent(null, this.maxDamage, this.breakLoss, this.damageExpr, this.state);
    }

    @Override
    @Nullable
    public Integer getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(@Nullable Integer damage) {
        if (damage == null) this.damage = null;
        else this.damage = Math.min(damage, this.maxDamage);
    }

    @Override
    public int getMaxDamage() {
        return this.maxDamage;
    }

    @Override
    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    @Override
    @Nullable
    public Boolean isBreakLoss() {
        return this.breakLoss;
    }

    @Override
    public void setBreakLoss(@Nullable Boolean breakLoss) {
        this.breakLoss = breakLoss;
    }

    @Override
    @Nullable
    public String getDamageExpr() {
        return this.damageExpr;
    }

    @Override
    public void setDamageExpr(@Nullable String damageExpr) {
        // 不删除原本的公式
        this.damageExpr = damageExpr;
        registerExpression();
    }

    @Override
    public int getDamageForUse(double damage) {
        if (damageExpr == null || damageExpr.isBlank()) return -1;
        try {
            double v = (double) TerraExpression.calculate(damageExpr, new Object[]{this.damage, this.maxDamage, damage});
            if (ConfigManager.isDebug()) TerraCraftLogger.debug("expression: " + damageExpr + ", damage_per_use result=" + v);
            return (int) v;
        } catch (Exception e) {
            TerraCraftLogger.error("Error when calculating damage_per_use in terra durability component: " + damageExpr + ". \n" + e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean broken() {
        if (this.damage == null) return false;
        return this.damage >= this.maxDamage;
    }

    @Override
    public String toString() {
        return BOLD + YELLOW + "terra_durability:" + RESET + "\n" +
                "    " + AQUA + "damage:" + WHITE + (damage != null ? damage : "null") + "\n" +
                "    " + AQUA + "max_damage:" + WHITE + maxDamage + "\n" +
                "    " + AQUA + "damage_per_use_expr:" + WHITE + damageExpr + "\n" +
                "    " + AQUA + "break_loss:" + WHITE + isBreakLoss() + "\n" +
                "    " + AQUA + "broken:" + WHITE + broken() + "\n" +
                "    " + AQUA + "state:" + WHITE + state + RESET;
    }

    private void registerExpression() {
        if (damageExpr == null || damageExpr.isBlank()) return;
        try {
            TerraExpression.register(
                    damageExpr,
                    damageExpr,
                    double.class,
                    // cur_damage 当前损伤值  max_damage 最大损伤值  damage 受到的伤害
                    new String[]{"damage", "max_damage", "harm"},
                    new Class[]{int.class, int.class, double.class}
            );
        } catch (Exception e) {
            TerraCraftLogger.error("Failed to register damage_per_use_expr expression: " + damageExpr + "\n" + e.getMessage());
        }
    }
}
