package xyz.n501yhappy.carryyou.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.DependsLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DependsChecker {
    private static Class<?> WorldGuard;
    private static Class<?> RegionContainer;
    private static Class<?> BukkitAdapter;
    private static Class<?> WG_World;
    private static Class<?> BlockVector3;
    private static Class<?> State;
    private static Class<?> ProtectedRegion;
    private static Class<?> Flag;

    private static Method getInstance;

    private static Class<?> residenceClass;
    private static Class<?> claimedResidenceClass;
    private static Class<?> residenceManagerClass;
    private static Class<?> residencePermissionsClass;

    private static Method residenceGetInstance;
    private static Method getResidenceManager;

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
        try {
            Object worldguard =getInstance.invoke(null);

            Object platform = worldguard.getClass().getMethod("getPlatform").invoke(worldguard);

            Object region_container = platform.getClass().getMethod("getRegionContainer").invoke(platform);

            Object LocalWorld = BukkitAdapter
                    .getMethod("adapt", org.bukkit.World.class)
                    .invoke(null, player.getWorld());

            Object RegionManager = RegionContainer.getMethod("get", WG_World)
                    .invoke(region_container, LocalWorld);

            if (RegionManager != null) {
                Object playerLocation = player.getClass().getMethod("getLocation").invoke(player);
                Object adaptResult = BukkitAdapter.getMethod("adapt", Location.class)
                        .invoke(null, playerLocation);

                Method toVectorMethod = adaptResult.getClass().getMethod("toVector");
                Object vector = toVectorMethod.invoke(adaptResult);

                Method toBlockPointMethod = vector.getClass().getMethod("toBlockPoint");
                Object blockPoint = toBlockPointMethod.invoke(vector);

                Method getApplicableRegionsMethod = RegionManager.getClass().getMethod("getApplicableRegions",
                        BlockVector3);
                Object applicableRegions = getApplicableRegionsMethod.invoke(RegionManager, blockPoint);
                Iterable<?> regionsIterable = (Iterable<?>) applicableRegions;

                Object flag = DependsLoader.FLAG_CARRIABLE;
                if (flag == null) return true;
                Field allowField = State.getField("ALLOW");
                Object allowState = allowField.get(null);

                Method getFlagMethod = ProtectedRegion.getMethod("getFlag", Flag);

                for (Object region : regionsIterable) {
                    Object sf = getFlagMethod.invoke(region, flag);
                    if (sf == null) continue;
                    if (!sf.equals(allowState)) return false;
                }
                return true;
            }
            return true;

        } catch (NoSuchMethodException |
                 InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return true;
        }
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