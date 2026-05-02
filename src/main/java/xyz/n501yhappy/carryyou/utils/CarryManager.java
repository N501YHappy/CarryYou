package xyz.n501yhappy.carryyou.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarryManager {
    private static Map<UUID,UUID> carryMapping = new ConcurrentHashMap<>(); // Carrier -> target
    private static Map<UUID,UUID> mappingCarry = new ConcurrentHashMap<>(); //反向映射，从value找key

    private static Map<UUID,UUID> armorMapping = new ConcurrentHashMap<>(); //Target -> Armor stand
    private static Map<UUID,UUID> mappingArmor = new ConcurrentHashMap<>(); //反向映射，从value找key
    
    public static Boolean carry(Entity carrier, Entity target) {
        UUID carrierUUID = carrier.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        
        if (carryMapping.containsKey(carrierUUID)) return false;
        
        put(carrierUUID, targetUUID);
        if (target instanceof Player){
            ((Player) target).setAllowFlight(true);
        }
        spawnArmorStand(carrierUUID, targetUUID);
        return true;
    }
    
    public static Boolean drop(Entity target) {
        UUID targetUUID = target.getUniqueId();
        if (!carryMapping.containsValue(targetUUID)) return false; //没有这个人
        target.setFallDistance(0); //别玩死了(?)
        removeArmorStandByTarget(targetUUID);
        removeByTarget(targetUUID);
        if (target instanceof Player){
            ((Player) target).setAllowFlight(false);
        }
        return true;
    }

    private static UUID spawnArmorStand(UUID carrierUUID, UUID targetUUID){
        Entity carrier = Bukkit.getEntity(carrierUUID);
        Entity target = Bukkit.getEntity(targetUUID);
        Location loc = carrier.getLocation();
        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        UUID armorUUID = armorStand.getUniqueId();
        Armorput(targetUUID, armorUUID);
        return armorUUID;
    }

    public static void put(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.put(carrierUUID, targetUUID);
        mappingCarry.put(targetUUID, carrierUUID);
    }

    public static void Armorput(UUID targetUUID, UUID armorUUID) { //保证原子性直接拿函数
        armorMapping.put(targetUUID, armorUUID);
        mappingArmor.put(armorUUID, targetUUID);
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

    // ArmorStand的一些东西
    public static void removeArmorStandByTarget(UUID targetUUID) { //根据target删除
        UUID armorUUID = armorMapping.remove(targetUUID);
        if (armorUUID != null) {
            mappingArmor.remove(armorUUID);
            ArmorStand as = (ArmorStand) Bukkit.getEntity(armorUUID);
            if (as != null) {
                as.remove();
            }
        }
    }
    public static void removeArmorStandByArmorStand(UUID armorUUID) {
        UUID targetUUID = mappingArmor.remove(armorUUID);
        if (targetUUID != null) {
            armorMapping.remove(targetUUID);
            ArmorStand as = (ArmorStand) Bukkit.getEntity(armorUUID);
            if (as != null) {
                as.remove();
            }
        }
    }

    //target get ArmorStand
    public static ArmorStand getArmorStandByTarget(UUID targetUUID) {
        UUID armorUUID = armorMapping.get(targetUUID);
        return armorUUID != null ? (ArmorStand) Bukkit.getEntity(armorUUID) : null;
    }

    // 通过 ArmorStand 获取 target UUID
    public static UUID getTargetByArmorStand(UUID armorUUID) {
        return mappingArmor.get(armorUUID);
    }

    // 检查 target 是否有对应的 ArmorStand
    public static boolean hasArmorStand(UUID targetUUID) {
        return armorMapping.containsKey(targetUUID);
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
}
