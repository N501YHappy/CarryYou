package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.Checkers;

public class CycleListener implements Listener {
    public void onMount(EntityMountEvent event){
        Entity want = event.getEntity();
        Entity target = event.getMount();
        if(CarryManager.getInstance().isCarrying(want.getUniqueId()) && Checkers.hasMountCircle(target,want)){
            CarryManager.getInstance().drop(target,0);
        }
    }
}
