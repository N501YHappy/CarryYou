package carryyou.api;

/**
 * Entry point for third-party plugins that integrate with CarryYou.
 *
 * <p>The plugin registers a {@link CarryManagerAPI} implementation during its
 * load phase. Consumers should wait until CarryYou is enabled before calling
 * {@link #getCarryManager()}.
 */
public final class CarryyouAPI {

    private static CarryManagerAPI carryManager;

    /**
     * Registers the carry manager implementation.
     *
     * <p>Called by CarryYou itself; external plugins normally do not call this.
     *
     * @param carryManager implementation to expose
     */
    public static void registerCarryManager(CarryManagerAPI carryManager) {
        CarryyouAPI.carryManager = carryManager;
    }

    /**
     * Returns the registered carry manager.
     *
     * @return the carry manager, or {@code null} if CarryYou has not registered one yet
     */
    public static CarryManagerAPI getCarryManager() {
        return carryManager;
    }
}
