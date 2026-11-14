package xyz.n501yhappy.carryyou.listeners;

import carryyou.nms.entitys.BlockEntityFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.models.CarriedBlock;
import xyz.n501yhappy.carryyou.services.CarryBlockService;
import xyz.n501yhappy.carryyou.services.CarryService;

public class PickCarriedBlock implements Listener {
    private CarryService carryService = CarryService.getInstance();
    private CarryBlockService carryBlockService = CarryBlockService.getInstance();
    @EventHandler
    public void onPick(EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Item itemEntity = event.getItem();
        if (!(itemEntity.getPersistentDataContainer().has(CarryYou.CARRIED_BLOCK_KEY, PersistentDataType.STRING))) return;
        event.setCancelled(true);

        if (carryService.isCarrying(player.getUniqueId())) return;

        ItemStack itemStack = itemEntity.getItemStack();

        Entity blockEntity = BlockEntityFactory.getInstance().create(player,itemStack).get(player.getLocation());
        CarriedBlock carriedBlock = new CarriedBlock(player.getUniqueId(),itemStack.getType(), ((BlockStateMeta) itemStack.getItemMeta()).getBlockState());

        carryService.carry(player,blockEntity);
        carryBlockService.register(blockEntity.getUniqueId(),carriedBlock);

        itemEntity.remove();
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent event){
        Item item = event.getItem();
        if (item.getPersistentDataContainer().has(CarryYou.CARRIED_BLOCK_KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent event){
        if (event.getEntity().getPersistentDataContainer().has(CarryYou.CARRIED_BLOCK_KEY, PersistentDataType.STRING)
                || event.getTarget().getPersistentDataContainer().has(CarryYou.CARRIED_BLOCK_KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event){
        if (event.getEntity().getPersistentDataContainer().has(CarryYou.CARRIED_BLOCK_KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }
}
