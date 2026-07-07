package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
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
            LivingEntity target = CarryManager.getTargetEntityByCarrier(playerUUID);
            if (target != null) CarryManager.drop(target,0);
        }
        if (CarryManager.isCarried(playerUUID)) {
            Entity carrier = CarryManager.getCarrierEntityByTarget(playerUUID);
            if (carrier != null) CarryManager.drop(player,0);
        }
        BreakRunnable.removePlayer(playerUUID);
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();
        if (CarryManager.isCarried(entityUUID)) {
            Entity carrier = CarryManager.getCarrierEntityByTarget(entityUUID);
            if (carrier != null) {
                CarryManager.drop((LivingEntity) entity,0);
            }
        }

        if (CarryManager.isCarrying(entityUUID)) {
            Entity target = CarryManager.getTargetEntityByCarrier(entityUUID);
            if (target != null) {
                CarryManager.drop((LivingEntity) target,0);
            }
        }
        if (entity instanceof Player) {
            BreakRunnable.removePlayer(entityUUID);
        }
    }
    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() != EntityType.CREEPER) return;
        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();

        if (CarryManager.isCarrying(entityUUID)) {
            Entity target = CarryManager.getTargetEntityByCarrier(entityUUID);
            if (target != null) {
                CarryManager.drop((LivingEntity) target,0);
            }
        }
        if (entity instanceof Player) {
            BreakRunnable.removePlayer(entityUUID);
        }
    }
    // 切换旁观的时候
    @EventHandler
    public void onChangingMode(PlayerGameModeChangeEvent event){
        Player player = event.getPlayer();
        if (event.getNewGameMode() != GameMode.SPECTATOR) return;
        UUID playerUUID = player.getUniqueId();
        if (CarryManager.isCarrying(playerUUID)) {
            LivingEntity target = CarryManager.getTargetEntityByCarrier(playerUUID);
            if (target != null) CarryManager.drop(target,0);
        }
        if (CarryManager.isCarried(playerUUID)) {
            Entity carrier = CarryManager.getCarrierEntityByTarget(playerUUID);
            if (carrier != null) CarryManager.drop(player,0);
        }
        BreakRunnable.removePlayer(playerUUID);
    }
}