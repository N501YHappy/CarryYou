package xyz.n501yhappy.carryyou.utils;

public class Misc {
    private static Boolean isPaper;
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
