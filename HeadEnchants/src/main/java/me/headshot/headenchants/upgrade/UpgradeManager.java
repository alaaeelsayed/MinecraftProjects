package me.headshot.headenchants.upgrade;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.utils.type.GearType;
import me.headshot.headenchants.utils.type.ItemType;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.lang.reflect.Constructor;
import java.util.*;

public class UpgradeManager {
	private SplittableRandom random = new SplittableRandom();
	private Map<String, Class<? extends Upgrade>> upgrades = new HashMap<>();

	public void register(Class<? extends Upgrade> upgradeClass) throws Exception {
		Constructor<? extends Upgrade> constructor = upgradeClass.getDeclaredConstructor(Integer.TYPE);
		constructor.setAccessible(true);
		Upgrade upgrade = constructor.newInstance(1);
		upgrades.put(upgrade.getID(), upgrade.getClass());
	}

	public void unregister(Class<? extends Upgrade> upgradeClass) throws Exception {
		Constructor<? extends Upgrade> constructor = upgradeClass.getDeclaredConstructor(Integer.TYPE);
		constructor.setAccessible(true);
		Upgrade upgrade = constructor.newInstance(1);
		upgrades.remove(upgrade.getID());
	}

	public void upgrade(Class<? extends Upgrade> upgrade, ItemStack item, int level) throws Exception {
		List<Upgrade> upgradeList = getUpgrades(item);
		boolean exists = false;
		for (Upgrade foundUpgrade : upgradeList)
			if (foundUpgrade.getClass().equals(upgrade)) {

				if(!foundUpgrade.isOfType(item)){
					throw new IllegalArgumentException(
							upgrade.getName() + " cannot be applied on " + item.getType().toString().toLowerCase());
				}

				if (level > foundUpgrade.getMaximumLevel()) {
					level = foundUpgrade.getMaximumLevel();
				}
				foundUpgrade.setLevel(level);
				exists = true;
				break;
			}
		if (!exists) {
			Constructor<? extends Upgrade> constructor = upgrade.getDeclaredConstructor(Integer.TYPE);
			constructor.setAccessible(true);
			upgradeList.add(constructor.newInstance(1));
		}
		setUpgrades(item, upgradeList);
	}

	public List<Upgrade> getUpgrades() throws Exception {
		List<Upgrade> ups = new ArrayList<>();
		for (String id : upgrades.keySet()) {
			Constructor<? extends Upgrade> constructor = upgrades.get(id).getDeclaredConstructor(Integer.TYPE);
			constructor.setAccessible(true);
			ups.add(constructor.newInstance(1));
		}
		return ups;
	}

	public int getUpgradeLevel(Class<? extends Upgrade> upgrade, ItemStack item) throws IllegalArgumentException {
		List<Upgrade> upgrades = getUpgrades(item);
		for (Upgrade foundUpgrade : upgrades) {
			if (foundUpgrade.getClass().equals(upgrade)) {
				return foundUpgrade.getLevel();
			}
		}
		return 0;
	}

	public List<Upgrade> getUpgrades(ItemStack item) {
		List<Upgrade> upgrades = new ArrayList<>();
		if (item == null || (item.getItemMeta() == null) || (item.getItemMeta().getLore() == null))
			return upgrades;
		for (String lore : item.getItemMeta().getLore()) {
			String stripped = ChatColor.stripColor(lore);
			try {
				Upgrade upgrade = getUpgrade(
						stripped.toLowerCase().substring(0, stripped.lastIndexOf(" ")).replaceAll(" ", "-"));
				upgrade.setLevel(stringToLevel(
						stripped.replaceAll(stripped.substring(0, stripped.lastIndexOf(" ")), "").trim()));
				upgrades.add(upgrade);
			} catch (Exception e) {
			}
		}
		return upgrades;
	}

