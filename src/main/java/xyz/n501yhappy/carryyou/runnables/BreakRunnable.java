package xyz.n501yhappy.carryyou.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.n501yhappy.carryyou.utils.CarryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreakRunnable extends BukkitRunnable {
    private static Map<UUID, Integer> score = new HashMap<>();
    private static Map<UUID, Long> lastActionTime = new HashMap<>(); // 记录最后一次操作的tick
    private static final int TARGET_SCORE = 20;
    private static final int TIMEOUT_TIME = 2*1000;
    private static final int PROGRESS_BAR_LENGTH = 20;

    private static long lastSub = 0;//上一次扣分的时间  2ticks = 100ms

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        UUID[] playerUUIDs = score.keySet().toArray(new UUID[0]);
        int koufen = 0; //对，扣分
        if (currentTime - lastSub > 200) {
            lastSub = currentTime;
            koufen = 1;
        }
        for (UUID playerUUID : playerUUIDs) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null || !player.isOnline() || !CarryManager.isCarried(playerUUID)) {
                removePlayer(playerUUID);
                continue;
            }

            Integer currentScore = score.get(playerUUID);
            Long lastTick = lastActionTime.get(playerUUID);

            if (currentScore == null || lastTick == null) continue;

            //扣分！
            currentScore -= koufen * (currentScore != 0 ? 1 : 0); //三元运算符很好用你知道吗(bushi
            score.put(playerUUID,currentScore);

            //不想玩啦？
            if (currentTime - lastTick > TIMEOUT_TIME) {
                player.sendTitle("", "", 0, 0, 0);
                removePlayer(playerUUID);
                continue;
            }

            // 显示进度条
            String progressBar = getProgressBar(currentScore, TARGET_SCORE);
            player.sendTitle(progressBar, "§e快速点击左键挣脱！", 0, 20, 10);
            
            //好耶！
            if (currentScore >= TARGET_SCORE) {
                Entity carrier = CarryManager.getCarrierEntityByTarget(playerUUID);
                if (carrier != null) {
                    player.sendTitle("§7(§a♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥§7)","§e快速点击左键挣脱！", 0, 20, 10);
                    CarryManager.drop(player);
                }
                removePlayer(playerUUID);
            }
        }
    }

    public static void addScore(UUID uuid) {
        int newScore = score.getOrDefault(uuid, 0) + 1;
        score.put(uuid, newScore);
        lastActionTime.put(uuid,System.currentTimeMillis());
    }

    public static void removePlayer(UUID uuid) {
        score.remove(uuid);
        lastActionTime.remove(uuid);
    }

    private static String getProgressBar(Integer score, Integer total) {
        if (score == null || total == null || total <= 0) {
            return "§7(§c坏掉了！§7)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("§7(");

        int needed = (int) (PROGRESS_BAR_LENGTH * ((double) score / total));

        sb.append("§c");
        for (int i = 1; i <= needed; i++) {
            sb.append("♥");
        }

        sb.append("§c");
        for (int i = needed + 1; i <= PROGRESS_BAR_LENGTH; i++) {
            sb.append("♡");
        }

        sb.append("§7)");
        return sb.toString();
    }
}