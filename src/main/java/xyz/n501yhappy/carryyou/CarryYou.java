package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.configs.MessageConfig;
import xyz.n501yhappy.carryyou.listeners.BreakListener;
import xyz.n501yhappy.carryyou.listeners.CarryCleanupListener;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.listeners.CarryProtection;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import adapts.impl.Version;

public final class CarryYou extends JavaPlugin {
    public static Boolean worldguard_enabled = false;
    public static Boolean residence_enabled = false;

    public static Plugin instance;

    @Override
    public void onLoad() {
        instance = this;
        worldguard_enabled = true;
        try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            DependsLoader.loadWGDepends();
        } catch (ClassNotFoundException e) {
            worldguard_enabled = false;
            //Worldgurad未安装
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
        //Load residence
        residence_enabled = true;
        try {
            Class.forName("com.bekvon.bukkit.residence.Residence");
            DependsLoader.loadResDepends();
        } catch (ClassNotFoundException e) {
            residence_enabled = false;
            //Residence未安装
        }
        //Loaded
        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);
        getServer().getPluginManager().registerEvents(new CarryProtection(), this);

        new Metrics(this, 29710);

        ConfigLoader.load();

        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());

        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new BreakRunnable(), 20L, 1);

        getLogger().info("§aPlugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("§cPlugin Disabled!");
    }
}
