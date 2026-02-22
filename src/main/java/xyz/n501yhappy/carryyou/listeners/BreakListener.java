package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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

        BreakRunnable.addScore(playerUUID);
    }
}
