package carryyou.nms.entitys;

import carryyou.nms.NMSLoader;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class BlockEntityFactory {
    private static BlockEntityFactory instance;

    public static BlockEntityFactory getInstance() {
        if(instance == null){
            instance = new BlockEntityFactory();
        }
        return instance;
    }
    private BlockEntityFactory(){}

    public BlockEntityImpl create(Player player, ItemStack block){
        String nmsPath = NMSLoader.getNMSPath();
        return newInstance(nmsPath + ".BlockEntity",player,block);
    }
    private BlockEntityImpl newInstance(String classPath, Player player, ItemStack block){
        try {
            Class<?> clazz = Class.forName(classPath);
            return (BlockEntityImpl) clazz.getConstructor(Player.class, ItemStack.class).newInstance(player, block);
        } catch (ReflectiveOperationException e) {
            NMSLoader.getLogger().log(Level.WARNING, "Failed to create NMS BlockEntity: " + classPath, e);
            return null;
        }
    }
}
