package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import xyz.n501yhappy.carryyou.utils.CarryManager;

public class CreeperCharge implements Listener { //抱起时苦力怕无法充能
    @EventHandler
    public void onCharge(ExplosionPrimeEvent event){
        if (event.getEntity() != null && event.getEntity() instanceof Creeper){

            if (CarryManager.isCarried(event.getEntity().getUniqueId())) event.setCancelled(true);
        }
    }
}
