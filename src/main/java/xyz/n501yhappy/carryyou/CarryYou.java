package xyz.n501yhappy.carryyou;

import carryyou.api.CarryyouAPI;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.carryyou.commands.MainCommand;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.depends.dominion.DominionService;
import xyz.n501yhappy.carryyou.depends.gsit.GSitService;
import xyz.n501yhappy.carryyou.depends.residence.ResidenceService;
import xyz.n501yhappy.carryyou.depends.worldguard.WorldguardService;
import xyz.n501yhappy.carryyou.listeners.*;
import xyz.n501yhappy.carryyou.runnables.BreakRunnable;
import xyz.n501yhappy.carryyou.runnables.StateEffector;
import xyz.n501yhappy.carryyou.services.MessageService;
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
        try {
            Version.init(getLogger());
        } catch (Exception e) {
            MessageService.getInstance().log(Level.SEVERE, "Plugin.plugin-error", e);
            getServer().getPluginManager().disablePlugin(this);
        }
        WorldguardService.getInstance().registerFlag();
        CarryyouAPI.registerCarryManager(carryManager);
    }

    @Override
    public void onEnable() {
        ConfigLoader.load(this);

        ResidenceService.getInstance().registerFlag();
        DominionService.getInstance().registerFlag();
        GSitService.getInstance().registerListener(this);

        getServer().getPluginManager().registerEvents(new CarryListener(), this);
        getServer().getPluginManager().registerEvents(new BreakListener(), this);
        getServer().getPluginManager().registerEvents(new CarryCleanupListener(), this);
        getServer().getPluginManager().registerEvents(new CarryProtection(), this);
        getServer().getPluginManager().registerEvents(new CreeperCharge(), this);
        getServer().getPluginManager().registerEvents(new CycleListener(), this);
        getServer().getPluginManager().registerEvents(new PluginListener(), this);

        getCommand("carryyou").setExecutor(new MainCommand());

        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new BreakRunnable(), 20L, 1);
        Version.getAdapts().GlobalRegionScheduler_runAtFixedRate(this, new StateEffector(20), 20L, 20);

        if(ConfigLoader.CHECK_UPDATE) Version.getAdapts().AsyncScheduler_run(this, () -> new VersionCheck(this).checkVersion());

        MessageService.getInstance().log(Level.INFO, "Plugin.plugin-enabled");

        metrics = new Metrics(this, 29710);
    }

    @Override
    public void onDisable() {
        carryManager.cleanup();
        Version.getAdapts().cancelTasks(this);
        metrics.shutdown();
        MessageService.getInstance().log(Level.INFO, "Plugin.plugin-disabled");
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
