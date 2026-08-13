package xyz.n501yhappy.carryyou.depends;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.function.Supplier;
import java.util.logging.Logger;


public class DependsResolver {
    private static Logger logger;

    public static void setLogger(Logger logger2) {
        logger = logger2;
    }

    public static <T> T resolve(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            logger.info(ChatColor.AQUA + "找到 " + ChatColor.GREEN + pluginName);
            return whenEnabled.get();
        }
        return fallback.get();
    }

    public static <T> T resolveBeforeEnable(String className, String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        try {
            Class.forName(className);
            logger.info(ChatColor.AQUA + "找到 " + ChatColor.GREEN + pluginName);
            return whenEnabled.get();
        } catch (ClassNotFoundException e) {
            return fallback.get();
        }
    }
}
