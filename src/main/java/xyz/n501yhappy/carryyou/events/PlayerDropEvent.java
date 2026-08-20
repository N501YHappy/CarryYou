package xyz.n501yhappy.carryyou.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDropEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final Entity carrier;
    private final Entity carried;


    public PlayerDropEvent(Entity player, Entity target){
        this.carrier = player;
        this.carried = target;
        this.cancelled = false;
    }

    public Entity getCarrier() {
        return carrier;
    }

    public Entity getCarried() {
        return carried;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled =cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
