package carryyou.api;

import org.bukkit.entity.Entity;

import java.util.UUID;

/**
 * Public API for querying and modifying carry relationships.
 *
 * <p>A relationship has a <em>carrier</em> (the entity holding another entity)
 * and a <em>target</em> (the entity being carried). Obtain the implementation
 * through {@link CarryyouAPI#getCarryManager()}.
 *
 * <p>Prefer the high-level {@link #carry(Entity, Entity)} and
 * {@link #drop(Entity, double, boolean)} methods. The {@link #put(UUID, UUID)}
 * and {@link #remove(UUID, UUID)} methods manipulate the internal mapping
 * directly and can leave the carrier/target state inconsistent when misused.
 */
public interface CarryManagerAPI {

    /**
     * Attempts to make {@code carrier} carry {@code target}.
     *
     * @param carrier the entity that will hold the target
     * @param target  the entity to be carried
     * @return {@code true} if the relationship was established
     */
    boolean carry(Entity carrier, Entity target);

    /**
     * Releases the entity carrying {@code target} and launches the target.
     *
     * @param target    the carried entity to release
     * @param power     throw-vector multiplier; {@code 0} releases without launching
     * @param callEvent whether to fire a cancellable
     *                  {@link carryyou.api.events.PlayerDropEvent} first; {@code false}
     *                  forces the drop for internal cleanup paths
     * @return {@code true} if the target was released
     */
    boolean drop(Entity target, double power, boolean callEvent);

    /**
     * Registers both directions of a carry relationship.
     *
     * <p>Low-level operation: it does not validate entities or fire events.
     *
     * @param carrierUUID UUID of the carrying entity
     * @param targetUUID  UUID of the carried entity
     */
    void put(UUID carrierUUID, UUID targetUUID);

    /**
     * Removes both directions of a carry relationship.
     *
     * <p>Low-level operation: it does not detach passengers or fire events.
     *
     * @param carrierUUID UUID of the carrying entity
     * @param targetUUID  UUID of the carried entity
     */
    void remove(UUID carrierUUID, UUID targetUUID);

    /**
     * Returns the target carried by the given carrier.
     *
     * @param carrierUUID UUID of the carrier
     * @return the target's UUID, or {@code null} if the carrier carries nothing
     */
    UUID getTargetByCarrier(UUID carrierUUID);

    /**
     * Returns the carrier holding the given target.
     *
     * @param targetUUID UUID of the carried entity
     * @return the carrier's UUID, or {@code null} if the entity is not carried
     */
    UUID getCarrierByTarget(UUID targetUUID);

    /**
     * Resolves the target carried by the given carrier to an entity.
     *
     * @param carrierUUID UUID of the carrier
     * @return the target entity, or {@code null} if absent or unloaded
     */
    Entity getTargetEntityByCarrier(UUID carrierUUID);

    /**
     * Resolves the carrier holding the given target to an entity.
     *
     * @param targetUUID UUID of the carried entity
     * @return the carrier entity, or {@code null} if absent or unloaded
     */
    Entity getCarrierEntityByTarget(UUID targetUUID);

    /**
     * Checks whether the entity is carrying another entity.
     *
     * @param carrierUUID UUID of the potential carrier
     * @return {@code true} if the entity currently carries a target
     */
    boolean isCarrying(UUID carrierUUID);

    /**
     * Checks whether the entity is being carried.
     *
     * @param targetUUID UUID of the potential target
     * @return {@code true} if the entity is currently carried
     */
    boolean isCarried(UUID targetUUID);

    /**
     * Releases all active carry relationships and clears internal mappings.
     *
     * <p>Intended for plugin shutdown; no events are fired.
     */
    void cleanup();

    /**
     * Marks whether an entity refuses to be carried.
     *
     * @param uuid     UUID of the entity
     * @param disabled {@code true} to prevent the entity from being carried
     */
    void setCarryDisabled(UUID uuid, boolean disabled);

    /**
     * Checks whether an entity refuses to be carried.
     *
     * @param uuid UUID of the entity
     * @return {@code true} if carrying is disabled for the entity
     */
    boolean isCarryDisabled(UUID uuid);
}
