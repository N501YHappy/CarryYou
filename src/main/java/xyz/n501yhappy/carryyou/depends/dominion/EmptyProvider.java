package xyz.n501yhappy.carryyou.depends.dominion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class EmptyProvider implements DominionMethods {
    @Override 
    public boolean check(Entity target, Player player) {
        return true;
    }

    @Override 
    public void registerFlag() {
    }
}
