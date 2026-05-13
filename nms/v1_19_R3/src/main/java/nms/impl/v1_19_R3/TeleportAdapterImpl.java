package nms.impl.v1_19_R3;

import net.minecraft.core.Position;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

public class TeleportAdapterImpl implements nms.impl.TeleportImpl {
    @Override
    public Boolean teleport(LivingEntity entity, Location loc) {
        CraftEntity _entity = (CraftEntity) entity;
        CraftWorld world = (CraftWorld) loc.getWorld();
        _entity.getHandle().teleportTo(
                world.getHandle(),
                new Position(loc.getX(),loc.getY(),loc.getZ())
        );
        return true;
    }
}
