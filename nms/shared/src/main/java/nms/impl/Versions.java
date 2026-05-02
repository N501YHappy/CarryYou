package nms.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Versions {

    // ============ 模板使用者 编辑引导区 ================

    private static ItemAdapter nmsItemAdapter;
    public static ItemAdapter getItemAdapter() {
        return nmsItemAdapter;
    }

    /**
     * 根据 NMS 版本，反射读取各个实现类
     */
    private static void load(String nmsVersion) throws Throwable {
        String pkg = Versions.class.getPackage().getName() + "." + nmsVersion;
        //nms.impl.
        nmsItemAdapter = newInstance(pkg, "ItemAdapterImpl");
    }

    // ================================================

    private static boolean loaded = false;
    // 1.20+ paper remapping org.bukkit.craftbukkit.v1_xx_Rx
    private static final Map<String, String> VERSION_TO_REVISION = new HashMap<String, String>() {{
        put("1.20", "v1_20_R1");
        put("1.20.1", "v1_20_R1");
        put("1.20.2", "v1_20_R2");
        put("1.20.3", "v1_20_R3");
        put("1.20.4", "v1_20_R3");
        put("1.20.5", "v1_20_R4");
        put("1.20.6", "v1_20_R4");
        put("1.21", "v1_21_R1");
        put("1.21.1", "v1_21_R1");
        put("1.21.2", "v1_21_R2");
        put("1.21.3", "v1_21_R2");
        put("1.21.4", "v1_21_R3");
        put("1.21.5", "v1_21_R4");
        put("1.21.6", "v1_21_R5");
        put("1.21.7", "v1_21_R5");
        put("1.21.8", "v1_21_R5");
        put("1.21.9", "v1_21_R6");
        put("1.21.10", "v1_21_R6");
        put("1.21.11", "v1_21_R7");
    }};

    public static boolean isLoaded() {
        return loaded;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean init(Logger logger) {
        if (loaded) return true;
        String nmsVersion = null;
        try {
            // noinspection JavaReflectionMemberAccess
            Item.class.getDeclaredMethod("getHealth");
            nmsVersion = "paper";
            logger.info("Found Minecraft: " + nmsVersion + "! Trying to find NMS support");
        } catch (ReflectiveOperationException ignored) {
        }
        if (nmsVersion == null) {
            // Thanks https://github.com/tr7zw/Item-NBT-API - MIT License
            try {
                String pkg = Bukkit.getServer().getClass().getPackage().getName();
                String ver = pkg.split("\\.")[3];
                logger.info("Found Minecraft: " + ver + "! Trying to find NMS support");
                nmsVersion = ver;
            } catch (Throwable e) {
                String bukkit = Bukkit.getServer().getBukkitVersion();
                int index = bukkit.indexOf('-');
                String ver = index > 4 ? bukkit.substring(0, index) : bukkit;
                nmsVersion = VERSION_TO_REVISION.getOrDefault(ver, "unknown");
                logger.info("Found Minecraft: " + ver + " (" + nmsVersion + ")! Trying to find NMS support");
            }
        }
        try {
            load(nmsVersion);
            loaded = true;
            logger.info("NMS support '" + nmsVersion + "' loaded!");
        } catch (Throwable t) {
            if (!(t instanceof ClassNotFoundException)) {
                StringWriter sw = new StringWriter();
                try (PrintWriter pw = new PrintWriter(sw)) {
                    t.printStackTrace(pw);
                }
                logger.warning(sw.toString());
            }
            logger.warning("This Server-Version(" + Bukkit.getServer().getBukkitVersion() + ", " + nmsVersion + ") is not supported by this plugin!");
        }
        return loaded;
    }

    private static <T> T newInstance(String pkg, String c) throws ReflectiveOperationException {
        Class<?> type = Class.forName(pkg + "." + c);
        return newInstance(type);
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T newInstance(Class<?> type) throws ReflectiveOperationException {
        return (T) type.getDeclaredConstructor().newInstance();
    }

    public static int getInt(Field field, Object obj) {
        try {
            return field.getInt(obj);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
