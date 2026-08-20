package xyz.n501yhappy.carryyou.depends.gsit;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import xyz.n501yhappy.carryyou.depends.DependsResolver;


public class GSitService {
    private Listener listener = null;
    private static GSitService instance = null;

    public static GSitService getInstance() {
        if (instance == null) {
            instance = new GSitService();
        }
        return instance;
    }

    private GSitService() {
        init();
    }

    public void init() {
        this.listener = DependsResolver.resolve("GSit",() -> new MethodProvider(),  EmptyProvider::new);
    }

    public void registerListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this.listener, plugin);
    }
}
