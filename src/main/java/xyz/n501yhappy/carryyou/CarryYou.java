package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.listeners.BreakListener;
import xyz.n501yhappy.carryyou.listeners.CarryCleanupListener;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.runnables.MoveRunnable;

public final class CarryYou extends JavaPlugin {
    
    private MoveRunnable moveRunnable;
    private BreakRunnable breakRunnable;

    public static Plugin instance;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);

        int pluginId = 29710;
        Metrics metrics = new Metrics(this, pluginId);

        moveRunnable = new MoveRunnable();
        moveRunnable.runTaskTimer(this, 20L, 1L);

        breakRunnable = new BreakRunnable();
        breakRunnable.runTaskTimer(this, 20L, 1L);

        instance = this;
        ConfigLoader.load();
        
        // 注册命令
        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());
        
        getLogger().info("§aCarryYou Enabled！");
    }

    @Override
    public void onDisable() {
        if (moveRunnable != null) {
            moveRunnable.cancel();
        }
        if (breakRunnable != null) {
            breakRunnable.cancel();
        }
        
        getLogger().info("§cCarryYou Disabled！");
    }
}
