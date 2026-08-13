package xyz.n501yhappy.carryyou.depends.dominion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public interface DominionMethods {
    boolean check(Entity entity, Player player);

    void registerFlag();
}
