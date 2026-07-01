package xyz.n501yhappy.carryyou.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static xyz.n501yhappy.carryyou.configs.ConfigLoader.PREFIX;

public class ReloadCommand implements CommandExecutor, TabExecutor {

    private static final List<String> SUB_COMMANDS = Arrays.asList("on", "off", "reload");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                boolean current = CarryManager.isCarryDisabled(player.getUniqueId());
                CarryManager.setCarryDisabled(player.getUniqueId(), !current);
                sender.sendMessage(PREFIX + (current ? MessageConfig.Message.ENABLE_CARRY.get() : MessageConfig.Message.DISABLE_CARRY.get()));

            }
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        } else if (args[0].equalsIgnoreCase("on")) {
            if (sender instanceof Player)
                CarryManager.setCarryDisabled(((Player) sender).getUniqueId(), false);
            sender.sendMessage(PREFIX + MessageConfig.Message.ENABLE_CARRY.get());
            return true;
        } else if (args[0].equalsIgnoreCase("off")) {
            if (sender instanceof Player)
                CarryManager.setCarryDisabled(((Player) sender).getUniqueId(), true);
            sender.sendMessage(PREFIX + MessageConfig.Message.DISABLE_CARRY.get());
            return true;
        }
        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("carryyou.reload")) {
            sender.sendMessage(PREFIX + MessageConfig.Message.COMMAND_NO_PERMISSION.get());
            return true;
        }
        try {
            ConfigLoader.reload();
            sender.sendMessage(PREFIX + MessageConfig.Message.COMMAND_RELOAD_SUCCESS.get());
            return true;
        } catch (Exception e) {
            sender.sendMessage(PREFIX + MessageConfig.Message.COMMAND_RELOAD_ERROR.get() + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return SUB_COMMANDS.stream()
                    .filter(s -> s.startsWith(input))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
