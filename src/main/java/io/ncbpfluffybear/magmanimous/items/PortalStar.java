package io.ncbpfluffybear.magmanimous.items;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * The PortalStar is an item that allows
 * players to teleport to any Overworld
 * or Nether location when they enter a
 * Nether Portal
 *
 * @author NCBPFluffyBear
 */
public class PortalStar extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    public static final NamespacedKey locationKey = new NamespacedKey(Magmanimous.getInstance(), "location");

    public PortalStar(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, Material input, int cost) {
        super(itemGroup, item, recipeType, new ItemStack[]{new ItemStack(input), NetherForgeRecipe.buildCatalystItem(cost)});
        NetherForgeRecipe.addCraft(Material.NETHER_STAR, cost, MagmaItems.PORTAL_STAR);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return new ItemUseHandler() {
            @Override
            public void onRightClick(PlayerRightClickEvent e) {
                Player p = e.getPlayer();

                if (p.isSneaking()) {
                    World.Environment env = e.getPlayer().getWorld().getEnvironment();
                    if (env != World.Environment.NORMAL && env != World.Environment.NETHER) {
                        Utils.send(p, "&c您无法在此世界设置传送点!");
                        return;
                    }

                    // Store location in PDC
                    Location l = p.getLocation();
                    ItemStack star = e.getItem();
                    ItemMeta starMeta = star.getItemMeta();
                    starMeta.getPersistentDataContainer().set(
                            locationKey, PersistentDataType.STRING, Utils.serializeLoc(l)
                    );
                    // Update location in lore
                    List<String> lore = starMeta.getLore();
                    lore.set(5, ChatColors.color("&e链接至" + l.getBlockX() + ", " + l.getBlockY()
                            + ", " + l.getBlockZ() + " @" + l.getWorld().getName())
                    );
                    starMeta.setLore(lore);
                    star.setItemMeta(starMeta);

                    Utils.send(p, "&a传送点已设置");
                } else {
                    Utils.send(p, "&eShift+右击 设置传送点");
                }
            }
        };
    }
}
