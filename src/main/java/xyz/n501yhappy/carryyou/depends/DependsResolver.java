package xyz.n501yhappy.carryyou.depends;

import org.bukkit.Bukkit;
import xyz.n501yhappy.carryyou.services.MessageService;
import xyz.n501yhappy.carryyou.utils.CarryLogger;

import java.util.function.Supplier;


public class DependsResolver {
    public static <T> T resolve(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            CarryLogger.info(MessageService.getInstance().getMessage("Plugin.plugin-link", pluginName));
            return whenEnabled.get();
        }
        return fallback.get();
    }

    public static <T> T resolveBeforeEnable(String pluginName, Supplier<T> whenEnabled, Supplier<T> fallback) {
        if (Bukkit.getPluginManager().getPlugin(pluginName) != null) {
            CarryLogger.info(MessageService.getInstance().getMessage("Plugin.plugin-link", pluginName));
            return whenEnabled.get();
        }
        return fallback.get();
    }
}
