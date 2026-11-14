package xyz.n501yhappy.carryyou.services;

import carryyou.api.CarryManagerAPI;
import carryyou.api.events.PlayerCarryEvent;
import carryyou.api.events.PlayerDropEvent;
import dev.geco.gsit.metric.BStatsMetric;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.models.CarriedBlock;
import xyz.n501yhappy.carryyou.utils.BlockSerializer;
import xyz.n501yhappy.carryyou.utils.CarryMethod;
import xyz.n501yhappy.carryyou.utils.Checkers;
import xyz.n501yhappy.carryyou.utils.Cooldown;
import xyz.n501yhappy.carryyou.utils.methods.RideMethod;
import xyz.n501yhappy.carryyou.utils.state.ChickenState;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central service that manages carry relationships between entities.
 *
 * <p>A relationship consists of a <em>carrier</em> (the entity holding another
 * entity) and a <em>target</em> (the entity being carried). Both directions are
 * kept in sync through {@code carryMapping} (carrier to target) and
 * {@code mappingCarry} (target to carrier). The actual mount/dismount work is
 * delegated to a {@link CarryMethod}.
 *
 * <p>Use {@link #getInstance()} to obtain the singleton instance. This class
 * also implements {@link CarryManagerAPI} for third-party plugins.
 */
public class CarryService implements CarryManagerAPI {
    public static CarryService instance;

    private final CarryMethod carryMethod = new RideMethod();

    private final CarryBlockService carryBlockService = CarryBlockService.getInstance();

    /**
     * Returns the singleton instance, creating it on first use.
     *
     * @return the shared {@code CarryService} instance
     */
    public static CarryService getInstance() {
        if(instance == null) instance = new CarryService();
        return instance;
    }

    private final Map<UUID,UUID> carryMapping = new ConcurrentHashMap<>(); // carrier -> target
    private final Map<UUID,UUID> mappingCarry = new ConcurrentHashMap<>(); // target -> carrier (reverse lookup)
    private final Map<UUID,Boolean> carryDisabled = new ConcurrentHashMap<>(); // true = this entity refuses to be carried

    /**
     * Attempts to make {@code carrier} carry {@code target}.
     *
     * <p>The attempt fails if the carrier already carries a live entity, already
     * has a passenger, would form a mount cycle, or if the cancellable
     * {@link PlayerCarryEvent} is cancelled. On success both mapping directions
     * are registered.
     *
     * @param carrier the entity that will hold the target
     * @param target  the entity to be carried
     * @return {@code true} if the carry relationship was established
     */
    public boolean carry(Entity carrier, Entity target) {
        UUID carrierUUID = carrier.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if (carryMapping.containsKey(carrierUUID)) {
            UUID oldTarget = carryMapping.get(carrierUUID);
            if (Bukkit.getEntity(oldTarget) == null) {
                remove(carrierUUID, oldTarget); // the previous target is gone; clean up stale data
            } else {
                return false;
            }
        }
        if (!carryMethod.isEmpty(carrier)) return false; // an entity is already riding the carrier

        if (carryMethod instanceof RideMethod){
            if (Checkers.hasMountCircle(carrier,target)) return false;
        }

        PlayerCarryEvent event = new PlayerCarryEvent(carrier,target);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        if(carryMethod.carryOn(carrier,target)){
            put(carrierUUID, targetUUID);
            return true;
        }
        return false;
    }

    /**
     * Force-releases the carried {@code target} and launches it along the
     * carrier's view direction. Equivalent to {@code drop(target, power, true)},
     * bypassing the {@link PlayerDropEvent} check
     *
     * @param target    the carried entity to release
     * @param power     multiplier applied to the calculated throw vector; {@code 0}
     *                  releases the target without launching it
     * @return {@code true} if the target was released; {@code false} if it was
     *         not carried, its carrier no longer exists, or the event was cancelled
     */

    public boolean dropForce(Entity target, double power){
        return drop(target,power,true);
    }

    /**
     * Releases the entity currently carrying {@code target} and launches the
     * target using the carrier's view direction.
     *
     * @param target    the carried entity to release
     * @param power     multiplier applied to the calculated throw vector; {@code 0}
     *                  releases the target without launching it
     * @param isForce   whether to force the trigger: {@code true} forces execution, {@code false} follows the regular (conditional) trigger logic.
     * @return {@code true} if the target was released; {@code false} if it was
     *         not carried, its carrier no longer exists, or the event was cancelled
     */
    public boolean drop(Entity target, double power,boolean isForce) {
        UUID targetUUID = target.getUniqueId();
        if (!mappingCarry.containsKey(targetUUID)) return false;
        UUID carrierUUID = getCarrierByTarget(targetUUID);
        Entity carrier = carrierUUID != null ? Bukkit.getEntity(carrierUUID) : null;

        if (carrier == null) {
            remove(carrierUUID, targetUUID);
            carryBlockService.remove(targetUUID);
            ChickenState.getInstance().remove(targetUUID);
            return false;
        }

        if (!isForce){
            PlayerDropEvent event = new PlayerDropEvent(carrier,target);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return false;
        }

        carryMethod.drop(carrier,target);
        remove(carrierUUID, targetUUID);

        if (carryBlockService.isCarriedBlock(targetUUID)){
            if (isForce){
                CarriedBlock carriedBlock = carryBlockService.get(targetUUID);
                ItemStack itemStack = new ItemStack(carriedBlock.material());
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta instanceof BlockStateMeta){
                    ((BlockStateMeta) itemMeta).setBlockState(carriedBlock.blockState());
                }
                itemStack.setItemMeta(itemMeta);
                String serialized = BlockSerializer.serializer(itemStack);
                Location location = target.getLocation();
                Item itemEntity = location.getWorld().dropItem(location,itemStack);
                itemEntity.getPersistentDataContainer().set(CarryYou.CARRIED_BLOCK_KEY, PersistentDataType.STRING,serialized);
                itemEntity.setInvulnerable(true);
            }
            carryBlockService.remove(targetUUID);
        }else {
            Vector vec = calcVector(carrier.getVelocity(), carrier.getLocation(),power);
            target.setVelocity(vec);
            ChickenState.getInstance().onDrop(carrier,target);
        }


        return true;
    }
    /**
     * Registers both directions of a carry relationship.
     *
     * <p>Callers must keep both maps consistent; prefer {@link #carry(Entity, Entity)}
     * unless maintaining the mapping manually.
     *
     * @param carrierUUID UUID of the carrying entity
     * @param targetUUID  UUID of the carried entity
     */
    public void put(UUID carrierUUID, UUID targetUUID) {
        carryMapping.put(carrierUUID, targetUUID);
        mappingCarry.put(targetUUID, carrierUUID);
    }

    /**
     * Removes both directions of a carry relationship.
     *
     * <p>Uses conditional map removal so a newer relationship created with the
     * same UUIDs is not accidentally deleted.
     *
     * @param carrierUUID UUID of the carrying entity
     * @param targetUUID  UUID of the carried entity
     */
    public void remove(UUID carrierUUID, UUID targetUUID) {
        carryMapping.remove(carrierUUID, targetUUID);
        mappingCarry.remove(targetUUID, carrierUUID);
    }

    /**
     * Returns the UUID of the entity carried by {@code carrierUUID}.
     *
     * @param carrierUUID UUID of the carrier
     * @return the carried entity's UUID, or {@code null} if the carrier carries nothing
     */
    public UUID getTargetByCarrier(UUID carrierUUID) {
        return carryMapping.get(carrierUUID);
    }


    /**
     * Returns the UUID of the entity carrying {@code targetUUID}.
     *
     * @param targetUUID UUID of the carried entity
     * @return the carrier's UUID, or {@code null} if the entity is not carried
     */
    public UUID getCarrierByTarget(UUID targetUUID) {
        return mappingCarry.get(targetUUID);
    }

    /**
     * Resolves the carried entity to a live Bukkit {@link Entity}.
     *
     * @param carrierUUID UUID of the carrier
     * @return the carried entity, or {@code null} if there is no relationship
     *         or the entity is no longer loaded
     */
    public Entity getTargetEntityByCarrier(UUID carrierUUID) {
        UUID targetUUID = getTargetByCarrier(carrierUUID);
        return targetUUID != null ? Bukkit.getEntity(targetUUID) : null;
    }

    /**
     * Resolves the carrying entity to a live Bukkit {@link Entity}.
     *
     * @param targetUUID UUID of the carried entity
     * @return the carrier entity, or {@code null} if there is no relationship
     *         or the entity is no longer loaded
     */
    public Entity getCarrierEntityByTarget(UUID targetUUID) {
        UUID carrierUUID = getCarrierByTarget(targetUUID);
        return carrierUUID != null ? Bukkit.getEntity(carrierUUID) : null;
    }

    /**
     * Checks whether {@code carrierUUID} is currently carrying another entity.
     *
     * @param carrierUUID UUID of the potential carrier
     * @return {@code true} if a carry relationship exists for this carrier
     */
    public boolean isCarrying(UUID carrierUUID) {
        return carryMapping.containsKey(carrierUUID);
    }

    /**
     * Checks whether {@code targetUUID} is currently being carried.
     *
     * @param targetUUID UUID of the potential target
     * @return {@code true} if a carry relationship exists for this target
     */
    public boolean isCarried(UUID targetUUID) {
        return mappingCarry.containsKey(targetUUID);
    }

    /**
     * Checks whether the entity is carrying a block-backed entity.
     *
     * @param carrierUUID UUID of the carrier
     * @return {@code true} if the carrier's target is a registered carried block
     */
    public boolean isCarryingBlock(UUID carrierUUID) {
        if(!isCarrying(carrierUUID)) return false;
        return carryBlockService.isCarriedBlock(getTargetByCarrier(carrierUUID));
    }

    /**
     * Checks whether the entity is a carried block.
     *
     * @param targetUUID UUID of the carried entity
     * @return {@code true} if the entity is carried and registered as a block
     */
    public boolean isCarriedBlock(UUID targetUUID) {
        if(!isCarried(targetUUID)) return false;
        return carryBlockService.isCarriedBlock(targetUUID);
    }

    /**
     * Releases all active carry relationships and clears the mapping tables.
     *
     * <p>Intended for plugin shutdown; no events are fired.
     */
    public void cleanup() {
        for (Map.Entry<UUID, UUID> entry : carryMapping.entrySet()) {
            Entity carrier = Bukkit.getEntity(entry.getKey());
            Entity target = Bukkit.getEntity(entry.getValue());
            if (carrier != null && target != null && !carryMethod.isEmpty(carrier)) {
                carryMethod.drop(carrier,target);
            }
        }
        carryMapping.clear();
        mappingCarry.clear();
    }

    /**
     * Marks whether an entity refuses to be carried.
     *
     * @param uuid     UUID of the entity
     * @param disabled {@code true} to prevent the entity from being carried
     */
    public void setCarryDisabled(UUID uuid, boolean disabled) {
        carryDisabled.put(uuid, disabled);
    }

    /**
     * Checks whether an entity refuses to be carried.
     *
     * @param uuid UUID of the entity
     * @return {@code true} if carrying is disabled for the entity
     */
    public boolean isCarryDisabled(UUID uuid) {
        return carryDisabled.getOrDefault(uuid, false);
    }

    /**
     * Runs all pre-carry checks and notifies the player when one fails.
     *
     * <p>Checks, in order: target opt-out, cooldown, denied worlds, permission,
     * WorldGuard/Residence/Dominion flags, denied entity types, and the target
     * player's own opt-out. Operators bypass every check except the target
     * opt-out check that runs before the operator test.
     *
     * @param player   the player attempting the carry
     * @param target   the entity to be carried
     * @param cooldown cooldown tracker to query for the player
     * @return {@code true} if the carry may proceed
     */
    public boolean checkCarry(Player player, Entity target, Cooldown cooldown) {
        if (isCarryDisabled(target.getUniqueId())) return false;

        if(!cooldown.checkCooldown(player.getUniqueId()) && !player.isOp()){
            double remainingSeconds = cooldown.getRemains(player.getUniqueId()) / 1000.0;
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.cooldown", String.format("%.2f", remainingSeconds)));
            return false;
        }
        if (ConfigLoader.DENY_WORLDS.contains(player.getWorld().getName()) && !player.isOp()) {
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.world-deny"));
            return false;
        }

        if (!player.hasPermission("carryyou.can") && !player.isOp()) {
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.permission-deny"));
            return false;
        }

        if (!Checkers.worldguard_check(target, player) && !player.isOp()) {
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.arean-deny"));
            return false;
        }

        if (!Checkers.residence_check(target, player) && !player.isOp()) {
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.arean-deny"));
            return false;
        }

        if (!Checkers.dominion_check(target, player) && !player.isOp()) {
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.arean-deny"));
            return false;
        }

        if (ConfigLoader.DENY_ENTITIES.contains(target.getType().name()) && !player.isOp()) {
            NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.entity-deny"));
            return false;
        }

        if (target instanceof Player targetP) {
            if (targetP.hasPermission("carryyou.uncarried") && !player.isOp()) {
                NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.player-uncarried"));
                return false;
            }
            if (isCarryDisabled(targetP.getUniqueId())  && !player.isOp()) {
                NotificationService.getInstance().warn(player, MessageService.getInstance().getMessage("Carry.player-uncarried"));
                return false;
            }
        }

        return true;
    }

    /**
     * Builds a throw vector from the carrier's view direction.
     *
     * <p>Minecraft yaw is clockwise from south and differs from the standard
     * mathematical coordinate system, so yaw is negated. Pitch is the vertical
     * head rotation. Any existing velocity is added to the result afterwards.
     *
     * @param speed base velocity to add to the throw vector, may be {@code null}
     * @param loc   location whose yaw and pitch define the throw direction
     * @param power multiplier applied to the throw vector
     * @return the resulting velocity
     */
    private Vector calcVector(Vector speed, Location loc,double power){
        double phi = Math.toRadians(90 + loc.getPitch()); // 90 - (-pitch)
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
