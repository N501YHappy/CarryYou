package nms.impl;

import org.bukkit.entity.LivingEntity;

public interface TeleportImpl {

    void teleport(LivingEntity entity, double x, double y, double z);
}
