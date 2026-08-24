package carryyou.api;

public final class CarryyouAPI {

    private static CarryManagerAPI carryManager;

    public static void registerCarryManager(CarryManagerAPI carryManager) {
        CarryyouAPI.carryManager = carryManager;
    }

    public static CarryManagerAPI getCarryManager() {
        return carryManager;
    }
}
