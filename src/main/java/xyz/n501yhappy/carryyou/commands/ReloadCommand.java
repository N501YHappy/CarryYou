package xyz.n501yhappy.carryyou.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import xyz.n501yhappy.carryyou.ConfigLoader;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("carryyou.reload")) {
            sender.sendMessage("§c你没有权限使用此命令！");
            return true;
        }
        
        try {
            ConfigLoader.reload();
            sender.sendMessage("§a配置文件已重新加载！");
            return true;
        } catch (Exception e) {
            sender.sendMessage("§c重新加载配置文件时出错: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(); // 无自动补全
    }
}