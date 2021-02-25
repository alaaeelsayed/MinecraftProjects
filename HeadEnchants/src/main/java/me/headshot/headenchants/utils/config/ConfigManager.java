
package me.headshot.headenchants.utils.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.upgrade.UpgradeTier;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import me.headshot.headenchants.upgrade.Upgrade;

public class ConfigManager {
	private final HeadEnchants plugin;
	File messagesFile, kitsFile;
	YamlConfiguration messagesConfig, kitsConfig;

	public ConfigManager(HeadEnchants plugin) {
		this.plugin = plugin;
		plugin.saveDefaultConfig();
		
		kitsFile = new File(plugin.getDataFolder(), "kits.yml");
		
		if (!kitsFile.exists()) {
			try {
				
				kitsFile.createNewFile();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		messagesFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!messagesFile.exists()) {
			try {
				messagesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadMessages();
		loadKits();
		Map<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("activation", "&a%upgrade% &2has activated!");
		defaults.put("no-permissions", "&4You don't have permission to use that command.");
		List<String> helpLines = new ArrayList<>();
		helpLines.add("&f------------ &2HeadEnchants Help &f------------");
		helpLines.add("&2/henchants reload - reloads the configuration files of the plugin.");
		helpLines.add("&2/henchants help - shows you this.");
		helpLines.add(
				"&2/henchants give [player] upgrade [upgrade] [amount] - gives a player a scroll of an upgrade.");
		helpLines.add("&f----------------------------------------------");
		defaults.put("help", helpLines);
		defaults.put("invalid-number", "&c%number% &4is not a valid number.");
		defaults.put("incorrect-usage",
				"&4Incorrect usage, please type /henchants help to see a list of valid commands.");
		defaults.put("invalid-player", "&c%player% &4is not an online player.");
		defaults.put("invalid-upgrade", "&c%upgrade% &4is not a valid upgrade.");

		for (String defaultItem : defaults.keySet()) {
			if (!messagesConfig.isConfigurationSection(defaultItem))
				messagesConfig.set(defaultItem, defaults.get(defaultItem));
		}
		try {
			messagesConfig.save(messagesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadAll() {
		loadMessages();
		plugin.reloadConfig();
		loadKits();
	}

	private void loadMessages() {
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
	}
	private void loadKits() {
		kitsConfig = YamlConfiguration.loadConfiguration(kitsFile);		
	}
	public FileConfiguration getKitsFile() {
		return kitsConfig;
	}
	public void saveKits() {
		
		try {
			kitsConfig.save(kitsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString(message));
	}

	public List<String> getMessages(String message) {
		loadMessages();
		List<String> colored = new ArrayList<>();
		for (String string : messagesConfig.getStringList(message))
			colored.add(ChatColor.translateAlternateColorCodes('&', string));
		return colored;
	}

	public int getRequiredXP(UpgradeTier tier) {
		FileConfiguration config = plugin.getConfig();
		if (config.contains("tiers." + tier.toString().toLowerCase() + ".requiredxp")) {
			return config.getInt("tiers." + tier.toString().toLowerCase() + ".requiredxp");
		} else {
			config.set("tiers." + tier.toString().toLowerCase() + ".requiredxp", 400);
			plugin.saveConfig();
		}
		return 400;
	}

	public Map<Upgrade, Map<Integer, Integer>> getUpgrades(UpgradeTier tier) throws Exception {

		FileConfiguration config = plugin.getConfig();

		HashMap<Upgrade, Map<Integer, Integer>> tierMap = new HashMap<>();
		for (Upgrade upgrade : HeadEnchants.getUpgradeManager().getUpgrades()) {
			Map<Integer, Integer> map = new HashMap<>();
			for (int i = 1; i <= upgrade.getMaximumLevel(); i++) {
				upgrade.setLevel(i);
				if (config.contains(
						"tiers." + tier.toString().toLowerCase() + ".chances." + upgrade.getID() + ".level." + i)) {
					map.put(i, config.getInt(
							"tiers." + tier.toString().toLowerCase() + ".chances." + upgrade.getID() + ".level." + i));
				} else {
					config.set("tiers." + tier.toString().toLowerCase() + ".chances." + upgrade.getID() + ".level." + i,
							0);
					plugin.saveConfig();
					map.put(i, 0);
				}
				tierMap.put(upgrade, map);
			}
		}

		return tierMap;
	}

	private String levelToString(int level) {
		switch (level) {
		case 1:
			return "I";
		case 2:
			return "II";
		case 3:
			return "III";
		}
		return "I";
	}

	public String getLore(String id) {
		FileConfiguration config = plugin.getConfig();
		String lore = null;
		if (config.contains("upgrades." + id + ".lore")) {
			lore = ChatColor.translateAlternateColorCodes('&', config.getString("upgrades." + id + ".lore"));
		}
		return lore;
	}

	public String getLore(Upgrade upgrade) {
		String id = upgrade.getID();
		FileConfiguration config = plugin.getConfig();
		String lore;
		if (config.contains("upgrades." + id + ".lore")) {
			lore = ChatColor.translateAlternateColorCodes('&', config.getString("upgrades." + id + ".lore"));
		} else {
			config.set("upgrades." + id + ".lore",
					"&6" + upgrade.getDefaultLore());
			plugin.saveConfig();
			lore = ChatColor.YELLOW + upgrade.getDefaultLore();
		}
		return lore;
	}

	public String getScrollName(Upgrade upgrade) {
		String id = upgrade.getID();
		FileConfiguration config = plugin.getConfig();
		String scrollname;
		if (config.contains("upgrades." + id + ".scrollname")) {
			scrollname = ChatColor.translateAlternateColorCodes('&', config.getString("upgrades." + id + ".scrollname")
					.replaceAll("%level%", levelToString(upgrade.getLevel())));
		} else {
			config.set("upgrades." + id + ".scrollname",
					"&" + ChatColor.YELLOW.getChar() + "" + "&" + ChatColor.BOLD.getChar() + "" + "&"
							+ ChatColor.UNDERLINE.getChar()
							+ upgrade.getDefaultLore());
			plugin.saveConfig();
			scrollname = "&" + ChatColor.YELLOW.getChar() + "" + "&" + ChatColor.BOLD.getChar() + "" + "&"
					+ ChatColor.UNDERLINE.getChar()
					+ upgrade.getDefaultLore().replace("%level%", levelToString(upgrade.getLevel()));
		}
		return scrollname;
	}

	public float getPercentage(String id, int level) {
		FileConfiguration config = plugin.getConfig();
		float percentage = 100;
		if (config.contains("upgrades." + id + ".level." + level + ".activation-percentage")) {
			percentage = config.getInt("upgrades." + id + ".level." + level + ".activation-percentage");
		}
		return percentage;
	}

	public List<String> getCommands(String id, int level) {
		FileConfiguration config = plugin.getConfig();
		List<String> commands = new ArrayList<>();
		if (config.contains("upgrades." + id + ".level." + level + ".execute-commands")) {
			commands = config.getStringList("upgrades." + id + ".level." + level + ".execute-commands");
		}
		return commands;
	}

}