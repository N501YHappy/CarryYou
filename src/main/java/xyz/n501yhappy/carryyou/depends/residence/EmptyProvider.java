package xyz.n501yhappy.carryyou.depends.residence;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class EmptyProvider implements ResidenceMethods {
    @Override 
    public boolean check(Entity target, Player player) {
        return true;
    }

    @Override 
    public void registerFlag() {
    }
}
