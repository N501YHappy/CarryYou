package xyz.n501yhappy.carryyou.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.n501yhappy.carryyou.events.PlayerCarryEvent;
import xyz.n501yhappy.carryyou.events.PlayerDropEvent;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.state.ChickenState;

public class PluginListener implements Listener {
    private final CarryManager carryManager = CarryManager.getInstance();
    private final ChickenState chickenState = ChickenState.getInstance();

    @EventHandler
    public void onCarry(PlayerCarryEvent event){
        chickenState.onCarry(event.getCarrier(), event.getCarried());
    }
    @EventHandler
    public void onDrop(PlayerDropEvent event){
        chickenState.onCarry(event.getCarrier(), event.getCarried());
    }
}
