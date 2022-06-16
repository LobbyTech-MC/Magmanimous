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
            "&6熔岩之息指南",
            "&e> 点击了解如何游玩熔岩之息"
    );

    public static final SlimefunItemStack NETHER_CATALYST = new SlimefunItemStack(
            "NETHER_CATALYST",
            Material.MAGMA_CREAM,
            "&c下界催化剂",
            "&7用于启动 &4熔岩锻造厂"
    );

    // Nether forge components
    public static final SlimefunItemStack NETHER_FORGE_PEDESTAL = new SlimefunItemStack(
            "NETHER_FORGE_PEDESTAL",
            Material.DISPENSER,
            "&c熔岩锻造厂基座",
            "&7手持一个物品",
            "&7右键点击放入 &4熔岩锻造厂",
            "",
            Strings.NETHER_FORGE_COMPONENT
    );

    public static final SlimefunItemStack MAGMA_TANK = new SlimefunItemStack(
            "MAGMA_TANK",
            Material.MAGMA_BLOCK,
            "&6岩浆罐",
            "&7最多储存 " + MagmaTank.getMaxStorage() + " 个岩浆块",
            "",
            "&e> 右键点击 &7存入一个岩浆块",
            "&e> Shift+右键点击 &7存入所有手持岩浆块",
            "",
            Strings.NETHER_FORGE_COMPONENT
    );

    // Attuned items are placeholder display items
    public static final SlimefunItemStack MAGMA_ATTUNED_HELMET = new SlimefunItemStack(
            "MAGMA_ATTUNED_HELMET",
            Material.NETHERITE_HELMET,
            "&4岩浆调谐头盔",
            "&7当穿上全套盔甲时",
            "&7火焰将给予你增益效果",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack MAGMA_ATTUNED_CHESTPLATE = new SlimefunItemStack(
            "MAGMA_ATTUNED_CHESTPLATE",
            Material.NETHERITE_CHESTPLATE,
            "&4岩浆调谐胸甲",
            "&7当穿上全套盔甲时",
            "&7火焰将给予你增益效果",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack MAGMA_ATTUNED_LEGGINGS = new SlimefunItemStack(
            "MAGMA_ATTUNED_LEGGINGS",
            Material.NETHERITE_LEGGINGS,
            "&4岩浆调谐护腿",
            "&7当穿上全套盔甲时",
            "&7火焰将给予你增益效果",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack MAGMA_ATTUNED_BOOTS = new SlimefunItemStack(
            "MAGMA_ATTUNED_BOOTS",
            Material.NETHERITE_BOOTS,
            "&4岩浆调谐靴子",
            "&7当穿上全套盔甲时",
            "&7火焰将给予你增益效果",
            "",
            Strings.ATTUNEMENT
    );

    public static final SlimefunItemStack PORTAL_STAR = new SlimefunItemStack(
            "PORTAL_STAR",
            Material.NETHER_STAR,
            "&5传送门之星",
            "&7当玩家手持传送门之星",
            "&7进入下界传送门时, 传送至绑定的位置",
            "",
            "&e> Shift+右键点击 &7链接地点",
            "",
            "&c未绑定位置",
            Strings.GADGET
    );

    public static final SlimefunItemStack COMBUSTION_POTION = new SlimefunItemStack(
            "COMBUSTION_POTION",
            Material.POTION,
            "&6燃烧药剂",
            "&7点燃饮用者一分钟",
            "&7可用于触发岩浆调谐效果",
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
