package xyz.n501yhappy.carryyou;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.depends.DependLoader;
import xyz.n501yhappy.carryyou.listeners.*;
import xyz.n501yhappy.carryyou.listeners.depends.GSIT_PlayerSitPlayer;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.runnables.StateEffector;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import adapts.impl.Version;

public final class CarryYou extends JavaPlugin {
    public static JavaPlugin instance;

    public static boolean worldguard_enable = false;
    public static boolean residence_enable = false;
    public static boolean dominion_enable = false;
    public static boolean gsit_enable = false;

    public static DependLoader worldGuardDepends;
    public static DependLoader residenceDepends;
    public static DependLoader dominionDepends;
    public static DependLoader gsitDepends;

    private Metrics metrics;

    @Override
    public void onLoad() {
        instance = this;
        worldGuardDepends = loadDepends(
                "xyz.n501yhappy.carryyou.depends.WorldGuardDepends",
                "com.sk89q.worldguard.WorldGuard", false);
        worldguard_enable = worldGuardDepends != null;
        try {
            Version.init(getLogger());
        } catch (Exception e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        residenceDepends = loadDepends(
                "xyz.n501yhappy.carryyou.depends.ResidenceDepends",
                "com.bekvon.bukkit.residence.Residence", true);
        residence_enable = residenceDepends != null;

        dominionDepends = loadDepends(
                "xyz.n501yhappy.carryyou.depends.DominionDepends",
                "cn.lunadeer.dominion.api.DominionAPI", true);
        dominion_enable = dominionDepends != null;

        gsitDepends = loadDepends(
                "xyz.n501yhappy.carryyou.depends.GSitDepends",
                "dev.geco.gsit.GSitMain", true);
        gsit_enable = gsitDepends != null;

        ConfigLoader.load();

        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);
        getServer().getPluginManager().registerEvents(new CarryProtection(), this);
        getServer().getPluginManager().registerEvents(new CreeperCharge(), this);
        if (gsit_enable)
            getServer().getPluginManager().registerEvents(new GSIT_PlayerSitPlayer(), this);

        metrics = new Metrics(this, 29710);

        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());

        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new BreakRunnable(), 20L, 1);
        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new StateEffector(20), 20L, 20);

        Version.getAdapts().AsyncScheduler_run(this, () -> new VersionCheck(this).checkVersion());

        getLogger().info("§aPlugin Enabled!§r");
    }

    @Override
    public void onDisable() {
        CarryManager.cleanup();
        Version.getAdapts().cancelTasks(this);
        metrics.shutdown();
        getLogger().info("§cPlugin Disabled!§r");
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    /**
     * 用反射检查依赖类是否存在
     */
    private DependLoader loadDepends(String dependsClassName, String checkClass, boolean checkEnabled) {
        try {
            Class.forName(checkClass);
        } catch (ClassNotFoundException e) {
            return null;
        }
        // 如果找到了就构造对应加载类，然后执行加载
        try {
            Class<?> clazz = Class.forName(dependsClassName);
            DependLoader loader = (DependLoader) clazz.getDeclaredConstructor().newInstance();
            if (loader.tryLoad(this, checkClass, checkEnabled)) {
                return loader;
            }
        } catch (Throwable t) {
            getLogger().warning("Failed to load " + dependsClassName + ": " + t.getMessage());
        }
        return null;
    }
}
