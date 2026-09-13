package xyz.n501yhappy.carryyou.depends.residence;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.locales.MessageInfo;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MethodProvider implements ResidenceMethods {
    private final Logger logger = CarryYou.getInstance().getLogger();

    @Override 
    public boolean check(Entity target, Player player) {
        ClaimedResidence residence = Residence.getInstance().getResidenceManager().getByLoc(target);
        if (residence == null) {
            return true;
        }
        return residence.getPermissions().playerHas(player.getName(), "carriable", true);
    }

    @Override 
    public void registerFlag() {
        try {
            FlagPermissions.addFlag("carriable");
        } catch (Exception e) {
            this.logger.log(Level.WARNING, ChatColor.RED + MessageInfo.current().anyError(), e);
        }
    }
}
