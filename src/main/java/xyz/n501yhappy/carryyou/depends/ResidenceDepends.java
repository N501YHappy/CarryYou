package xyz.n501yhappy.carryyou.depends;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.bekvon.bukkit.residence.protection.ResidencePermissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;

public class ResidenceDepends {

    public static void load() {
        CarryYou.residence_enable = CarryYou.instance.getServer().getPluginManager().isPluginEnabled("Residence");
        if (!CarryYou.residence_enable) return;

        try {
            FlagPermissions.addFlag("carriable");
        } catch (Exception e) {
            CarryYou.residence_enable = false;
            CarryYou.instance.getLogger().warning("§cResidence flag注册失败: " + e.getMessage());
        }
    }

    public static boolean check(Entity target, Player player) {
        ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(target);
        if (residence == null) return true;
        ResidencePermissions permissions = residence.getPermissions();
        return permissions.playerHas(player.getName(), "carriable", true);
    }
}
