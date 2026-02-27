package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.ConfigLoader;
import xyz.n501yhappy.carryyou.DependsLoader;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.DependsChecker;

public class CarryListener implements Listener {
    
    @EventHandler
    public void onCarry(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        event.setCancelled(true); // 取消交互事件，防止其他插件处理
        //是不是抱别人了？
        if (CarryManager.isCarrying(player.getUniqueId())) {
            Entity currentTarget = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
            if (currentTarget != null) {
                CarryManager.drop(currentTarget);
            }
            return;
        }
        Entity target = getTargetEntity(player);
        if (target == null) return;

        if (target.getUniqueId().equals(player.getUniqueId()))return;
        if (ConfigLoader.DENY_WORLDS.contains(player.getWorld().getName()) && !player.isOp()){
            player.sendMessage(ConfigLoader.PREFIX + "§c当前世界不允许你抱它...");
            return;
        }
        if (!player.hasPermission("carryyou.can") && !player.isOp()){
            player.sendMessage(ConfigLoader.PREFIX + "§c你太小啦，等你再长大一点点，它才愿意钻到你怀里哦");
            return;
        }
        if (DependsLoader.worldguard_enabled && !DependsChecker.worldguardCheck(player)){
            player.sendMessage(ConfigLoader.PREFIX + "§c小guard告诉我这是别人的领地！你不可以这样！");
            return;
        }
        if (DependsLoader.residence_enabled && !DependsChecker.residenceCheck(player)){
            player.sendMessage(ConfigLoader.PREFIX + "§cres管理员不让你这么做哦");
            return;
        }

        //不能抢别人的
        if (CarryManager.isCarried(target.getUniqueId())) return;
        if (ConfigLoader.DENY_ENTITIES.contains(target.getType().name()) && !player.isOp()){
            player.sendMessage(ConfigLoader.PREFIX + "§c你不能抱它！");
            return;
        }
        if (target instanceof Player){
            Player targetP = (Player) target;
            if (targetP.hasPermission("carryyou.uncarried") && !player.isOp()){
                player.sendMessage(ConfigLoader.PREFIX + "§c你不能抱它！");
                return;
            }
        }

        if (CarryManager.carry(player, target)) event.setCancelled(true); // 取消交互事件，防止其他插件处理
    }
    private Entity getTargetEntity(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = player.getWorld().rayTrace(
                eyeLocation,
                direction,
                5,   // 最大距离
                FluidCollisionMode.NEVER,
                true,
                0.1,
                entity -> entity instanceof LivingEntity && !entity.equals(player)
        );
        return (result != null && result.getHitEntity() != null) ? result.getHitEntity() : null;
    }
    @EventHandler
    public void onDrop(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
        if (target != null) {
            Vector playerVector = player.getEyeLocation().getDirection();
            Vector targetVector = target.getVelocity();
            Vector dirt = playerVector.subtract(targetVector).normalize().multiply(0.5);
            CarryManager.drop(target);
            target.setVelocity(dirt);
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
        if (target != null) {
            event.setCancelled(true);
            Vector playerVector = player.getEyeLocation().getDirection();
            Vector targetVector = target.getVelocity();
            Vector dirt = playerVector.subtract(targetVector).normalize().multiply(0.9);
            CarryManager.drop(target);
            target.setVelocity(dirt);
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
        if (target != null) {
            event.setCancelled(true);
            Vector playerVector = player.getEyeLocation().getDirection();
            Vector targetVector = target.getVelocity();
            Vector dirt = playerVector.subtract(targetVector).normalize().multiply(0.9);
            CarryManager.drop(target);
            target.setVelocity(dirt);
            event.setCancelled(true);
        }
    }

}
