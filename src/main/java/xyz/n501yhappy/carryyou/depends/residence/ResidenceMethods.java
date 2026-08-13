package xyz.n501yhappy.carryyou.depends.residence;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public interface ResidenceMethods {
    void registerFlag();

    boolean check(Entity entity, Player player);
}
