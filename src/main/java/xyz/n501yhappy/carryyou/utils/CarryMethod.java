package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;

/**
 * Strategy for physically attaching a carried entity to its carrier.
 *
 * <p>Implementations decide how the carry is represented (for example, riding
 * the carrier or being attached through packets). {@link xyz.n501yhappy.carryyou.services.CarryService}
 * owns the bookkeeping and delegates the actual mount/dismount operations here.
 */
public interface CarryMethod {
    /**
     * Checks whether an entity can accept a new carried entity.
     *
     * @param entity carrier candidate
     * @return {@code true} if the entity has no passenger
     */
    boolean isEmpty(Entity entity);

    /**
     * Attaches {@code carried} to {@code carrier}.
     *
     * @param carrier entity that will hold the other entity
     * @param carried entity to attach
     * @return {@code true} if the attachment succeeded
     */
    boolean carryOn(Entity carrier,Entity carried);

    /**
     * Detaches {@code carried} from {@code carrier}.
     *
     * @param carrier entity currently holding the other entity
     * @param carried entity to detach
     * @return {@code true} if the detachment succeeded
     */
    boolean drop(Entity carrier,Entity carried);
}
