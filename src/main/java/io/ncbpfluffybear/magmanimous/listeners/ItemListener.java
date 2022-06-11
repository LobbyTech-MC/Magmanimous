package io.ncbpfluffybear.magmanimous.listeners;

import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.items.NetherForgePedestal;
import javax.annotation.Nonnull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.persistence.PersistentDataType;

/**
 * This class houses listeners relating to
 * {@link org.bukkit.entity.Item} entities
 *
 * @author NCBPFluffyBear
 */
public class ItemListener implements Listener {

    public ItemListener(@Nonnull Magmanimous plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Block any entity from picking up Magmanimous display items
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityItemPickUp(EntityPickupItemEvent e) {
        if (e.getItem().getPersistentDataContainer().getOrDefault(NetherForgePedestal.dispItemKey,
                PersistentDataType.BYTE, (byte) 0) == (byte) 1
        ) {
            e.setCancelled(true);
        }
    }

    /**
     * Block inventories from picking up Magmanimous display items
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInventoryItemPickup(InventoryPickupItemEvent e){
        if (e.getItem().getPersistentDataContainer().getOrDefault(NetherForgePedestal.dispItemKey,
                PersistentDataType.BYTE, (byte) 0) == (byte) 1
        ) {
            e.setCancelled(true);
        }
    }

}
