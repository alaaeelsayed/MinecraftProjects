package me.headshot.headenchants.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.listeners.gui.GUIManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.upgrade.UpgradeTier;

public class ScrollListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		if (event.getInventory().getTitle() == null)
			return;
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
			return;
		if(!event.getInventory().getTitle().equals(GUIManager.enchants.getTitle())) return;
		event.setCancelled(true);
		if (event.getClickedInventory().getTitle() != null
					&& (event.getClickedInventory().getTitle().equals(ChatColor.GREEN + "Upgrades") || event.getInventory().getTitle().equals(ChatColor.GREEN + "Upgrades"))) {
				event.setCancelled(true);
				ItemMeta meta = event.getCurrentItem().getItemMeta();
				UpgradeTier tier = UpgradeTier.valueOf(ChatColor.stripColor(meta.getDisplayName())
						.substring(0, ChatColor.stripColor(meta.getDisplayName()).lastIndexOf(" ")).toUpperCase());
				Player player = (Player) event.getWhoClicked();
				if (player.getTotalExperience() < HeadEnchants.getConfigManager().getRequiredXP(tier)) {
					player.sendMessage(
							ChatColor.RED + "You don't have enough XP, current XP: " + player.getTotalExperience());
					return;
				}
				if (player.getInventory().firstEmpty() != -1) {
					int currentxp = player.getTotalExperience();

					player.setTotalExperience(0);
					player.setLevel(0);
					player.setExp(0);
					player.giveExp(currentxp - HeadEnchants.getConfigManager().getRequiredXP(tier));

					player.getInventory().addItem(HeadEnchants.getUpgradeManager().generateScroll(tier));
					player.sendMessage(
							ChatColor.GREEN + "You have received a[n] " + tier.toString().toLowerCase() + " scroll!");
					return;
				}
				player.sendMessage(ChatColor.DARK_RED + "You don't have enough space in your inventory!");
		} else if(event.getClickedInventory().getTitle() != null && (event.getClickedInventory().getTitle().equals(GUIManager.enchants.getTitle()) || event.getInventory().getTitle().equals(GUIManager.enchants.getTitle()))){
			event.setCancelled(true);
		}
		if (event.getRawSlot() == 5 || event.getRawSlot() == 6 || event.getRawSlot() == 7 || event.getRawSlot() == 8)
			return;
		if(event.getClickedInventory().getType() != InventoryType.PLAYER) return;
		if (HeadEnchants.getUpgradeManager().isScroll(event.getCursor())) {
			try {
				if(!HeadEnchants.getUpgradeManager().getScroll(event.getCursor()).getUpgrade()
						.isOfType(event.getCurrentItem())){
					return;
				}
				List<Upgrade> upgrades = new ArrayList<>();
				for (Upgrade upgrade : HeadEnchants.getUpgradeManager().getUpgrades(event.getCurrentItem()))
					if (!upgrade.getID().equals(HeadEnchants.getUpgradeManager().getScroll(event.getCursor())
							.getUpgrade().getID()))
						upgrades.add(upgrade);
				upgrades.add(HeadEnchants.getUpgradeManager().getScroll(event.getCursor()).getUpgrade());
				HeadEnchants.getUpgradeManager().setUpgrades(event.getCurrentItem(), upgrades);
				event.setCursor(new ItemStack(Material.AIR));
				event.getWhoClicked().getLocation().getWorld().playSound(event.getWhoClicked().getLocation(),
						Sound.LEVEL_UP, 10, 1);
			} catch (Exception e) {
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() == null)
			return;

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (HeadEnchants.getUpgradeManager().isTierScroll(event.getItem())) {
				if (event.getPlayer().getItemInHand().getAmount() == 1
						|| event.getPlayer().getInventory().firstEmpty() != -1) {
					if (event.getPlayer().getInventory().getItemInHand().getAmount() > 1)
						event.getPlayer().getInventory().getItemInHand()
								.setAmount(event.getPlayer().getInventory().getItemInHand().getAmount() - 1);
					else
						event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
					event.getPlayer().updateInventory();
					Random random = new Random();
					try {
						UpgradeTier tier = HeadEnchants.getUpgradeManager().getTier(event.getItem());
						Map<Upgrade, Map<Integer, Integer>> chances = HeadEnchants.getConfigManager()
								.getUpgrades(tier);
						Upgrade decided = null;
						int randNum = random.nextInt(101);
						Map<Upgrade, Map<Integer, Integer>> addedUp = new HashMap<>();
						int i = 0;
						for (Upgrade upgrade : chances.keySet()) {
							Map<Integer, Integer> map = new HashMap<>();
							Map<Integer, Integer> chance = chances.get(upgrade);
							for (int chance2 : chance.keySet()) {
								if (chance.get(chance2) == 0)
									continue;
								i += chance.get(chance2);
								map.put(chance2, i);
							}
							addedUp.put(upgrade, map);
						}
						for (Upgrade upgrade : addedUp.keySet()) {
							Map<Integer, Integer> chance = addedUp.get(upgrade);
							for (int chance2 : chance.keySet()) {
								if (chance.get(chance2) <= randNum) {
									decided = upgrade;
									decided.setLevel(chance2);
								}
							}
							if (decided == null)
								decided = upgrade;
						}
						event.getPlayer().sendMessage(ChatColor.GREEN + "Successfully recieved "
								+ ChatColor.stripColor(decided.getLore()) + " Scroll!");
						event.getPlayer().getInventory()
								.addItem(HeadEnchants.getUpgradeManager().generateScroll(decided));

						event.getPlayer().updateInventory();

					} catch (Exception e) {
					}

				} else {
					event.setCancelled(true);
					event.getPlayer()
							.sendMessage(ChatColor.DARK_RED + "You don't have enough space in your inventory!");
				}
			}
		}
	}
}
