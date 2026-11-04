package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.depends.dominion.DominionService;
import xyz.n501yhappy.carryyou.depends.residence.ResidenceService;
import xyz.n501yhappy.carryyou.depends.worldguard.WorldguardService;

/**
 * Common validation helpers used by the carry services.
 *
 * <p>The region-check methods delegate to the optional WorldGuard, Residence
 * and Dominion integrations. Each returns {@code true} when the plugin is
 * absent or the player is allowed to carry the target.
 */
public class Checkers {
    private static final WorldguardService worldguardService = WorldguardService.getInstance();
    private static final ResidenceService residenceService = ResidenceService.getInstance();
    private static final DominionService dominionService = DominionService.getInstance();

    /**
     * Checks the WorldGuard {@code carriable} flag at the target's location.
     *
     * @param target entity being carried
     * @param player player attempting the carry
     * @return {@code true} if no WorldGuard region denies the carry
     */
    public static boolean worldguard_check(Entity target, Player player) {
        return worldguardService.check(target, player);
    }

    /**
     * Checks the Residence {@code carriable} flag at the target's location.
     *
     * @param target entity being carried
     * @param player player attempting the carry
     * @return {@code true} if no Residence denies the carry
     */
    public static boolean residence_check(Entity target, Player player) {
        return residenceService.check(target, player);
    }

    /**
     * Checks the Dominion {@code carriable} flag at the target's location.
     *
     * @param target entity being carried
     * @param player player attempting the carry
     * @return {@code true} if no Dominion territory denies the carry
     */
    public static boolean dominion_check(Entity target, Player player) {
        return dominionService.check(target, player);
    }

    /**
     * Walks the passenger chain to detect whether {@code test} is already part
     * of it, which would create a mount cycle.
     *
     * @param bottom entity whose passenger tree is inspected
     * @param test   entity to search for
     * @return {@code true} if {@code test} is {@code bottom} or one of its
     *         passengers (recursively)
     */
    public static boolean hasMountCircle(Entity bottom,Entity test){
        if (bottom.equals(test)) return true;
        if (bottom.getPassengers().isEmpty()) return false;
        for (Entity entity : bottom.getPassengers()){
            if(hasMountCircle(entity,test)) return true;
        }
        return false;
    }
}
