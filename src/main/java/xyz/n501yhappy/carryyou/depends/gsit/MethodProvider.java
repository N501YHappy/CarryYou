package xyz.n501yhappy.carryyou.depends.gsit;

import dev.geco.gsit.api.event.PrePlayerPlayerSitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.n501yhappy.carryyou.services.CarryService;

import java.util.UUID;


public class MethodProvider implements Listener {
    private final CarryService carryService = CarryService.getInstance();

    @EventHandler
    public void onSit(PrePlayerPlayerSitEvent event) {
        Player player = event.getPlayer();
        UUID player_uuid = player.getUniqueId();
        if (this.carryService.isCarried(player_uuid) || this.carryService.isCarrying(player_uuid)) {
            event.setCancelled(true);
        }
    }
}
