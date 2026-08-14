package xyz.n501yhappy.carryyou.utils.state;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StatePusher {
    private final Class<?> targetClass;

    private final Map<UUID, Boolean> hasTarget = new ConcurrentHashMap<>();
    private final List<UUID> withTarget = new ArrayList<>();
    private final Map<UUID, UUID> linked = new ConcurrentHashMap<>(); // 被抓的指向抓人的

    public StatePusher(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    private boolean isTarget(Entity entity){
        return targetClass.isInstance(entity);
    }
    public abstract boolean trigger();

    public void onCarry(Entity carrier, Entity carried) {
        if (!trigger()) return;
        if (isTarget(carried)){ // 如果抱的是目标，直接把状态转移下去
            pushDown(carrier.getUniqueId(), carried.getUniqueId(),true);
        } else {
            // 如果不是，就把他用linked记录关系
            linked.put(carried.getUniqueId(), carrier.getUniqueId());
            boolean b = hasTarget.getOrDefault(carried.getUniqueId(), false);
            if (b){ //如果他也抱着目标，就传递状态
                pushDown(carrier.getUniqueId(),carried.getUniqueId(), true);
            }
        }
    }
    /**
     * 向下传递状态
     * @param uuid 当前实体UUID
     * @param fa 当前实体的父节点UUID（即谁抱着当前实体）
     * @param b 是否携带目标的状态
     */
    private void pushDown(UUID uuid,UUID fa, Boolean b){
        UUID next;
        if (b){
            hasTarget.put(uuid, true);
            withTarget.add(uuid);
            withTarget.remove(fa);
            next = linked.getOrDefault(uuid, null);
            if (next == null) return;
            withTarget.remove(uuid);
        } else {
            if (hasTarget.getOrDefault(fa, false)){
                withTarget.add(fa);
            }
            withTarget.remove(uuid);
            Entity entity = Bukkit.getEntity(uuid);
            if (isTarget(entity)) return;
            hasTarget.put(uuid, false);
            next = linked.getOrDefault(uuid, null);
            if (next == null) return;
        }
        pushDown(next,uuid, b);
    }

    public void onDrop(Entity carrier, Entity carried) {
        if (!ConfigLoader.WITH_CHICKEN) return;
        if (isTarget(carried) || hasTarget.getOrDefault(carrier.getUniqueId(), false)){
            pushDown(carrier.getUniqueId(), carried.getUniqueId(),false);
        }
        if (!isTarget(carried)) {
            linked.remove(carried.getUniqueId());
        }
    }

    public boolean remove(UUID uuid) {
        boolean removed = hasTarget.remove(uuid) != null;
        withTarget.remove(uuid);
        linked.remove(uuid);
        return removed;
    }

    public List<UUID> getWithTarget() {
        return new ArrayList<>(withTarget);
    }
}