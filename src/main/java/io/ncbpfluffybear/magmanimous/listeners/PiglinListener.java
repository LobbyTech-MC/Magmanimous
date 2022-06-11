package io.ncbpfluffybear.magmanimous.listeners;

import io.github.thebusybiscuit.slimefun4.libraries.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.items.MagmaItems;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nonnull;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Defines listeners for piglin related events
 *
 * @author NCBPFluffyBear
 */
public class PiglinListener implements Listener {

    private final Magmanimous plugin;
    private final NamespacedKey NETHER_MERCHANT = new NamespacedKey(Magmanimous.getInstance(), "nether_merchant");

    // Barter Drops and their Chances
    private final Map<ItemStack, Integer> merchantDrops = new LinkedHashMap<>();
    private int totalDropChance = 0;

    public PiglinListener(@Nonnull Magmanimous plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        merchantDrops.put(new ItemStack(Material.EXPERIENCE_BOTTLE, 16), 10);
        merchantDrops.put(new ItemStack(Material.GOLDEN_APPLE), 20);
        merchantDrops.put(new ItemStack(Material.NETHER_STAR), 10);
        merchantDrops.put(new ItemStack(Material.NETHERITE_INGOT, 2), 5);
        merchantDrops.put(MagmaItems.NETHER_CATALYST, 20);

        // Remap drops to scale chances
        for (Map.Entry<ItemStack, Integer> drop : merchantDrops.entrySet()) {
            this.totalDropChance += drop.getValue();
            drop.setValue(totalDropChance);
        }
    }

    /**
     * Listener for growing piglins into Nether Merchants
     * Requirements:
     * - Player's interaction item is a Golden Carrot
     * - Piglin is baby
     * - Piglin has potion effect strength
     */
    @EventHandler(ignoreCancelled = true)
    private void onPiglinFeed(PlayerInteractAtEntityEvent e) {
        Entity en = e.getRightClicked();
        Player p = e.getPlayer();

        if (en instanceof Piglin) {

            // Check interaction item requirement
            ItemStack carrot = e.getPlayer().getInventory().getItem(e.getHand());
            if (carrot == null || carrot.getType() != Material.GOLDEN_CARROT) {
                return;
            }

            Piglin piglin = (Piglin) en;
            // Check piglin age requirement and potion effect requirement
            if (piglin.isAdult() || !piglin.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                return;
            }

            // Make sure piglin isn't already being converted
            if (piglin.getPersistentDataContainer().getOrDefault(NETHER_MERCHANT, PersistentDataType.BYTE, (byte) 0) == (byte) 1) {
                Utils.send(p, "&cThis piglin has already been converted into a Nether Merchant");
                return;
            }

            ItemUtils.consumeItem(carrot, false);
            piglin.getPersistentDataContainer().set(NETHER_MERCHANT, PersistentDataType.BYTE, (byte) 1);

            // Trigger every second
            new BukkitRunnable() {
                int iter = 0;

                @Override
                public void run() {
                    if (piglin.isDead()) {
                        cancel();
                    }

                    // Green happy effect
                    piglin.playEffect(EntityEffect.VILLAGER_HAPPY);
                    // Armor equip sound
                    piglin.getLocation().getWorld().playSound(piglin.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 2, 1);

                    if (iter == 4) {
                        // Explosion effect (No damage)
                        piglin.getLocation().getWorld().createExplosion(piglin.getLocation(), 0);
                        convertPiglin(piglin);
                        Utils.send(p, "&aThis piglin has been converted into a Nether Merchant");
                        cancel();
                    }

                    iter++;
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }


    /**
     * Listener for changing the trades of a Nether Merchant
     * New drops defined by merchantDrops
     */
    @EventHandler(ignoreCancelled = true)
    private void onNetherMerchantTrade(PiglinBarterEvent e) {
        // Verify that the piglin is a Nether Merchant
        if (!isMerchant(e.getEntity())) {
            return;
        }

        // Randomize new drop
        int rand = ThreadLocalRandom.current().nextInt(totalDropChance);

        for (Map.Entry<ItemStack, Integer> entry : merchantDrops.entrySet()) {
            if (entry.getValue() > rand) {
                // Replace drop with randomized new drop
                List<ItemStack> outcome = e.getOutcome();
                outcome.clear();
                outcome.add(entry.getKey().clone());
                return;
            }
        }

    }

    /**
     * Prevents Nether Merchants from picking up
     * materials other than Netherite Ingots
     */
    @EventHandler
    private void onPiglinPickup(EntityPickupItemEvent e) {
        if (isMerchant(e.getEntity()) && e.getItem().getItemStack().getType() != Material.NETHERITE_INGOT) {
            e.setCancelled(true);
        }
    }

    /**
     * Prevents Nether Merchants from bartering with
     * materials other than Netherite Ingots
     */
    @EventHandler
    private void onPiglinBarter(PiglinBarterEvent e) {
        if (isMerchant(e.getEntity()) && e.getInput().getType() != Material.NETHERITE_INGOT) {
            e.setCancelled(true);
        }
    }

    /**
     * Prevents players from right-clicking
     * Nether Merchants with gold ingots
     */
    @EventHandler
    private void onPiglinTrade(PlayerInteractAtEntityEvent e) {
        if (isMerchant(e.getRightClicked()) && e.getRightClicked() instanceof Piglin
                && e.getPlayer().getInventory().getItem(e.getHand()).getType() == Material.GOLD_INGOT
        ) {
            e.setCancelled(true);
        }
    }

    /**
     * Converts specified piglin into a Nether Merchant
     * Changes:
     * - Converted to adult
     * - 40 Health
     * - No longer hunts hoglins
     * - Can not be zombified
     * - Equipped with netherite gear, will not drop on death
     * - Picks up and can be interacted with netherite ingots
     */
    private void convertPiglin(Piglin piglin) {
        // Update piglin attributes
        piglin.setAdult();
        piglin.setIsAbleToHunt(false);
        piglin.setImmuneToZombification(true);
        piglin.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40d);
        piglin.setCustomName(ChatColors.color("&4Nether Merchant"));
        // Update equipment
        EntityEquipment equipment = piglin.getEquipment();
        equipment.setHelmet(new ItemStack(Material.NETHERITE_HELMET), true);
        equipment.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE), true);
        equipment.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS), true);
        equipment.setBoots(new ItemStack(Material.NETHERITE_BOOTS), true);
        equipment.setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD), true);
        equipment.setHelmetDropChance(0);
        equipment.setChestplateDropChance(0);
        equipment.setLeggingsDropChance(0);
        equipment.setBootsDropChance(0);
        equipment.setItemInMainHandDropChance(0);
        // Update piglin trades
        piglin.addMaterialOfInterest(Material.NETHERITE_INGOT); // Tries to pick up this item now
        piglin.addBarterMaterial(Material.NETHERITE_INGOT); // Trades with this item now
    }

    private boolean isMerchant(Entity piglin) {
        return piglin.getPersistentDataContainer().getOrDefault(NETHER_MERCHANT, PersistentDataType.BYTE, (byte) 0) == (byte) 1;
    }
}
