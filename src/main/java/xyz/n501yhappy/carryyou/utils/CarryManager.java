package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarryManager {
    public static Map<UUID,UUID> carryMapping = new HashMap<>();
    public static Map<UUID,UUID> mappingCarry = new HashMap<>(); //反向映射，从value找key
    
    public static Boolean carry(Entity carrier, Entity target) {
        UUID carrierUUID = carrier.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        
        if (carryMapping.containsKey(carrierUUID)) return false;
        
        put(carrierUUID, targetUUID);
        if (target instanceof Player) ((Player) target).setFlying(true);
        target.setGravity(false);
        return true;
    }
    
    public static Boolean drop(Entity target) {
        UUID targetUUID = target.getUniqueId();
        if (!carryMapping.containsValue(targetUUID)) return false; //没有这个人
        
        removeByTarget(targetUUID);
        if (target instanceof Player) ((Player) target).setFlying(false);
        target.setGravity(true);
        return true;
    }
    
    public static void put(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.put(carrierUUID, targetUUID);
        mappingCarry.put(targetUUID, carrierUUID);
    }
    
    public static void remove(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.remove(carrierUUID, targetUUID);
        mappingCarry.remove(targetUUID, carrierUUID);
    }
    
    public static void removeByTarget(UUID targetUUID) { //保证原子性直接拿函数
        UUID carrierUUID = mappingCarry.get(targetUUID);
        if (carrierUUID != null) {
            carryMapping.remove(carrierUUID, targetUUID);
            mappingCarry.remove(targetUUID);
        }
    }
}
