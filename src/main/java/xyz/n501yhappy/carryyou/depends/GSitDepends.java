package xyz.n501yhappy.carryyou.depends;
import org.bukkit.Bukkit;
import xyz.n501yhappy.carryyou.CarryYou;

public class GSitDepends {
    public static void load() {
        CarryYou.gsit_enable = CarryYou.instance.getServer().getPluginManager().isPluginEnabled("GSit");
        if (!CarryYou.gsit_enable) return;
        if (!Bukkit.getPluginManager().isPluginEnabled("GSit")) {
            CarryYou.instance.getLogger().warning("GSit plugin is not enabled. Please ensure it is installed and enabled.");
            CarryYou.gsit_enable = false;
            return;
        }
    }
}
