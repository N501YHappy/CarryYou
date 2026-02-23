package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.UUID;

public class CarryCleanupListener implements Listener { //这个监听器是为了防止坏蛋故意触发bug的.
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (CarryManager.isCarrying(playerUUID)) {
            Entity target = CarryManager.getTargetEntityByCarrier(playerUUID);
            if (target != null) CarryManager.drop(target);
        }
        if (CarryManager.isCarried(playerUUID)) {
            Entity carrier = CarryManager.getCarrierEntityByTarget(playerUUID);
            if (carrier != null) CarryManager.drop(player);
        }
        BreakRunnable.removePlayer(playerUUID);
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();
        if (CarryManager.isCarried(entityUUID)) {
            Entity carrier = CarryManager.getCarrierEntityByTarget(entityUUID);
            if (carrier != null) CarryManager.drop(entity);
        }

        if (CarryManager.isCarrying(entityUUID)) {
            Entity target = CarryManager.getTargetEntityByCarrier(entityUUID);
            if (target != null) CarryManager.drop(target);
        }
        if (entity instanceof Player) {
            BreakRunnable.removePlayer(entityUUID);
        }
    }
}