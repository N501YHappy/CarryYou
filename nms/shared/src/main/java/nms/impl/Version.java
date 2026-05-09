package nms.impl;

import org.bukkit.Bukkit;

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
            this.put("1.20", "v1_20_R1");
            this.put("1.20.1", "v1_20_R1");
            this.put("1.20.2", "v1_20_R2");
            this.put("1.20.3", "v1_20_R3");
            this.put("1.20.4", "v1_20_R3");
            this.put("1.20.5", "v1_20_R4");
            this.put("1.20.6", "v1_20_R4");
            this.put("1.21", "v1_21_R1");
            this.put("1.21.1", "v1_21_R1");
            this.put("1.21.2", "v1_21_R2");
            this.put("1.21.3", "v1_21_R2");
            this.put("1.21.4", "v1_21_R3");
            this.put("1.21.5", "v1_21_R4");
            this.put("1.21.6", "v1_21_R5");
            this.put("1.21.7", "v1_21_R5");
            this.put("1.21.8", "v1_21_R5");
            this.put("1.21.9", "v1_21_R6");
            this.put("1.21.10", "v1_21_R6");
            this.put("1.21.11", "v1_21_R7");
            this.put("26.1", "v26_1");
            this.put("26.1.1", "v26_1");
            this.put("26.1.2", "v26_1");
        }
    };
    @SuppressWarnings("UnusedReturnValue")
    public static boolean init(Logger logger) {
        if (loaded) return true;
        String nmsVersion = null;
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        logger.info(pkg);
        if (nmsVersion == null) {
            // Thanks https://github.com/tr7zw/Item-NBT-API - MIT License
            try {
                String ver = pkg.split("\\.")[3];
                logger.info("Found Minecraft: " + ver + "! Trying to find NMS support");
                nmsVersion = ver;
            } catch (Throwable e) {
                String bukkit = Bukkit.getServer().getBukkitVersion();
                logger.info("The bukkit version is " + bukkit);
                int index = bukkit.indexOf('-');
                String ver = index != -1 ? bukkit.substring(0, index) : bukkit;
                nmsVersion = VERSION_TO_REVISION.getOrDefault(ver, "unknown");
                logger.info("Found Minecraft: " + ver + " (" + nmsVersion + ")! Trying to find NMS support");
            }
        }
        try {
            load(nmsVersion,logger);
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
    private static void load(String nmsVersion,Logger logger) throws Throwable {
        String pkg = Version.class.getPackage().getName() + "." + nmsVersion;
        logger.info(pkg);
        nmsTeleport = newInstance(pkg, "TeleportAdapterImpl");
    }
}
