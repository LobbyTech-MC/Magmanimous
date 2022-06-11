package io.ncbpfluffybear.magmanimous.utils;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.items.MagmaItems;
import io.ncbpfluffybear.magmanimous.items.MagmaTank;
import io.ncbpfluffybear.magmanimous.items.NetherForgePedestal;
import io.ncbpfluffybear.magmanimous.items.NetherForgeRecipe;
import io.ncbpfluffybear.magmanimous.items.PortalStar;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Sets up all Magmanimous items
 *
 * @author NCBPFluffyBear
 */
public class MagmaItemSetup {

    public static final NamespacedKey magmaKey = new NamespacedKey(Magmanimous.getInstance(), "magmanimous_items");
    public static final ItemGroup magmaCategory = new ItemGroup(magmaKey, new CustomItemStack(Material.MAGMA_BLOCK, "&cMagmanimous"));

    private static final RecipeType NETHER_MERCHANT = new RecipeType(
            new NamespacedKey(Magmanimous.getInstance(), "nether_merchant_barter"),
            new CustomItemStack(
                    Material.PIGLIN_SPAWN_EGG,
                    "&cNether Merchant",
                    "&7Barter with a Nether Merchant",
                    "&eFind the Magmanimous Guide in the",
                    "&eSlimefun Guide to learn about Nether Merchants"
            ));

    public MagmaItemSetup() {
    }

    public static void setup(@Nonnull Magmanimous plugin) {
        new SlimefunItem(magmaCategory, MagmaItems.MAGMANIMOUS_GUIDE, RecipeType.NULL, new ItemStack[9]).register(plugin);
        new SlimefunItem(magmaCategory, MagmaItems.NETHER_CATALYST, NETHER_MERCHANT, new ItemStack[]{
                null, null, null, null, new ItemStack(Material.NETHERITE_INGOT), null, null, null, null
        }).register(plugin);

        new NetherForgePedestal(magmaCategory, MagmaItems.NETHER_FORGE_PEDESTAL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.MAGMA_BLOCK), new ItemStack(Material.MAGMA_BLOCK), new ItemStack(Material.MAGMA_BLOCK),
                MagmaItems.NETHER_CATALYST, new ItemStack(Material.DISPENSER), MagmaItems.NETHER_CATALYST,
                new ItemStack(Material.NETHER_BRICKS), new ItemStack(Material.NETHER_BRICKS), new ItemStack(Material.NETHER_BRICKS)
        }).register(plugin);

        new MagmaTank(magmaCategory, MagmaItems.MAGMA_TANK, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                new ItemStack(Material.MAGMA_BLOCK), new ItemStack(Material.BARREL), new ItemStack(Material.MAGMA_BLOCK),
                new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN), new ItemStack(Material.OBSIDIAN)
        }).register(plugin);

        // Attunement Recipes
        new NetherForgeRecipe(magmaCategory, MagmaItems.MAGMA_ATTUNED_HELMET, Material.NETHERITE_HELMET, 100).register(plugin);
        new NetherForgeRecipe(magmaCategory, MagmaItems.MAGMA_ATTUNED_CHESTPLATE, Material.NETHERITE_CHESTPLATE, 100).register(plugin);
        new NetherForgeRecipe(magmaCategory, MagmaItems.MAGMA_ATTUNED_LEGGINGS, Material.NETHERITE_LEGGINGS, 100).register(plugin);
        new NetherForgeRecipe(magmaCategory, MagmaItems.MAGMA_ATTUNED_BOOTS, Material.NETHERITE_BOOTS, 100).register(plugin);

        // Forge crafting recipes
        new PortalStar(magmaCategory, MagmaItems.PORTAL_STAR, NetherForgeRecipe.NETHER_FORGE, Material.NETHER_STAR, 75).register(plugin);

        // Regular recipes
        new SlimefunItem(magmaCategory, MagmaItems.COMBUSTION_POTION, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.CRIMSON_FUNGUS), new ItemStack(Material.WARPED_FUNGUS), new ItemStack(Material.CRIMSON_FUNGUS),
                new ItemStack(Material.WARPED_FUNGUS), new ItemStack(Material.GLASS_BOTTLE), new ItemStack(Material.WARPED_FUNGUS),
                new ItemStack(Material.CRIMSON_FUNGUS), new ItemStack(Material.MAGMA_BLOCK), new ItemStack(Material.CRIMSON_FUNGUS)
        }).register(plugin);
    }
}
