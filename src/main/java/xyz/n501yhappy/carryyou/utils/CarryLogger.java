package xyz.n501yhappy.carryyou.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.services.NotificationService;

public class CarryLogger {
    public static CarryLogger instance;

    private CommandSender sender;

    public CarryLogger(JavaPlugin plugin){
        instance = this;
        this.sender = plugin.getServer().getConsoleSender();
    }

    public static void info(String message){
        NotificationService.getInstance().info(instance.sender, message);
    }

    public static void warn(String message){
        NotificationService.getInstance().warn(instance.sender, message);
    }

    public static void error(String message,Throwable error){
        NotificationService.getInstance().error(instance.sender, message);
        error.printStackTrace();
    }
}
