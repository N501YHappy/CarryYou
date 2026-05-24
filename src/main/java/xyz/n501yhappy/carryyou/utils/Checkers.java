package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.depends.DominionDepends;
import xyz.n501yhappy.carryyou.depends.ResidenceDepends;
import xyz.n501yhappy.carryyou.depends.WorldGuardDepends;

public class Checkers {
    public static Boolean worldguard_check(Entity target, Player player) {
        return WorldGuardDepends.check(target, player);
    }

    public static Boolean residence_check(Entity target, Player player) {
        return ResidenceDepends.check(target, player);
    }

    public static Boolean dominion_check(Entity target, Player player) {
        return DominionDepends.check(target, player);
    }
}
