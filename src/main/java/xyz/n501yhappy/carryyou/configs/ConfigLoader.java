package xyz.n501yhappy.carryyou.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.n501yhappy.carryyou.CarryYou;

import java.util.ArrayList;
import java.util.List;


public class ConfigLoader {
    public static String PREFIX = "&7[&aCarry&bYou&7] ";
    public static Double NEEDED_CPS = 6.0;
    public static Integer COOLDOWN = 1000;
    public static String PROGRESS_BAR_LEFT = "§7[";
    public static String PROGRESS_BAR_RIGHT = "§7]";
    public static String PROGRESS_BAR_EMPTY = "§c♡";
    public static String PROGRESS_BAR_FILLED = "§c♥";
    public static Integer PROGRESS_BAR_LENGTH = 20;
    public static List<String> DENY_WORLDS = new ArrayList<>();
    public static List<String> DENY_ENTITIES = new ArrayList<>();
    public static Double THROW_POWER_DROP = 0.5;
    public static Double THROW_POWER_ATTACK = 0.9;
    public static Double THROW_POWER_INTERACT = 0.9;
    public static Boolean TRIGGER_SHIFT_F = true;
    public static Boolean TRIGGER_EMPTY = true;
    public static Boolean WITH_CHICKEN = true;
    public static Boolean WITH_CREEPER = true;

    public static void load() {
        CarryYou.instance.saveDefaultConfig();
        CarryYou.instance.reloadConfig();
        FileConfiguration config = CarryYou.instance.getConfig();
        MessageConfig.load(CarryYou.instance, CarryYou.instance.getLogger());

        PREFIX = translateColors(config.getString("prefix", "&7[&aCarry&bYou&7] "));
        NEEDED_CPS = config.getDouble("needed_cps", 6.0);
        COOLDOWN = (int) config.getDouble("cooldown", 1000);

        if (config.contains("progress_bar")) {
            PROGRESS_BAR_LEFT = translateColors(config.getString("progress_bar.left", "&7["));
            PROGRESS_BAR_RIGHT = translateColors(config.getString("progress_bar.right", "&7]"));
            PROGRESS_BAR_EMPTY = translateColors(config.getString("progress_bar.empty", "&c♡"));
            PROGRESS_BAR_FILLED = translateColors(config.getString("progress_bar.filled", "&c♥"));
            PROGRESS_BAR_LENGTH = config.getInt("progress_bar.length", 20);
        }
        DENY_WORLDS = config.getStringList("deny_worlds");
        for (int i = 0; i < DENY_WORLDS.size(); i++) {
            DENY_WORLDS.set(i,DENY_WORLDS.get(i).toLowerCase());
        }
        DENY_ENTITIES = config.getStringList("deny_entities");
        for (int i = 0; i < DENY_ENTITIES.size(); i++) {
            DENY_ENTITIES.set(i,DENY_ENTITIES.get(i).toUpperCase());
        }
        if (config.contains("throw_power")) {
            THROW_POWER_DROP = config.getDouble("throw_power.drop", 0.5);
            THROW_POWER_ATTACK = config.getDouble("throw_power.attack", 0.9);
            THROW_POWER_INTERACT = config.getDouble("throw_power.interact", 0.9);
        }
        if (config.contains("trigger")) {
            TRIGGER_SHIFT_F = config.getBoolean("trigger.shift_f_q", true);
            TRIGGER_EMPTY = config.getBoolean("trigger.empty", true);
        }
        if (config.contains("fun")) {
            WITH_CHICKEN = config.getBoolean("fun.with_chicken", true);
            WITH_CREEPER = config.getBoolean("fun.with_creeper", true);
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
