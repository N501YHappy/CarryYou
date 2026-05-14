package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.listeners.BreakListener;
import xyz.n501yhappy.carryyou.listeners.CarryCleanupListener;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;


public final class CarryYou extends JavaPlugin {
    public static Boolean worldguard_enabled = false;
    public static Boolean residence_enabled = false;
    private BreakRunnable breakRunnable;

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

        new Metrics(this, 29710);

        breakRunnable = new BreakRunnable();
        breakRunnable.runTaskTimer(this, 20L, 1L);

        ConfigLoader.load();

        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());

        getLogger().info("§aCarryYou Enabled！");
    }

    @Override
    public void onDisable() {
        if (breakRunnable != null) {
            breakRunnable.cancel();
        }
        getLogger().info("§cCarryYou Disabled！");
    }
}
