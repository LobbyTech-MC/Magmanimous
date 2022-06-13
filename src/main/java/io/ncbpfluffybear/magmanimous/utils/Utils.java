package io.ncbpfluffybear.magmanimous.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Utility class with method used around
 * the plugin
 *
 * @author NCBPFluffyBear
 */
public class Utils {

    private static final NamespacedKey attunedKey = new NamespacedKey(Magmanimous.getInstance(), "attuned");

    public Utils() {}

    /**
     * Sends message to targeted player with plugin prefix
     */
    public static void send(CommandSender s, String msg) {
        s.sendMessage(ChatColors.color("&7[&c熔岩之息&7]&r " + msg));
    }

    /**
     * Tries to send an item to a player's inventory
     * Drops item at player's location if inventory is full
     */
    public static void giveOrDropItem(ItemStack item, Player p) {
        HashMap<Integer, ItemStack> leftovers = p.getInventory().addItem(item);
        if (!leftovers.isEmpty()) {
            for (ItemStack leftover : leftovers.values()) {
                p.getLocation().getWorld().dropItem(p.getLocation(), leftover);
            }
        }
    }

    /**
     * Applies attunement key onto the item
     */
    public static void attune(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(attunedKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
    }

    /**
     * Checks if specified item is attuned
     */
    public static boolean isAttuned(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(attunedKey, PersistentDataType.BYTE, (byte) 0) == 1;
    }

    /**
     * Serializes the player's location into a string
     * that can be stored in a PersistentDataContainer
     */
    public static String serializeLoc(@Nonnull Location loc) {
        return loc.getWorld().getUID() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

    /**
     * Parses a location from a string
     */
    public static Location deserializeLoc(String loc) {
        String[] locArr = loc.split(":");
        World world = Bukkit.getWorld(UUID.fromString(locArr[0]));
        return new Location(world, Integer.parseInt(locArr[1]) + 0.5, Integer.parseInt(locArr[2]), Integer.parseInt(locArr[3]) + 0.5);
    }

}
