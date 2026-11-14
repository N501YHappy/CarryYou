package xyz.n501yhappy.carryyou.services;

import carryyou.nms.entitys.BlockEntityImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import xyz.n501yhappy.carryyou.models.CarriedBlock;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores snapshots of blocks that are currently being carried.
 *
 * <p>Carried blocks are represented by a temporary entity (see the NMS module).
 * The snapshot is keyed by that entity's UUID so the original block data and
 * inventory contents can be restored when the block is placed again.
 *
 * <p>Use {@link #getInstance()} to access the shared instance.
 */
public class CarryBlockService {
    private static CarryBlockService instance;

    /**
     * Returns the singleton instance, creating it on first use.
     *
     * @return the shared {@code CarryBlockService} instance
     */
    public static CarryBlockService getInstance() {
        if (instance == null) instance = new CarryBlockService();
        return instance;
    }

    private CarryBlockService() {}

    private final Map<UUID, CarriedBlock> carriedBlocks = new ConcurrentHashMap<>();


    /**
     * Registers the snapshot for a carried block entity.
     *
     * @param entityId UUID of the entity representing the carried block
     * @param block    snapshot to store
     */
    public void register(UUID entityId, CarriedBlock block) {
        carriedBlocks.put(entityId, block);
    }

    /**
     * Returns the snapshot for a carried block entity.
     *
     * @param entityId UUID of the entity representing the carried block
     * @return the stored snapshot, or {@code null} if the entity is not a registered block
     */
    public CarriedBlock get(UUID entityId) {
        return carriedBlocks.get(entityId);
    }

    /**
     * Checks whether an entity is a registered carried block.
     *
     * @param entityId UUID of the entity
     * @return {@code true} if a snapshot exists for the entity
     */
    public boolean isCarriedBlock(UUID entityId) {
        return carriedBlocks.containsKey(entityId);
    }

    /**
     * Removes a carried block and its backing entity.
     *
     * <p><strong>Side effect:</strong> if the entity is still loaded, it is
     * removed from the world before its snapshot is discarded.
     *
     * @param entityId UUID of the entity representing the carried block
     * @return the removed snapshot, or {@code null} if none was registered
     */
    public CarriedBlock remove(UUID entityId) {
        Entity blockEntity = Bukkit.getEntity(entityId);
        if(blockEntity != null) blockEntity.remove();
        return carriedBlocks.remove(entityId);
    }

    /**
     * Drops snapshots whose backing entity is no longer loaded.
     */
    public void cleanInvalid() {
        carriedBlocks.keySet().removeIf(entityId -> Bukkit.getEntity(entityId) == null);
    }

    /**
     * Removes every carried block entity and clears all snapshots.
     *
     * <p>Intended for plugin shutdown.
     */
    public void cleanup() {
        for (UUID entityId : carriedBlocks.keySet()) {
            Entity entity = Bukkit.getEntity(entityId);
            if (entity != null) entity.remove();
        }
        carriedBlocks.clear();
    }
}
