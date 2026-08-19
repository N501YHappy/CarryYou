package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.depends.dominion.DominionService;
import xyz.n501yhappy.carryyou.depends.residence.ResidenceService;
import xyz.n501yhappy.carryyou.depends.worldguard.WorldguardService;

public class Checkers {
    private static final WorldguardService worldguardService = WorldguardService.getInstance();
    private static final ResidenceService residenceService = ResidenceService.getInstance();
    private static final DominionService dominionService = DominionService.getInstance();
    public static boolean worldguard_check(Entity target, Player player) {
        return worldguardService.check(target, player);
    }

    public static boolean residence_check(Entity target, Player player) {
        return residenceService.check(target, player);
    }

    public static boolean dominion_check(Entity target, Player player) {
        return dominionService.check(target, player);
    }
    public static boolean hasMountCircle(Entity bottom,Entity test){
        if (bottom.getPassengers().isEmpty()) return false;
        for (Entity entity : bottom.getPassengers()){
            if(hasMountCircle(entity,test)) return true;
        }
        return false;
    }
}
