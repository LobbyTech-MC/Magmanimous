package io.ncbpfluffybear.magmanimous.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.items.MagmaItems;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Sets up all the research
 *
 * @author NCBPFluffyBear
 */
public class ResearchSetup {

    private ResearchSetup() {
    }

    public static void setup() {

        register("nether_catalyst", 5021, "地狱催化剂", 10, MagmaItems.NETHER_CATALYST);
        register("nether_forge_components", 5022, "地狱锻造厂基座", 30, MagmaItems.MAGMA_TANK, MagmaItems.NETHER_FORGE_PEDESTAL);
        register("attuned_armor", 5023, "岩浆调谐盔甲", 100,
                MagmaItems.MAGMA_ATTUNED_HELMET, MagmaItems.MAGMA_ATTUNED_CHESTPLATE,
                MagmaItems.MAGMA_ATTUNED_LEGGINGS, MagmaItems.MAGMA_ATTUNED_BOOTS
        );
        register("magma_items", 5024, "Magma Items", 30, MagmaItems.PORTAL_STAR, MagmaItems.COMBUSTION_POTION);

    }

    private static void register(String key, int id, String name, int defaultCost, ItemStack... items) {
        Research research = new Research(new NamespacedKey(Magmanimous.getInstance(), key), id, name, defaultCost);

        for (ItemStack item : items) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null) {
                research.addItems(sfItem);
            }
        }

        research.register();
    }

}
