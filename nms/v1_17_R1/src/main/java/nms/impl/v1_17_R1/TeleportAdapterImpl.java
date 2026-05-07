package nms.impl.v1_17_R1;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

public class TeleportAdapterImpl implements nms.impl.TeleportImpl {
    @Override
    public void teleport(LivingEntity entity, Location loc) {
        CraftEntity _entity = (CraftEntity) entity;
        _entity.getHandle().setPositionRotation(loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch());
    }
}
