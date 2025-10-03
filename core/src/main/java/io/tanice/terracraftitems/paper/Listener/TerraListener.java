package io.tanice.terracraftitems.paper.Listener;

import io.tanice.terracraftitems.api.item.component.custom.TerraCommandComponent;
import io.tanice.terracraftitems.paper.Listener.item.ItemDurabilityListener;
import io.tanice.terracraftitems.paper.Listener.item.ItemUpdateListener;
import io.tanice.terracraftitems.paper.TerraCraftItems;
import io.tanice.terracraftitems.paper.item.component.custom.CommandComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class TerraListener implements Listener {

    private final ItemDurabilityListener itemDurabilityListener;
    private final ItemUpdateListener itemUpdateListener;

    public TerraListener() {
        itemDurabilityListener = new ItemDurabilityListener();
        itemUpdateListener = new ItemUpdateListener();
        TerraCraftItems.inst().getServer().getPluginManager().registerEvents(this, TerraCraftItems.inst());
    }

    /**
     * 可消耗的组件效果
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        /* 原版食物效果自动生效, 额外效果由指令替代 */
        TerraCommandComponent component = CommandComponent.from(event.getItem());
        if (component == null) return;
        String[] commands = component.getCommands();
        if (commands == null) return;
        String playerName = event.getPlayer().getName();
        for (String command : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("@self", playerName));
        }
    }
}
