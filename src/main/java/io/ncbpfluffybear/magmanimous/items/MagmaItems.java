package io.ncbpfluffybear.magmanimous.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.ncbpfluffybear.magmanimous.utils.Strings;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;

/**
 * Magmanimous Items
 *
 * @author NCBPFluffyBear
 */
public class MagmaItems {

    public MagmaItems() {
    }

    public static final SlimefunItemStack MAGMANIMOUS_GUIDE = new SlimefunItemStack(
            "MAGMANIMOUS_GUIDE",
            Material.YELLOW_STAINED_GLASS_PANE,
            "&6Magmanimous Guide",
            "&e> Click here to get a link to the guide"
    );

    public static final SlimefunItemStack NETHER_CATALYST = new SlimefunItemStack(
            "NETHER_CATALYST",
            Material.MAGMA_CREAM,
            "&cNether Catalyst",
            "&7Used to start &4Nether Forge &7reactions"
    );

    // Nether forge components
    public static final SlimefunItemStack NETHER_FORGE_PEDESTAL = new SlimefunItemStack(
            "NETHER_FORGE_PEDESTAL",
            Material.DISPENSER,
            "&cNether Forge Pedestal",
            "&7Holds an item in the &4Nether Forge",
            "",
            Strings.NETHER_FORGE_COMPONENT
    );

    public static final SlimefunItemStack MAGMA_TANK = new SlimefunItemStack(
            "MAGMA_TANK",
            Material.MAGMA_BLOCK,
            "&6Magma Tank",
            "&7Stores up to " + MagmaTank.getMaxStorage() + " Magma Blocks",
            "&e> Right Click &7to insert 1 Magma Block",
            "&e> Sneak + Right Click &7to insert all held Magma Blocks",
            "",
            Strings.NETHER_FORGE_COMPONENT
    );

    // Attuned items are placeholder display items
    public static final SlimefunItemStack MAGMA_ATTUNED_HELMET = new SlimefunItemStack(
            "MAGMA_ATTUNED_HELMET",
            Material.NETHERITE_HELMET,
            "&4Magma Attuned Helmet",
            "&7Fire grants positive effects",
            "&7if all four armor pieces are worn",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack MAGMA_ATTUNED_CHESTPLATE = new SlimefunItemStack(
            "MAGMA_ATTUNED_CHESTPLATE",
            Material.NETHERITE_CHESTPLATE,
            "&4Magma Attuned Chestplate",
            "&7Fire grants positive effects",
            "&7if all four armor pieces are worn",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack MAGMA_ATTUNED_LEGGINGS = new SlimefunItemStack(
            "MAGMA_ATTUNED_LEGGINGS",
            Material.NETHERITE_LEGGINGS,
            "&4Magma Attuned Leggings",
            "&7Fire grants positive effects",
            "&7if all four armor pieces are worn",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack MAGMA_ATTUNED_BOOTS = new SlimefunItemStack(
            "MAGMA_ATTUNED_BOOTS",
            Material.NETHERITE_BOOTS,
            "&4Magma Attuned Boots",
            "&7Fire grants positive effects",
            "&7if all four armor pieces are worn",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack PORTAL_STAR = new SlimefunItemStack(
            "PORTAL_STAR",
            Material.NETHER_STAR,
            "&5Portal Star",
            "&7Teleports a player to a linked",
            "&7location when they enter a nether portal",
            "",
            "&e> Sneak + Right Click &7to link current location",
            "",
            "&cNo location currently bound",
            Strings.GADGET
    );

    public static final SlimefunItemStack COMBUSTION_POTION = new SlimefunItemStack(
            "COMBUSTION_POTION",
            Material.POTION,
            "&6Combustion Potion",
            "&7Sets drinker on fire for a minute",
            "&7Useful for triggering attunement effects",
            "",
            Strings.CONSUMABLE
    );

    static {
        // In case an operator uses /sf give on placeholder items
        Utils.attune(MAGMA_ATTUNED_HELMET);
        Utils.attune(MAGMA_ATTUNED_CHESTPLATE);
        Utils.attune(MAGMA_ATTUNED_LEGGINGS);
        Utils.attune(MAGMA_ATTUNED_BOOTS);

        // Potion colors
        PotionMeta potionMeta = (PotionMeta) COMBUSTION_POTION.getItemMeta();
        potionMeta.setColor(Color.RED);
        COMBUSTION_POTION.setItemMeta(potionMeta);
    }
}
