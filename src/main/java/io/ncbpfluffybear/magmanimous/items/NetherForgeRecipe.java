package io.ncbpfluffybear.magmanimous.items;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Dummy {@link SlimefunItem} that registers recipes
 * for the Nether Forge
 *
 * This recipe adds attributes to an existing item,
 * so the input item is accepted as a Material
 *
 * @author NCBPFluffyBear
 */
public class NetherForgeRecipe extends SlimefunItem {

    public static final RecipeType NETHER_FORGE = new RecipeType(
            new NamespacedKey(Magmanimous.getInstance(), "nether_forge"),
            new CustomItemStack(
                    Material.POLISHED_BLACKSTONE_WALL,
                    "&4熔岩锻造配方",
                    "&7在熔岩锻造厂中合成该物品",
                    "&e请查询熔岩之息指南以获取",
                    "&e更多关于熔岩锻造厂的信息"
            ));

    private static final Map<Material, Integer> attunements = new HashMap<>();
    // Crafts are just stored here for forge use
    private static final Map<Material, Pair<Integer, ItemStack>> crafts = new HashMap<>();

    public NetherForgeRecipe(ItemGroup itemGroup, SlimefunItemStack item, Material material, int cost) {
        super(itemGroup, item, NETHER_FORGE, new ItemStack[]{new ItemStack(material), buildCatalystItem(cost)});
        attunements.put(material, cost);
    }

    /**
     * Clones and adds lore to a Nether Catalyst
     * to reflect the cost of the recipe
     */
    public static ItemStack buildCatalystItem(int cost) {
        ItemStack catalyst = MagmaItems.NETHER_CATALYST.clone();

        ItemMeta catalystMeta = catalyst.getItemMeta();
        List<String> lore = catalystMeta.getLore();

        lore.add("");
        lore.add(ChatColors.color("&e耗费: " + cost + "个&e岩浆块"));

        catalystMeta.setLore(lore);
        catalyst.setItemMeta(catalystMeta);

        return catalyst;
    }

    /**
     * Registeres a new Nether Forge crafting recipe
     */
    public static void addCraft(@Nonnull Material input, int cost, @Nonnull ItemStack output) {
        crafts.put(input, new Pair<>(cost, output));
    }

    public static Map<Material, Integer> getAttunements() {
        return attunements;
    }

    public static Map<Material, Pair<Integer, ItemStack>> getCrafts() {
        return crafts;
    }
}
