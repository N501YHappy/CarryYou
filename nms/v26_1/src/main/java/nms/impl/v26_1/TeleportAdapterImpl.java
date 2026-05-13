package nms.impl.v26_1;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Set;

public class TeleportAdapterImpl implements nms.impl.TeleportImpl {
    @Override
    public Boolean teleport(LivingEntity entity, Location loc) {
        CraftEntity _entity = (CraftEntity) entity;
        CraftWorld world = (CraftWorld) loc.getWorld();
        _entity.getHandle().teleportTo(
                world.getHandle(),
                loc.getX(), loc.getY(), loc.getZ(),
                Set.of(),
                loc.getYaw(), loc.getPitch(),
                false,
                PlayerTeleportEvent.TeleportCause.PLUGIN
        );
        return true;
    }
}
