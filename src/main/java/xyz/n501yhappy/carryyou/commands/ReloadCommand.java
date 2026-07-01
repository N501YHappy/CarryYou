package xyz.n501yhappy.carryyou.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("carryyou.reload")) {
            sender.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.COMMAND_NO_PERMISSION.get());
            return true;
        }

        try {
            ConfigLoader.reload();
            sender.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.COMMAND_RELOAD_SUCCESS.get());
            return true;
        } catch (Exception e) {
            sender.sendMessage(ConfigLoader.PREFIX + MessageConfig.Message.COMMAND_RELOAD_ERROR.get() + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(); // 无自动补全
    }
}