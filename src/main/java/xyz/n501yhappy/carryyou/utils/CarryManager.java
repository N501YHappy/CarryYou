package xyz.n501yhappy.carryyou.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarryManager {
    private static Map<UUID,UUID> carryMapping = new HashMap<>();
    private static Map<UUID,UUID> mappingCarry = new HashMap<>(); //反向映射，从value找key
    
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
    

    public static UUID getTargetByCarrier(UUID carrierUUID) {//通过抓取者获取被抓实体
        return carryMapping.get(carrierUUID);
    }
    

    public static UUID getCarrierByTarget(UUID targetUUID) {//通过被抓实体获取抓取者
        return mappingCarry.get(targetUUID);
    }
    
    // 获取被抓实体的Entity对象
    public static Entity getTargetEntityByCarrier(UUID carrierUUID) {
        UUID targetUUID = getTargetByCarrier(carrierUUID);
        return targetUUID != null ? Bukkit.getEntity(targetUUID) : null;
    }
    
    // 获取抓取者的Entity对象
    public static Entity getCarrierEntityByTarget(UUID targetUUID) {
        UUID carrierUUID = getCarrierByTarget(targetUUID);
        return carrierUUID != null ? Bukkit.getEntity(carrierUUID) : null;
    }
    
    // 检查抓取者是否在携带实体
    public static boolean isCarrying(UUID carrierUUID) {
        return carryMapping.containsKey(carrierUUID);
    }
    
    // 检查实体是否被携带
    public static boolean isCarried(UUID targetUUID) {
        return mappingCarry.containsKey(targetUUID);
    }
    
    // 获取所有被抓实体的UUID
    public static UUID[] getAllTargets() {
        return mappingCarry.keySet().toArray(new UUID[0]);
    }
    
    // 获取所有抓取者的UUID
    public static UUID[] getAllCarriers() {
        return carryMapping.keySet().toArray(new UUID[0]);
    }
    
    public static Boolean isCarrier(Player player){ //是抓人的
        return carryMapping.containsKey(player.getUniqueId());
    }
    public static Boolean isCarried(Entity entity){ //是被抓的
        return mappingCarry.containsKey(entity.getUniqueId());
    }
}
