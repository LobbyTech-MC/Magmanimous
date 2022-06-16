package io.ncbpfluffybear.magmanimous.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.items.MagmaItems;
import io.ncbpfluffybear.magmanimous.items.PortalStar;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Handles custom events for all Magmanimous items
 *
 * @author NCBPFluffyBear
 */
public class MagmanimousListener implements Listener {

    public MagmanimousListener(@Nonnull Magmanimous plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Handles teleport events for the {@link PortalStar}
     */
    @EventHandler
    private void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            Player p = e.getPlayer();
            ItemStack star = p.getInventory().getItemInMainHand();

            // Check for Slimefun Item
            SlimefunItem portalStar = SlimefunItem.getByItem(star);
            if (portalStar == null) {
                return;
            }

            // Make sure item is a portal star
            if (!portalStar.isItem(MagmaItems.PORTAL_STAR)) {
                return;
            }

            e.setCancelled(true);

            // Get stored location
            String locStr = star.getItemMeta().getPersistentDataContainer().get(PortalStar.locationKey, PersistentDataType.STRING);
            if (locStr == null) {
                Utils.send(p, "&c传送门之星未绑定位置!");
                return;
            }

            // Teleport player
            Location tpLoc = Utils.deserializeLoc(locStr);
            p.teleport(tpLoc);
            p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.5F, -5F);
            Utils.send(p, "&a传送门之星已把你传送到绑定位置!");
        }
    }

    /**
     * Handles drinking of the Combustion Potion
     */
    @EventHandler(ignoreCancelled = true)
    private void onCombustionDrink(PlayerItemConsumeEvent e) {
        ItemStack item = e.getItem();

        if (item.getType() != Material.POTION) {
            return;
        }

        SlimefunItem sfPotion = SlimefunItem.getByItem(item);
        if (sfPotion == null) {
            return;
        }

        if (sfPotion.isItem(MagmaItems.COMBUSTION_POTION)) {
            Player p = e.getPlayer();
            p.playSound(p.getLocation(), Sound.ITEM_BUCKET_FILL_LAVA, 1F, -10F);
            p.setFireTicks(20 * 60);
            Utils.send(p, "&c你已经已被点燃");
        }
    }

}
