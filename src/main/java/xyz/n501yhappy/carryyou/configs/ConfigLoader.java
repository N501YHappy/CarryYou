package xyz.n501yhappy.carryyou.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.services.MessageService;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class ConfigLoader {
    private MessageService messageService = MessageService.getInstance();

    public static Boolean CHECK_UPDATE = true;
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
    public static Double THROW_POWER_ATTACK = 1.5;
    public static Double THROW_POWER_INTERACT = 0.5;
    public static Boolean TRIGGER_SHIFT_F = false;
    public static Boolean TRIGGER_EMPTY = false;
    public static Boolean WITH_CHICKEN = true;
    public static Boolean WITH_CREEPER = true;

    public static Configuration langConfig = null;

    public static void load(Plugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        PREFIX = translateColors(config.getString("prefix", "&7[&aCarry&bYou&7] "));
        String locales = config.getString("locales", "zh_cn");

        if(!isValidLocale(locales)){
            plugin.getLogger().log(Level.WARNING,"The \"" + locales + "\" language file was not found; it has been replaced with en_us.");
            locales = "en_us";
        }
        File lang_file = new File(plugin.getDataFolder().getPath() + "/langs/" + locales + ".yml");
        if (!lang_file.exists()){
            plugin.saveResource("langs/" + locales + ".yml",true);
        }
        MessageService.getInstance().setLangConfig(YamlConfiguration.loadConfiguration(lang_file));

        CHECK_UPDATE = config.getBoolean("check_update", true);
        NEEDED_CPS = config.getDouble("needed_cps", 6.0);
        COOLDOWN = (int) config.getDouble("cooldown", 1000);
        CarryListener.setCarryCooldown(COOLDOWN);

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
            THROW_POWER_ATTACK = config.getDouble("throw_power.attack", 0.9);
            THROW_POWER_INTERACT = config.getDouble("throw_power.interact", 0.9);
        }
        if (config.contains("trigger")) {
            TRIGGER_SHIFT_F = config.getBoolean("trigger.shift_f", true);
            TRIGGER_EMPTY = config.getBoolean("trigger.empty", false);
        }
        if (config.contains("fun")) {
            WITH_CHICKEN = config.getBoolean("fun.with_chicken", true);
            WITH_CREEPER = config.getBoolean("fun.with_creeper", true);
        }
    }
    public static void reload() {
        load(CarryYou.getInstance());
    }
    private static String translateColors(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    private static boolean isValidLocale(String locale){
        return locale.equals("en_us") || locale.equals("zh_cn");
    }
}
