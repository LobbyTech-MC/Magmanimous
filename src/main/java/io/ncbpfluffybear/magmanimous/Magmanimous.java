package io.ncbpfluffybear.magmanimous;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.ncbpfluffybear.magmanimous.listeners.AttunedListener;
import io.ncbpfluffybear.magmanimous.listeners.InventoryListener;
import io.ncbpfluffybear.magmanimous.listeners.ItemListener;
import io.ncbpfluffybear.magmanimous.listeners.MagmanimousListener;
import io.ncbpfluffybear.magmanimous.listeners.PiglinListener;
import io.ncbpfluffybear.magmanimous.utils.MagmaItemSetup;
import io.ncbpfluffybear.magmanimous.utils.ResearchSetup;

/**
 * Main class of Magmanimous
 *
 * @author NCBPFluffyBear
 */
public class Magmanimous extends JavaPlugin implements SlimefunAddon {

    private static Magmanimous instance;

    @Override
    public void onEnable() {
        instance = this;

        new Metrics(this, 15439);

        // Register items
        MagmaItemSetup.setup(getInstance());

        // Register listeners
        new PiglinListener(getInstance());
        new ItemListener(getInstance());
        new AttunedListener(getInstance());
        new MagmanimousListener(getInstance());
        new InventoryListener(getInstance());

        // Register Research
        ResearchSetup.setup();

        /*
        if (getConfig().getBoolean("options.auto-update") && getDescription().getVersion().startsWith("Build")) {
            new GuizhanBuildsUpdater(this, getFile(), "SlimefunGuguProject", "Magmanimous", "main", false, "zh-CN").start();
        }
        */
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/SlimefunGuguProject/Magmanimous/issues";
    }

    @Nonnull
    public static Magmanimous getInstance() {
        return instance;
    }
}
