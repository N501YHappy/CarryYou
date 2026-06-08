package adapts.impl.folia;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import adapts.impl.schedulerAdapts;

public class schedulerImpl implements schedulerAdapts{

    @Override
    public void GlobalRegionScheduler_runAtFixedRate(Plugin plugin, Runnable runnable, long delay, long period) {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delay, period);
    }

    @Override
    public void EntityScheduler_execute(Plugin plugin, Entity entity, Runnable task) {
        entity.getScheduler().run(plugin, scheduledTask -> task.run(), null);
    }

    @Override
    public void cancelTasks(Plugin plugin) {
        Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
    }
}
