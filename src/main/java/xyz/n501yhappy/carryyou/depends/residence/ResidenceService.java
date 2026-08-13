package xyz.n501yhappy.carryyou.depends.residence;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.depends.DependsResolver;


public class ResidenceService {
    private ResidenceMethods methods = null;
    private static ResidenceService instance = new ResidenceService();

    public static ResidenceService getInstance() {
        if (instance == null) {
            instance = new ResidenceService();
        }
        return instance;
    }

    private ResidenceService() {
        init();
    }

    public void init() {
        this.methods = DependsResolver.resolve("Residence", MethodProvider::new, EmptyProvider::new);
    }

    public boolean check(Entity target, Player player) {
        return this.methods.check(target, player);
    }

    public void registerFlag() {
        this.methods.registerFlag();
    }
}
