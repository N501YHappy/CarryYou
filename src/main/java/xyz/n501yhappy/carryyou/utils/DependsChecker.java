package xyz.n501yhappy.carryyou.utils;

import com.sk89q.worldedit.world.World;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DependsChecker {
    public static Boolean worldguardCheck(Player player){
        try {
            Class<?> WorldGuard = Class.forName("com.sk89q.worldguard.WorldGuard");
            Object worldguard = WorldGuard.getMethod("getInstance").invoke(null); //worldguard是我对象(?

            Object platform = worldguard.getClass().getMethod("getPlatform").invoke(worldguard);

            Object region_container = platform.getClass().getMethod("getRegionContainer").invoke(platform);
            Class<?> RegionContainer = Class.forName("com.sk89q.worldguard.protection.regions.RegionContainer");

            Object LocalWorld = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter").getMethod("adapt", org.bukkit.World.class).invoke(player.getWorld());

            Object RegionManager =  RegionContainer.getMethod("get", World.class).invoke(region_container,LocalWorld);

            if (RegionManager != null){
                Object playerLocation = player.getClass().getMethod("getLocation").invoke(player);

                Class<?> bukkitAdapterClass = Class.forName("org.bukkit.BukkitAdapter");
                Method adaptMethod = bukkitAdapterClass.getMethod("adapt", Class.forName("org.bukkit.Location"));
                Object adaptResult = adaptMethod.invoke(null, playerLocation);

                Method toVectorMethod = adaptResult.getClass().getMethod("toVector");
                Object vector = toVectorMethod.invoke(adaptResult);

                Method toBlockPointMethod = vector.getClass().getMethod("toBlockPoint");
                Object blockPoint = toBlockPointMethod.invoke(vector);

                Method getApplicableRegionsMethod = RegionManager.getClass().getMethod("getApplicableRegions",
                        Class.forName("org.bukkit.util.BlockVector"));
                Object regionsAtLocation = getApplicableRegionsMethod.invoke(RegionManager, blockPoint);

                Iterable<?> iterable = (Iterable<?>) regionsAtLocation;
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}