package xyz.n501yhappy.carryyou.depends.dominion;

import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.api.dtos.flag.PriFlag;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;
import xyz.n501yhappy.carryyou.services.MessageService;

import java.util.logging.Level;


public class MethodProvider implements DominionMethods {
    private DominionAPI dominionAPI;
    private PriFlag carriableFlag;
    private final MessageService messageService = MessageService.getInstance();

    @Override
    public void registerFlag() {
        this.dominionAPI = DominionAPI.getInstance();
        this.carriableFlag = new PriFlag("carriable", messageService.getMessage("Flags.flag-carry"), messageService.getMessage("Flags.flag-territory"), false, true, Material.LEAD);
        Flags.registerPriFlag(CarryYou.instance, this.carriableFlag);
        try {
            Flags.applyNewCustomFlags();
        } catch (Exception e) {
            messageService.log(Level.WARNING, "Plugin.plugin-error", e);
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
