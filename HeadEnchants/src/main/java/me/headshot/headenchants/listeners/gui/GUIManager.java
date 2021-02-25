package me.headshot.headenchants.listeners.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.upgrade.UpgradeTier;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIManager {
	public static Inventory upgrades = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Upgrades");
	public static Inventory enchants;

	static {
		int i = 1;
		for (UpgradeTier tier : UpgradeTier.values()) {
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) getColor(getColor(tier)));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.valueOf(getColor(tier).toUpperCase()) + ""
					+ StringUtils.capitalize(tier.toString().toLowerCase()) + " Upgrade");
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "Use the scroll by right clicking it.");
			lore.add(" ");
			lore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "Required XP: " + ChatColor.RESET
					+ HeadEnchants.getConfigManager().getRequiredXP(tier));
			meta.setLore(lore);
			item.setItemMeta(meta);
			upgrades.setItem(i, item);
			i += 2;
		}
		int size = HeadEnchants.get().getConfig()
				.getInt("main.size");
		String title = ChatColor.translateAlternateColorCodes('&',
				HeadEnchants.get().getConfig().getString("main.title"));
		enchants = Bukkit.createInventory(null, size, title);
		for(int j = 0; j < size; j++){
			ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
			ItemMeta meta = fillerItem.getItemMeta();
			meta.setDisplayName(" ");
			fillerItem.setItemMeta(meta);
			enchants.setItem(j, fillerItem);
		}
		for (String item : HeadEnchants.get().getConfig().getConfigurationSection("main.items").getKeys(false)) {
			String name = ChatColor.translateAlternateColorCodes('&',
					HeadEnchants.get().getConfig().getString("main.items." + item + ".name"));
			List<String> lores = HeadEnchants.get().getConfig()
					.getStringList("main.items." + item + ".lore")
					.stream().map(s -> ChatColor.translateAlternateColorCodes('&',
							s))
					.collect(Collectors.toList());
			Material type = Material.getMaterial(HeadEnchants.get().getConfig()
					.getString("main.items." + item + ".type").toUpperCase());
			ItemStack mainItem = new ItemStack(type);
			ItemMeta itemMeta = mainItem.getItemMeta();
			itemMeta.setDisplayName(name);
			itemMeta.setLore(lores);
			mainItem.setItemMeta(itemMeta);
			int slot = HeadEnchants.get().getConfig().getInt("main.items." + item + ".slot");
			enchants.setItem(slot, mainItem);
		}

	}

	private static String getColor(UpgradeTier tier) {
		switch (tier) {
		case TIER1:
			return "white";
		case TIER2:
			return "dark_green";
		case TIER3:
			return "aqua";
		case TIER4:
			return "yellow";
		default:
			return "white";
		}
	}

	private static int getColor(String color) {
		switch (color.toLowerCase()) {
		case "white":
			return 0;
		case "gold":
			return 1;
		case "light_purple":
			return 2;
		case "blue":
			return 3;
		case "yellow":
			return 4;
		case "green":
			return 5;
		case "red":
			return 6;
		case "dark_gray":
			return 7;
		case "gray":
			return 8;
		case "aqua":
			return 9;
		case "purple":
			return 10;
		case "dark_blue":
			return 11;
		case "brown":
			return 12;
		case "dark_green":
			return 13;
		case "dark_red":
			return 14;
		case "black":
			return 15;
		default:
			return 0;
		}
	}

	public static void displayUpgrades(Player player) {

		player.openInventory(upgrades);

	}
	public static void displayEnchants(Player player) {
		
		player.openInventory(enchants);
		
	}
}
