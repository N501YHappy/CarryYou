package nms.impl;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface TeleportImpl {

    void teleport(LivingEntity entity, Location loc);
}
