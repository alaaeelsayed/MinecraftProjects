package me.headshot.headenchants;

import me.headshot.headenchants.commands.EnchanterCommand;
import me.headshot.headenchants.commands.EnchantsCommand;
import me.headshot.headenchants.commands.HeadEnchantsCommand;
import me.headshot.headenchants.listeners.ArmorListener;
import me.headshot.headenchants.listeners.ScrollListener;
import me.headshot.headenchants.listeners.UpgradeListener;
import me.headshot.headenchants.upgrade.UpgradeManager;
import me.headshot.headenchants.utils.config.ConfigManager;
import me.headshot.headenchants.upgrade.upgrades.*;
import net.minelink.ctplus.CombatTagPlus;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HeadEnchants extends JavaPlugin {

	private static ConfigManager configManager;
	private static UpgradeManager upgradeManager;
	public static HeadEnchants main;
	private CombatTagPlus combatTagPlus;

//	Statement statement;

	public void onEnable() {
		main = this;
		combatTagPlus = (CombatTagPlus) Bukkit.getServer().getPluginManager().getPlugin("CombatTagPlus");
		configManager = new ConfigManager(this);
		upgradeManager = new UpgradeManager();
		if (!upgradeManager.setupEconomy()) Bukkit.getLogger().severe("Could not initialize vault!");

		
		try {

			upgradeManager.register(IncombustibleUpgrade.class);
			upgradeManager.register(ScubaUpgrade.class);
			upgradeManager.register(FeedingUpgrade.class);
			upgradeManager.register(NightGogglesUpgrade.class);
			upgradeManager.register(RegenerationUpgrade.class);
			upgradeManager.register(SwiftnessUpgrade.class);
			upgradeManager.register(StrengthUpgrade.class);
			upgradeManager.register(AntiGravityUpgrade.class);
			upgradeManager.register(AbsorptionUpgrade.class);
			upgradeManager.register(HasteUpgrade.class);
			upgradeManager.register(UndyingFuryUpgrade.class);
			upgradeManager.register(HeadshotUpgrade.class);
			upgradeManager.register(ObsidianDestroyerUpgrade.class);
			upgradeManager.register(GrindUpgrade.class);
			upgradeManager.register(ReforgeUpgrade.class);
			upgradeManager.register(LifeStealUpgrade.class);
			upgradeManager.register(KeyHunterUpgrade.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		getCommand("henchants").setExecutor(new HeadEnchantsCommand());
		getCommand("enchanter").setExecutor(new EnchanterCommand());
		getCommand("enchants").setExecutor(new EnchantsCommand());

		getServer().getPluginManager().registerEvents(new ArmorListener(), this);
		getServer().getPluginManager().registerEvents(new UpgradeListener(), this);
		getServer().getPluginManager().registerEvents(new ScrollListener(), this);
		
	}

	public void onDisable() {
		main = null;
		configManager = null;
		upgradeManager = null;

	}

	public static HeadEnchants get() {
		return main;

	}

	public static ConfigManager getConfigManager() {
		return configManager;
	}

	public static UpgradeManager getUpgradeManager() {
		return upgradeManager;
	}

	public CombatTagPlus getCombatTagPlus(){
		return combatTagPlus;
	}

//	public Connection loadDataBase() {
//
//		String hostname, port, database, username, password;
//
//		hostname = getConfig().getString("mysql.hostname");
//		port = getConfig().getString("mysql.port");
//		database = getConfig().getString("mysql.database");
//		username = getConfig().getString("mysql.username");
//		password = getConfig().getString("mysql.password");
//
//		MySQL mysql = new MySQL(hostname, port, database, username, password);
//
//		Connection c = null;
//
//		try {
//
//			c = mysql.openConnection();
//
//		} catch (ClassNotFoundException | SQLException e) {
//
//			e.printStackTrace();
//
//		}
//		return c;
//	}

//	public Statement getStatement() {
//		return statement;
//	}
}
