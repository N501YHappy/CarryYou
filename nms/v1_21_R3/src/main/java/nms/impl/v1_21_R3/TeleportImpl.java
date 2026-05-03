package nms.impl.v1_21_R3;

import net.minecraft.world.entity.EntityLiving;
import org.bukkit.entity.LivingEntity;

public class TeleportImpl implements nms.impl.TeleportImpl {
    @Override
    public void teleport(LivingEntity entity, double x, double y, double z) {
        EntityLiving _entity = (EntityLiving) entity;
        _entity.b(x,y,z,false);
    }
}
