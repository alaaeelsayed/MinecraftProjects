package me.headshot.headenchants.commands;

import java.util.List;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.upgrade.Upgrade;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HeadEnchantsCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			switch (args[0].toLowerCase()) {
			case "reload":
				if (sender.hasPermission("headenchants.reload")) {
					HeadEnchants.getConfigManager().reloadAll();
					sender.sendMessage(ChatColor.GREEN + "Successfully reloaded config.");
					return true;
				}
				sender.sendMessage(HeadEnchants.getConfigManager().getMessage("no-permissions"));
				return false;
			case "help":
				if (sender.hasPermission("headenchants.view.help")) {
					List<String> lines = HeadEnchants.getConfigManager().getMessages("help");
					sender.sendMessage(lines.toArray(new String[0]));
					return true;
				}
				sender.sendMessage(HeadEnchants.getConfigManager().getMessage("no-permissions"));
				return false;
			case "give":
				if (sender.hasPermission("headenchants.give")) {
					if (args.length > 2) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player != null) {
							switch (args[2]) {
							case "upgrade":
								if (args.length == 5) {
									try {
										Upgrade upgrade = HeadEnchants.getUpgradeManager().getUpgrade(args[3]);
										if (NumberUtils.isNumber(args[4]) && Integer.parseInt(args[4]) > 0) {
											int level = Integer.parseInt(args[4]);
											if(Integer.parseInt(args[4]) > upgrade.getMaximumLevel())
												level = upgrade.getMaximumLevel();
											upgrade.setLevel(level);
											if (player.getInventory().firstEmpty() != -1) {
												player.getInventory().addItem(
														HeadEnchants.getUpgradeManager().generateScroll(upgrade));
												sender.sendMessage(ChatColor.GREEN + "Successfully sent player a[n] "
														+ upgrade.getLore() + " scroll!");
												player.sendMessage(ChatColor.GREEN + "You have received a[n] "
														+ upgrade.getLore() + " scroll!");
												return true;
											}
											sender.sendMessage(ChatColor.DARK_RED
													+ "Player doesn't have enough space in their inventory!");
											return false;
										}
										sender.sendMessage(HeadEnchants.getConfigManager()
												.getMessage("invalid-number").replaceAll("%number%", args[4]));
										return false;
									} catch (Exception e) {
										sender.sendMessage(HeadEnchants.getConfigManager()
												.getMessage("invalid-upgrade").replaceAll("%upgrade%", args[3]));
										return false;
									}
								}
							sender.sendMessage(HeadEnchants.getConfigManager().getMessage("incorrect-usage"));
							return false;
							}
							sender.sendMessage(HeadEnchants.getConfigManager().getMessage("incorrect-usage"));
							return false;
						}
						sender.sendMessage(HeadEnchants.getConfigManager().getMessage("invalid-player")
								.replaceAll("%player%", args[1]));
						return false;
					}
					sender.sendMessage(HeadEnchants.getConfigManager().getMessage("incorrect-usage"));
					return false;
				}
				sender.sendMessage(HeadEnchants.getConfigManager().getMessage("no-permissions"));
				return false;
			}
		}
		sender.sendMessage(HeadEnchants.getConfigManager().getMessage("incorrect-usage"));
		return false;
	}
}
