package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.services.CarryService;

import java.util.UUID;

public class CarryCleanupListener implements Listener { //这个监听器是为了防止坏蛋故意触发bug的.
    private final CarryService carryService = CarryService.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (carryService.isCarrying(playerUUID)) {
            Entity target = carryService.getTargetEntityByCarrier(playerUUID);
            if (target != null) carryService.drop(target,0,false);
        }
        if (carryService.isCarried(playerUUID)) {
            Entity carrier = carryService.getCarrierEntityByTarget(playerUUID);
            if (carrier != null) carryService.drop(player,0,false);
        }
        BreakRunnable.removePlayer(playerUUID);
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        UUID entityUUID = entity.getUniqueId();
        if (carryService.isCarried(entityUUID)) {
            Entity carrier = carryService.getCarrierEntityByTarget(entityUUID);
            if (carrier != null) {
                carryService.drop( entity,0,false);
            }
        }

        if (carryService.isCarrying(entityUUID)) {
            Entity target = carryService.getTargetEntityByCarrier(entityUUID);
            if (target != null) {
                carryService.drop( target,0,false);
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

        if (carryService.isCarrying(entityUUID)) {
            Entity target = carryService.getTargetEntityByCarrier(entityUUID);
            if (target != null) {
                carryService.drop( target,0,false);
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
        if (carryService.isCarrying(playerUUID)) {
            Entity target = carryService.getTargetEntityByCarrier(playerUUID);
            if (target != null) carryService.drop(target,0,false);
        }
        if (carryService.isCarried(playerUUID)) {
            Entity carrier = carryService.getCarrierEntityByTarget(playerUUID);
            if (carrier != null) carryService.drop(player,0,false);
        }
        BreakRunnable.removePlayer(playerUUID);
    }
}