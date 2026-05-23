package adapts.impl;


import java.util.logging.Logger;

public class Version {
    private static schedulerAdapts adapts;
    private static Boolean isFolia = true;

    public static schedulerAdapts getAdapts() {
        return adapts;
    }

    public static void init(Logger logger) throws ReflectiveOperationException {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
        logger.info("§aCarryYou is running on §b" + (isFolia ? "folia" : "bukkit") + "§r");
        String pkg = Version.class.getPackage().getName() + "." + (isFolia ? "folia" : "bukkit");
        adapts = newInstance(pkg, "schedulerImpl");
    }
    private static <T> T newInstance(String pkg, String c) throws ReflectiveOperationException {
        Class<?> type = Class.forName(pkg + "." + c);
        return newInstance(type);
    }

    private static <T> T newInstance(Class<?> type) throws ReflectiveOperationException {
        return (T) type.getDeclaredConstructor().newInstance();
    }
}
