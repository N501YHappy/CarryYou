package xyz.n501yhappy.carryyou.depends.worldguard;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class EmptyProvider implements WorldguardMethods {
    @Override 
    public boolean check(Entity target, Player player) {
        return true;
    }

    @Override 
    public void registerFlag() {
    }
}
