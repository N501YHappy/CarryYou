package xyz.n501yhappy.carryyou.listeners;

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
import org.bukkit.event.player.*;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;
import xyz.n501yhappy.carryyou.depends.DominionDepends;
import xyz.n501yhappy.carryyou.depends.ResidenceDepends;
import xyz.n501yhappy.carryyou.depends.WorldGuardDepends;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.Checkers;
import xyz.n501yhappy.carryyou.utils.Cooldown;
import xyz.n501yhappy.carryyou.utils.StatePusher;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarryListener implements Listener {

    private static final double MAX_RAY_DISTANCE = 3;
    private static final double MAX_RAY_DISTANCE_CREATIVE = MAX_RAY_DISTANCE + 2;

    @EventHandler
    public void onActive(PlayerSwapHandItemsEvent event) {
        if (!ConfigLoader.TRIGGER_SHIFT_F) return; //不用shift+f
        onCarry(event);
    }
    @EventHandler
    public void onActive(PlayerDropItemEvent event) {
        if (ConfigLoader.TRIGGER_SHIFT_F) return; //不用shift+f
        onCarry(event);
    }
    private <T extends PlayerEvent & Cancellable> void onCarry(T event){
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
        handlePickup(player);
    }
    private void handlePickup(Player player) {
        LivingEntity target = getTargetEntity(player);
        if (target == null) return;
        if (target.getUniqueId().equals(player.getUniqueId())) return;

        if (!checkCarry(player, target)) return;
        if (CarryManager.isCarried(target.getUniqueId())) return;

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
        LivingEntity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
        if (target == null) return;
        CarryManager.drop(target, power);
    }
    private boolean checkCarry(Player player, Entity target) {
        if(!Cooldown.checkCooldown(player.getUniqueId() )  && !player.isOp()){
            double remainingSeconds = Cooldown.getRemains(player.getUniqueId()) / 1000.0;
            String message = ConfigLoader.PREFIX + MessageConfig.Message.COOLDOWN.get()
                    .replace("%s", String.format("%.2f", remainingSeconds));
            player.sendMessage(message);
            return false;
        }
        if (ConfigLoader.DENY_WORLDS.contains(player.getWorld().getName()) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_WORLD_DENY.get());
            return false;
        }

        if (!player.hasPermission("carryyou.can") && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_NO_PERMISSION.get());
            return false;
        }

        if (CarryYou.worldguard_enable && !Checkers.worldguard_check(target, player) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_WORLDGUARD_DENY.get());
            return false;
        }

        if (CarryYou.residence_enable && !Checkers.residence_check(target, player) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_RESIDENCE_DENY.get());
            return false;
        }

        if (CarryYou.dominion_enable && !Checkers.dominion_check(target, player) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_DOMINION_DENY.get());
            return false;
        }

        if (ConfigLoader.DENY_ENTITIES.contains(target.getType().name()) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_ENTITY_DENY.get());
            return false;
        }

        if (target instanceof Player) {
            Player targetP = (Player) target;
            if (targetP.hasPermission("carryyou.uncarried") && !player.isOp()) {
                player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_PLAYER_UNCARRIED.get());
                return false;
            }
        }

        return true;
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
