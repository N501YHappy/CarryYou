package xyz.n501yhappy.carryyou;

import carryyou.api.CarryyouAPI;
import carryyou.nms.NMSLoader;
import java.util.logging.Level;
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
import xyz.n501yhappy.carryyou.services.CarryBlockService;
import xyz.n501yhappy.carryyou.services.MessageService;
import xyz.n501yhappy.carryyou.services.CarryService;
import xyz.n501yhappy.carryyou.services.NotificationService;
import xyz.n501yhappy.carryyou.utils.CarryLogger;
import adapts.impl.Version;

public final class CarryYou extends JavaPlugin {
    public static JavaPlugin instance;

    private final CarryService carryService = CarryService.getInstance();

    private Metrics metrics;

    @Override
    public void onLoad() {
        instance = this;
        new NotificationService(this);
        new CarryLogger(this);
        try {
            Version.init(getLogger());
        } catch (Exception e) {
            CarryLogger.error(MessageService.getInstance().getMessage("Plugin.plugin-error"), e);
            getServer().getPluginManager().disablePlugin(this);
        }
        WorldguardService.getInstance().registerFlag();
        CarryyouAPI.registerCarryManager(carryService);
    }

    @Override
    public void onEnable() {
        ConfigLoader.load(this);

        ResidenceService.getInstance().registerFlag();
        DominionService.getInstance().registerFlag();
        GSitService.getInstance().registerListener(this);

        NMSLoader.init((level, message, error) -> {
            if (error != null) CarryLogger.error(message, error);
            else if (level == Level.WARNING) CarryLogger.warn(message);
            else CarryLogger.info(message);
        });
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

        CarryLogger.info(MessageService.getInstance().getMessage("Plugin.plugin-enabled"));

        metrics = new Metrics(this, 29710);
    }

    @Override
    public void onDisable() {
        carryService.cleanup();
        CarryBlockService.getInstance().cleanup();
        Version.getAdapts().cancelTasks(this);
        metrics.shutdown();
        CarryLogger.info(MessageService.getInstance().getMessage("Plugin.plugin-disabled"));
    }

    public static JavaPlugin getInstance() {
        return instance;
    }
}
