package io.ncbpfluffybear.magmanimous.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.utils.Strings;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * A Nether Forge component that hosts
 * the item being operated on
 *
 * Core component that handles the crafting operation
 *
 * @author NCBPFluffyBear
 */
public class NetherForgePedestal extends SlimefunItem {

    public static final NamespacedKey dispItemKey = new NamespacedKey(Magmanimous.getInstance(), "display_item");
    private static final BlockFace[] STAIR_FACES = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    public NetherForgePedestal(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onUse(), onBreak());
    }

    /**
     * Handles item insertion and removal from
     * the pedestal
     */
    private BlockUseHandler onUse() {
        return new BlockUseHandler() {
            @Override
            public void onRightClick(PlayerRightClickEvent e) {
                Player p = e.getPlayer();

                if (!canUse(p, true)) {
                    return;
                }

                ItemStack item = e.getItem();

                // Allow placement of stairs
                if (p.isSneaking() && item.getType() == Material.POLISHED_BLACKSTONE_STAIRS) {
                    return;
                }

                e.cancel();

                Block pedestal = e.getClickedBlock().get();
                Dispenser dispenser = (Dispenser) pedestal.getState();

                // Crafting
                if (tryCraft(dispenser, item, p)) {
                    return;
                }

                // Try to give item back to player
                if (removeItem(pedestal, dispenser, p, true)) {
                    return;
                }

                // Require item to be held
                if (item.getType() == Material.AIR) {
                    return;
                }

                // Try to insert held item
                insertItem(pedestal, dispenser, item);

            }
        };
    }

    /**
     * Tries to craft with the given items
     *
     * @return true if crafting was successfully attempted
     */
    private boolean tryCraft(Dispenser dispenser, ItemStack catalyst, Player p) {
        // Check if player is holding Nether Catalyst
        if (!MagmaItems.NETHER_CATALYST.getItem().isItem(catalyst)) {
            return false;
        }

        if (!MagmaItems.NETHER_CATALYST.getItem().canUse(p, false)) {
            return true;
        }

        Block tank = dispenser.getBlock().getRelative(BlockFace.UP, 2);
        if (!checkStructure(dispenser.getBlock(), tank, p)) {
            return true;
        }

        // Make sure item in pedestal
        ItemStack item = dispenser.getInventory().getItem(0);

        if (item == null) {
            Utils.send(p, "&c首先将一个物品插入基座!");
            return true;
        }

        // Block Slimefun and unsupported items
        if (item instanceof SlimefunItemStack) {
            Utils.send(p, "&c无法在此使用粘液科技物品!");
            return true;
        }

        // Determine what type of recipe is being used in the forge
        // If an item is not an attunement, it is a crafting recipe (if it passes these checks)
        boolean attunement = false;
        if (NetherForgeRecipe.getAttunements().containsKey(item.getType())) {
            attunement = true;
        } else if (!NetherForgeRecipe.getCrafts().containsKey(item.getType())) {
            Utils.send(p, "&c该物品无法被用于该锻造厂!");
            return true;
        }

        if (attunement && Utils.isAttuned(item)) {
            Utils.send(p, "&c此物品已被调谐!");
            return true;
        }

        // Verify item has no non-default metadata
        if (!attunement && !SlimefunUtils.isItemSimilar(item, new ItemStack(item.getType()), true)) {
            Utils.send(p, "&c您不能使用修改过的物品作为合成材料!");
            return true;
        }

        // Make sure tank has sufficient magma
        String tankVolStr = BlockStorage.getLocationInfo(tank.getLocation(), "stored");

        int tankVol = 0;
        if (tankVolStr != null) {
            tankVol = Integer.parseInt(tankVolStr);
        }

        // Get magma block cost of operation
        int cost;
        if (attunement) {
            cost = NetherForgeRecipe.getAttunements().get(item.getType());
        } else { // Assumed crafting recipe
            cost = NetherForgeRecipe.getCrafts().get(item.getType()).getFirstValue(); // Never null
        }

        if (tankVol < cost) {
            Utils.send(p, "&c附近的岩浆罐有" + tankVol + "个岩浆块，但是反应需要" + cost + "个岩浆块!");
            return true;
        }

        // Craft
        craft(dispenser, item, p, attunement);
        BlockStorage.addBlockInfo(tank, "stored", String.valueOf(tankVol - cost));
        ItemUtils.consumeItem(catalyst, false);
        return true;
    }

    /**
     * Plays a crafting animation before
     * replacing item with the crafting output
     */
    private void craft(Dispenser dispenser, ItemStack item, Player p, boolean attunement) {

        // Set item output, do this before animation in case player removes item
        // Attune item
        if (attunement) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore.add(ChatColors.color("&4岩浆调谐"));
            meta.setLore(lore);
            item.setItemMeta(meta);

            Utils.attune(item);
        } else { // Crafting recipe
            // Replace item with crafting output
            removeItem(dispenser.getBlock(), dispenser, p, false);
            insertItem(dispenser.getBlock(), dispenser,
                    NetherForgeRecipe.getCrafts().get(item.getType()).getSecondValue().clone()
            );
        }

        // Trigger every second
        new BukkitRunnable() {
            int iter = 0;
            final Location itemLocation = dispenser.getBlock().getRelative(0, 1, 0).getLocation().add(0.5, 0, 0.5);

            @Override
            public void run() {
                if (iter < 3) {
                    dispenser.getWorld().spawnParticle(Particle.LAVA, itemLocation,
                            15, 0.2, 0, 0.2
                    );
                    dispenser.getWorld().playSound(itemLocation, Sound.BLOCK_LAVA_POP, 1F, -5F);
                    iter++;
                } else {
                    dispenser.getWorld().createExplosion(itemLocation, 0);
                    Particle.DustOptions dustOption = new Particle.DustOptions(Color.RED, 1);
                    dispenser.getWorld().spawnParticle(Particle.REDSTONE, itemLocation, 60, 0.2, 0.2, 0.2, dustOption);
                    cancel();
                }
            }
        }.runTaskTimer(Magmanimous.getInstance(), 0L, 20L);
    }

    /**
     * Verifies that the full forge has been constructed
     * Does not check for pedestal because it is the
     * block being interacted with
     */
    private boolean checkStructure(Block dispenser, Block magmaTank, Player p) {
        if (dispenser.getLocation().getWorld().getEnvironment() != World.Environment.NETHER) {
            Utils.send(p, "&c地狱锻造厂仅能在地狱工作!");
            return false;
        }

        // Check for Magma Tank
        if (MagmaItems.MAGMA_TANK.getItem() != BlockStorage.check(magmaTank)) {
            Utils.send(p, "&c地狱锻造厂缺少一个岩浆罐，位于"
                    + magmaTank.getX() + ", " + magmaTank.getY() + ", " + magmaTank.getZ()
            );
            Utils.send(p, "&e你可以在建造地狱锻造厂时参考这个指南: " + Strings.FORGE_GUIDE);
            return false;
        }

        // Check for stairs around Pedestal
        for (BlockFace face : STAIR_FACES) {
            Block stair = dispenser.getRelative(face);
            if (stair.getType() != Material.POLISHED_BLACKSTONE_STAIRS) {
                Utils.send(p, "&c该地狱锻造厂缺少磨制黑石楼梯，位于"
                        + stair.getX() + ", " + stair.getY() + ", " + stair.getZ()
                );
                Utils.send(p, "&e你可以在建造地狱锻造厂时参考这个指南: " + Strings.FORGE_GUIDE);
                return false;
            }
        }

        // Check for stairs around Magma Tank
        for (BlockFace face : STAIR_FACES) {
            Block stair = magmaTank.getRelative(face);
            if (stair.getType() != Material.POLISHED_BLACKSTONE_STAIRS) {
                Utils.send(p, "&c该地狱锻造厂缺少磨制黑石楼梯，位于 "
                        + stair.getX() + ", " + stair.getY() + ", " + stair.getZ()
                );
                Utils.send(p, "&e你可以在建造地狱锻造厂时参考这个指南: " + Strings.FORGE_GUIDE);
                return false;
            }
        }

        // All checks passed
        return true;
    }

    /**
     * Drops item stored in pedestal when broken
     */
    private BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                Block pedestal = e.getBlock();
                if (pedestal.getType() != Material.DISPENSER) {
                    return;
                }

                Dispenser dispenser = ((Dispenser) pedestal.getState());
                ItemStack pedestalItem = dispenser.getInventory().getItem(0);

                if (pedestalItem != null) {
                    drops.add(pedestalItem);
                }

                removeItem(pedestal, dispenser, e.getPlayer(), true);
            }
        };
    }

    /**
     * Attempts to retrieve item from pedestal's inventory
     * to the player's inventory
     * Deletes attached holographic item
     *
     * @return true if pedestal contains an item
     */
    private boolean removeItem(Block pedestal, Dispenser dispenser, Player p, boolean returnItem) {
        ItemStack displayItem = dispenser.getInventory().getItem(0);
        if (displayItem != null && displayItem.getType() != Material.AIR) {
            if (returnItem) {
                Utils.giveOrDropItem(displayItem, p);
            }
            dispenser.getInventory().clear(0);

            // Delete item (target by UUID)
            String dispItemId = BlockStorage.getLocationInfo(pedestal.getLocation(), "display-item");
            if (dispItemId != null) {
                try {
                    UUID dispItemUUID = UUID.fromString(dispItemId);
                    Entity dispItem = Magmanimous.getInstance().getServer().getEntity(dispItemUUID);
                    if (dispItem != null) {
                        dispItem.remove();
                    }
                } catch (IllegalArgumentException ignored) {
                    // Still success, but item could not be deleted...
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Inserts the item that the player is holding
     * Creates and attaches holographic item
     */
    private void insertItem(Block pedestal, Container dispenser, ItemStack item) {
        // Save item
        dispenser.getInventory().setItem(0, new CustomItemStack(item, 1));

        // Floating item
        Item dispItem = dispenser.getWorld().dropItem(
                dispenser.getLocation().add(0.5, 1.2, 0.5), new CustomItemStack(item, 1)
        );

        // Prevent despawns, internal anti-pickup key
        // Technically already handled when marked as no pickup
        dispItem.setUnlimitedLifetime(true);
        dispItem.setVelocity(new Vector(0, 0, 0));
        dispItem.setInvulnerable(true);
        dispItem.setGravity(false);
        dispItem.getPersistentDataContainer().set(dispItemKey, PersistentDataType.BYTE, (byte) 1);

        // 3rd party plugin acknowledgement
        SlimefunUtils.markAsNoPickup(dispItem, "magmanimous_display_item");

        // Save UUID of item for future removal
        BlockStorage.addBlockInfo(pedestal, "display-item", String.valueOf(dispItem.getUniqueId()));

        // Remove one from player
        ItemUtils.consumeItem(item, false);
    }
}
