# TerraCraftItems - 物品库

## 🔍 插件概述
TerraCraftItems 是一款为 Minecraft 1.20+ 版本设计的高级物品定制插件，通过组件化配置系统，允许服务器管理员管理员灵活管理员创建具有独特属性的自定义物品，适用于 RPG、生存、创意等多种服务器类型。

## ✨ 核心特性

### 🔧 组件化物品系统
- **原版组件支持**：支持自定义物品名称、材质、模型数据、最大堆叠数、染色等基础属性（基于 NBT API 开发，支持大多数 Minecraft 原版物品组件，写法与 MC 数据组件几乎一致）
    - `attribute_modifiers`（属性修饰符）
    - `blocks_attacks`（格挡攻击）
    - `break_sound`（破坏音效）
    - `consumable`（可消耗）
    - `custom_model_data`（自定义模型数据）
    - `custom_name`（自定义名称）
    - `damage`（伤害值）
    - `damage_resistant`（抗伤害）
    - `death_protection`（死亡保护）
    - `dyed_color`（染色颜色）
    - `enchantable`（可附魔）
    - `enchantment_glint_override`（附魔光效覆盖）
    - `enchantments`（附魔）
    - `equippable`（可装备）
    - `food`（食物）
    - `glider`（滑翔翼）
    - `item_name`（物品名称）
    - `jukebox_playable`（可在唱片机播放）
    - `lore`（描述文本）
    - `max_damage`（最大耐久）
    - `max_stack_size`（最大堆叠数量）
    - `potion_contents`（药水内容）
    - `potion_duration_scale`（药水持续时间缩放）
    - `rarity`（稀有度）
    - `repairable`（可修复）
    - `repair_cost`（修复成本）
    - `tool`（工具）
    - `tooltip_display`（提示框显示）
    - `tooltip_style`（提示框样式）
    - `unbreakable`（不可破坏）
    - `use_cooldown`（使用冷却）
    - `use_remainder`（使用后剩余物）
    - `weapon`（武器）
- **额外组件拓展**（只支持以下的额外组件，配置文件中其他组件均由开发中的附属插件拓展）：
  - `terra_durability`：自定义耐久度管理（**不支持耐久附魔**）
      - 优先识别自定义的**占位符耐久公式**
      - 支持识别 `weapon`组件中的耐久消耗
      - 支持识别`break_sound`物品破碎声音识别
  - `command`：物品消耗时触发指定指令
      - 使用`@self`占位，执行时替换为使用者的名称

### 🛠️ 灵活的配置系统
- 基于**YAML**格式的物品配置文件，支持**MiniMessage**语法的文字样式（渐变、颜色等）
- 支持通过表达式配置概率、伤害公式等动态属性
- 多语言支持（默认提供中文、英文），可自定义消息文本

## 📋 命令支持
| 命令                                                  | 说明           | 权限                             |
| :---------------------------------------------------- | -------------- | -------------------------------- |
| `/terracraftitems reload`                             | 重载插件配置   | `terracraftitems.command.reload` |
| `/terracraftitems get <物品名> [数量]`                | 获取自定义物品 | `terracraftitems.command.get`    |
| `/terracraftitems durability <add|reduce|set> <数值>` | 调整物品耐久度 | `terracraftitems.command`        |

## 💡 插件API
- **提供组件拓展api**
    - 示例文件中的`meta`等配置是本插件的附属【TerraCraftAttributes】拓展的自定义组件（还在测试中）