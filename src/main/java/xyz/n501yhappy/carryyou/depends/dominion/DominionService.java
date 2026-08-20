package xyz.n501yhappy.carryyou.depends.dominion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.depends.DependsResolver;


public class DominionService {
    private DominionMethods methods = null;
    private static DominionService instance;

    public static DominionService getInstance() {
        if (instance == null) {
            instance = new DominionService();
        }
        return instance;
    }

    private DominionService() {
        init();
    }

    public void init() {
        this.methods = DependsResolver.resolve("Dominion", () -> new MethodProvider(), EmptyProvider::new);
    }

    public boolean check(Entity target, Player player) {
        return this.methods.check(target, player);
    }

    public void registerFlag() {
        this.methods.registerFlag();
    }
}
