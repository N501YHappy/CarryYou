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


/**
 * Loads and exposes the plugin configuration as static fields.
 *
 * <p>Values are populated by {@link #load(Plugin)} during startup and may be
 * refreshed with {@link #reload()} after a configuration reload. Fields keep
 * their previous values when an optional section is absent from the file.
 */
public class ConfigLoader {

    /** Whether the plugin should check for updates on startup. */
    public static Boolean CHECK_UPDATE = true;

    /** Prefix prepended to all plugin messages. */
    public static String PREFIX = "&7[&aCarry&bYou&7] ";

    /** Clicks per second required to break free from a carry. */
    public static Double NEEDED_CPS = 6.0;

    /** Carry cooldown in milliseconds. */
    public static Integer COOLDOWN = 1000;

    /** Left delimiter of the break-free progress bar. */
    public static String PROGRESS_BAR_LEFT = "§7[";

    /** Right delimiter of the break-free progress bar. */
    public static String PROGRESS_BAR_RIGHT = "§7]";

    /** Character used for an empty progress-bar segment. */
    public static String PROGRESS_BAR_EMPTY = "§c♡";

    /** Character used for a filled progress-bar segment. */
    public static String PROGRESS_BAR_FILLED = "§c♥";

    /** Number of segments in the progress bar. */
    public static Integer PROGRESS_BAR_LENGTH = 20;

    /** Lower-cased world names where carrying is denied. */
    public static List<String> DENY_WORLDS = new ArrayList<>();

    /** Upper-cased entity type names that cannot be carried. */
    public static List<String> DENY_ENTITIES = new ArrayList<>();

    /** Throw power used when attacking while carrying. */
    public static Double THROW_POWER_ATTACK = 1.5;

    /** Throw power used when right-clicking while carrying. */
    public static Double THROW_POWER_INTERACT = 0.5;

    /** Whether carrying is triggered by shift+F instead of shift+right-click. */
    public static Boolean TRIGGER_SHIFT_F = false;

    /** Whether the chicken state effect is enabled. */
    public static Boolean WITH_CHICKEN = true;

    /** Whether carried creepers are prevented from charging. */
    public static Boolean WITH_CREEPER = true;

    /** Loaded language file, or {@code null} before the first load. */
    public static Configuration langConfig = null;

    /**
     * Loads the main config and language file into the static fields.
     *
     * <p>Saves the default resources when missing, falls back to {@code en_us}
     * for unknown locales, translates {@code &} color codes, and normalizes
     * world and entity name casing. Also pushes the configured cooldown to the
     * carry listener.
     *
     * @param plugin plugin whose data folder and config are used
     */
    public static void load(Plugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        PREFIX = translateColors(config.getString("prefix", "&7[&aCarry&bYou&7] "));
        String locales = config.getString("locales", "zh_cn");

        locales= locales.toLowerCase();

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
        }
        if (config.contains("fun")) {
            WITH_CHICKEN = config.getBoolean("fun.with_chicken", true);
            WITH_CREEPER = config.getBoolean("fun.with_creeper", true);
        }
    }
    /**
     * Reloads the configuration using the running plugin instance.
     *
     * @see #load(Plugin)
     */
    public static void reload() {
        load(CarryYou.getInstance());
    }

    /**
     * Translates {@code &}-style color codes to section-sign codes.
     *
     * @param text raw text, may be {@code null}
     * @return the translated text, or an empty string for {@code null}
     */
    private static String translateColors(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    /**
     * Checks whether a bundled language file exists for the locale.
     *
     * @param locale lower-case locale code
     * @return {@code true} for {@code en_us} or {@code zh_cn}
     */
    private static boolean isValidLocale(String locale){
        return locale.equals("en_us") || locale.equals("zh_cn");
    }
}
