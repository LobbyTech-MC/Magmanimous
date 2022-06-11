package io.ncbpfluffybear.magmanimous.listeners;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.items.MagmaItems;
import io.ncbpfluffybear.magmanimous.utils.Strings;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import javax.annotation.Nonnull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handles inventory related events
 *
 * @author NCBPFluffyBear
 */
public class InventoryListener implements Listener {

    public InventoryListener(@Nonnull Magmanimous plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onGuideClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        SlimefunItem guide = SlimefunItem.getByItem(item);
        if (guide != null && guide.isItem(MagmaItems.MAGMANIMOUS_GUIDE)) {
            Utils.send(e.getWhoClicked(), "&aClick this link to view the guide:");
            Utils.send(e.getWhoClicked(), "&e" + Strings.WIKI);
            e.setCancelled(true);
        }
    }
}
