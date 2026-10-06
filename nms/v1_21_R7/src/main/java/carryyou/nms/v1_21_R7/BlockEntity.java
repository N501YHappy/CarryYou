package carryyou.nms.v1_21_R7;

import carryyou.nms.entitys.BlockEntityImpl;
import net.minecraft.core.Rotations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BlockEntity extends ArmorStand implements BlockEntityImpl {
    private Player player;
    private ItemStack itemStack;

    private boolean summoned = false;


    public BlockEntity(Player player, ItemStack block) {
        super(EntityType.ARMOR_STAND, ((CraftWorld) player.getLocation().getWorld()).getHandle());
        this.player = player;
        this.itemStack = block;
        Location location = player.getLocation();
        setPos(location.x(), location.y(), location.z());

        setItemSlot(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.fromBukkitCopy(this.itemStack));


        for (int i = 0; i <= 16; i += 8){  // https://minecraft.wiki/w/Armor_Stand
            disabledSlots |= (1 << EquipmentSlot.HEAD.getFilterBit(i));
            disabledSlots |= (1 << EquipmentSlot.FEET.getFilterBit(i));
            disabledSlots |= (1 << EquipmentSlot.CHEST.getFilterBit(i));
            disabledSlots |= (1 << EquipmentSlot.LEGS.getFilterBit(i));
            disabledSlots |= (1 << EquipmentSlot.SADDLE.getFilterBit(i));
        }

        setInvulnerable(true);
        setInvisible(true);
        setSilent(true);
        setNoGravity(false);
        setSmall(true);
        setHeadPose(new Rotations(180, 0, 0));
        persist = false;
    }

    public BlockEntity(EntityType<? extends ArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    public void kill(ServerLevel level) {
    }


    @Override
    public UUID getUniqueId() {
        return getBukkitEntity().getUniqueId();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public Entity get(Location location) {
        if (!summoned) summon(location);
        return getBukkitEntity();
    }

    private boolean summon(Location location) {
        return summoned = ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this);
    }
}
