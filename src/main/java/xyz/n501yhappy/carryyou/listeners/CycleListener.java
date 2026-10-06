package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;
import xyz.n501yhappy.carryyou.services.CarryService;
import xyz.n501yhappy.carryyou.utils.Checkers;

public class CycleListener implements Listener {

    @EventHandler
    public void onMount(EntityMountEvent event){
        Entity want = event.getEntity();
        Entity target = event.getMount();
        if(CarryService.getInstance().isCarrying(want.getUniqueId()) && Checkers.hasMountCircle(target,want)){
            CarryService.getInstance().drop(target,0,false);
        }
    }
}
