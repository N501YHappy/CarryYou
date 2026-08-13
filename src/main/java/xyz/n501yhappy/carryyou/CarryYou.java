package xyz.n501yhappy.carryyou;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.depends.DependsResolver;
import xyz.n501yhappy.carryyou.depends.dominion.DominionService;
import xyz.n501yhappy.carryyou.depends.gsit.GSitService;
import xyz.n501yhappy.carryyou.depends.residence.ResidenceService;
import xyz.n501yhappy.carryyou.depends.worldguard.WorldguardService;
import xyz.n501yhappy.carryyou.listeners.*;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.runnables.StateEffector;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import adapts.impl.Version;

import java.util.logging.Level;

public final class CarryYou extends JavaPlugin {
    public static JavaPlugin instance;

    private final CarryManager carryManager = CarryManager.getInstance();

    private Metrics metrics;

    @Override
    public void onLoad() {
        instance = this;
        DependsResolver.setLogger(getLogger());
        WorldguardService.getInstance().registerFlag();
        try {
            Version.init(getLogger());
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, ChatColor.RED + "唔……糟糕，我可能不行了",e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        ResidenceService.getInstance().registerFlag();
        DominionService.getInstance().registerFlag();
        GSitService.getInstance().registerListener(this);

        ConfigLoader.load();

        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);
        getServer().getPluginManager().registerEvents(new CarryProtection(), this);
        getServer().getPluginManager().registerEvents(new CreeperCharge(), this);

        metrics = new Metrics(this, 29710);

        getCommand("carryyou").setExecutor(new xyz.n501yhappy.carryyou.commands.ReloadCommand());

        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new BreakRunnable(), 20L, 1);
        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new StateEffector(20), 20L, 20);

        if(ConfigLoader.CHECK_UPDATE)         Version.getAdapts().AsyncScheduler_run(this, () -> new VersionCheck(this).checkVersion());

        getLogger().info("§aPlugin Enabled!§r");
    }

    @Override
    public void onDisable() {
        carryManager.cleanup();
        Version.getAdapts().cancelTasks(this);
        metrics.shutdown();
        getLogger().info("§cPlugin Disabled!§r");
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
