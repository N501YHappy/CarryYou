

package xyz.n501yhappy.carryyou.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.DependsLoader;

import static xyz.n501yhappy.carryyou.DependsLoader.FLAG_CARRIABLE;

public class FuckFanshe {
    public static Boolean worldguardCheck(Player player){
        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        if (rm != null){
            ApplicableRegionSet regionsAtLocation = rm.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()).toVector().toBlockPoint()); //这里也是
            for (ProtectedRegion region : regionsAtLocation) {
                StateFlag.State sf = region.getFlag(FLAG_CARRIABLE);
                return sf == StateFlag.State.ALLOW || sf == null;
            }
        }
        return true;
    }
}
