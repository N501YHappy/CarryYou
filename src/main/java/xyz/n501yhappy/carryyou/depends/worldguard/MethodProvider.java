package xyz.n501yhappy.carryyou.depends.worldguard;

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
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.locales.MessageInfo;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MethodProvider implements WorldguardMethods {
    private StateFlag carriableFlag;
    private final Logger logger = CarryYou.getInstance().getLogger();

    @Override 
    public boolean check(Entity target, Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regionSet = query.getApplicableRegions(BukkitAdapter.adapt(target.getLocation()));
        return regionSet.queryState(WorldGuardPlugin.inst().wrapPlayer(player), this.carriableFlag) != StateFlag.State.DENY;
    }

    @Override 
    public void registerFlag() {
        try {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            try {
                StateFlag flag = new StateFlag("carriable", true);
                registry.register(flag);
                this.carriableFlag = flag;
            } catch (FlagConflictException e) {
                Flag<?> stateFlag = registry.get("carriable");
                if (stateFlag instanceof StateFlag) {
                    this.carriableFlag = (StateFlag) stateFlag;
                } else {
                    this.logger.log(Level.WARNING, ChatColor.YELLOW + MessageInfo.current().anyError(), e);
                }
            }
        } catch (Exception e2) {
            this.logger.log(Level.WARNING, ChatColor.RED + MessageInfo.current().anyError(), e2);
        }
    }
}
