package xyz.n501yhappy.carryyou.depends;

import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.api.dtos.flag.PriFlag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;

public class DominionDepends {
    public static DominionAPI dominionAPI;
    public static PriFlag DM_FLAG_CARRIABLE;

    public static void load() {
        CarryYou.dominion_enable = CarryYou.instance.getServer().getPluginManager().isPluginEnabled("Dominion");
        if (!CarryYou.dominion_enable) return;
        if (Bukkit.getPluginManager().isPluginEnabled("Dominion")) {
            dominionAPI = DominionAPI.getInstance();
        } else {
            CarryYou.instance.getLogger().warning("Dominion plugin is not enabled. Please ensure it is installed and enabled.");
            CarryYou.dominion_enable = false;
            return;
        }
        DM_FLAG_CARRIABLE = new PriFlag("carriable", "可使用抓举", "在你的领地内，其他人可以抓举生物", false, true, Material.LEAD);
        Flags.registerPriFlag(CarryYou.instance, DM_FLAG_CARRIABLE);
        try {
            Flags.applyNewCustomFlags(); //应用到其系统中
        } catch (Exception e) {
            e.printStackTrace();
            CarryYou.dominion_enable = false;
        }
    }

    public static boolean check(Entity target, Player player) {
        DominionDTO dominion = dominionAPI.getDominion(target.getLocation());
        if (dominion == null) return true;
        return dominionAPI.checkPrivilegeFlagSilence(
                target.getLocation(),
                DM_FLAG_CARRIABLE,
                player
        );
    }
}
