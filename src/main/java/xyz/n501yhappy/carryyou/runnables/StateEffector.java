package xyz.n501yhappy.carryyou.runnables;

import adapts.impl.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.utils.StatePusher;

import java.util.UUID;

public class StateEffector implements Runnable { //用于给予玩家特殊状态
    private static Integer DURATION = 20;

    public StateEffector(int dur) {
        DURATION = dur;
    }

    @Override
    public void run() {
        for (UUID uuid : StatePusher.getWithChicken()) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity == null) {
                StatePusher.remove(uuid);
                continue;
            }
            if (!(entity instanceof LivingEntity)) continue;
            Version.getAdapts().EntityScheduler_execute(CarryYou.instance,entity,() -> {
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, DURATION+10, 1));
            });
        }
    }
}
