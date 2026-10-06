package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.n501yhappy.carryyou.services.CarryService;

public class CarryProtection implements Listener {
    private final CarryService carryService = CarryService.getInstance();

    @EventHandler
    public void onSuffocation(EntityDamageEvent event){ //抱在墙里面窒息
        if (!carryService.isCarried(event.getEntity().getUniqueId())) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) event.setCancelled(true);
    }
}
