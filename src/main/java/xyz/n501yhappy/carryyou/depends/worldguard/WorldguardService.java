package xyz.n501yhappy.carryyou.depends.worldguard;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.depends.DependsResolver;


public class WorldguardService {
    private WorldguardMethods methods = null;
    private static WorldguardService instance = new WorldguardService();

    public static WorldguardService getInstance() {
        if (instance == null) {
            instance = new WorldguardService();
        }
        return instance;
    }

    private WorldguardService() {
        init();
    }

    public void init() {
        this.methods = DependsResolver.resolveBeforeEnable("com.sk89q.worldguard.WorldGuard", "WorldGuard", MethodProvider::new, EmptyProvider::new);
    }

    public boolean check(Entity target, Player player) {
        return this.methods.check(target, player);
    }

    public void registerFlag() {
        this.methods.registerFlag();
    }
}
