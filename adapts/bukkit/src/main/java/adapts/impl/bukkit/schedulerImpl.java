package adapts.impl.bukkit;

import adapts.impl.schedulerAdapts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class schedulerImpl implements schedulerAdapts {

    @Override
    public void GlobalRegionScheduler_runAtFixedRate(Plugin plugin, Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    @Override
    public void EntityScheduler_execute(Plugin plugin, Entity entity, Runnable task) {
        Bukkit.getScheduler().runTask(plugin,task);
    }
    @Override
    public void EntityScheduler_runDelayed(Plugin plugin, Entity entity, Runnable task,long delay) {
        Bukkit.getScheduler().runTaskLater(plugin,task,delay);
    }
    @Override
    public void AsyncScheduler_run(Plugin plugin,Runnable task){
        Bukkit.getScheduler().runTaskAsynchronously(plugin,task);
    }


    @Override
    public void cancelTasks(Plugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
