package io.tanice.terracraftitems.paper.util.adapter;

import io.tanice.terracraftitems.paper.util.version.MCVersion;
import io.tanice.terracraftitems.paper.util.version.MinecraftVersions;
import io.tanice.terracraftitems.paper.util.version.ServerVersion;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class BukkitAttribute {
    private static final Map<String, BukkitAttribute> ATTRIBUTES = new HashMap<>();
    private final String key;
    private final String name;
    private final MCVersion addedInVersion;

    public static final BukkitAttribute ATTACK_DAMAGE = BukkitAttribute.register("ATTACK_DAMAGE", "generic.attack_damage", "attack_damage");
    public static final BukkitAttribute ATTACK_SPEED = BukkitAttribute.register("ATTACK_SPEED", "generic.attack_speed", "attack_speed");
    public static final BukkitAttribute MAX_HEALTH = BukkitAttribute.register("MAX_HEALTH", "generic.max_health", "max_health");
    public static final BukkitAttribute MOVEMENT_SPEED = BukkitAttribute.register("MOVEMENT_SPEED", "generic.movement_speed", "movement_speed");
    public static final BukkitAttribute FLYING_SPEED = BukkitAttribute.register("FLYING_SPEED", "generic.flying_speed", "flying_speed");
    public static final BukkitAttribute ATTACK_KNOCKBACK = BukkitAttribute.register("ATTACK_KNOCKBACK", "generic.attack_knockback", "attack_knockback");
    public static final BukkitAttribute KNOCKBACK_RESISTANCE = BukkitAttribute.register("KNOCKBACK_RESISTANCE", "generic.knockback_resistance", "knockback_resistance");
    public static final BukkitAttribute ARMOR = BukkitAttribute.register("ARMOR", "generic.armor", "armor");
    public static final BukkitAttribute ARMOR_TOUGHNESS = BukkitAttribute.register("ARMOR_TOUGHNESS", "generic.armor_toughness", "armor_toughness");
    public static final BukkitAttribute LUCK = BukkitAttribute.register("LUCK", "generic.luck", "luck");
    public static final BukkitAttribute SCALE = BukkitAttribute.register("SCALE", "generic.scale", "scale", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute FOLLOW_RANGE = BukkitAttribute.register("FOLLOW_RANGE", "generic.follow_range", "follow_range");
    public static final BukkitAttribute FALL_DAMAGE_MULTIPLIER = BukkitAttribute.register("FALL_DAMAGE_MULTIPLIER", "generic.fall_damage_multiplier", "fall_damage_multiplier", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute SAFE_FALL_DISTANCE = BukkitAttribute.register("SAFE_FALL_DISTANCE", "generic.safe_fall_distance", "safe_fall_distance", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute GRAVITY = BukkitAttribute.register("GRAVITY", "generic.gravity", "gravity", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute BLOCK_BREAK_SPEED = BukkitAttribute.register("BLOCK_BREAK_SPEED", "player.block_break_speed", "block_break_speed", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute ENTITY_INTERACTION_RANGE = BukkitAttribute.register("ENTITY_INTERACTION_RANGE", "player.entity_interaction_range", "entity_interaction_range", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute BLOCK_INTERACTION_RANGE = BukkitAttribute.register("BLOCK_INTERACTION_RANGE", "player.block_interaction_range", "block_interaction_range", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute JUMP_HEIGHT = BukkitAttribute.register("JUMP_HEIGHT", "generic.jump_strength", "jump_strength");
    public static final BukkitAttribute STEP_HEIGHT = BukkitAttribute.register("STEP_HEIGHT", "generic.step_height", "step_height", MinecraftVersions.v1_20_5);
    public static final BukkitAttribute MAX_ABSORPTION = BukkitAttribute.register("MAX_ABSORPTION", "generic.max_absorption", "max_absorption", MinecraftVersions.v1_20_2);
    public static final BukkitAttribute BURNING_TIME = BukkitAttribute.register("BURNING_TIME", "generic.burning_time", "burning_time", MinecraftVersions.v1_21);
    public static final BukkitAttribute EXPLOSION_KNOCKBACK_RESISTANCE = BukkitAttribute.register("EXPLOSION_KNOCKBACK_RESISTANCE", "generic.explosion_knockback_resistance", "explosion_knockback_resistance", MinecraftVersions.v1_21);
    public static final BukkitAttribute MOVEMENT_EFFICIENCY = BukkitAttribute.register("MOVEMENT_EFFICIENCY", "generic.movement_efficiency", "movement_efficiency", MinecraftVersions.v1_21);
    public static final BukkitAttribute OXYGEN = BukkitAttribute.register("OXYGEN", "generic.oxygen_bonus", "oxygen_bonus", MinecraftVersions.v1_21);
    public static final BukkitAttribute SNEAKING_SPEED = BukkitAttribute.register("SNEAKING_SPEED", "player.sneaking_speed", "sneaking_speed", MinecraftVersions.v1_21);
    public static final BukkitAttribute WATER_MOVEMENT_EFFICIENCY = BukkitAttribute.register("WATER_MOVEMENT_EFFICIENCY", "generic.water_movement_efficiency", "water_movement_efficiency", MinecraftVersions.v1_21);
    public static final BukkitAttribute SUBMERGED_MINING_SPEED = BukkitAttribute.register("SUBMERGED_MINING_SPEED", "player.submerged_mining_speed", "submerged_mining_speed", MinecraftVersions.v1_21);
    public static final BukkitAttribute MINING_EFFICIENCY = BukkitAttribute.register("MINING_EFFICIENCY", "player.mining_efficiency", "mining_efficiency", MinecraftVersions.v1_21);
    public static final BukkitAttribute TEMPT_RANGE = BukkitAttribute.register("TEMPT_RANGE", "generic.tempt_range", "tempt_range", MinecraftVersions.v1_21_2);
    public static final BukkitAttribute SWEEPING_DAMAGE_RATIO = BukkitAttribute.register("SWEEPING_DAMAGE_RATIO", "player.sweeping_damage_ratio", "sweeping_damage_ratio", MinecraftVersions.v1_21);
    public static final BukkitAttribute SPAWN_REINFORCEMENTS = BukkitAttribute.register("SPAWN_REINFORCEMENTS", "zombie.spawn_reinforcements", "spawn_reinforcements");

    private BukkitAttribute(String name, String key, MCVersion addedInVersion) {
        this.name = name;
        this.key = key;
        this.addedInVersion = addedInVersion;
    }

    private static BukkitAttribute register(String name, String keyPre21, String keyPost21) {
        return BukkitAttribute.register(name, keyPre21, keyPost21, MinecraftVersions.v1_18);
    }

    private static BukkitAttribute register(String name, String keyPre21, String keyPost21, MCVersion addedInVersion) {
        String computedKey = ServerVersion.isBeforeOrEq(MinecraftVersions.v1_21_1) ? keyPre21 : keyPost21;
        BukkitAttribute attribute = new BukkitAttribute(name.toUpperCase(), computedKey, addedInVersion);
        ATTRIBUTES.put(name.toUpperCase(), attribute);
        return attribute;
    }

    public static Collection<BukkitAttribute> values() {
        return ATTRIBUTES.values().stream().filter(attribute -> ServerVersion.isAfterOrEq(attribute.addedInVersion)).collect(Collectors.toSet());
    }

    @Nullable
    public static BukkitAttribute get(@Nullable String name) {
        if (name == null) return null;
        return ATTRIBUTES.get(name.toUpperCase());
    }

    public Attribute getBukkitAttribute() {
        return Registry.ATTRIBUTE.get(this.getAttributeKey());
    }

    public NamespacedKey getAttributeKey() {
        return NamespacedKey.minecraft(this.key);
    }

    public String getName() {
        return name;
    }
}
