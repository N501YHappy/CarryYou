package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.utils.CarryManager;

public class DropListener implements Listener {
    @EventHandler
    public void onActive(PlayerSwapHandItemsEvent event) {
        if (!ConfigLoader.TRIGGER_SHIFT_F) return; //不用shift+f
        onDrop(event);
    }
    @EventHandler
    public void onActive(PlayerDropItemEvent event) {
        if (ConfigLoader.TRIGGER_SHIFT_F) return; //不用shift+f
        onDrop(event);
    }
    private <T extends PlayerEvent & Cancellable> void onDrop(T event){
        Player player = event.getPlayer();
        if (ConfigLoader.TRIGGER_EMPTY && !(player.getEquipment().getItemInMainHand() == null || player.getEquipment().getItemInMainHand().getType() == Material.AIR)){
            return;
        }
        if (!player.isSneaking()) return;
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        event.setCancelled(true);
        if (CarryManager.isCarrying(player.getUniqueId())) {
            Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
            if (target != null) {
                throwEntity(player, ConfigLoader.THROW_POWER_DROP);
                return;
            }
        }
    }
    @EventHandler
    public void onDrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            throwEntity(player, ConfigLoader.THROW_POWER_ATTACK);
            event.setCancelled(true);
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            throwEntity(player, ConfigLoader.THROW_POWER_INTERACT);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_ATTACK);
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_INTERACT);
        event.setCancelled(true);
    }

    private void throwEntity(Player player, double power) {
        Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
        if (target == null) return;
        CarryManager.drop(target, power);
    }
}
