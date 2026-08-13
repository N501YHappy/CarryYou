package xyz.n501yhappy.carryyou.depends.gsit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public interface GSitMethods {
    boolean check(Entity entity, Player player);

    void registerFlag();
}
