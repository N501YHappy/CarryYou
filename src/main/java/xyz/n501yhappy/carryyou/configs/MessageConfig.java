package xyz.n501yhappy.carryyou.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class MessageConfig {
    public enum Message {
        BREAK_WEAKNESS("break.weakness", "&c你现在处于虚弱状态！"),
        BREAK_UNBREAK("break.unbreak", "&c你现在不能挣脱哦...忍一会吧~"),
        BREAK_FREE_SUBTITLE("break.break_free_subtitle", "&e快速点击左键挣脱！"),
        BREAK_PROGRESS_BAR_BROKEN("break.progress_bar_broken", "&7(&c坏掉了！&7)"),

        CARRY_WORLD_DENY("carry.world_deny", "&c当前世界不允许你抱它..."),
        CARRY_NO_PERMISSION("carry.no_permission", "&c你太小啦，等你再长大一点点，它才愿意钻到你怀里哦"),
        CARRY_WORLDGUARD_DENY("carry.worldguard_deny", "&c小guard告诉我这是别人的领地！你不可以这样！"),
        CARRY_DOMINION_DENY("carry.dominion_deny", "&c不行！这是别人的地盘！"),
        CARRY_RESIDENCE_DENY("carry.residence_deny", "&cres管理员不让你这么做哦"),
        CARRY_ENTITY_DENY("carry.entity_deny", "&c你不能抱它！"),
        CARRY_PLAYER_UNCARRIED("carry.player_uncarried", "&c你不能抱它！"),

        COMMAND_NO_PERMISSION("command.no_permission", "&c你没有权限使用此命令！"),
        COMMAND_RELOAD_SUCCESS("command.reload_success", "&a配置文件已重新加载！"),
        COMMAND_RELOAD_ERROR("command.reload_error", "&c重新加载配置文件时出错: "),
        COOLDOWN("carry.cooldown", "&c你还要再等 %s 秒"),
        DISABLE_CARRY("carry.disable_carry", "&c你现在不可以被抱起来！"),
        ENABLE_CARRY("carry.enable_carry", "&a你现在可以被抱起来！");




        private final String path;
        private final String def;
        private String val;

        Message(String path, String def) {
            this.path = path;
            this.def = def;
            this.val = def;
        }

        public String getPath() {
            return path;
        }

        public String getDefault() {
            return def;
        }

        public String get() {
            return val;
        }

        public void set(String value) {
            this.val = value;
        }
    }

    public static void load(Plugin plugin, Logger logger) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        Configuration config = YamlConfiguration.loadConfiguration(file);
        if (config.getKeys(false).isEmpty() && file.length() > 0) {
            logger.warning("§c配置文件格式错误！才不是特意提醒你的呢喵！");
            return;
        }
        for (Message message : Message.values()) {
            String raw = config.getString(message.getPath(), message.getDefault());
            message.set(ChatColor.translateAlternateColorCodes('&', raw) + "§r");
        }
    }
}
