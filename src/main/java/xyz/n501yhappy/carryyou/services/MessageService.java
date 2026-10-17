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

    private String buildString(String s,Object ...vars){
        if(vars != null) s = s.formatted(vars);
        s = color(s);
        return s;
    }

    private String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