	public void setUpgrades(ItemStack item, List<Upgrade> upgrades) {
		ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
		List<String> lore = new ArrayList<>();
		for (Upgrade upgrade : upgrades) {
			if (!lore.contains(upgrade.getLore()))
				lore.add(upgrade.getLore());
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public Upgrade getUpgrade(String id) throws Exception {
		if (!upgrades.containsKey(id)) {
			throw new IllegalArgumentException("Invalid enchant id: " + id);
		}
		Constructor<? extends Upgrade> constructor = upgrades.get(id).getDeclaredConstructor(Integer.TYPE);
		constructor.setAccessible(true);
		return constructor.newInstance(1);
	}

	public void performEvent(Event event, Player player, ItemStack... items) {
		try {
			Set<Upgrade> upgrades = new HashSet<>();
			for (ItemStack item : items) {
				upgrades.addAll(getUpgrades(item));
			}
			for (Upgrade upgrade : upgrades) {
				upgrade.onEvent(event,player);
			}
		} catch (Exception e) {
		}
	}

	public void performDamage(EntityDamageByEntityEvent event,Player player, ItemStack... items){
		try {
			Set<Upgrade> upgrades = new HashSet<>();
			for (ItemStack item : items) {
				upgrades.addAll(getUpgrades(item));
			}
			for (Upgrade upgrade : upgrades) {
				upgrade.onDamage(event, player);
			}
		} catch (Exception e) {
		}
	}

	public void performDamage(EntityDamageByEntityEvent event, Player player) {
		try {
			Set<Upgrade> upgrades = new HashSet<>();
			for (ItemStack item : player.getInventory().getArmorContents()) {
				upgrades.addAll(getUpgrades(item));
			}
			for (Upgrade upgrade : upgrades) {
				upgrade.onDamage(event, player);
			}
		} catch (Exception e) {
		}
	}

	public void performArmor(ArmorEquipEvent event, Player wearer, boolean removed) {
		performArmor(event, wearer, removed, wearer.getInventory().getArmorContents());
	}

	public void performArmor(ArmorEquipEvent event, Player wearer, boolean removed, ItemStack... contents) {
		try {
			Set<Upgrade> upgrades = new HashSet<>();
			for (ItemStack item : contents) {
				upgrades.addAll(getUpgrades(item));
			}
			for (Upgrade upgrade : upgrades) {
				upgrade.onWear(event, wearer, removed);
			}
		} catch (Exception e) {
		}
	}

	public void performSwitch(PlayerItemHeldEvent event, ItemStack oldItem, ItemStack newItem) {
		try {
			Set<Upgrade> oldUpgrades = new HashSet<>(getUpgrades(oldItem));
			Set<Upgrade> newUpgrades = new HashSet<>(getUpgrades(newItem));
			for (Upgrade upgrade : oldUpgrades) {
				upgrade.onSwitch(event, true);
			}
			for (Upgrade upgrade : newUpgrades) {
				upgrade.onSwitch(event, false);
			}
		} catch (Exception e) {
		}
	}

	public boolean getRandomPercentage(float percentage) {
		double d = random.nextDouble();
		return d < percentage / 100f;
	}

	public String getRandomCommand(List<String> commands) {
		int size = commands.size();
		if(size == 0) return "";
		return commands.get(random.nextInt(size));
	}

	public void addHextech(ItemStack item) {
		if(isScroll(item)){
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add(ChatColor.YELLOW + "Hextech Protected");
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

	public ItemStack generateScroll(UpgradeTier tier) {
		ItemStack scroll = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = scroll.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + StringUtils.capitalize(tier.toString().toLowerCase())
				+ " Upgrade Scroll (Right Click)");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Right click to examine this scroll!");
		meta.setLore(lore);
		scroll.setItemMeta(meta);
		return scroll;
	}

	public ItemStack generateScroll(Upgrade upgrade) {
		List<String> types = new ArrayList<>();
		List<ItemType> itemTypes = new ArrayList<>(Arrays.asList(upgrade.getTypes()));
		for(GearType gearType : GearType.values()) {
			if(itemTypes.containsAll(
					Arrays.asList(gearType.getItemTypes()))){
				types.add(gearType.toString());
				itemTypes.removeAll(Arrays.asList(gearType.getItemTypes()));
			}
		}

		for(ItemType itemType : itemTypes){
			types.add(itemType.toString());
		}
		String typesString = String.join(",", types);

		ItemStack scroll = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = scroll.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HeadEnchants.getConfigManager().getScrollName(upgrade)));

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GRAY + "Upgrade Scroll");
		lore.add(ChatColor.GRAY + "Can be used on: " + typesString);

		meta.setLore(lore);

		scroll.setItemMeta(meta);

		return scroll;
	}



	public boolean isHextechOrb(ItemStack item){
		if(!isScroll(item))
			return false;
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		return ChatColor.stripColor(lore.get(3)).equalsIgnoreCase("hextech protected");
	}

