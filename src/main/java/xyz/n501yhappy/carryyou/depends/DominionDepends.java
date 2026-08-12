package xyz.n501yhappy.carryyou.depends;

import cn.lunadeer.dominion.api.DominionAPI;
import cn.lunadeer.dominion.api.dtos.DominionDTO;
import cn.lunadeer.dominion.api.dtos.flag.Flags;
import cn.lunadeer.dominion.api.dtos.flag.PriFlag;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.n501yhappy.carryyou.CarryYou;

public class DominionDepends extends DependLoader {
    private DominionAPI dominionAPI;
    private PriFlag carriableFlag;

    public DominionDepends() {
        super("Dominion");
    }

    @Override
    protected void onLoad() throws Exception {
        dominionAPI = DominionAPI.getInstance();
        carriableFlag = new PriFlag("carriable", "可使用抓举", "在你的领地内，其他人可以抓举生物", false, true, Material.LEAD);
        Flags.registerPriFlag(CarryYou.instance, carriableFlag);
        try {
            Flags.applyNewCustomFlags();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean check(Entity target, Player player) {
        DominionDTO dominion = dominionAPI.getDominion(target.getLocation());
        if (dominion == null) return true;
        return dominionAPI.checkPrivilegeFlagSilence(
                target.getLocation(),
                carriableFlag,
                player
        );
    }
}
