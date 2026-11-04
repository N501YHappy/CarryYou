package xyz.n501yhappy.carryyou.utils;

/**
 * Small platform-detection helpers.
 */
public class Misc {
    private static Boolean isPaper;

    /**
     * Detects whether the server runs Paper (or a Paper fork with Folia
     * scheduling classes). The result is cached after the first call.
     *
     * @return {@code true} if Paper-specific scheduling APIs are available
     */
    public static boolean isPaper(){
        if (isPaper != null) return isPaper;
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            isPaper = true;
        } catch (ClassNotFoundException e) {
            isPaper = false;
        }
        return isPaper;
    }
}
