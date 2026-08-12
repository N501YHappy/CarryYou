package xyz.n501yhappy.carryyou.depends;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;

public class ResidenceDepends extends DependLoader {

    public ResidenceDepends() {
        super("Residence");
    }

    @Override
    protected void onLoad() {
        try {
            FlagPermissions.addFlag("carriable");
        } catch (Exception e) {
            CarryYou.instance.getLogger().warning("§cResidence flag注册失败: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean check(Entity target, Player player) {
        ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(target);
        if (residence == null) return true;
        return residence.getPermissions().playerHas(player.getName(), "carriable", true);
    }
}
