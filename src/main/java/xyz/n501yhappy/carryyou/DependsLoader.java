package xyz.n501yhappy.carryyou;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.bekvon.bukkit.residence.protection.FlagPermissions;

import static xyz.n501yhappy.carryyou.CarryYou.worldguard_enabled;
import static xyz.n501yhappy.carryyou.CarryYou.residence_enabled;

public class DependsLoader { //这里用来加载依赖的一些小东西

    public static StateFlag FLAG_CARRIABLE; // 我自己的旗帜！
    public static void loadWGDepends(){
        if (!worldguard_enabled) return;
        try {
            CarryYou.instance.getLogger().info("§a看到了！看到小Guard了！我要去找她玩了！");
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

            try {
                StateFlag flag = new StateFlag("carriable", true);
                registry.register(flag);
                FLAG_CARRIABLE = flag;

            } catch (FlagConflictException e) {
                Flag<?> existing = registry.get("carriable");
                if (existing instanceof StateFlag) {
                    FLAG_CARRIABLE = (StateFlag) existing;
                } else {
                    CarryYou.instance.getLogger().warning("§6有别的flag叫这个名字但不是StateFlag...");
                }
            } catch (Exception e) {
                CarryYou.instance.getLogger().warning("§c哇啊啊啊啊啊: " + e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            CarryYou.instance.getLogger().warning("§c小guard哈气了！她打我了: " + e.getMessage());
            worldguard_enabled = false; // 和guard绝交了QAQ
            e.printStackTrace();
        }
    }

    public static void loadResDepends(){
        // Residence的小东西 放到这里加载是因为res起的比较晚...
        residence_enabled = CarryYou.instance.getServer().getPluginManager().getPlugin("Residence").isEnabled();
        if (!residence_enabled) return;

        try {
            CarryYou.instance.getLogger().info("§aRes!我在这里");
            FlagPermissions.addFlag("carriable");
        } catch (Exception e) {
            residence_enabled = false; // 绝！交！
            CarryYou.instance.getLogger().warning("§cResidence flag注册失败: " + e.getMessage());
        }
    }
}