package xyz.n501yhappy.carryyou.depends.worldguard;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public interface WorldguardMethods {
    boolean check(Entity entity, Player player);

    void registerFlag();
}
