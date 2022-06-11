package io.ncbpfluffybear.magmanimous.listeners;

import io.ncbpfluffybear.magmanimous.Magmanimous;
import io.ncbpfluffybear.magmanimous.utils.Utils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Registers listeners related to attuned items
 *
 * @author NCBPFluffyBear
 */
public class AttunedListener implements Listener {

    private final Set<EntityDamageEvent.DamageCause> BURN_CAUSES = new HashSet<>(Arrays.asList(
            EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.HOT_FLOOR
    ));

    public AttunedListener(@Nonnull Magmanimous plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Negates negative effects and applies positive
     * effects on a player if they are wearing a full
     * set of attuned armor
     *
     * Attuned effects:
     * - Strength 3
     * - Regeneration 2
     * - Red Glow
     */
    @EventHandler
    private void onPlayerDamage(EntityDamageEvent e) {
        // Damage cause is by fire
        if (e.getEntity() instanceof Player && BURN_CAUSES.contains(e.getCause())) {
            Player p = (Player) e.getEntity();
            // Verify all armor has attunement key
            for (ItemStack armor : p.getInventory().getArmorContents()) {
                if (!Utils.isAttuned(armor)) {
                    return;
                }
            }

            // Cancel damage
            e.setCancelled(true);

            // Apply effects
            glowPlayer(p);
            // Regeneration works per tick, can not constantly refresh
            tryAddEffect(p, PotionEffectType.REGENERATION, 3);
            // Add other effects like regeneration for consistency
            tryAddEffect(p, PotionEffectType.INCREASE_DAMAGE, 2);
            tryAddEffect(p, PotionEffectType.GLOWING, 0);
        }
    }

    /**
     * Adds a 3 second duration potion effect
     * to a player if they do not have it
     * or if the duration remaining is less than
     * 1.5 seconds, to help with color flashing visual issues
     */
    private void tryAddEffect(Player p, PotionEffectType effect, int amplifier) {
        PotionEffect currEffect = p.getPotionEffect(effect);
        if (currEffect == null || currEffect.getDuration() < 30) {
            p.addPotionEffect(effect.createEffect(60, amplifier));
        }
    }

    /**
     * Removes the red color from a player
     * with attuned effects applied when the effect
     * expires
     */
    @EventHandler
    private void onGlowExpire(EntityPotionEffectEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getOldEffect() != null && e.getOldEffect().getType().equals(PotionEffectType.GLOWING)
                    && e.getCause() == EntityPotionEffectEvent.Cause.EXPIRATION
            ) {
                // Fix an issue where glow color changes before expiration
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        unGlowPlayer(((Player) e.getEntity()));
                    }
                }.runTaskLater(Magmanimous.getInstance(), 10L);
            }
        }
    }

    /**
     * Creates or gets the glow team and applies it to the player
     */
    private void glowPlayer(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        p.setScoreboard(board);
        Team team;
        try {
            team = board.registerNewTeam(ChatColor.RED + "attuned_glow");
            team.setColor(ChatColor.RED);
        } catch (IllegalArgumentException ignored) {
            team = board.getTeam(ChatColor.RED + "attuned_glow");
        }
        team.addEntry(p.getName());
    }

    /**
     * Removes a player from a glowing team
     */
    private void unGlowPlayer(Player p) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        p.setScoreboard(board);
        Team team = board.getTeam(ChatColor.RED + "attuned_glow");
        if (team == null) {
            return;
        }

        try {
            team.removeEntry(p.getName());
        } catch (IllegalArgumentException | IllegalStateException ignored) {
        }
    }

}
