package carryyou.nms;

import carryyou.nms.entitys.BlockEntityFactory;
import org.bukkit.Bukkit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

//Thanks https://github.com/MrXiaoM/nms-template

public class NMSLoader {

    private static String NMSPath;
    private static Logger logger;

    public static String getNMSPath() {
        return NMSPath;
    }

    public static Logger getLogger() {
        return logger;
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
        put("26.1", "v26_1");
        put("26.1.1", "v26_1");
        put("26.1.2", "v26_1");
    }};

    public static boolean isLoaded() {
        return loaded;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean init(Logger logger) {
        if (loaded) return true;
        NMSLoader.logger = logger;
        String nmsVersion;
        try {
            String pkg = Bukkit.getServer().getClass().getPackage().getName();
            String ver = pkg.split("\\.")[3];
            logger.info("Found Minecraft: " + ver + "! Trying to find NMS support");
            nmsVersion = VERSION_TO_REVISION.getOrDefault(ver, "unknown");
        } catch (Throwable e) {
            String bukkit = Bukkit.getServer().getBukkitVersion();
            int index = bukkit.indexOf('-');
            String ver = index != -1 ? bukkit.substring(0, index) : bukkit;
            nmsVersion = VERSION_TO_REVISION.getOrDefault(ver, "unknown");
            logger.info("Found Minecraft: " + ver + " (" + nmsVersion + ")! Trying to find NMS support");
        }
        loaded = true;
        NMSPath = NMSLoader.class.getPackage().getName() + "." + nmsVersion;
        return loaded ;
    }
}
