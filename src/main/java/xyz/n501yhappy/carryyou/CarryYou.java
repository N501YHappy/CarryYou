package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.listeners.CarryListener;
import xyz.n501yhappy.carryyou.runnables.MoveRunnable;

public final class CarryYou extends JavaPlugin {
    
    private MoveRunnable moveRunnable;

    @Override
    public void onEnable() {
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        
        // 启动移动任务（每 tick 执行一次）
        moveRunnable = new MoveRunnable();
        moveRunnable.runTaskTimer(this, 0L, 1L);
        
        getLogger().info("§aCarryYou 插件已启用！");
    }

    @Override
    public void onDisable() {
        // 取消所有任务
        if (moveRunnable != null) {
            moveRunnable.cancel();
        }
        
        getLogger().info("§cCarryYou 插件已禁用！");
    }
}
