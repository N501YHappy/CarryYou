package xyz.n501yhappy.carryyou;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;


public class ConfigLoader {
    public static double NEEDED_CPS = 6.0;
    public static String PROGRESS_BAR_LEFT = "§7[";
    public static String PROGRESS_BAR_RIGHT = "§7]";
    public static String PROGRESS_BAR_EMPTY = "§c♡";
    public static String PROGRESS_BAR_FILLED = "§c♥";
    public static int PROGRESS_BAR_LENGTH = 20;


    public static void load() {
        CarryYou.instance.saveDefaultConfig();
        CarryYou.instance.reloadConfig();
        FileConfiguration config = CarryYou.instance.getConfig();

        NEEDED_CPS = config.getDouble("needed_cps", 6.0);

        if (config.contains("progress_bar")) {
            PROGRESS_BAR_LEFT = translateColors(config.getString("progress_bar.left", "&7["));
            PROGRESS_BAR_RIGHT = translateColors(config.getString("progress_bar.right", "&7]"));
            PROGRESS_BAR_EMPTY = translateColors(config.getString("progress_bar.empty", "&c♡"));
            PROGRESS_BAR_FILLED = translateColors(config.getString("progress_bar.filled", "&c♥"));
            PROGRESS_BAR_LENGTH = config.getInt("progress_bar.length", 20);
        }
    }
    public static void reload() {
        load();
    }
    private static String translateColors(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
