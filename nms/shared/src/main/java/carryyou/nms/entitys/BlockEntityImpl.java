package carryyou.nms.entitys;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface BlockEntityImpl {
    public UUID getUniqueId();
    public Player getPlayer();
    public ItemStack getItemStack();
    public Entity get(Location location);
}
