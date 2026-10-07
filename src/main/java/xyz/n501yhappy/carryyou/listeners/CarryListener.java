package xyz.n501yhappy.carryyou.listeners;

import carryyou.nms.entitys.BlockEntityFactory;
import carryyou.nms.entitys.BlockEntityImpl;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
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
import xyz.n501yhappy.carryyou.services.CarryService;
import xyz.n501yhappy.carryyou.utils.Cooldown;

import java.util.UUID;

public class CarryListener implements Listener {
    private final CarryService carryService = CarryService.getInstance();
    private final CarryBlockService carryBlockService = CarryBlockService.getInstance();


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
        if (!(player.getEquipment().getItemInMainHand().getType() == Material.AIR)){
            return;
        }
        if (player.getGameMode() == GameMode.SPECTATOR) return;
        if (carryService.isCarrying(player.getUniqueId()))  return;
        Object target = getTargetEntity(player);

        if(target == null) target = getTargetBlock(player);
        if(target == null) return;

        Entity carrier;
        CarryBlockService.CarriedBlock carriedBlock = null;
        Block carriedSource = null;

        if (target instanceof Entity){
            if (!isValidTarget(player, (Entity) target)) return;
            if (!carryService.checkCarry(player, (Entity) target,carryCooldown)) return;
            carrier = (Entity) target;
        }else {
            Block block = (Block) target;
            carriedSource = block;
            if(!isValidBlock(block)) return;
            ItemStack itemStack= new ItemStack(block.getType());
            BlockEntityImpl blockEntity = BlockEntityFactory.getInstance().create(player,itemStack);
            if (blockEntity == null) return;
            carrier = blockEntity.get(block.getLocation());

            carriedBlock = getCarriedBlock(block,player.getUniqueId());

            block.setType(Material.AIR);

        }
        event.setCancelled(true);
        if (!handlePickup(player, carrier)){
            if (carriedBlock != null) {
                carrier.remove();
                // 抱起失败时把箱子原样放回，避免方块和物品凭空丢失
                applyCarriedBlock(carriedSource, carriedBlock);
            }
            return;
        }
        if (carriedBlock != null) {
            CarryBlockService.getInstance().register(carrier.getUniqueId(), carriedBlock);
        }
    }
    private boolean isValidTarget(Player player, Entity target) {
        if(target == null) return false;
        if(target.getUniqueId().equals(player.getUniqueId())) return false;
        if(carryService.isCarried(target.getUniqueId())) return false;
        if(target instanceof LivingEntity) return true;
        if(target instanceof TNTPrimed) return true;
        if(target instanceof WitherSkull) return true;
        if(target.getType().getName().contains("fireball")) return true;
        if(target.getType().getName().contains("minecart")) return true;
        if(target.getType().getName().contains("boat")) return true;
        return target.getType().getName().contains("wind");
    }
    private boolean isValidBlock(Block block){
        if (block == null) return false;
        Material type = block.getType();
        if (type == Material.CHEST) return true;
        return false;
    }

    private boolean handlePickup(Player player,Entity target) {
        if (carryService.carry(player, target)){
            carryCooldown.updateCooldown(player.getUniqueId());
            CDCooldown.updateCooldown(player.getUniqueId());
            return true;
        }
        return false;
    }
    @EventHandler
    public void onDrop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!carryService.isCarrying(player.getUniqueId())) return;
        if(carryService.isCarryingBlock(player.getUniqueId())){
            throwBlock(player,event.getClickedBlock(),event.getBlockFace(),event);
        }else{
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                throwEntity(player, ConfigLoader.THROW_POWER_ATTACK,event);
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (player.isSneaking()) return; //防止与抓举冲突

                throwEntity(player, ConfigLoader.THROW_POWER_INTERACT,event);
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!carryService.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_ATTACK,event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) return; //防止与抓举冲突
        if (!carryService.isCarrying(player.getUniqueId())) return;
        throwEntity(player, ConfigLoader.THROW_POWER_INTERACT,event);
    }

    private <T extends Cancellable> void throwEntity(Player player, double power,T event) {
        if (!CDCooldown.checkCooldown(player.getUniqueId())) return;
        UUID targetUUID = carryService.getTargetByCarrier(player.getUniqueId());
        if (targetUUID == null){
            return;
        }
        Entity target = Bukkit.getEntity(targetUUID);
        if (target == null){
            carryService.remove(player.getUniqueId(),targetUUID);
            return;
        }
        event.setCancelled(true);
        carryService.drop(target, power,true);
        CDCooldown.updateCooldown(player.getUniqueId());
    }

    private <T extends Cancellable> void throwBlock(Player player,Block block,BlockFace face, T event) {
        if (!CDCooldown.checkCooldown(player.getUniqueId())) return;
        if(block == null) return;
        UUID carried = carryService.getTargetByCarrier(player.getUniqueId());
        Block target_block = player.getWorld().getBlockAt(block.getLocation().add(face.getDirection()));
        CarryBlockService.CarriedBlock carried_block = carryBlockService.get(carried);

        applyCarriedBlock(target_block,carried_block);

        carryBlockService.remove(carried);
        event.setCancelled(true);
        carryService.remove(player.getUniqueId(),carried);
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

    private CarryBlockService.CarriedBlock getCarriedBlock(Block block,UUID player){
        CarryBlockService.CarriedBlock result = null;

        BlockState block_state = block.getState();
        BlockData block_data = block.getBlockData();
        ItemStack[] contents = null;
        if(block_state instanceof org.bukkit.block.Chest chest){
            Chest chest_data = (Chest) block_data;
            chest_data.setType(Chest.Type.SINGLE);
            contents = chest.getBlockInventory().getContents();
        }
        result = new CarryBlockService.CarriedBlock(
                player,
                block_data,
                contents
        );

        return result;
    }

    private void applyCarriedBlock (Block block, CarryBlockService.CarriedBlock carried_block){
        block.setBlockData(carried_block.blockData());
        // setBlockData 不会刷新 Block 对象缓存的 BlockData，必须重新获取，
        // 否则 getState() 拿到的是旧方块的快照，箱子物品无法写回
        BlockState state = block.getWorld().getBlockAt(block.getLocation()).getState();
        if (state instanceof org.bukkit.block.Chest chest) {
            chest.getBlockInventory().setContents(carried_block.contents());
        }
    }

}

