package xyz.n501yhappy.carryyou.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.DependsLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public class Checkers {

    public static void init(){
        if (DependsLoader.worldguard_enabled){
            try {
                WorldGuard = Class.forName("com.sk89q.worldguard.WorldGuard");
                RegionContainer = Class.forName("com.sk89q.worldguard.protection.regions.RegionContainer");
                BukkitAdapter = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
                WG_World = Class.forName("com.sk89q.worldedit.world.World");
                BlockVector3 = Class.forName("com.sk89q.worldedit.math.BlockVector3");
                State = Class.forName("com.sk89q.worldguard.protection.flags.StateFlag$State");
                ProtectedRegion = Class.forName("com.sk89q.worldguard.protection.regions.ProtectedRegion");
                Flag = Class.forName("com.sk89q.worldguard.protection.flags.Flag");

                getInstance =  WorldGuard.getMethod("getInstance");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        if (DependsLoader.residence_enabled){
            try {
                residenceClass = Class.forName("com.bekvon.bukkit.residence.Residence");
                claimedResidenceClass = Class.forName("com.bekvon.bukkit.residence.protection.ClaimedResidence");
                residenceManagerClass = Class.forName("com.bekvon.bukkit.residence.protection.ResidenceManager");
                residencePermissionsClass = Class.forName("com.bekvon.bukkit.residence.protection.ResidencePermissions");

                residenceGetInstance = residenceClass.getMethod("getInstance");
                getResidenceManager = residenceClass.getMethod("getResidenceManager");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static Boolean worldguardCheck(Player player) {
        WorldGuard worldGuard = WorldGuard.getInstance();
        WorldGuardPlatform platform = worldGuard.getPlatform();
        RegionContainer container = platform.getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regions != null) {
            RegionQuery query = container.createQuery();
            LocalPlayer WPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            return query.testState(BukkitAdapter.adapt(player.getLocation()), WPlayer, DependsLoader.FLAG_CARRIABLE) ||
                    platform.getSessionManager().hasBypass(WPlayer,BukkitAdapter.adapt(player.getWorld()));
        }
        return true;
    }
    public static boolean residenceCheck(Player player){
        if (!DependsLoader.residence_enabled) {
            return true;
        }

        Location loc = player.getLocation();
        String playerName = player.getName();
        boolean default_value = true;

        try {
            Object residence = residenceGetInstance.invoke(null);
            Object res_manager =  getResidenceManager.invoke(residence);
            Object res = residenceManagerClass.getMethod("getByLoc", Location.class).invoke(res_manager,loc);
            if (res == null) return true;
            Object perms = res.getClass().getMethod("getPermissions").invoke(res);
            boolean hasPermission = (boolean) residencePermissionsClass.getMethod("playerHas", String.class, String.class,boolean.class).invoke(perms,playerName,"carriable",default_value);
            return hasPermission;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}