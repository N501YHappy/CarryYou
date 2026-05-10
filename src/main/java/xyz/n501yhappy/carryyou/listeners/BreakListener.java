package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import xyz.n501yhappy.carryyou.ConfigLoader;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.UUID;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (!CarryManager.isCarried(playerUUID)) return;
        event.setCancelled(true);
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§c你现在处于虚弱状态！");
            return;
        }
        if (player.hasPermission("carryyou.unbreak") && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§c你现在不能挣脱哦...忍一会吧~");
            return;
        }
        BreakRunnable.addScore(playerUUID,1);
    }
    @EventHandler
    public void onBreak_Dismount(EntityDismountEvent event) {
        if (event.getDismounted().getCustomName() == null) return;
        if (!event.getDismounted().getCustomName().equals("Chihaya Anon")) return;//这里现在由我接管awa
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            UUID playerUUID = player.getUniqueId();

            if (!CarryManager.isCarried(playerUUID)) return;
            event.setCancelled(true);
        }
    }
}
