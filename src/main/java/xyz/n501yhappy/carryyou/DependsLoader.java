package xyz.n501yhappy.carryyou;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DependsLoader {
    public static Boolean worldguard_enabled = false;
    public static Boolean residence_enabled = false;

    public static Object FLAG_CARRIABLE; // 我自己的旗帜！

    private static Class<?> WorldGuard;
    private static Class<?> Flag;
    private static Class<?> StateFlag;
    private static Class<?> FlagRegistry;

    private static Class<?> FlagPermissions;

    private static Method getInstanceMethod;
    private static Method getFlagRegistryMethod;
    private static Method registerMethod;
    private static Method getFlagMethod;

    private static Method addFlag;

    private static Constructor<?> stateFlagConstructor;

    //初始化
    static {
        //worldguard的小东西
        try {
            WorldGuard = Class.forName("com.sk89q.worldguard.WorldGuard");
            Flag = Class.forName("com.sk89q.worldguard.protection.flags.Flag");
            StateFlag = Class.forName("com.sk89q.worldguard.protection.flags.StateFlag");
            FlagRegistry = Class.forName("com.sk89q.worldguard.protection.flags.registry.FlagRegistry");


            getInstanceMethod = WorldGuard.getMethod("getInstance");
            getFlagRegistryMethod = WorldGuard.getMethod("getFlagRegistry");
            registerMethod = FlagRegistry.getMethod("register", Flag);
            getFlagMethod = FlagRegistry.getMethod("get", String.class);
            stateFlagConstructor = StateFlag.getConstructor(String.class, boolean.class);

            worldguard_enabled = true;
        } catch (ClassNotFoundException | NoSuchMethodException | UnsupportedClassVersionError e) {
            worldguard_enabled = false;
        }
    }

    public static void loadWGDepends() {
        if (!worldguard_enabled) return;
        try {
            CarryYou.instance.getLogger().info("§a看到了！看到小Guard了！我要去找她玩了！");
            registerFlag();

        } catch (Exception e) {
            CarryYou.instance.getLogger().warning("§c小guard哈气了！她打我了: " + e.getMessage());
            worldguard_enabled = false; //和guard绝交了QAQ
            e.printStackTrace();
        }
    }

    private static void registerFlag() throws Exception {
        Object worldguardInstance = getInstanceMethod.invoke(null);
        Object registry = getFlagRegistryMethod.invoke(worldguardInstance);
        Object flag = stateFlagConstructor.newInstance("carriable", true);

        try {
            registerMethod.invoke(registry, flag);
            FLAG_CARRIABLE = flag;

        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null && cause.getClass().getName()
                    .equals("com.sk89q.worldguard.protection.flags.registry.FlagConflictException")) {
                Object existing = getFlagMethod.invoke(registry, "carriable");
                if (existing != null && StateFlag.isInstance(existing)) {
                    FLAG_CARRIABLE = existing;
                    CarryYou.instance.getLogger().info("§a获取到已存在的 flag: carriable");
                } else {
                    CarryYou.instance.getLogger().warning("§6有别的flag叫这个名字但不是StateFlag...");
                }
            } else {
                throw e;
            }
        }
    }
    public static void loadResDepends(){
        //residence的小东西 放到这里加载是因为res起的比较晚...
        //CarryYou.instance.getLogger().info("RES我求你了，只要你能加载成功我什么都会做的QAQ");
        try {
            FlagPermissions = Class.forName("com.bekvon.bukkit.residence.protection.FlagPermissions");
            addFlag = FlagPermissions.getMethod("addFlag",String.class);

            residence_enabled = true;

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            residence_enabled = false;
        }
        if (!residence_enabled) return;
        try {
            CarryYou.instance.getLogger().info("§aRes!我在这里");
            addFlag.invoke(null,"carriable");
        } catch (IllegalAccessException | InvocationTargetException e) {
            residence_enabled = false; // 绝！交！
        }
    }
}