package xyz.n501yhappy.carryyou.models;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.UUID;
/**
 * Snapshot of a carried block.
 *
 * @param owner     UUID of the player who picked the block up
 * @param material  the material of Block
 * @param blockState  the BlockState of Block
 */
public record CarriedBlock(UUID owner, Material material, BlockState blockState) {
    /**
     * Convenience constructor that creates a {@code CarriedBlock} from a {@link Block}.
     * <p>
     * The instance is created using the UUID of the player owning the block,
     * the block's {@link Material} type, and the block's {@link BlockState}.
     *
     * @param player the UUID of the player owning the block; must not be {@code null}
     * @param block  the block to be carried; must not be {@code null}
     * @see #CarriedBlock(UUID, Material, BlockState)
     */
    public CarriedBlock(UUID player, Block block){
        this(player,block.getType(),block.getState());
    }
}