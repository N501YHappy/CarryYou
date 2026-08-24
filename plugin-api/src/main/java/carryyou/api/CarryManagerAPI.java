package carryyou.api;

import org.bukkit.entity.Entity;

import java.util.UUID;

public interface CarryManagerAPI {
    boolean carry(Entity carrier, Entity target);

    boolean drop(Entity target, double power, boolean callEvent);

    void put(UUID carrierUUID, UUID targetUUID);

    void remove(UUID carrierUUID, UUID targetUUID);

    UUID getTargetByCarrier(UUID carrierUUID);

    UUID getCarrierByTarget(UUID targetUUID);

    Entity getTargetEntityByCarrier(UUID carrierUUID);

    Entity getCarrierEntityByTarget(UUID targetUUID);

    boolean isCarrying(UUID carrierUUID);

    boolean isCarried(UUID targetUUID);

    void cleanup();

    void setCarryDisabled(UUID uuid, boolean disabled);

    boolean isCarryDisabled(UUID uuid);
}
