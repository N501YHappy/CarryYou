package xyz.n501yhappy.carryyou.utils;

import xyz.n501yhappy.carryyou.configs.ConfigLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cooldown {
    private static Map<UUID,Long> last_action = new HashMap<>();

    public static boolean checkCooldown(UUID uuid){
        long now = System.currentTimeMillis();
        Long last = last_action.get(uuid);

        if (last == null) { // 第一次使用无冷却
            last_action.put(uuid, now);
            return true;
        }

        if (now - last >= ConfigLoader.COOLDOWN) {
            last_action.put(uuid, now);
            return true;
        }
        return false;
    }

    public static Integer getRemains(UUID uuid){
        Long last = last_action.get(uuid);
        if (last == null) {
            return 0;
        }
        long remaining = ConfigLoader.COOLDOWN - (System.currentTimeMillis() - last);
        return (int) Math.max(remaining, 0);
    }
}