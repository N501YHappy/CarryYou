package xyz.n501yhappy.carryyou.utils.state;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks whether a configured target entity type (for example a chicken) is
 * somewhere below an entity in a carry chain, and propagates that state as the
 * chain changes.
 *
 * <p>Subclasses define the target class through the constructor and decide via
 * {@link #trigger()} whether the state should be tracked at all. Consumers call
 * {@link #onCarry(Entity, Entity)} and {@link #onDrop(Entity, Entity)} when
 * relationships change, then query {@link #getWithTarget()} for the entities
 * that should receive the associated effect.
 */
public abstract class StatePusher {
    private final List<Class<?>> targetClasses;

    private final Map<UUID, Boolean> hasTarget = new ConcurrentHashMap<>();
    private final List<UUID> withTarget = new ArrayList<>();
    private final Map<UUID, UUID> linked = new ConcurrentHashMap<>(); // carried -> carrier

    /**
     * Creates a state tracker for the given target entity classes.
     *
     * @param targetClasses entity classes that count as the tracked target
     */
    public StatePusher(Class<?>... targetClasses) {
        this.targetClasses = List.of(targetClasses);
    }

    /**
     * Checks whether an entity is one of the tracked target types.
     *
     * @param entity entity to test, may be {@code null}
     * @return {@code true} if the entity matches a target class
     */
    private boolean isTarget(Entity entity){
        for(Class<?> targetClass : targetClasses){
            if(targetClass.isInstance(entity)) return true;
        }
        return false;
    }

    /**
     * Returns whether state tracking is currently active.
     *
     * @return {@code true} if carry relationships should update the state
     */
    public abstract boolean trigger();

    /**
     * Updates the tracked state after {@code carrier} picks up {@code carried}.
     *
     * <p>If the carried entity is the target, the state is propagated down the
     * chain immediately. Otherwise the relationship is remembered so the state
     * can be propagated when a target is added later.
     *
     * @param carrier entity doing the carrying
     * @param carried entity being carried
     */
    public void onCarry(Entity carrier, Entity carried) {
        if (!trigger()) return;
        if (isTarget(carried)){ // the carried entity is the target: push the state down immediately
            pushDown(carrier.getUniqueId(), carried.getUniqueId(),true);
        } else {
            // Record the relationship so state can travel through this link later
            linked.put(carried.getUniqueId(), carrier.getUniqueId());
            boolean b = hasTarget.getOrDefault(carried.getUniqueId(), false);
            if (b){ // the carried entity already has a target below it: propagate upward
                pushDown(carrier.getUniqueId(),carried.getUniqueId(), true);
            }
        }
    }

    /**
     * Walks down the carry chain and applies or clears the target state.
     *
     * @param uuid UUID of the entity whose state is being updated
     * @param fa   UUID of the entity immediately below it (the one it carries)
     * @param b    {@code true} to propagate the target state downward,
     *             {@code false} to clear it
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

    /**
     * Updates the tracked state after {@code carrier} releases {@code carried}.
     *
     * <p>If the released entity was the target, or the carrier still has a
     * target below it, the state is recalculated downward. The link is removed
     * when the released entity was not itself a target.
     *
     * @param carrier entity that was carrying
     * @param carried entity that was released
     */
    public void onDrop(Entity carrier, Entity carried) {
        if (!trigger()) return;
        if (isTarget(carried) || hasTarget.getOrDefault(carrier.getUniqueId(), false)){
            pushDown(carrier.getUniqueId(), carried.getUniqueId(),false);
        }
        if (!isTarget(carried)) {
            linked.remove(carried.getUniqueId());
        }
    }

    /**
     * Forgets all tracked state for a UUID.
     *
     * @param uuid UUID to remove
     * @return {@code true} if the UUID was marked as having a target below it
     */
    public boolean remove(UUID uuid) {
        boolean removed = hasTarget.remove(uuid) != null;
        withTarget.remove(uuid);
        linked.remove(uuid);
        return removed;
    }

    /**
     * Returns a snapshot of the entities that currently have a target below
     * them in their carry chain.
     *
     * @return a copy of the tracked UUID list
     */
    public List<UUID> getWithTarget() {
        return new ArrayList<>(withTarget);
    }
}