package nms.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.plugin.PluginLogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Version {
    private static TeleportImpl nmsTeleport;

    public static TeleportImpl getTeleport() {
        return nmsTeleport;
    }

    private static Boolean loaded = false;
    private static final Map<String, String> VERSION_TO_REVISION = new HashMap<String, String>() {
        {
            this.put("1.20", "MC1_20_R1");
            this.put("1.20.1", "MC1_20_R1");
            this.put("1.20.2", "MC1_20_R2");
            this.put("1.20.3", "MC1_20_R3");
            this.put("1.20.4", "MC1_20_R3");
            this.put("1.20.5", "MC1_20_R4");
            this.put("1.20.6", "MC1_20_R4");
            this.put("1.21", "MC1_21_R1");
            this.put("1.21.1", "MC1_21_R1");
            this.put("1.21.2", "MC1_21_R2");
            this.put("1.21.3", "MC1_21_R2");
            this.put("1.21.4", "MC1_21_R3");
            this.put("1.21.5", "MC1_21_R4");
            this.put("1.21.6", "MC1_21_R5");
            this.put("1.21.7", "MC1_21_R5");
            this.put("1.21.8", "MC1_21_R5");
            this.put("1.21.9", "MC1_21_R6");
            this.put("1.21.10", "MC1_21_R6");
            this.put("1.21.11", "MC1_21_R7");
            this.put("26.1", "MC26_1");
        }
    };
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
    private static void load(String nmsVersion) throws Throwable {
        String pkg = Version.class.getPackage().getName() + "." + nmsVersion;
        nmsTeleport = newInstance(pkg, "TeleportImpl");
    }
}
