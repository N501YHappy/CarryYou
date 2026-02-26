package xyz.n501yhappy.carryyou.utils;

import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.DependsLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DependsChecker {

    public static Boolean worldguardCheck(Player player) {
        try {
            Class<?> WorldGuard = Class.forName("com.sk89q.worldguard.WorldGuard");
            Object worldguard = WorldGuard.getMethod("getInstance").invoke(null);

            Object platform = worldguard.getClass().getMethod("getPlatform").invoke(worldguard);

            Object region_container = platform.getClass().getMethod("getRegionContainer").invoke(platform);
            Class<?> RegionContainer = Class.forName("com.sk89q.worldguard.protection.regions.RegionContainer");

            Object LocalWorld = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter")
                    .getMethod("adapt", org.bukkit.World.class)
                    .invoke(null, player.getWorld());

            Object RegionManager = RegionContainer.getMethod("get", Class.forName("com.sk89q.worldedit.world.World"))
                    .invoke(region_container, LocalWorld);

            if (RegionManager != null) {
                Object playerLocation = player.getClass().getMethod("getLocation").invoke(player);

                Class<?> bukkitAdapterClass = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter");
                Object adaptResult = bukkitAdapterClass.getMethod("adapt", Class.forName("org.bukkit.Location"))
                        .invoke(null, playerLocation);

                Method toVectorMethod = adaptResult.getClass().getMethod("toVector");
                Object vector = toVectorMethod.invoke(adaptResult);

                Method toBlockPointMethod = vector.getClass().getMethod("toBlockPoint");
                Object blockPoint = toBlockPointMethod.invoke(vector);

                Method getApplicableRegionsMethod = RegionManager.getClass().getMethod("getApplicableRegions",
                        Class.forName("com.sk89q.worldedit.math.BlockVector3"));
                Object applicableRegions = getApplicableRegionsMethod.invoke(RegionManager, blockPoint);
                Iterable<?> regionsIterable = (Iterable<?>) applicableRegions;

                Object flag = DependsLoader.FLAG_CARRIABLE;
                if (flag == null) return true;

                Class<?> stateClass = Class.forName("com.sk89q.worldguard.protection.flags.StateFlag$State");
                Field allowField = stateClass.getField("ALLOW");
                Object allowState = allowField.get(null);

                Class<?> protectedRegionClass = Class.forName("com.sk89q.worldguard.protection.regions.ProtectedRegion");
                Class<?> flagClass = Class.forName("com.sk89q.worldguard.protection.flags.Flag");
                Method getFlagMethod = protectedRegionClass.getMethod("getFlag", flagClass);

                for (Object region : regionsIterable) {
                    Object sf = getFlagMethod.invoke(region, flag);
                    if (sf == null) continue;
                    if (!sf.equals(allowState)) return false;
                }
                return true;
            }
            return true;

        } catch (ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return true;
        }
    }
}