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
        if (!carrier.getPassengers().isEmpty()) return false; //有人在上面也不行

        put(carrierUUID, targetUUID);
        carrier.addPassenger(target); //坐在头上
        return true;
    }
    
    public static Boolean drop(Entity target,double power) {
        UUID targetUUID = target.getUniqueId();
        if (!carryMapping.containsValue(targetUUID)) return false; //没有这个人
        Entity carrier = getCarrierEntityByTarget(targetUUID);
        Vector vec = calcVector(carrier.getVelocity(), carrier.getLocation(),power);

        remove(carrier.getUniqueId(),targetUUID); //一定记住清理映射，否则会被BreakListener拦截掉
        carrier.removePassenger(target);
        target.setVelocity(vec);
        return true;
    }
    public static void put(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.put(carrierUUID, targetUUID);
        mappingCarry.put(targetUUID, carrierUUID);
    }
    
    public static void remove(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.remove(carrierUUID);
        mappingCarry.remove(targetUUID);
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
    private static Vector calcVector(Vector speed, Location loc,double power){
        //mc里面yaw和平面直角坐标系里面那个不一样，这里给乘-1就好了
        //Yaw: 我们 水平旋转 我们的头，也就是左右转头，这就是一次Yaw转动
        //Pitch: 我们 上下旋转 我们的头，也就是上下点头，这就是一次Pitch转动
        double phi = Math.toRadians(90 + loc.getPitch()); //90 - (-pitch)
        double theta = Math.toRadians(-loc.getYaw());
        Vector result = new Vector();
        double x = Math.cos(theta)*Math.sin(phi);
        double z = Math.cos(phi);
        double y = Math.sin(theta)*Math.sin(phi);

        result.setX(y);
        result.setY(z);
        result.setZ(x);
        result.multiply(power);
        if(speed != null) result.add(speed);
        return result;
    }
}
