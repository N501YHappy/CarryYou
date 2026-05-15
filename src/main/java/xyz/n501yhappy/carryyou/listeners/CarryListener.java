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
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.Checkers;

import static xyz.n501yhappy.carryyou.CarryYou.residence_enabled;
import static xyz.n501yhappy.carryyou.CarryYou.worldguard_enabled;

public class CarryListener implements Listener {

    private static final double MAX_RAY_DISTANCE = 2.5; //玩家可以够到的距离
    private static final double THROW_POWER_DROP = 0.5; //drop时的力气
    private static final double THROW_POWER_ATTACK = 0.9; //攻击丢出去的力气
    private static final double THROW_POWER_INTERACT = 0.9; //右键丢出去的力气

    @EventHandler
    public void onCarry(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        event.setCancelled(true);
        // 不能抱两个，暂时不行
        if (CarryManager.isCarrying(player.getUniqueId())){
            Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
            if (target != null) throwEntity(player,THROW_POWER_DROP);
        }
        handlePickup(player);
    }
    // 处理抱起
    private void handlePickup(Player player) {
        Entity target = getTargetEntity(player);
        if (target == null) return;
        if (target.getUniqueId().equals(player.getUniqueId())) return;

        if (!checkCarry(player, target)) return;
        // 不能抢别人的
        if (CarryManager.isCarried(target.getUniqueId())) return;

        CarryManager.carry(player, target);
    }

    // 玩家能不能抱
    private boolean checkCarry(Player player, Entity target) {
        // 世界检查
        if (ConfigLoader.DENY_WORLDS.contains(player.getWorld().getName()) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§c当前世界不允许你抱它...");
            return false;
        }

        // 权限检查
        if (!player.hasPermission("carryyou.can") && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§c你太小啦，等你再长大一点点，它才愿意钻到你怀里哦");
            return false;
        }

        // WorldGuard 检查
        if (worldguard_enabled && !Checkers.worldguard_check(player) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§c小guard告诉我这是别人的领地！你不可以这样！");
            return false;
        }

        // Residence 检查
        if (residence_enabled && !Checkers.residence_check(player) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§cres管理员不让你这么做哦");
            return false;
        }

        // 禁止实体检查
        if (ConfigLoader.DENY_ENTITIES.contains(target.getType().name()) && !player.isOp()) {
            player.sendMessage(ConfigLoader.PREFIX + "§c你不能抱它！");
            return false;
        }

        // 权限检查
        if (target instanceof Player) {
            Player targetP = (Player) target;
            if (targetP.hasPermission("carryyou.uncarried") && !player.isOp()) {
                player.sendMessage(ConfigLoader.PREFIX + "§c你不能抱它！");
                return false;
            }
        }

        return true;
    }
    @EventHandler
    public void onDrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, THROW_POWER_ATTACK);
        event.setCancelled(true);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, THROW_POWER_ATTACK);
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!CarryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, THROW_POWER_INTERACT);
        event.setCancelled(true);
    }
    //扔出去
    private void throwEntity(Player player, double power) {
        Entity target = CarryManager.getTargetEntityByCarrier(player.getUniqueId());
        if (target == null) return;
        CarryManager.drop(target,power);
    }


    //获取玩家盯着的实体
    private Entity getTargetEntity(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = player.getWorld().rayTrace(
                eyeLocation,
                direction,
                MAX_RAY_DISTANCE,
                FluidCollisionMode.NEVER,
                true,
                0.1,
                entity -> entity instanceof LivingEntity && !entity.equals(player)
        );
        return (result != null && result.getHitEntity() != null) ? result.getHitEntity() : null;
    }
}
