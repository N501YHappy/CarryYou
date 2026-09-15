package carryyou.nms;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockEntity extends ArmorStand {
    private Player player;

    public BlockEntity(Player player, ItemStack block) {
        super(EntityType.ARMOR_STAND, ((CraftWorld) player.getLocation().getWorld()).getHandle());
        this.player = player;
        Location location = player.getLocation();
        setPos(location.x(),location.y(),location.z());
        setItemSlot(EquipmentSlot.HEAD, net.minecraft.world.item.ItemStack.fromBukkitCopy(block));
        disabledSlots |= 1 << EquipmentSlot.HEAD.getFilterBit(0);
        // https://www.digminecraft.com/data_tags/armor_stand.php
        setInvulnerable(true);
        setNoGravity(true);
        setInvisible(true);
        setSilent(true);
        setNoBasePlate(true);
        setMarker(true);
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
    public void move(MoverType type, Vec3 movement) {}

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    public void kill(ServerLevel level) {}
}
