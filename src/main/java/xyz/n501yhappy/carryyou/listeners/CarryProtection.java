package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.n501yhappy.carryyou.utils.CarryManager;

public class CarryProtection implements Listener {
    @EventHandler
    public void onSuffocation(EntityDamageEvent event){ //抱在墙里面窒息
        if (!CarryManager.isCarried(event.getEntity().getUniqueId())) return;
        event.setCancelled(true);
    }
}
