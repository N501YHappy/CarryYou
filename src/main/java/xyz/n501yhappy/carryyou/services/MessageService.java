package xyz.n501yhappy.carryyou.services;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;

import java.util.logging.Level;

/**
 * Resolves localized messages and translates color codes.
 *
 * <p>Messages are read from the language configuration set by
 * {@link #setLangConfig(Configuration)}. Missing keys fall back to the key
 * itself, which makes untranslated messages visible instead of throwing.
 *
 * <p>Use {@link #getInstance()} to access the shared instance.
 */
public class MessageService {
    private static MessageService instance = null;
    private Configuration langConfig;


    private MessageService(){}

    /**
     * Returns the singleton instance, creating it on first use.
     *
     * @return the shared {@code MessageService} instance
     */
    public synchronized static MessageService getInstance(){
        if (instance == null){
            instance = new MessageService();
        }
        return instance;
    }

    /**
     * Sets the language configuration used for subsequent lookups.
     *
     * @param langConfig loaded YAML configuration, may be {@code null} to disable translations
     */
    public void setLangConfig(Configuration langConfig) {
        this.langConfig = langConfig;
    }

    /**
     * Returns a localized message with color codes translated.
     *
     * <p>If no language configuration is loaded, or the path is missing, the
     * path itself is returned. The message is formatted with
     * {@link String#formatted(Object...)} before color translation.
     *
     * @param path localization key, also used as the fallback text
     * @param vars optional format arguments referenced by {@code %s} placeholders
     * @return the formatted, color-translated message
     */
    public String getMessage(String path,Object ...vars){
        String msg = langConfig == null ? path : langConfig.getString(path, path);
        return buildString(msg,vars);
    }

    /**
     * Applies format arguments and translates {@code &} color codes.
     *
     * @param s    raw message
     * @param vars optional format arguments
     * @return the formatted, color-translated message
     */
    private String buildString(String s,Object ...vars){
        if(vars != null) s = s.formatted(vars);
        s = color(s);
        return s;
    }

    /**
     * Translates {@code &}-style color codes to section-sign codes.
     *
     * @param text raw text, may be {@code null}
     * @return the translated text, or an empty string for {@code null}
     */
    private String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
