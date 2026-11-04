package xyz.n501yhappy.carryyou.utils.methods;

import org.bukkit.entity.Entity;
import xyz.n501yhappy.carryyou.utils.CarryMethod;

/**
 * {@link CarryMethod} implementation that uses Bukkit's passenger API.
 *
 * <p>The carried entity becomes a passenger of the carrier, so the carrier's
 * existing passengers must be empty before a carry can start.
 */
public class RideMethod implements CarryMethod {
    /**
     * {@inheritDoc}
     *
     * @return {@code true} if the entity currently has no passengers
     */
    @Override
    public boolean isEmpty(Entity entity) {
        return entity.getPassengers().isEmpty();
    }

    /**
     * {@inheritDoc}
     *
     * @return the result of {@link Entity#addPassenger(Entity)}
     */
    @Override
    public boolean carryOn(Entity carrier, Entity carried) {
        return carrier.addPassenger(carried);
    }

    /**
     * {@inheritDoc}
     *
     * @return the result of {@link Entity#removePassenger(Entity)}
     */
    @Override
    public boolean drop(Entity carrier, Entity carried) {
        return carrier.removePassenger(carried);
    }
}
