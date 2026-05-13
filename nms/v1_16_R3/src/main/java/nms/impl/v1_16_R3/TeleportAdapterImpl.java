package nms.impl.v1_16_R3;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

public class TeleportAdapterImpl implements nms.impl.TeleportImpl {
    @Override
    public Boolean teleport(LivingEntity entity, Location loc) {
        Entity _entity = ((CraftEntity) entity).getHandle();
        if (!loc.getWorld().equals(entity.getWorld())) {
            _entity.teleportTo(((CraftWorld)loc.getWorld()).getHandle(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
            return true;
        } else {
            _entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            _entity.setHeadRotation(loc.getYaw());
            return true;
        }
    }
}
