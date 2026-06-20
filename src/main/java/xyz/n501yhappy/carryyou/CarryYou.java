package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.listeners.*;
import xyz.n501yhappy.carryyou.runnables.StateEffector;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.depends.DominionDepends;
import xyz.n501yhappy.carryyou.depends.ResidenceDepends;
import xyz.n501yhappy.carryyou.depends.WorldGuardDepends;
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
        } catch (ClassNotFoundException ignored) {
            worldguard_enable = false;
        } catch (Throwable e) {
            worldguard_enable = false;
            getLogger().warning("Failed to load WorldGuard integration: " + e.getMessage());
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
        } catch (ClassNotFoundException ignored) {
            residence_enable = false;
        } catch (Throwable e) {
            residence_enable = false;
            getLogger().warning("Failed to load Residence integration: " + e.getMessage());
        }
        // Load dominion
        try {
            Class.forName("cn.lunadeer.dominion.api.DominionAPI");
            DominionDepends.load();
        } catch (ClassNotFoundException ignored) {
            dominion_enable = false;
        } catch (Throwable e) {
            dominion_enable = false;
            getLogger().warning("Failed to load Dominion integration: " + e.getMessage());
        }

        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);
        getServer().getPluginManager().registerEvents(new CarryProtection(), this);
        getServer().getPluginManager().registerEvents(new CreeperCharge(), this);

        new Metrics(this, 29710);

        ConfigLoader.load();

        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());

        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new BreakRunnable(), 20L, 1);
        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new StateEffector(20), 20L, 20);

        getLogger().info("§aPlugin Enabled!§r");
    }

    @Override
    public void onDisable() {
        CarryManager.cleanup();
        Version.getAdapts().cancelTasks(this);
        getLogger().info("§cPlugin Disabled!§r");
    }
}
