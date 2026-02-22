package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.listeners.BreakListener;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.runnables.MoveRunnable;

public final class CarryYou extends JavaPlugin {
    
    private MoveRunnable moveRunnable;
    private BreakRunnable breakRunnable;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);

        moveRunnable = new MoveRunnable();
        moveRunnable.runTaskTimer(this, 0L, 1L);

        breakRunnable = new BreakRunnable();
        breakRunnable.runTaskTimer(this, 0L, 1L);
        
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
