package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.utils.CarryManager;

public class CreeperCharge implements Listener { //抱起时苦力怕无法充能
    private final CarryManager carryManager = CarryManager.getInstance();

    @EventHandler
    public void onCharge(ExplosionPrimeEvent event){
        if (!ConfigLoader.WITH_CREEPER) return;
        if (event.getEntity() != null && event.getEntity() instanceof Creeper){

            if (carryManager.isCarried(event.getEntity().getUniqueId())) event.setCancelled(true);
        }
    }
}
