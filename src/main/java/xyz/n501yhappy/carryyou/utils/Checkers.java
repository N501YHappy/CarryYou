package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;

public class Checkers {
    public static boolean worldguard_check(Entity target, Player player) {
        return CarryYou.worldGuardDepends.check(target, player);
    }

    public static boolean residence_check(Entity target, Player player) {
        return CarryYou.residenceDepends.check(target, player);
    }

    public static boolean dominion_check(Entity target, Player player) {
        return CarryYou.dominionDepends.check(target, player);
    }
}
