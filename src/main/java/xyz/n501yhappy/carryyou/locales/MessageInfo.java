package xyz.n501yhappy.carryyou.locales;

import org.bukkit.ChatColor;

public interface MessageInfo {
    String onlyPlayer();
    String reloadSuccess();
    String reloadError();
    String reloadErrorLog();
    String anyError();
    String checkRequestError(int statusCode);
    String checkError(String message);
    String checkSkipped();
    String updateAvailable(String latestVersion);
    String upToDate(String currentVersion);
    String findSoftDepends();
    String WrongConfig();
    String[] FlagInfo();

    static MessageInfo current() {
        return Holder.IMPL;
    }

    static void set(MessageInfo impl) {
        Holder.IMPL = impl;
    }

    static String color(String raw) {
        return ChatColor.translateAlternateColorCodes('&', raw) + "§r";
    }

    final class Holder {
        static MessageInfo IMPL = new en_US();
    }
}
