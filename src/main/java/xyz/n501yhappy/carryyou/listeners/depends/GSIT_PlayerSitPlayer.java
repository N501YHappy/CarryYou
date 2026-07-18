package xyz.n501yhappy.carryyou.listeners.depends;

import dev.geco.gsit.api.event.PrePlayerPlayerSitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.UUID;

public class GSIT_PlayerSitPlayer implements Listener {
    @EventHandler
    public void onSit(PrePlayerPlayerSitEvent event){
        Player player = event.getPlayer();
        UUID player_uuid = player.getUniqueId();
        if(CarryManager.isCarried(player_uuid)) event.setCancelled(true);

    }
}
