package xyz.n501yhappy.carryyou.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldown {
    private Map<UUID,Long> last_action = new ConcurrentHashMap<>();
    private int cooldown = 1000;
    public Cooldown(int cooldown){
        this.cooldown = cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean checkCooldown(UUID uuid){
        long now = System.currentTimeMillis();
        Long last = last_action.get(uuid);

        if (last == null) { // 第一次使用无冷却
            return true;
        }

        if (now - last >= cooldown) {
            return true;
        }
        return false;
    }
    public void updateCooldown(UUID uuid){
        long now = System.currentTimeMillis();
        last_action.put(uuid, now);
    }

    public Integer getRemains(UUID uuid){
        Long last = last_action.get(uuid);
        if (last == null) {
            return 0;
        }
        long remaining = cooldown - (System.currentTimeMillis() - last);
        return (int) Math.max(remaining, 0);
    }
}