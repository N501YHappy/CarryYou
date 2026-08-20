package xyz.n501yhappy.carryyou.depends.gsit;

import dev.geco.gsit.api.event.PrePlayerPlayerSitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.UUID;


public class MethodProvider implements Listener {
    private final CarryManager carryManager = CarryManager.getInstance();

    @EventHandler
    public void onSit(PrePlayerPlayerSitEvent event) {
        Player player = event.getPlayer();
        UUID player_uuid = player.getUniqueId();
        if (this.carryManager.isCarried(player_uuid) || this.carryManager.isCarrying(player_uuid)) {
            event.setCancelled(true);
        }
    }
}
