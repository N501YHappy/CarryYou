package xyz.n501yhappy.carryyou.depends;

import org.bukkit.Bukkit;
import xyz.n501yhappy.carryyou.services.MessageService;

import java.util.function.Supplier;
import java.util.logging.Level;


public class DependsResolver {
    public static <T> T resolve(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            MessageService.getInstance().log(Level.INFO, "Plugin.plugin-link", pluginName);
            return whenEnabled.get();
        }
        return fallback.get();
    }

    public static <T> T resolveBeforeEnable(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().getPlugin(pluginName) != null) {
            MessageService.getInstance().log(Level.INFO, "Plugin.plugin-link", pluginName);
            return whenEnabled.get();
        }
        return fallback.get();
    }
}
