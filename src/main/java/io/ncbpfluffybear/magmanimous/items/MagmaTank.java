package io.ncbpfluffybear.magmanimous.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A Nether Forge component that stores
 * {@value MAX_STORAGE} Magma Blocks
 *
 * @author NCBPFluffyBear
 */
public class MagmaTank extends SlimefunItem {

    private static final int MAX_STORAGE = 100;

    public MagmaTank(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onUse());
    }

    /**
     * Handles interactions with the Magma Tank
     */
    public BlockUseHandler onUse() {
        return new BlockUseHandler() {
            @Override
            public void onRightClick(PlayerRightClickEvent e) {
                ItemStack item = e.getItem();

                // Allow placement of Polished Blackstone Stairs
                if (item.getType() == Material.POLISHED_BLACKSTONE_STAIRS) {
                    return;
                }

                Player p = e.getPlayer();
                Block tank = e.getClickedBlock().get();
                e.cancel();

                // Get number of magma blocks currently stored
                String storedStr = BlockStorage.getLocationInfo(tank.getLocation(), "stored");
                int stored = 0;
                if (storedStr != null) {
                    stored = Integer.parseInt(storedStr);
                }

                // Require player to be holding a Magma Block
                if (item.getType() != Material.MAGMA_BLOCK) {
                    Utils.send(p, "&eThis tank currently has " + stored + " &eMagma Block(s). Right click with Magma Blocks to add more in.");
                    return;
                }

                if (SlimefunItem.getByItem(item) != null) {
                    Utils.send(p, "&cYou can not use Slimefun Items here!");
                    return;
                }

                // Check if already full
                if (stored >= getMaxStorage()) {
                    Utils.send(p, "&cThis tank already has the maximum number of Magma Blocks! (" + getMaxStorage() + "&c)");
                    return;
                }

                // Bulk fill
                if (p.isSneaking()) {
                    // Amount being inserted exceeds maximum
                    if (stored + item.getAmount() > getMaxStorage()) {
                        ItemUtils.consumeItem(item, 100 - stored, false);
                        stored = getMaxStorage();
                    } else { // Amount fits into rank
                        stored += item.getAmount();
                        ItemUtils.consumeItem(item, item.getAmount(), false);
                    }

                } else { // Single block insert
                    stored += 1;
                    ItemUtils.consumeItem(item, false);
                }

                // Update stored amount
                Utils.send(p, "&aThis tank now has " + stored + " &aMagma Block(s).");
                BlockStorage.addBlockInfo(tank, "stored", String.valueOf(stored));
            }
        };
    }

    public static int getMaxStorage() {
        return MAX_STORAGE;
    }
}
