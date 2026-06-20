package xyz.n501yhappy.carryyou.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;

import java.util.*;

public class StatePusher {
    private static Map<UUID, Boolean> hasChicken = new HashMap<>();
    private static List<UUID> withChicken = new ArrayList<>();
    private static Map<UUID, UUID> linked = new HashMap<>();

    private static boolean isChicken(LivingEntity entity){
        return entity instanceof Chicken;
    }

    public static void onCarry(LivingEntity carrier, LivingEntity carried) {
        if (!ConfigLoader.WITH_CHICKEN) return;
        if (isChicken(carried)){ // 如果抱的是鸡，直接把状态转移下去
            pushDown(carrier.getUniqueId(), carried.getUniqueId(),true);
        } else {
            // 如果不是，就把他用linked记录关系
            linked.put(carried.getUniqueId(), carrier.getUniqueId());
            boolean b = hasChicken.getOrDefault(carried.getUniqueId(), false);
            if (b){ //如果他也抱着鸡，就传递状态
                pushDown(carrier.getUniqueId(),carried.getUniqueId(), true);
            }
        }
    }
    /**
     * 向下传递状态
     * @param uuid 当前实体UUID
     * @param fa 当前实体的父节点UUID（即谁抱着当前实体）
     * @param b 是否携带鸡的状态
     */
    private static void pushDown(UUID uuid,UUID fa, Boolean b){
        UUID next;
        if (b){
            hasChicken.put(uuid, true);
            withChicken.add(uuid);
            withChicken.remove(fa);
            next = linked.getOrDefault(uuid, null);
            if (next == null) return;
            withChicken.remove(uuid);
        } else {
            withChicken.remove(uuid);
            LivingEntity entity = getLivingEntity(uuid);
            if (entity != null && isChicken(entity)) return;
            hasChicken.put(uuid, false);
            next = linked.getOrDefault(uuid, null);
            if (next == null) return;
        }
        pushDown(next,uuid, b);
    }

    private static LivingEntity getLivingEntity(UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity instanceof LivingEntity) {
            return (LivingEntity) entity;
        }
        return null;
    }

    public static void onDrop(LivingEntity carrier, LivingEntity carried) {
        if (!ConfigLoader.WITH_CHICKEN) return;
        if (isChicken(carried) || hasChicken.getOrDefault(carrier.getUniqueId(), false)){
            pushDown(carrier.getUniqueId(), null,false);
        }
        if (!isChicken(carried)) {
            linked.put(carried.getUniqueId(), null);
        }
    }

    public static boolean remove(UUID uuid) {
        boolean removed = hasChicken.remove(uuid) != null;
        withChicken.remove(uuid);
        linked.remove(uuid);
        return removed;
    }

    public static List<UUID> getWithChicken() {
        return new ArrayList<>(withChicken);
    }
}