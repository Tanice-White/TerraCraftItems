# TerraCraftItems - Item Library

## 🔍 Plugin Overview

**✨TerraCraftItems** is an advanced **item customization plugin** designed for Minecraft 1.20+ versions（**paper only**）. Through a component-based configuration system, it allows server administrators to flexibly create custom items with unique properties, suitable for various server types such as RPG, survival, and creative.

## ✨ Core Features

### 🔧 Component-based Item System

-   **Vanilla Component Support**: Supports customizing basic properties like item name, material, model data, maximum stack size, dyeing, etc. (Developed based on NBT API, supports most Minecraft vanilla item components with syntax nearly identical to MC data components)

    -   `attribute_modifiers`
    -   `blocks_attacks`
    -   `break_sound`
    -   `consumable`
    -   `custom_model_data`
    -   `custom_name`
    -   `damage`
    -   `damage_resistant`
    -   `death_protection`
    -   `dyed_color`
    -   `enchantable`
    -   `enchantment_glint_override`
    -   `enchantments`
    -   `equippable`
    -   `food`
    -   `glider`
    -   `item_name`
    -   `jukebox_playable`
    -   `lore`
    -   `max_damage`
    -   `max_stack_size`
    -   `potion_contents`
    -   `potion_duration_scale`
    -   `rarity`
    -   `repairable`
    -   `repair_cost`
    -   `tool`
    -   `tooltip_display`
    -   `tooltip_style`
    -   `unbreakable`
    -   `use_cooldown`
    -   `use_remainder`
    -   `weapon`

-   **Additional Component Extensions**(Only the following additional components are supported. Other components in the configuration file are extended by the affiliated plugin under development):

    -    `terra_durability`: Custom durability management (does not support durability enchantments)
        -   Prioritizes recognizing custom **placeholder durability formulas**
        -   Supports recognizing durability consumption in `weapon` components
        -   Supports recognizing item breaking sounds from `break_sound`

    -   `command`:Triggers specified commands when the item is consumed
        -   Use `@self` placeholder, which will be replaced with the user's name when executed

### 🛠️ Flexible Configuration System

-   Item configuration files based on **YAML** format, supporting text styling (gradients, colors, etc.) using **MiniMessage** syntax
-   Supports configuring dynamic properties like probabilities and damage formulas through expressions
-   Multi-language support (Chinese and English provided by default), with customizable message texts

```yaml
武器耐久测试:
  id: golden_sword
  display_name: "<yellow>武器耐久测试"
  lore:
    - "<gradient:#FFD700:#8B4513>你的升级之路必定艰难()</gradient>"
    - "<red>玻璃大炮</red>"
    - "<gray>用升级材料即可, 没错就是我懒得写了</gray>"
    - "<blue>耐久值:{terra_durability}"
  max_stack_size: 1
  terra_durability:
    damage: 0
    max_damage: 125
    break_loss: false
    # 支持java的Math库，damage-目前的损伤值  max_damage 此物品最大损伤值  harm-武器或护甲则表示造成或承受的伤害，钓竿，弓，弩的值为0
    damage_per_use_expr: "Math.max(1, harm * (damage / max_damage + 1))"
  glint: true
  weapon:
    damage_per_attack: 5  # 本插件支持识别，三叉戟也支持
```

## 📋 Command Support

| Command                                               | Description                  | Permission                       |
| :---------------------------------------------------- | :--------------------------- | :-------------------------------- |
| `/terracraftitems reload`                             | Reload the plugin  | `terracraftitems.command.reload` |
| `/terracraftitems get <item name> [quantity]`         | Obtain custom items          | `terracraftitems.command.get`    |
| `/terracraftitems durability <add|reduce|set> <value>`| Adjust item durability       | `terracraftitems.command`        |

## 💡 Plugin API

-   **Provides component extension API**
