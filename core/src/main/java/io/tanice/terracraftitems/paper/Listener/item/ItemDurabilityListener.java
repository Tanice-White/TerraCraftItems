package io.tanice.terracraftitems.paper.Listener.item;

import io.tanice.terracraftitems.api.item.component.custom.TerraDurabilityComponent;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.item.component.custom.DurabilityComponent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import javax.annotation.Nullable;

import static io.tanice.terracraftitems.paper.util.nbtapi.TerraNBTAPI.*;

public class ItemDurabilityListener implements Listener {

    public ItemDurabilityListener() {
        TerraCraftItems.inst().getServer().getPluginManager().registerEvents(this, TerraCraftItems.inst());
    }

    /* 耐久监听-射箭*/
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (processDurability(player, event.getBow(), 0, 1)) {
            ItemStack item = event.getConsumable();
            if (event.shouldConsumeItem() && item != null && !item.getType().isAir())
                player.getInventory().addItem(item);
            event.setCancelled(true);
        }
    }

    /* 耐久监听-钓鱼 */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerReelIn(PlayerFishEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        int d = -1;
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH || event.getState() == PlayerFishEvent.State.IN_GROUND) d = 1;
        else if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) d = 2;
        if (d > 0) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.FISHING_ROD) {
                if (processDurability(player, item, 0, 1)) event.setCancelled(true);
            } else {
                item = player.getInventory().getItemInOffHand();
                if (item.getType() == Material.FISHING_ROD) {
                    if (processDurability(player, item, 0, 1)) event.setCancelled(true);
                }
            }
        }
    }

    /* 耐久监听-方块交互 */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        /* 硬度为0不消耗耐久 */
        if (event.getBlock().getType().getHardness() == 0.0f) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (processDurability(player, item, 0, getOriDamagePerBlockInToolComponent(item))) event.setCancelled(true);
    }

    /* 耐久监听*/
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entityAttacker = event.getDamager();
        Entity entityDefender = event.getEntity();
        ItemStack item;
        /* 攻击方处理 */
        if (entityAttacker instanceof Player attacker) {
            if (attacker.getGameMode() != GameMode.CREATIVE) {
                item = attacker.getInventory().getItemInMainHand();
                /* 将伤害值为0 */
                if (processDurability(attacker, item, event.getFinalDamage(), getOriDamagePerAttackInWeaponComponent(item))) event.setDamage(0);
            }
            /* 抛射物 */
        } else if (entityAttacker instanceof Projectile projectile) {
            ProjectileSource source = projectile.getShooter();
            if (source instanceof Player attacker && attacker.getGameMode() != GameMode.CREATIVE) {
                /* 只有三叉戟需要监听耐久 */
                if (projectile instanceof Trident trident && processDurability(attacker, trident.getItem(), event.getFinalDamage(), getOriDamagePerAttackInWeaponComponent(trident.getItem()))) {
                    event.setDamage(0);  /* 伤害变为0 */
                }
            }
        }
        /* 防御方处理 */
        if (entityDefender instanceof Player defender && defender.getGameMode() != GameMode.CREATIVE) {
            EntityEquipment equipment = defender.getEquipment();
            /* 护甲耐久减少 */
            for (ItemStack i : equipment.getArmorContents()) processDurability(defender, i, event.getFinalDamage(), equipmentDamage(event.getFinalDamage()));
        }
    }

    /**
     * 处理耐久损失
     * @param item 需要丢失耐久的物品
     * @return 物品是否损坏--用于取消事件
     */
    private boolean processDurability(Player player, @Nullable ItemStack item, double harm, int defaultDamage) {
        if (item == null) return false;
        TerraDurabilityComponent component = DurabilityComponent.from(item);
        if (component == null) return false;
        /* 先判定是否已经损坏 */
        if (component.broken()) return true;

        Integer oriDamage = component.getDamage();
        int useDamage = component.getDamageForUse(harm);
        oriDamage = (oriDamage == null) ? 0 : oriDamage;
        component.setDamage(oriDamage + (useDamage < 0 ? defaultDamage : useDamage));
        component.cover(item);

        if (component.broken()) {
            player.playSound(player, getOriBreakSound(item), 1f, 1.2f);
            Boolean isBreakLoss = component.isBreakLoss();
            if ((isBreakLoss == null) || isBreakLoss) item.setAmount(0);
            /* 本次可以使用 */
        }
        return false;
    }

    /**
     * 护甲耐久损失计算
     */
    private int equipmentDamage(double damage) {
        return (int) Math.floor(Math.max(1, damage / 4));
    }
}
