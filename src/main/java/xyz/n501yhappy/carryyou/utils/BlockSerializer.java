package xyz.n501yhappy.carryyou.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class BlockSerializer {
    public static String serializer(ItemStack itemStack){
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(outputStream)){
            bukkitStream.writeObject(itemStack);
            bukkitStream.flush();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            CarryLogger.error("Failed to serialize item",e);
            throw new IllegalStateException(e);
        }
    }

    public static ItemStack deserializer(String base64String){
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64String));
             BukkitObjectInputStream bukkitStream = new BukkitObjectInputStream(inputStream)){
            inputStream.readAllBytes();
            return (ItemStack) bukkitStream.readObject();
        } catch (Exception e) {
            CarryLogger.error("Failed to deserialize item",e);
            throw new IllegalStateException(e);
        }
    }
}
