package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.Cooldown;
import xyz.n501yhappy.carryyou.utils.StatePusher;

import java.util.UUID;

public class CarryListener implements Listener {

    private static final double MAX_RAY_DISTANCE = 3;
    private static final double MAX_RAY_DISTANCE_CREATIVE = MAX_RAY_DISTANCE + 2;

    @EventHandler
    public void onActive(PlayerSwapHandItemsEvent event) {
        if (!ConfigLoader.TRIGGER_SHIFT_F) return; // 用shift+f进行触发时
        if (!event.getPlayer().isSneaking()) return;
        onCarry(event);
    }
    @EventHandler
    public void onActive(PlayerInteractEntityEvent event) {
        if (ConfigLoader.TRIGGER_SHIFT_F) return; // 用shift+right click进行触发时

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        // 左右手都会触发，这里做一下过滤
        if (!event.getPlayer().isSneaking()) return;
        onCarry(event);
    }
    private <T extends PlayerEvent & Cancellable> void onCarry(T event){
        Player player = event.getPlayer();
        if (ConfigLoader.TRIGGER_EMPTY && !(player.getEquipment().getItemInMainHand() == null || player.getEquipment().getItemInMainHand().getType() == Material.AIR)){
            return;
        }
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        event.setCancelled(true);
        if (CarryManager.isCarrying(player.getUniqueId())) {
            Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
            if (target != null) {
                throwEntity(player, ConfigLoader.THROW_POWER_DROP,event);
                return;
            }
        }
        LivingEntity target = getTargetEntity(player);
        if (!isValidTarget(player, target)) return;
        if (!CarryManager.checkCarry(player, target)) return;

        handlePickup(player, target);
    }
    private boolean isValidTarget(Player player, LivingEntity target) {
        return target != null
            && !target.getUniqueId().equals(player.getUniqueId())
            && !CarryManager.isCarried(target.getUniqueId());
    }

    private void handlePickup(Player player,LivingEntity target) {
        if (CarryManager.carry(player, target)){
            StatePusher.onCarry(player,target);
            Cooldown.setCooldown(player.getUniqueId());
        }
    }
    @EventHandler
    public void onDrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            throwEntity(player, ConfigLoader.THROW_POWER_ATTACK,event);
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            throwEntity(player, ConfigLoader.THROW_POWER_INTERACT,event);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_ATTACK,event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking() && !ConfigLoader.TRIGGER_SHIFT_F) return; //防止与抓举冲突
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_INTERACT,event);
    }

    private <T extends Cancellable> void throwEntity(Player player, double power,T event) {
        UUID targetUUID = CarryManager.getTargetByCarrier(player.getUniqueId());
        if (targetUUID == null){
            return;
        }
        Entity target = Bukkit.getEntity(targetUUID);
        if (target == null){
            CarryManager.remove(player.getUniqueId(),targetUUID);
            return;
        }
        event.setCancelled(true);
        CarryManager.drop((LivingEntity) target, power);
    }


    private LivingEntity getTargetEntity(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = player.getWorld().rayTrace(
                eyeLocation,
                direction,
                (player.getGameMode() == GameMode.CREATIVE ? MAX_RAY_DISTANCE_CREATIVE : MAX_RAY_DISTANCE),
                FluidCollisionMode.NEVER,
                true,
                0.1,
                entity -> entity instanceof LivingEntity && !entity.equals(player) && !entity.isDead()
        );
        if ((result != null && result.getHitEntity() != null)){
            if (result.getHitEntity() instanceof LivingEntity){
                return (LivingEntity) result.getHitEntity();
             }
        }
        return null;
    }
}
