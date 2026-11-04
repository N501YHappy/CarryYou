package xyz.n501yhappy.carryyou.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.utils.Misc;

/**
 * Sends formatted, colored messages and titles to players or the console.
 *
 * <p>On Paper servers messages are delivered as Adventure components. On
 * Spigot, console output is converted to ANSI and player output to legacy
 * section-sign text.
 *
 * <p>An instance must be created during plugin loading; afterwards use
 * {@link #getInstance()}.
 */
public class NotificationService {
    private static NotificationService instance;

    private JavaPlugin plugin;

    /**
     * Creates the service and registers it as the singleton instance.
     *
     * @param plugin owning plugin
     */
    public NotificationService(JavaPlugin plugin){
        instance = this;
        this.plugin = plugin;
    }

    /**
     * Returns the singleton instance created during plugin loading.
     *
     * @return the shared {@code NotificationService}, or {@code null} if it has not been constructed yet
     */
    public static NotificationService getInstance() {
        return instance;
    }

    /**
     * Sends an informational message.
     *
     * @param sender  recipient
     * @param message message text, may contain color codes
     */
    public void info(CommandSender sender, String message){
        send(sender, ConfigLoader.PREFIX + "§a " + message);
    }

    /**
     * Sends a formatted informational message.
     *
     * @param sender  recipient
     * @param message format string
     * @param vars    arguments for {@link String#format(String, Object...)}
     */
    public void info(CommandSender sender, String message, Object... vars){
        info(sender, String.format(message, vars));
    }

    /**
     * Sends a warning message.
     *
     * @param sender  recipient
     * @param message message text, may contain color codes
     */
    public void warn(CommandSender sender, String message){
        send(sender, ConfigLoader.PREFIX + "§6 " + message);
    }

    /**
     * Sends a formatted warning message.
     *
     * @param sender  recipient
     * @param message format string
     * @param vars    arguments for {@link String#format(String, Object...)}
     */
    public void warn(CommandSender sender, String message, Object... vars){
        warn(sender, String.format(message, vars));
    }

    /**
     * Sends an error message.
     *
     * @param sender  recipient
     * @param message message text, may contain color codes
     */
    public void error(CommandSender sender, String message){
        send(sender, ConfigLoader.PREFIX + "§c " + message);
    }

    /**
     * Sends a formatted error message.
     *
     * @param sender  recipient
     * @param message format string
     * @param vars    arguments for {@link String#format(String, Object...)}
     */
    public void error(CommandSender sender, String message, Object... vars){
        error(sender, String.format(message, vars));
    }

    /**
     * Delivers a legacy-formatted message using the platform-appropriate encoding.
     *
     * @param sender        recipient
     * @param legacyMessage message using section-sign color codes
     */
    private void send(CommandSender sender, String legacyMessage){
        Component component = LegacyComponentSerializer.legacySection()
                .deserialize(legacyMessage);

        if (Misc.isPaper()) {
            sender.sendMessage(component);
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ANSIComponentSerializer.ansi().serialize(component));
        } else {
            sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(component));
        }
    }

    /**
     * Shows a title and subtitle to a player.
     *
     * @param player   recipient
     * @param title    main title text, may contain color codes
     * @param subtitle subtitle text, may contain color codes
     * @param fadeIn   fade-in time in ticks
     * @param stay     stay time in ticks
     * @param fadeOut  fade-out time in ticks
     */
    public void title(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut){
        Title titleComponent = Title.title(
                LegacyComponentSerializer.legacySection().deserialize(title),
                LegacyComponentSerializer.legacySection().deserialize(subtitle),
                fadeIn, stay, fadeOut
        );
        if (Misc.isPaper()) {
            player.showTitle(titleComponent);
        } else {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
}