	public boolean isScroll(ItemStack item) {
		if (item == null || item.getType() == Material.AIR)
			return false;
		if (!item.hasItemMeta())
			return false;
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasLore())
			return false;
		List<String> lore = meta.getLore();
		return ChatColor.stripColor(lore.get(0)).equals("Upgrade Scroll");
	}

	public boolean isTierScroll(ItemStack item) {
		if (!item.hasItemMeta())
			return false;
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasDisplayName())
			return false;
		if (!meta.hasLore())
			return false;
		return meta.getDisplayName().endsWith("Upgrade Scroll (Right Click)")
				&& meta.getLore().contains(ChatColor.GRAY + "Right click to examine this scroll!");
	}

	public UpgradeScroll getScroll(ItemStack item) throws Exception {
		ItemMeta meta = item.getItemMeta();
		if (!isScroll(item))
			return null;
		String stripped = ChatColor.stripColor(meta.getDisplayName());
		Upgrade upgrade = getUpgrade(
				stripped.toLowerCase().substring(0, stripped.lastIndexOf(" ")).replaceAll(" ", "-"));
		upgrade.setLevel(
				stringToLevel(stripped.replaceAll(stripped.substring(0, stripped.lastIndexOf(" ")), "").trim()));
		return new UpgradeScroll(upgrade);
	}

	public UpgradeTier getTier(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (!isTierScroll(item))
			return null;
		return UpgradeTier.valueOf(ChatColor.stripColor(meta.getDisplayName())
				.replaceAll(" Upgrade Scroll \\(Right Click\\)", "").toUpperCase());
	}

	private int stringToLevel(String level) {
		switch (level) {
		case "I":
			return 1;
		case "II":
			return 2;
		case "III":
			return 3;
		}
		return 1;
	}

	private List<TNTPrimed> tnt = new ArrayList<>();

	public void addTNT(TNTPrimed tnt) {
		this.tnt.add(tnt);
	}

	public boolean willBreak(TNTPrimed tnt) {
		return !this.tnt.contains(tnt);
	}

	public void removeTNT(TNTPrimed tnt){
		this.tnt.remove(tnt);
	}

	public float getSaturationValue(ItemStack food){
		Material type = food.getType();
		switch (type) {
		case APPLE:
			return 2.4F;
		case BAKED_POTATO:
			return 6;
		case BREAD:
			return 6;
		case CAKE:
			return 0.4F;
		case CARROT:
			return 3.6F;
		case COOKED_CHICKEN:
			return 7.2F;
		case COOKED_MUTTON:
			return 9.6F;
		case GRILLED_PORK:
			return 12.8F;
		case COOKED_BEEF:
			return 12.8F;
		case COOKED_RABBIT:
			return 6;
		case COOKIE:
			return 0.4F;
		case GOLDEN_CARROT:
			return 14.4F;
		case MUSHROOM_SOUP:
			return 7.2F;
		case POISONOUS_POTATO:
			return 1.2F;
		case POTATO:
			return 0.6F;
		case PUMPKIN_PIE:
			return 4.8F;
		case RABBIT_STEW:
			return 12;
		case RAW_BEEF:
			return 1.8F;
		case RAW_CHICKEN:
			return 1.2F;
		case MUTTON:
			return 1.2F;
		case PORK:
			return 1.8F;
		case RABBIT:
			return 1.8F;
		case ROTTEN_FLESH:
			return 0.8F;
		case SPIDER_EYE:
			return 3.2F;
		case GOLDEN_APPLE:
			return 9.6F;
		default:
			@SuppressWarnings("deprecation")
			byte d = food.getData().getData();
			if(type == Material.RAW_FISH){
				if(d == 0 || d == 1){
					//Salmon, raw fish
					return 0.4F;
				}else return 0.2F;
			}else if(type == Material.COOKED_FISH){
				if(d == 0){
					//Raw fish
					return 6;
				}else if(d == 1){
					//Salmon
					return 9.6F;
				}
			}
			return 0;
		}

	}

	HashMap<UUID, Integer> taskTimers = new HashMap<UUID, Integer>();

	public void putTaskTimer(UUID u, int ID){
		taskTimers.put(u, ID);
	}

	public void removeTaskTimer(UUID u){
		taskTimers.remove(u);
	}

	public int getTaskTimerID(UUID u){
		return taskTimers.get(u);
	}

	Set<UUID> cannotMove = new HashSet<UUID>();

	public boolean canPlayerMove(UUID u){
		return !cannotMove.contains(u);
	}

	public void setCanMove(UUID u, boolean b){
		if(!b) cannotMove.add(u);
		else cannotMove.remove(u);
	}

	public ItemStack makeHead(String name){

		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		headMeta.setDisplayName("Skull of " + name);
		headMeta.setOwner(name);
		head.setItemMeta(headMeta);

		return head;
	}

	HashMap<UUID, Integer> piercingLevels = new HashMap<UUID, Integer>();

	public void incrementPiercingLevel(UUID u){

		if(!piercingLevels.containsKey(u)){
			piercingLevels.put(u, 1);
			return;
		}else{
			int i = piercingLevels.get(u) + 1;
			if(i > 100) i = 100;
			piercingLevels.put(u, i);
		}

	}

	public int getPiercingLevel(UUID u){
		return piercingLevels.get(u);
	}

	HashMap<UUID, ItemStack> lastStack = new HashMap<UUID, ItemStack>();

	public void putLastStack(UUID u, ItemStack i){
		lastStack.put(u, i);
	}

	public ItemStack getLastStack(UUID u){
		return lastStack.get(u);
	}

	public void removeLastStack(UUID u){
		lastStack.remove(u);
	}

	public static Economy econ = null;

	public boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

	public Economy getEconomy(){
		return econ;
	}

	public boolean isHovering(Location loc) {
	    Block lower = loc.getBlock().getRelative(BlockFace.DOWN);
		return (lower.getRelative(BlockFace.NORTH).getType() != Material.AIR) ||
				(lower.getRelative(BlockFace.SOUTH).getType() != Material.AIR) ||
				(lower.getRelative(BlockFace.EAST).getType() != Material.AIR) ||
				(lower.getRelative(BlockFace.WEST).getType() != Material.AIR) ||
				(lower.getRelative(BlockFace.DOWN).getType() != Material.AIR);
	}
}
