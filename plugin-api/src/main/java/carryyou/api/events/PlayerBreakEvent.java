package carryyou.api.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a carried entity completes the break-free click sequence.
 *
 * <p>Cancelling this event prevents the escape; the break-free progress is
 * reset and the entity remains carried.
 */
public class PlayerBreakEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final Entity carrier;
    private final Entity carried;


    /**
     * Creates the event.
     *
     * @param player the entity currently carrying the target
     * @param target the entity attempting to break free
     */
    public PlayerBreakEvent(Entity player, Entity target){
        this.carrier = player;
        this.carried = target;
        this.cancelled = false;
    }

    /**
     * Returns the entity whose carry is being escaped.
     *
     * @return the carrier
     */
    public Entity getCarrier() {
        return carrier;
    }

    /**
     * Returns the entity attempting to break free.
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
