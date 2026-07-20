package xyz.n501yhappy.carryyou.depends;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.CarryYou;

public abstract class DependLoader {
    private final String pluginName;
    protected boolean enabled = false;

    protected DependLoader(String pluginName) {
        this.pluginName = pluginName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPluginName() {
        return pluginName;
    }

    protected abstract void onLoad() throws Exception;

    public boolean check(Entity target, Player player) {
        return true;
    }

    public boolean tryLoad(JavaPlugin plugin, String checkClass) {
        return tryLoad(plugin, checkClass, true);
    }

    public boolean tryLoad(JavaPlugin plugin, String checkClass, boolean checkEnabled) {
        loadInfo();
        if (checkEnabled && !plugin.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            return false;
        }
        try {
            onLoad();
            enabled = true;
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load " + pluginName + " : " + e.getMessage());
            return false;
        }
    }
    private void loadInfo(){
        CarryYou.getInstance().getLogger().info("§aFound soft depend: §e" +pluginName);
    }
}
