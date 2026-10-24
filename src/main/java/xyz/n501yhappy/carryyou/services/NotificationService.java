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

public class NotificationService {
    private static NotificationService instance;

    private JavaPlugin plugin;

    public NotificationService(JavaPlugin plugin){
        instance = this;
        this.plugin = plugin;
    }

    public static NotificationService getInstance() {
        return instance;
    }

    public void info(CommandSender sender, String message){
        send(sender, ConfigLoader.PREFIX + "§a " + message);
    }

    public void info(CommandSender sender, String message, Object... vars){
        info(sender, String.format(message, vars));
    }

    public void warn(CommandSender sender, String message){
        send(sender, ConfigLoader.PREFIX + "§6 " + message);
    }

    public void warn(CommandSender sender, String message, Object... vars){
        warn(sender, String.format(message, vars));
    }

    public void error(CommandSender sender, String message){
        send(sender, ConfigLoader.PREFIX + "§c " + message);
    }

    public void error(CommandSender sender, String message, Object... vars){
        error(sender, String.format(message, vars));
    }

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