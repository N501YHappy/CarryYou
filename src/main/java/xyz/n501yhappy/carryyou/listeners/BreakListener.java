package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.UUID;

public class BreakListener implements Listener {

    @EventHandler
    public void onBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) return;
        if (!CarryManager.isCarried(playerUUID)) return;
        event.setCancelled(true);
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.BREAK_WEAKNESS.get());
            return;
        }
        if (player.hasPermission("carryyou.unbreak") && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.BREAK_UNBREAK.get());
            return;
        }
        BreakRunnable.addScore(playerUUID,1);
    }
    @EventHandler
    public void onBreak_Dismount(EntityDismountEvent event) {
        if (!CarryManager.isCarrying(event.getDismounted().getUniqueId())) return;
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            UUID playerUUID = player.getUniqueId();

            if (!CarryManager.isCarried(playerUUID)) return;
            event.setCancelled(true);
        }
    }
}
