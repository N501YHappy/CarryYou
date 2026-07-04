package xyz.n501yhappy.carryyou.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarryManager {
    private static Map<UUID,UUID> carryMapping = new ConcurrentHashMap<>(); // Carrier -> target
    private static Map<UUID,UUID> mappingCarry = new ConcurrentHashMap<>(); //反向映射，从value找key
    private static Map<UUID,Boolean> carryDisabled = new ConcurrentHashMap<>(); // true = 禁止抱起

    public static Boolean carry(Entity carrier, Entity target) {
        UUID carrierUUID = carrier.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if (carryMapping.containsKey(carrierUUID)) {
            UUID oldTarget = carryMapping.get(carrierUUID);
            if (Bukkit.getEntity(oldTarget) == null) {
                remove(carrierUUID, oldTarget); // 目标已被不见了，清理脏数据
            } else {
                return false;
            }
        }
        if (!carrier.getPassengers().isEmpty()) return false; //有人在上面也不行

        if(carrier.addPassenger(target)){
            put(carrierUUID, targetUUID);
            return true;
        }
        return false;
    }
    
    public static Boolean drop(LivingEntity target, double power) {
        UUID targetUUID = target.getUniqueId();
        if (!mappingCarry.containsKey(targetUUID)) return false;
        UUID carrierUUID = getCarrierByTarget(targetUUID);
        LivingEntity carrier = carrierUUID != null ?(LivingEntity) Bukkit.getEntity(carrierUUID) : null;

        if (carrier == null) {
            remove(carrierUUID, targetUUID);
            return false;
        }
        Vector vec = calcVector(carrier.getVelocity(), carrier.getLocation(),power);

        remove(carrierUUID, targetUUID);
        StatePusher.onDrop(carrier,target);
        carrier.removePassenger(target);
        target.setVelocity(vec);
        return true;
    }
    public static void put(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.put(carrierUUID, targetUUID);
        mappingCarry.put(targetUUID, carrierUUID);
    }
    
    public static void remove(UUID carrierUUID, UUID targetUUID) { //保证原子性直接拿函数
        carryMapping.remove(carrierUUID) ;
        mappingCarry.remove(targetUUID);
    }
    public static UUID getTargetByCarrier(UUID carrierUUID) {//通过抓取者获取被抓实体
        return carryMapping.get(carrierUUID);
    }
    

    public static UUID getCarrierByTarget(UUID targetUUID) {//通过被抓实体获取抓取者
        return mappingCarry.get(targetUUID);
    }
    
    // 获取被抓实体的Entity对象
    public static LivingEntity getTargetEntityByCarrier(UUID carrierUUID) {
        UUID targetUUID = getTargetByCarrier(carrierUUID);
        return targetUUID != null ? (LivingEntity) Bukkit.getEntity(targetUUID) : null;
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
    public static void cleanup() {
        for (Map.Entry<UUID, UUID> entry : carryMapping.entrySet()) {
            Entity carrier = Bukkit.getEntity(entry.getKey());
            Entity target = Bukkit.getEntity(entry.getValue());
            if (carrier != null && target != null) {
                carrier.removePassenger(target);
            }
        }
        carryMapping.clear();
        mappingCarry.clear();
    }

    public static void setCarryDisabled(UUID uuid, boolean disabled) {
        carryDisabled.put(uuid, disabled);
    }

    public static boolean isCarryDisabled(UUID uuid) {
        return carryDisabled.getOrDefault(uuid, false);
    }

    public static boolean checkCarry(Player player, Entity target) {
        if (isCarryDisabled(target.getUniqueId())) return false;

        if(!Cooldown.checkCooldown(player.getUniqueId()) && !player.isOp()){
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
            if (isCarryDisabled(targetP.getUniqueId())  && !player.isOp()) {
                player.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.CARRY_PLAYER_UNCARRIED.get());
                return false;
            }
        }

        return true;
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
