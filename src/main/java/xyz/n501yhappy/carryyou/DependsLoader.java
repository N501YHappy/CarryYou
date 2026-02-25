package xyz.n501yhappy.carryyou;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class DependsLoader {
    public static Boolean worldguard_enabled = false;

    public static StateFlag FLAG_CARRIABLE; //我自己的旗帜！
    public static void loadWGDepends(){
        if (worldguard_enabled) {
            CarryYou.instance.getLogger().info("§a看到了！看到小Guard了！我要去找她玩了！");

            try {
                FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

                StateFlag flag = new StateFlag("carriable", true);
                registry.register(flag);
                FLAG_CARRIABLE = flag;

            } catch (FlagConflictException e) {
                Flag<?> existing = WorldGuard.getInstance().getFlagRegistry().get("carriable");

                if (existing instanceof StateFlag) {
                    FLAG_CARRIABLE = (StateFlag) existing;
                } else {
                    CarryYou.instance.getLogger().warning("§6小Guard不想陪我玩...有别的flag叫这个名字但不是StateFlag...");
                }
            } catch (Exception e) {
                CarryYou.instance.getLogger().warning("§c小guard哈气了！她打我了: " + e.getMessage());
            }
        } else {
            CarryYou.instance.getLogger().warning("§c没找到小Guard...我一个人也很好...");
        }
    }
}
