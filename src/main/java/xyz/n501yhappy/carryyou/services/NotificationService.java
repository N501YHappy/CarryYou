package xyz.n501yhappy.carryyou.services;

import java.time.Duration;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.utils.Misc;

public class NotificationService {
    private static NotificationService instance;

    private BukkitAudiences audiences;

    public NotificationService(JavaPlugin plugin){
        instance = this;
        if (!Misc.isPaper()){
            audiences = BukkitAudiences.create(plugin);
        }
    }

    public static NotificationService getInstance() {
        return instance;
    }

    public void info(CommandSender sender,String message){
        Component component = LegacyComponentSerializer.legacySection().deserialize(
                ConfigLoader.PREFIX + "§a " + message
        );
        if(audiences == null){
            sender.sendMessage(component);
        }else {
            audiences.sender(sender).sendMessage(component);
        }
    }

    public void warn(CommandSender sender,String message){
        Component component = LegacyComponentSerializer.legacySection().deserialize(
                ConfigLoader.PREFIX + "§6 " + message
        );
        if(audiences == null){
            sender.sendMessage(component);
        }else {
            audiences.sender(sender).sendMessage(component);
        }
    }

    public void error(CommandSender sender,String message){
        Component component = LegacyComponentSerializer.legacySection().deserialize(
                ConfigLoader.PREFIX + "§c " + message
        );
        if(audiences == null){
            sender.sendMessage(component);
        }else {
            audiences.sender(sender).sendMessage(component);
        }
    }

    public void error(CommandSender sender,String message,Object ...vars) {error(sender, String.format(message, vars));}

    public void title(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut){
        Title titleComponent = Title.title(
                LegacyComponentSerializer.legacySection().deserialize(title),
                LegacyComponentSerializer.legacySection().deserialize(subtitle),
                fadeIn,stay,fadeOut
        );
        if(audiences == null){
            player.showTitle(titleComponent);
        }else {
            audiences.player(player).showTitle(titleComponent);
        }
    }
}
