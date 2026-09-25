package carryyou.nms.entitys;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BlockEntityImpl {
    public Player getPlayer();
    public ItemStack getItemStack();
    public Entity getEntity();
}
