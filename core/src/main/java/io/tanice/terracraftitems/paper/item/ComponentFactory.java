package io.tanice.terracraftitems.paper.item;

import io.tanice.terracraftitems.api.item.TerraComponentFactory;
import io.tanice.terracraftitems.api.item.component.TerraBaseComponent;
import io.tanice.terracraftitems.paper.item.component.custom.CommandComponent;
import io.tanice.terracraftitems.paper.item.component.custom.DurabilityComponent;
import io.tanice.terracraftitems.paper.item.component.custom.ExtraNBTComponent;
import io.tanice.terracraftitems.paper.item.component.vanilla.*;
import io.tanice.terracraftitems.paper.util.MiniMessageUtil;
import io.tanice.terracraftitems.core.util.namespace.TerraNamespaceKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ComponentFactory implements TerraComponentFactory {

    private static final ComponentFactory INSTANCE = new ComponentFactory();

    private final Map<String, Function<ConfigurationSection, ? extends TerraBaseComponent>> creators = new ConcurrentHashMap<>();
    private final Map<String, Consumer<ItemStack>> removers = new ConcurrentHashMap<>();
    private final Map<String, Function<ItemStack, ? extends TerraBaseComponent>> from_creators = new ConcurrentHashMap<>();

    private ComponentFactory() {
        registerAllComponents();
    }

    /**
     * 获取单例实例
     * @return 组件工厂实例
     */
    public static ComponentFactory inst() {
        return INSTANCE;
    }

    @Override
    public <T extends TerraBaseComponent> void register(
            @Nonnull String componentName,
            @Nonnull Function<ConfigurationSection, T> creator,
            @Nonnull Function<ItemStack, T> from_creator,
            @Nonnull Consumer<ItemStack> remover
    ) {
        Objects.requireNonNull(componentName, "Component name cannot be null");
        Objects.requireNonNull(creator, "Component creator cannot be null");
        Objects.requireNonNull(remover, "Component remover cannot be null");
        Objects.requireNonNull(from_creator, "Component from creator cannot be null");

        creators.put(componentName, creator);
        removers.put(componentName, remover);
        from_creators.put(componentName, from_creator);
    }

    @Override
    @Nullable
    public TerraBaseComponent create(@Nonnull String componentName, @Nullable ConfigurationSection cfg) {
        Objects.requireNonNull(componentName, "Component name cannot be null");
        Function<ConfigurationSection, ? extends TerraBaseComponent> creator = creators.get(componentName);
        return creator != null ? creator.apply(cfg) : null;
    }

    @Override
    @Nonnull
    public List<TerraBaseComponent> getCustomComponentsFrom(@Nonnull ItemStack item) {
        List<TerraBaseComponent> components = new ArrayList<>();
        TerraBaseComponent c;
        for (Function<ItemStack, ? extends TerraBaseComponent> from_creator : from_creators.values()) {
            c = from_creator.apply(item);
            if (c != null) components.add(c);
        }
        return components;
    }

    @Override
    public void remove(@Nonnull String componentName, @Nonnull ItemStack item) {
        Objects.requireNonNull(componentName, "Component name cannot be null");
        Objects.requireNonNull(item, "Item cannot be null");

        Consumer<ItemStack> remover = removers.get(componentName);
        if (remover != null) {
            remover.accept(item);
        }
    }

    @Override
    public boolean isRegistered(@Nonnull String componentName) {
        Objects.requireNonNull(componentName, "Component name cannot be null");
        return creators.containsKey(componentName);
    }

    @Override
    public void processComponents(
            @Nonnull ConfigurationSection cfg,
            @Nonnull List<TerraBaseComponent> components,
            @Nonnull ItemStack bukkitItem
    ) {
        Objects.requireNonNull(cfg, "ConfigurationSection cannot be null");
        Objects.requireNonNull(components, "Components list cannot be null");
        Objects.requireNonNull(bukkitItem, "Bukkit item cannot be null");

        for (String componentName : creators.keySet()) {
            if (cfg.isSet(componentName)) {
                /* key对应部分是section则传入对应section，否则传入整个物品的section，由register中的creator逻辑处理 */
                TerraBaseComponent component = create(componentName, cfg.isConfigurationSection(componentName) ? cfg.getConfigurationSection(componentName) : cfg);
                if (component == null) continue;
                components.add(component);
                component.cover(bukkitItem);
            }
            if (cfg.isSet("!" + componentName)) remove(componentName, bukkitItem);
        }
    }

    /**
     * 内部使用（注册原版类）
     */
    private <T extends TerraBaseComponent> void register(
            @Nonnull String componentName,
            @Nonnull Function<ConfigurationSection, T> creator,
            @Nonnull Consumer<ItemStack> remover
    ) {
        Objects.requireNonNull(componentName, "Component name cannot be null");
        Objects.requireNonNull(creator, "Component creator cannot be null");
        Objects.requireNonNull(remover, "Component remover cannot be null");
        creators.put(componentName, creator);
        removers.put(componentName, remover);
    }

    /**
     * 注册所有内置组件
     */
    private void registerAllComponents() {
        /* vanilla */
        register("attribute", AttributeModifiersComponent::new, AttributeModifiersComponent::remove);
        register("shield", BlocksAttacksComponent::new, BlocksAttacksComponent::remove);
        register("break_sound", BreakSoundComponent::new, BreakSoundComponent::remove);
        register("consumable", ConsumableComponent::new, ConsumableComponent::remove);
        register("custom_model_data", cfg -> new CustomModelDataComponent(cfg.getInt("custom_model_data")), CustomModelDataComponent::remove);
        register("display_name", cfg -> new CustomNameComponent(cfg.getString("display_name")), CustomNameComponent::remove);
        register("ori_durability", DamageComponent::new, DamageComponent::remove);
        register("resistant", cfg -> new DamageResistantComponent(TerraNamespaceKey.from(cfg.getString("resistant"))), DamageResistantComponent::remove);
        register("death_protection", DeathProtectionComponent::new, DeathProtectionComponent::remove);
        register("color", cfg -> new DyedColorComponent(cfg.getString("color")), DyedColorComponent::remove);
        register("enchant", EnchantComponent::new, EnchantComponent::remove);
        register("glint", cfg -> new EnchantmentGlintOverrideComponent(cfg.getBoolean("glint")), EnchantmentGlintOverrideComponent::remove);
        register("equippable", EquippableComponent::new, EquippableComponent::remove);
        register("food", FoodComponent::new, FoodComponent::remove);
        register("glider", cfg -> new GliderComponent(), GliderComponent::remove);
        register("music_disc", cfg -> new JukeboxPlayable(cfg.getString("music_disc")), JukeboxPlayable::remove);
        register("lore", cfg -> new LoreComponent(cfg.getStringList("lore").stream().map(MiniMessageUtil::serialize).toList()), LoreComponent::remove);
        register("max_stack_size", cfg -> new MaxStackSizeComponent(cfg.getInt("max_stack_size")), MaxStackSizeComponent::remove);
        register("potion", PotionComponent::new, PotionComponent::remove);
        register("rarity", cfg -> new RarityComponent(cfg.getString("rarity")), RarityComponent::remove);
        register("repair", RepairComponent::new, RepairComponent::remove);
        register("tool", ToolComponent::new, ToolComponent::remove);
        register("tooltip", TooltipComponent::new, TooltipComponent::remove);
        register("use_cooldown", UseCooldownComponent::new, UseCooldownComponent::remove);
        register("use_remainder", UseRemainderComponent::new, UseRemainderComponent::remove);
        register("weapon", WeaponComponent::new, WeaponComponent::remove);

        /* custom */
        register("command", CommandComponent::new, CommandComponent::from, CommandComponent::remove);
        /* 在 lore 组件之后 */
        register("terra_durability", DurabilityComponent::new, DurabilityComponent::from, DurabilityComponent::remove);
        register("nbt", ExtraNBTComponent::new, ExtraNBTComponent::from, ExtraNBTComponent::remove);
        // innerName updateCode 由实例掌管
    }
}
