package nms.impl.v1_21_R6;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

public class TeleportAdapterImpl implements nms.impl.TeleportImpl {
    @Override
    public void teleport(LivingEntity entity, Location loc) {
        CraftEntity _entity = (CraftEntity) entity;
        _entity.getHandle().b(loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch());
    }
}
