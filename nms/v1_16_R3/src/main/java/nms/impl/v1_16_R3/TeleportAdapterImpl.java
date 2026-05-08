package nms.impl.v1_16_R3;

import net.minecraft.server.v1_16_R3.BlockPosition;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public class TeleportAdapterImpl implements nms.impl.TeleportImpl {
    @Override
    public void teleport(LivingEntity entity, Location loc) {
        CraftEntity _entity = (CraftEntity) entity;
        CraftWorld world = (CraftWorld) loc.getWorld();
        _entity.getHandle().teleportTo(
                world.getHandle(),
                new BlockPosition(loc.getX(),loc.getY(),loc.getZ())
        );
    }
}
