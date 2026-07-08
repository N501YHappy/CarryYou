package adapts.impl;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public interface schedulerAdapts {
    void GlobalRegionScheduler_runAtFixedRate(Plugin plugin, Runnable runnable, long delay, long period);

    void EntityScheduler_execute(Plugin plugin, Entity entity, Runnable task);
    void EntityScheduler_runDelayed(Plugin plugin, Entity entity, Runnable task,long delay);
    void AsyncScheduler_run(Plugin plugin,Runnable task);
    void cancelTasks(Plugin plugin);
}
