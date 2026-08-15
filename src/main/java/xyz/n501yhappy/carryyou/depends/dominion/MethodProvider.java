package xyz.n501yhappy.carryyou.depends.dominion;

import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.api.dtos.flag.PriFlag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.locales.MessageInfo;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MethodProvider implements DominionMethods {
    private DominionAPI dominionAPI;
    private PriFlag carriableFlag;
    private final Logger logger = CarryYou.getInstance().getLogger();

    @Override 
    public void registerFlag() {
        this.dominionAPI = DominionAPI.getInstance();
        this.carriableFlag = new PriFlag("carriable", MessageInfo.current().FlagInfo()[0], MessageInfo.current().FlagInfo()[1], false, true, Material.LEAD);
        Flags.registerPriFlag(CarryYou.instance, this.carriableFlag);
        try {
            Flags.applyNewCustomFlags();
        } catch (Exception e) {
            this.logger.log(Level.WARNING, ChatColor.RED + MessageInfo.current().anyError(), e);
        }
    }

    @Override 
    public boolean check(Entity target, Player player) {
        DominionDTO dominion = this.dominionAPI.getDominion(target.getLocation());
        if (dominion == null) {
            return true;
        }
        return this.dominionAPI.checkPrivilegeFlagSilence(target.getLocation(), this.carriableFlag, player);
    }
}
