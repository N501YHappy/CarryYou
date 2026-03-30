package xyz.n501yhappy.carryyou.utils;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.DependsLoader;

public class Checkers {
    public static Boolean worldguard_check(Player player){
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet regionSet =  query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        Boolean result  = (regionSet.queryState(WorldGuardPlugin.inst().wrapPlayer(player), DependsLoader.FLAG_CARRIABLE) != StateFlag.State.DENY); //21世纪最爽的一段石山
        return  result;
    }
    public static Boolean residence_check(Player player){
        ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(player);
        if (residence == null) return true; //如果没有领地就true
        ResidencePermissions permissions = residence.getPermissions();
        String player_name = player.getName();
        boolean defaultValue = true;

        boolean hasPermission = permissions.playerHas(player_name, "build", defaultValue);

        if(!hasPermission) return false;
        return true;
    }
}