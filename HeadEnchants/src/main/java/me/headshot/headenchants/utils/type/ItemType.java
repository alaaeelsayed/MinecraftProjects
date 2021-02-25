package me.headshot.headenchants.utils.type;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ItemType {

    HELMET(5, Material.DIAMOND_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
            Material.LEATHER_HELMET),
    CHESTPLATE(6, Material.DIAMOND_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE,
            Material.IRON_CHESTPLATE, Material.LEATHER_CHESTPLATE),
    LEGGINGS(7, Material.DIAMOND_LEGGINGS,
            Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.LEATHER_LEGGINGS),
    BOOTS(8, Material.DIAMOND_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS,
            Material.LEATHER_BOOTS),
    PICKAXE(Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE,
            Material.DIAMOND_PICKAXE),
    AXE(Material.WOOD_AXE, Material.STONE_AXE, Material.GOLD_AXE, Material.IRON_AXE,
            Material.DIAMOND_AXE),
    SHOVEL(Material.WOOD_SPADE, Material.STONE_SPADE, Material.GOLD_SPADE,
            Material.IRON_SPADE, Material.DIAMOND_SPADE),
    SWORD(Material.WOOD_SWORD, Material.STONE_SWORD, Material.GOLD_SWORD, Material.IRON_SWORD,
            Material.DIAMOND_SWORD),
    BOW(Material.BOW);

    private int defaultSlot;
    private Material[] materials;

    ItemType(int defaultSlot, Material... mat) {
        this.defaultSlot = defaultSlot;
        this.materials = mat;
    }

    ItemType(Material... mat) {
        this(-1, mat);
    }

    public static ItemType fromItem(ItemStack item) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.isType(item.getType())) {
                return itemType;
            }
        }
        return null;
    }

    public boolean isType(Material mat) {
        return Arrays.asList(materials).contains(mat);
    }

    public int getDefaultSlot() {
        return defaultSlot;
    }
}
