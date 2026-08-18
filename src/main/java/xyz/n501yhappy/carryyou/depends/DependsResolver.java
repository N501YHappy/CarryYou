package xyz.n501yhappy.carryyou.depends;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.n501yhappy.carryyou.locales.MessageInfo;

import java.util.function.Supplier;
import java.util.logging.Logger;


public class DependsResolver {
    private static Logger logger;

    public static void setLogger(Logger logger2) {
        logger = logger2;
    }

    public static <T> T resolve(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            logger.info(MessageInfo.current().findSoftDepends() + " " + ChatColor.GREEN + pluginName);
            return whenEnabled.get();
        }
        return fallback.get();
    }

    public static <T> T resolveBeforeEnable(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().getPlugin(pluginName) != null) {
            logger.info(MessageInfo.current().findSoftDepends() + " " + ChatColor.GREEN + pluginName);
            return whenEnabled.get();
        }
        return fallback.get();
    }
}
