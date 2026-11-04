package carryyou.api.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired before a carried entity is released.
 *
 * <p>Only fired for drops requested with {@code callEvent = true}; forced
 * cleanup drops skip this event. Cancelling it prevents the release.
 */
public class PlayerDropEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final Entity carrier;
    private final Entity carried;


    /**
     * Creates the event.
     *
     * @param player the entity currently carrying the target
     * @param target the entity about to be released
     */
    public PlayerDropEvent(Entity player, Entity target){
        this.carrier = player;
        this.carried = target;
        this.cancelled = false;
    }

    /**
     * Returns the entity performing the drop.
     *
     * @return the carrier
     */
    public Entity getCarrier() {
        return carrier;
    }

    /**
     * Returns the entity being released.
     *
     * @return the carried entity
     */
    public Entity getCarried() {
        return carried;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns the handler list for this event type.
     *
     * @return the static handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
