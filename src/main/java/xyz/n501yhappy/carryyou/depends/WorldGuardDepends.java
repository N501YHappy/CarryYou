package xyz.n501yhappy.carryyou.depends;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;

public class WorldGuardDepends {
    public static StateFlag WG_FLAG_CARRIABLE;

    public static void load() {
        CarryYou.worldguard_enable = true;
        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            try {
                StateFlag flag = new StateFlag("carriable", true);
                registry.register(flag);
                WG_FLAG_CARRIABLE = flag;

            } catch (FlagConflictException e) {
                Flag<?> existing = registry.get("carriable");
                if (existing instanceof StateFlag) {
                    WG_FLAG_CARRIABLE = (StateFlag) existing;
                } else {
                    CarryYou.instance.getLogger().warning("§6有别的flag叫这个名字但不是StateFlag...");
                }
            } catch (Exception e) {
                CarryYou.instance.getLogger().warning("§c哇啊啊啊啊啊: " + e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            CarryYou.instance.getLogger().warning("§c小guard哈气了！她打我了: " + e.getMessage());
            CarryYou.worldguard_enable = false;
            e.printStackTrace();
        }
    }

    public static boolean check(Entity target, Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regionSet = query.getApplicableRegions(BukkitAdapter.adapt(target.getLocation()));
        return (regionSet.queryState(WorldGuardPlugin.inst().wrapPlayer(player), WG_FLAG_CARRIABLE) != StateFlag.State.DENY);
    }
}
