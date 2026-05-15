package xyz.n501yhappy.carryyou.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarryManager {
    private static Map<UUID,UUID> carryMapping = new ConcurrentHashMap<>(); // Carrier -> target
    private static Map<UUID,UUID> mappingCarry = new ConcurrentHashMap<>(); //反向映射，从value找key
    
    public static Boolean carry(Entity carrier, Entity target) {
        UUID carrierUUID = carrier.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        
        if (carryMapping.containsKey(carrierUUID)) return false;
        
        put(carrierUUID, targetUUID);
        carrier.addPassenger(target); //坐在头上
        return true;
    }
    
    public static Boolean drop(Entity target,double power) {
        UUID targetUUID = target.getUniqueId();
        if (!carryMapping.containsValue(targetUUID)) return false; //没有这个人
        Entity carrier = getCarrierEntityByTarget(targetUUID);
        Vector vec = calcVector(carrier.getLocation(),power);
        carrier.eject();
        target.setVelocity(vec);
        remove(carrier.getUniqueId(),targetUUID);
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
    // 检查实体是否抓着别人
    public static boolean isCarrying(UUID carrierUUID) {
        return carryMapping.containsKey(carrierUUID);
    }
    // 检查实体是否被抓
    public static boolean isCarried(UUID targetUUID) {
        return mappingCarry.containsKey(targetUUID);
    }
    private static Vector calcVector(Location loc,double power){
        double yaw = Math.toRadians(-loc.getYaw()); //minecraft的小巧思
        //mc里面yaw和平面直角坐标系里面那个不一样，这里给乘-1就好了
        double pitch = Math.toRadians(-loc.getPitch());
        Vector result = new Vector();
        result.setX(Math.sin(yaw));
        result.setZ(Math.cos(yaw));
        result.setY(Math.sin(pitch));
        result.multiply(power);
        return result;
    }
}
