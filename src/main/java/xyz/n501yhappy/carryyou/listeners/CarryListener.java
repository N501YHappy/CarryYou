package xyz.n501yhappy.carryyou.listeners;

import carryyou.nms.entitys.BlockEntityFactory;
import carryyou.nms.entitys.BlockEntityImpl;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.n501yhappy.carryyou.configs.ConfigLoader;
import xyz.n501yhappy.carryyou.services.CarryBlockService;
import xyz.n501yhappy.carryyou.utils.CarryManager;
import xyz.n501yhappy.carryyou.utils.Checkers;
import xyz.n501yhappy.carryyou.utils.Cooldown;
import xyz.n501yhappy.carryyou.utils.state.ChickenState;

import java.util.UUID;

public class CarryListener implements Listener {
    private final CarryManager carryManager = CarryManager.getInstance();

    private static final double MAX_RAY_DISTANCE = 3;
    private static final double MAX_RAY_DISTANCE_CREATIVE = MAX_RAY_DISTANCE + 2;

    private static final Cooldown carryCooldown = new Cooldown(ConfigLoader.COOLDOWN);
    private static final Cooldown CDCooldown = new Cooldown(100);

    public static void setCarryCooldown(int cooldown) {
        CarryListener.carryCooldown.setCooldown(cooldown);
    }

    @EventHandler
    public void onActive(PlayerSwapHandItemsEvent event) {
        if (!ConfigLoader.TRIGGER_SHIFT_F) return; // 用shift+f进行触发时
        if (!event.getPlayer().isSneaking()) return;
        onCarry(event);
    }

    @EventHandler
    public void onActive(PlayerInteractEvent event) { // 用于判断右键方块
        if (ConfigLoader.TRIGGER_SHIFT_F) return; // 用shift+right click进行触发时

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        // 左右手都会触发，这里做一下过滤
        if (!event.getPlayer().isSneaking()) return;
        onCarry(event);
    }

    @EventHandler
    public void onActive(PlayerInteractEntityEvent event) { //用于判断右键实体
        if (ConfigLoader.TRIGGER_SHIFT_F) return; // 用shift+right click进行触发时

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        // 左右手都会触发，这里做一下过滤
        if (!event.getPlayer().isSneaking()) return;
        onCarry(event);
    }

    private <T extends PlayerEvent & Cancellable> void onCarry(T event){
        Player player = event.getPlayer();
        if (ConfigLoader.TRIGGER_EMPTY && !(player.getEquipment().getItemInMainHand() == null || player.getEquipment().getItemInMainHand().getType() == Material.AIR)){
            return;
        }
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        event.setCancelled(true);
        if (carryManager.isCarrying(player.getUniqueId())) {
            return;
        }
        Object target = getTargetEntity(player);
        player.sendMessage(String.valueOf(target == null));

        if(target == null) target = getTargetBlock(player);
        if(target == null) return;

        Entity carrier;
        CarryBlockService.CarriedBlock carriedBlock = null;

        if (target instanceof Entity){
            if (!isValidTarget(player, (Entity) target)) return;
            if (!carryManager.checkCarry(player, (Entity) target,carryCooldown)) return;
            carrier = (Entity) target;
        }else {
            Block block = (Block) target;
            ItemStack itemStack= new ItemStack(block.getType());
            BlockEntityImpl blockEntity = BlockEntityFactory.getInstance().create(player,itemStack);
            if (blockEntity == null) return;
            carrier = blockEntity.get(block.getLocation());
            carriedBlock = new CarryBlockService.CarriedBlock(
                    player.getUniqueId(),
                    blockEntity,
                    block.getBlockData().clone()
            );
        }

        if (!handlePickup(player, carrier)){
            if (carriedBlock != null) carrier.remove();
            return;
        }
        if (carriedBlock != null) {
            CarryBlockService.getInstance().register(carrier.getUniqueId(), carriedBlock);
        }
    }
    private boolean isValidTarget(Player player, Entity target) {
        if(target == null) return false;
        if(target.getUniqueId().equals(player.getUniqueId())) return false;
        if(carryManager.isCarried(target.getUniqueId())) return false;
        if(target instanceof LivingEntity) return true;
        if(target instanceof TNTPrimed) return true;
        if(target instanceof WitherSkull) return true;
        if(target.getType().getName().contains("fireball")) return true;
        if(target.getType().getName().contains("minecart")) return true;
        if(target.getType().getName().contains("boat")) return true;
        return target.getType().getName().contains("wind");
    }

    private boolean handlePickup(Player player,Entity target) {
        if (carryManager.carry(player, target)){
            carryCooldown.updateCooldown(player.getUniqueId());
            CDCooldown.updateCooldown(player.getUniqueId());
            return true;
        }
        return false;
    }
    @EventHandler
    public void onDrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!carryManager.isCarrying(player.getUniqueId())) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            throwEntity(player, ConfigLoader.THROW_POWER_ATTACK,event);
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.isSneaking()) return; //防止与抓举冲突

            throwEntity(player, ConfigLoader.THROW_POWER_INTERACT,event);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!carryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_ATTACK,event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) return; //防止与抓举冲突
        if (!carryManager.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_INTERACT,event);
    }

    private <T extends Cancellable> void throwEntity(Player player, double power,T event) {
        if (!CDCooldown.checkCooldown(player.getUniqueId())) return;
        UUID targetUUID = carryManager.getTargetByCarrier(player.getUniqueId());
        if (targetUUID == null){
            return;
        }
        Entity target = Bukkit.getEntity(targetUUID);
        if (target == null){
            carryManager.remove(player.getUniqueId(),targetUUID);
            return;
        }
        event.setCancelled(true);
        carryManager.drop(target, power,true);
        CDCooldown.updateCooldown(player.getUniqueId());
    }


    private RayTraceResult traceResult(Player player){
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        RayTraceResult result = player.getWorld().rayTrace(
                eyeLocation,
                direction,
                (player.getGameMode() == GameMode.CREATIVE ? MAX_RAY_DISTANCE_CREATIVE : MAX_RAY_DISTANCE),
                FluidCollisionMode.NEVER,
                true,
                0.1,
                entity -> !entity.equals(player) && !entity.isDead()
        );
        return result;
    }

    private Entity getTargetEntity(Player player) {
        RayTraceResult result = traceResult(player);
        if(result == null) return null;
        return result.getHitEntity();
    }

    private Block getTargetBlock(Player player) {
        RayTraceResult result = traceResult(player);
        if(result == null) return null;
        return result.getHitBlock();
    }
}
