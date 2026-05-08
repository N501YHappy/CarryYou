package nms.impl.v1_21_R6;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity;
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
                loc.getX(), loc.getY(), loc.getZ(),
                Set.of(),
                loc.getYaw(), loc.getPitch(),
                false,
                PlayerTeleportEvent.TeleportCause.PLUGIN
        );
    }
}
