package xyz.n501yhappy.carryyou.services;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;

import java.util.logging.Level;

public class MessageService {
    private static MessageService instance = null;
    private Configuration langConfig;


    private MessageService(){}
    public synchronized static MessageService getInstance(){
        if (instance == null){
            instance = new MessageService();
        }
        return instance;
    }

    public void setLangConfig(Configuration langConfig) {
        this.langConfig = langConfig;
    }

    public String getMessage(String path,Object ...vars){
        String msg = langConfig == null ? path : langConfig.getString(path, path);
        return buildString(msg,vars);
    }

    public void log(Level level, String path,Object ...vars){
        CarryYou.getInstance().getLogger().log(level,getMessage(path,vars));
    }

    public void log(Level level, String path, Throwable throwable){
        CarryYou.getInstance().getLogger().log(level,getMessage(path),throwable);
    }

    public void sendMessage(CommandSender sender, String path,Object ...vars){
        sender.sendMessage(getMessage(path,vars));
    }

    public void sendTitle(Player player, String title, String subtitlePath, int fadeIn, int stay, int fadeOut){
        player.sendTitle(title,getMessage(subtitlePath),fadeIn,stay,fadeOut);
    }

    private String buildString(String s,Object ...vars){
        s = addPluginPerfix(s);
        if(vars != null) s = format(s,vars);
        s = color(s);
        return s;
    }

    private String addPluginPerfix(String msg){
        return msg.replace("[P]",ConfigLoader.PREFIX);
    }
    private String format(String msg,Object ...vars){
        return msg.formatted(vars);
    }

    private String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
