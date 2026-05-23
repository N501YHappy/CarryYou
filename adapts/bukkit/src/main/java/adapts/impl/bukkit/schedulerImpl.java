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
        task.run();
    }
}
