package xyz.n501yhappy.carryyou.services;

import carryyou.nms.entitys.BlockEntityImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarryBlockService {
    private static CarryBlockService instance;

    public static CarryBlockService getInstance() {
        if (instance == null) instance = new CarryBlockService();
        return instance;
    }

    private CarryBlockService() {}

    private final Map<UUID, CarriedBlock> carriedBlocks = new ConcurrentHashMap<>();

    public record CarriedBlock(UUID owner, BlockData blockData, ItemStack[] contents) {}

    public void register(UUID entityId, CarriedBlock block) {
        carriedBlocks.put(entityId, block);
    }

    public CarriedBlock get(UUID entityId) {
        return carriedBlocks.get(entityId);
    }

    public boolean isCarriedBlock(UUID entityId) {
        return carriedBlocks.containsKey(entityId);
    }

    public CarriedBlock remove(UUID entityId) {
        Entity blockEntity = Bukkit.getEntity(entityId);
        if(blockEntity != null) blockEntity.remove();
        return carriedBlocks.remove(entityId);
    }

    public void cleanInvalid() {
        carriedBlocks.keySet().removeIf(entityId -> Bukkit.getEntity(entityId) == null);
    }

    public void cleanup() {
        for (UUID entityId : carriedBlocks.keySet()) {
            Entity entity = Bukkit.getEntity(entityId);
            if (entity != null) entity.remove();
        }
        carriedBlocks.clear();
    }
}
