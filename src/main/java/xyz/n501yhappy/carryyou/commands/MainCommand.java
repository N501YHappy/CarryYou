package xyz.n501yhappy.carryyou.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;
import xyz.n501yhappy.carryyou.locales.MessageInfo;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static xyz.n501yhappy.carryyou.configs.ConfigLoader.PREFIX;

public class MainCommand implements CommandExecutor, TabExecutor {
    private final CarryManager carryManager = CarryManager.getInstance();

    private static final List<String> SUB_COMMANDS = Arrays.asList("on", "off", "reload");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                boolean current = carryManager.isCarryDisabled(player.getUniqueId());
                carryManager.setCarryDisabled(player.getUniqueId(), !current);
                sender.sendMessage(PREFIX + (current ? MessageConfig.Message.ENABLE_CARRY.get() : MessageConfig.Message.DISABLE_CARRY.get()));

            }
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        } else if (args[0].equalsIgnoreCase("on")) {
            if (sender instanceof Player)
                carryManager.setCarryDisabled(((Player) sender).getUniqueId(), false);
            sender.sendMessage(PREFIX + MessageConfig.Message.ENABLE_CARRY.get());
            return true;
        } else if (args[0].equalsIgnoreCase("off")) {
            handleToggle(sender,"off");
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
            sender.sendMessage(PREFIX + MessageInfo.current().reloadSuccess());
            return true;
        } catch (Exception e) {
            sender.sendMessage(PREFIX + MessageInfo.current().reloadError() + e.getMessage());
            CarryYou.getInstance().getLogger().log(Level.SEVERE, MessageInfo.current().reloadErrorLog(), e);
            return true;
        }
    }
    private void handleToggle(CommandSender sender,String arg) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageInfo.current().onlyPlayer());
            return;
        }
        if(!sender.hasPermission("carryyou.can_toggle")){
            sender.sendMessage(MessageConfig.Message.COMMAND_NO_PERMISSION.get());
            return;
        }
        carryManager.setCarryDisabled(((Player) sender).getUniqueId(), arg.equalsIgnoreCase("off"));
        sender.sendMessage(PREFIX + MessageConfig.Message.DISABLE_CARRY.get());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return SUB_COMMANDS.stream()
                    .filter(s -> s.startsWith(input))
                    .filter(s -> {
                        if (s.equals("off") || s.equals("on")) {
                            return sender.hasPermission("carryyou.can_toggle");
                        }
                        if (s.equals("reload")) {
                            return sender.hasPermission("carryyou.reload");
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
