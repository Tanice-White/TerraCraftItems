package io.tanice.terracraftitems.paper.item.component.custom;

import io.tanice.terracraftitems.api.item.component.AbstractCustomComponent;
import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.api.item.component.custom.TerraDurabilityComponent;
import io.tanice.terracraftitems.api.item.ComponentState;
import io.tanice.terracraftitems.api.item.component.custom.TerraInnerNameComponent;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.item.datatype.DurabilityComponentDataType;
import io.tanice.terracraftitems.paper.util.MiniMessageUtil;
import io.tanice.terracraftitems.paper.util.config.ConfigManager;
import io.tanice.terracraftitems.paper.util.logger.TerraLogger;
import io.tanice.terracraftitems.paper.util.message.MessageManager;
import io.tanice.terracraftitems.paper.util.TerraExpression;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static io.tanice.terracraftitems.paper.util.constance.PDCKey.DURABILITY_KEY;
import static io.tanice.terracraftitems.paper.util.constance.PDCKey.TERRA_COMPONENT_KEY;
import static io.tanice.terracraftitems.paper.util.pdc.PDCUtil.getTerraContainer;
import static io.tanice.terracraftitems.paper.util.pdc.PDCUtil.removeSubTerraContainer;

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
        item.editMeta(meta -> {
            PersistentDataContainer root = meta.getPersistentDataContainer();
            PersistentDataContainer container = root.get(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER);
            if (container == null) container = root.getAdapterContext().newPersistentDataContainer();
            // 这里也是全量更新
            container.set(DURABILITY_KEY, DurabilityComponentDataType.INSTANCE, this);
            root.set(TERRA_COMPONENT_KEY, PersistentDataType.TAG_CONTAINER, container);
            /* 默认原版不可破坏 */
            meta.setUnbreakable(true);
        });
    }

    public static void clear(ItemStack item) {
        removeSubTerraContainer(item, DURABILITY_KEY);
    }

    public static void remove(ItemStack item) {
        clear(item);
    }

    @Override
    public void updateLore(ItemStack item) {
        item.editMeta(meta -> {
            TerraInnerNameComponent nameComponent = InnerNameComponent.from(item);
            if (nameComponent == null) return;
            TerraCraftItems.inst().getItemManager().getItem(nameComponent.name()).ifPresent(terraItem -> {
                int idx = terraItem.getLorePlaceholderIdx(getComponentName());
                if (idx == -1) return;
                List<Component> lore = meta.lore();
                if (lore == null) return;
                /* 初始化 */
                int d = damage == null ? 0 : damage;
                String durabilityText = String.format(
                        MessageManager.getMessage("terra_durability.lore"),
                        maxDamage - d,
                        maxDamage
                );
                /* 执行替换 */
                lore.set(idx, MiniMessageUtil.serialize(durabilityText));
                meta.lore(lore);
            });
        });
    }

    /* damage 不能算入 */
    @Override
    public int hashCode() {
        return Objects.hash(maxDamage, breakLoss);
    }

    @Override
    public String getComponentName() {
        return "terra_durability";
    }

    @Override
    public TerraBaseComponent updatePartial(ItemStack old) {
        DurabilityComponent oldComponent = DurabilityComponent.from(old);
        if (oldComponent == null) return this;
        /* 组件的初始值不能变，返回一个新的值，实现继承原本的数值 */
        return new DurabilityComponent(oldComponent.damage, this.maxDamage, this.breakLoss, this.damageExpr, this.state);
    }

    @Override
    @Nullable
    public Integer getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(@Nullable Integer damage) {
        if (damage == null) {
            this.damage = null;
            return;
        }
        if (damage < 0) damage = 0;
        this.damage = Math.min(damage, this.maxDamage);
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
            if (ConfigManager.isDebug()) TerraLogger.debug("expression: " + damageExpr + ", damage_per_use result=" + v);
            return (int) v;
        } catch (Exception e) {
            TerraLogger.error("Error when calculating damage_per_use in terra durability component: " + damageExpr, e);
            return -1;
        }
    }

    @Override
    public boolean broken() {
        if (this.damage == null) return false;
        return this.damage >= this.maxDamage;
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
            TerraLogger.error("Failed to register damage_per_use_expr expression: " + damageExpr, e);
        }
    }
}
