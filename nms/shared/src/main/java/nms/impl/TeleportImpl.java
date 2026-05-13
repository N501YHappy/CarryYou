package nms.impl;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface TeleportImpl {

    Boolean teleport(LivingEntity entity, Location loc);
}
