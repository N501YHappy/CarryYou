package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.depends.DominionDepends;
import xyz.n501yhappy.carryyou.depends.ResidenceDepends;
import xyz.n501yhappy.carryyou.depends.WorldGuardDepends;
import xyz.n501yhappy.carryyou.listeners.BreakListener;
import xyz.n501yhappy.carryyou.listeners.CarryCleanupListener;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.listeners.CarryProtection;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import adapts.impl.Version;

public final class CarryYou extends JavaPlugin {
    public static JavaPlugin instance;

    public static Boolean worldguard_enable = false;
    public static Boolean residence_enable = false;
    public static Boolean dominion_enable = false;
    @Override
    public void onLoad() {
        instance = this;
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            WorldGuardDepends.load();
        } catch (ClassNotFoundException e) {
            worldguard_enable = false;
        }
        try {
            Version.init(getLogger());
        } catch (Throwable e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        // Load residence
        try {
            Class.forName("com.bekvon.bukkit.residence.Residence");
            ResidenceDepends.load();
        } catch (ClassNotFoundException e) {
            residence_enable = false;
        }
        // Load dominion
        DominionDepends.load();

        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);
        getServer().getPluginManager().registerEvents(new CarryProtection(), this);

        new Metrics(this, 29710);

        ConfigLoader.load();

        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());

        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new BreakRunnable(), 20L, 1);

        getLogger().info("§aPlugin Enabled!§r");
    }

    @Override
    public void onDisable() {
        getLogger().info("§cPlugin Disabled!§r");
    }
}
