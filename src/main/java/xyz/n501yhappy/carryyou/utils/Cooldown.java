package xyz.n501yhappy.carryyou.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks a per-UUID cooldown in milliseconds.
 *
 * <p>{@link #checkCooldown(UUID)} only reads the current state; call
 * {@link #updateCooldown(UUID)} to start a new cooldown after a successful action.
 */
public class Cooldown {
    private final Map<UUID,Long> last_action = new ConcurrentHashMap<>();
    private int cooldown = 1000;

    /**
     * Creates a cooldown tracker.
     *
     * @param cooldown cooldown duration in milliseconds
     */
    public Cooldown(int cooldown){
        this.cooldown = cooldown;
    }

    /**
     * Changes the cooldown duration for future checks.
     *
     * @param cooldown cooldown duration in milliseconds
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Checks whether the UUID is allowed to act again.
     *
     * @param uuid UUID to check
     * @return {@code true} if no cooldown was recorded or the cooldown has elapsed
     */
    public boolean checkCooldown(UUID uuid){
        long now = System.currentTimeMillis();
        Long last = last_action.get(uuid);

        if (last == null) { // first use has no cooldown
            return true;
        }
        return (now - last >= cooldown);
    }

    /**
     * Starts (or restarts) the cooldown for a UUID.
     *
     * @param uuid UUID whose cooldown should be refreshed
     */
    public void updateCooldown(UUID uuid){
        long now = System.currentTimeMillis();
        last_action.put(uuid, now);
    }

    /**
     * Returns the remaining cooldown time.
     *
     * @param uuid UUID to query
     * @return remaining time in milliseconds, never negative; {@code 0} if ready
     */
    public Integer getRemains(UUID uuid){
        Long last = last_action.get(uuid);
        if (last == null) {
            return 0;
        }
        long remaining = cooldown - (System.currentTimeMillis() - last);
        return (int) Math.max(remaining, 0);
    }
}