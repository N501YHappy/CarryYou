package xyz.n501yhappy.carryyou.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.UUID;

public class MoveRunnable extends BukkitRunnable {
    private static final double RANGE = 1.1;
    
    @Override
    public void run() {
        // 获取所有被抓实体的UUID
        UUID[] targetUUIDs = CarryManager.getAllTargets();
        
        for (UUID targetUUID : targetUUIDs) {
            Entity target = Bukkit.getEntity(targetUUID);
            UUID carrierUUID = CarryManager.getCarrierByTarget(targetUUID);
            
            if (carrierUUID == null) continue;
            Player carrier = (Player) Bukkit.getEntity(carrierUUID);
            
            if (target == null || carrier == null) {
                CarryManager.removeByTarget(targetUUID);
                continue;
            }
            
            if (!carrier.isOnline()) {
                CarryManager.removeByTarget(targetUUID);
                continue;
            }

            Location loc = getCarryLoc(carrier);
            loc.setYaw(carrier.getLocation().getYaw());
            if (target instanceof Shulker) {
                target.teleport(loc);
            } else {
                smoothMove(target, loc);
            }
        }
    }
    
    private Location getCarryLoc(Player carrier) {
        Location carryLoc = carrier.getLocation();
        double radians = Math.toRadians(carryLoc.getYaw());
        double nx = carryLoc.getX() - Math.sin(radians) * RANGE;
        double ny = carryLoc.getY() + 1.2;
        double nz = carryLoc.getZ() + Math.cos(radians) * RANGE;
        return new Location(carryLoc.getWorld(), nx, ny, nz);
    }
    
    private void smoothMove(Entity target, Location loc) {
        target.teleport(loc); //骗你的，这就是平滑
    }
}
