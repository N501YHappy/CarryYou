package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.utils.CarryManager;

public class CarryListener implements Listener {
    
    @EventHandler
    public void onCarry(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        //是不是抱别人了？
        if (CarryManager.carryMapping.containsKey(player.getUniqueId())) {
            Entity currentTarget = Bukkit.getEntity(CarryManager.carryMapping.get(player.getUniqueId()));
            if (currentTarget != null) {
                CarryManager.drop(currentTarget);
                player.sendMessage("§aDropped " + currentTarget.getName());
            }
            return;
        }
        Entity target = getTargetEntity(player);
        if (target == null) return;
        if (!player.isSneaking()) return;
        if (target.getUniqueId().equals(player.getUniqueId()))return;
        


        //不能抢别人的
        if (CarryManager.mappingCarry.containsKey(target.getUniqueId())) return;

        if (CarryManager.carry(player, target)) {
            player.sendMessage("§aCarried " + target.getName());
            event.setCancelled(true); // 取消交互事件，防止其他插件处理
        }
    }
    private Entity getTargetEntity(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = player.getWorld().rayTrace(
                eyeLocation,           // 起点：玩家眼睛位置
                direction,             // 方向：玩家视线方向
                5,   // 最大距离
                FluidCollisionMode.NEVER, // 忽略流体
                true,                  // 忽略可通过的方块
                0.1,              // 射线粗细
                entity -> entity instanceof LivingEntity && !entity.equals(player)
        );
        return (result != null && result.getHitEntity() != null) ? result.getHitEntity() : null;
    }
}
